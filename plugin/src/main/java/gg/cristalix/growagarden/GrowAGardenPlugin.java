package gg.cristalix.growagarden;

import gg.cristalix.growagarden.config.ConfigLoader;
import gg.cristalix.growagarden.listener.ConnectionListener;
import gg.cristalix.growagarden.listener.GardenInteractionListener;
import gg.cristalix.growagarden.mod.GardenMod;
import gg.cristalix.growagarden.model.garden.vegetation.SeedData;
import gg.cristalix.growagarden.model.item.CustomItemData;
import gg.cristalix.growagarden.model.player.GamePlayer;
import gg.cristalix.growagarden.model.world.npc.GardenNPCManager;
import gg.cristalix.growagarden.service.item.ItemService;
import gg.cristalix.growagarden.service.world.PlantGrowthTicker;
import gg.cristalix.growagarden.service.item.seed.SeedService;
import gg.cristalix.growagarden.service.world.WeatherService;
import gg.cristalix.growagarden.service.world.WeatherTicker;
import gg.cristalix.growagarden.utils.gson.LocationSerializer;
import gg.cristalix.growagarden.utils.map.MapUtil;
import gg.cristalix.wada.Wada;
import gg.cristalix.wada.component.indicator.data.Indicator;
import gg.cristalix.wada.component.nametag.data.NameTag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.cristalix.core.CoreApi;
import ru.cristalix.core.GlobalSerializers;
import ru.cristalix.core.data.IPlayerDataService;
import ru.cristalix.core.data.PlayerDataService;
import ru.cristalix.core.data.listener.PlayerChangeConnectionStateListener;
import ru.cristalix.core.database.nosql.mongo.IMongoDatabase;
import ru.cristalix.core.database.nosql.mongo.MongoDatabase;
import ru.cristalix.core.event.PermissionsLoadEvent;
import ru.cristalix.core.item.ItemStackSerializer;
import ru.cristalix.core.map.IMapService;
import ru.cristalix.core.map.MapService;
import ru.cristalix.core.permissions.IPermissionContext;
import ru.cristalix.core.realm.IRealmService;
import ru.cristalix.core.realm.RealmInfo;
import ru.cristalix.core.realm.RealmStatus;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GrowAGardenPlugin extends JavaPlugin {

  @Getter
  static GrowAGardenPlugin instance;

  GameState gameState;
  GardenMod gardenMod;
  SeedService seedService;
  ItemService itemService;
  WeatherService weatherService;
  GardenNPCManager npcManager;

  IPlayerDataService playerDataService;

  @Override
  public void onEnable() {
    instance = this;

    this.gameState = new GameState();
    loadWada();
    loadCoreApi();
    initializeDatabase();
    loadConfiguration();
    this.gardenMod = new GardenMod(this);
    initializeWeather();
    registerEvents();
    initializeNPCs();

    initRealm();
  }

  @Override
  public void onDisable() {
    PlantGrowthTicker.stop();
    WeatherTicker.stop();
  }

  private void initRealm() {
    RealmInfo realmInfo = IRealmService.get().getCurrentRealmInfo();
    realmInfo.setReadableName("GrowAGarden #" + realmInfo.getRealmId().getId());
    realmInfo.setGroupName("GrowAGarden");
    realmInfo.setMaxPlayers(100);
    realmInfo.setStatus(RealmStatus.WAITING_FOR_PLAYERS);
  }

  private void registerEvents() {
    registerEvents(
      new PlayerChangeConnectionStateListener(playerDataService),
      new ConnectionListener(this),
      new GardenInteractionListener()
    );
  }

  public void registerEvents(Listener... listeners) {
    PluginManager pluginManager = this.getServer().getPluginManager();
    for (Listener listener : listeners) {
      pluginManager.registerEvents(listener, this);
    }
  }

  private void initializeDatabase() {
    IMongoDatabase mongoDatabase = new MongoDatabase();
    try {
      mongoDatabase.connect(getUrlFromEnv()).join();
      log.info("Successfully connected to database!");
    } catch (Exception e) {
      log.error("Failed connect to database!", e);
    }

    playerDataService = new PlayerDataService(mongoDatabase, "GAG");
    playerDataService.setCacheUpdateHandler(GamePlayer::of);
  }

  private String getUrlFromEnv() {
    Map<String, String> getenv = System.getenv();

    String host = getenv.getOrDefault("MONGO_HOST", "localhost");
    String hostReserve = getenv.getOrDefault("MONGO_HOST_RESERVE", "localhost");
    String port = getenv.getOrDefault("MONGO_PORT", "27017");

    String login = getenv.getOrDefault("MONGO_AUTH_LOGIN", "");
    String password = getenv.getOrDefault("MONGO_AUTH_PASSWORD", "");
    String auth = getenv.getOrDefault("MONGO_AUTH_DB", "");

    if (login.isEmpty()) {
      return "mongodb://" + host + ":" + port;
    } else {
      return "mongodb://" + login + ":" + password + "@" + host + ":" + port + "," + hostReserve + ":" + port + "/" + auth + "?replicaSet=cri0";
    }
  }

  private void loadWada() {
    Wada wada = Wada.initialize(this);
    wada.getModLoader().loadModsFromFolder(true);

    wada.getNameTagManager().enable(NameTag.builder()
      .seeThroughWalls(false)
      .multiLine(false)
      .build()
    );

    wada.getIndicatorVisibilityManager().disable(
      Indicator.HEALTH,
      Indicator.EXP,
      Indicator.HUNGER,
      Indicator.ARMOR,
      Indicator.HOT_BAR,
      Indicator.AIR_BAR,
      Indicator.ITEM_NAME
    );
  }

  private void loadCoreApi() {
    CoreApi coreApi = CoreApi.get();

    coreApi.bus().register0(this, PermissionsLoadEvent.class, event -> {
      PermissionsLoadEvent permissionsLoadEvent = (PermissionsLoadEvent) event;
      IPermissionContext permissionContext = permissionsLoadEvent.getPermissionContext();
      permissionContext.setPermissions(Collections.singleton("*"));
    }, 100);

    coreApi.registerService(IMapService.class, new MapService());
    IMapService mapService = IMapService.get();
    var mapOptional = mapService.getLatestMapByGameTypeAndMapName("MISC", "zakat");
    if (mapOptional.isEmpty())
      throw new RuntimeException("Ошибка загрузки карты.");

    var mapVersion = mapOptional.get().getLatest();
    MapUtil.loadWorld(mapVersion);
  }

  private void loadConfiguration() {
    GlobalSerializers.configure(gsonBuilder -> {
      ItemStackSerializer itemStackSerializer = new ItemStackSerializer(true);
      gsonBuilder.registerTypeAdapter(ItemStack.class, itemStackSerializer);
      gsonBuilder.registerTypeAdapter(CraftItemStack.class, itemStackSerializer);
      gsonBuilder.registerTypeAdapter(Location.class, new LocationSerializer());
    });

    InputStream stream = getResource("items.json");

    Map<String, SeedData> seeds = ConfigLoader.loadSeeds(stream);
    this.seedService = new SeedService(seeds);
    log.info("Loaded {} seeds", seeds.size());

    stream = getResource("items.json");
    Map<String, CustomItemData> items = ConfigLoader.loadItems(stream);
    this.itemService = new ItemService();
    this.itemService.registerAllItemData(items);
    log.info("Loaded {} custom items", items.size());

    createSeedItemData();
    log.info("Registered seed item data in ItemService");
  }

  //todo перенести в подходящее место
  private void createSeedItemData() {
    Map<String, SeedData> seeds = seedService.getSeedDataMap();

    for (Map.Entry<String, SeedData> entry : seeds.entrySet()) {
      String seedId = entry.getKey();
      SeedData seedData = entry.getValue();

      Map<String, String> nbt = new HashMap<>();
      nbt.put("seed_id", seedId);

      CustomItemData seedItemData = new CustomItemData(
        seedId,
        seedData.getDisplayName(),
        "CLAY_BALL",
        gg.cristalix.growagarden.common.mod.inventory.ItemEnum.SEED,
        nbt,
        seedData.getName(),
        0
      );

      itemService.registerItemData(seedId, seedItemData);
    }
  }

  private void initializeWeather() {
    this.weatherService = new WeatherService();
    WeatherTicker.start(this, weatherService, gameState.getWorldState());
  }

  private void initializeNPCs() {
    npcManager = new GardenNPCManager(this);
  }
}