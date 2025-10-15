package gg.cristalix.growagarden.util;

import dev.xdark.clientapi.ClientApi;
import dev.xdark.clientapi.resource.ResourceLocation;
import dev.xdark.clientapi.texture.Texture;
import gg.cristalix.enginex.Enginex;
import gg.cristalix.growagarden.GrowAGardenMod;
import gg.cristalix.growagarden.module.IModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextureLoader implements IModule {

  final Map<String, ResourceLocation> cache = new ConcurrentHashMap<>();
  final Map<String, List<Consumer<ResourceLocation>>> callbacks = new ConcurrentHashMap<>();

  public static void setTexture(String link, Consumer<ResourceLocation> consumer) {
    GrowAGardenMod.instance.getModuleManager().getModule(TextureLoader.class, TextureLoader::new).asyncGet(link, consumer);
  }

  private void asyncGet(String link, Consumer<ResourceLocation> callback) {
    ResourceLocation resourceLocation = cache.get(link);
    if (resourceLocation != null) {
      callback.accept(resourceLocation);
      return;
    }

    List<Consumer<ResourceLocation>> list = callbacks.computeIfAbsent(link, s -> new ArrayList<>());
    list.add(callback);
    if (list.size() == 1) {
      ResourceLocation location = ResourceLocation.of("gag", sha256(link));
      load(location, link)
        .thenAccept(texture -> {
          cache.put(link, location);
          for (Consumer<ResourceLocation> textureResourceLocationBiConsumer : callbacks.remove(link)) {
            textureResourceLocationBiConsumer.accept(location);
          }
        }).exceptionally(throwable -> {
          GrowAGardenMod.error("Не удалось загрузить картинку " + link, throwable);
          return null;
        });
    }
  }

  public CompletableFuture<Texture> load(ResourceLocation location, String link) {
    CompletableFuture<Texture> future = new CompletableFuture<>();

    URL url;
    try {
      url = new URL(link);
    } catch (Exception e) {
      future.completeExceptionally(e);
      return future;
    }

    ClientApi clientApi = Enginex.getClientApi();

    Texture texture = clientApi.renderEngine().newRemoteStaticTexture(url, location);

    Enginex.getMinecraft().execute(() -> {
      clientApi.renderEngine().loadTexture(location, texture);
      future.complete(texture);
    });
    return future;
  }

  public String sha256(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      return hex(md.digest(input.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }

  public String hex(byte[] b) {
    StringBuilder result = new StringBuilder();
    for (byte value : b) {
      result.append(Integer.toString((value & 0xff) + 0x100, 16).substring(1));
    }
    return result.toString();
  }

}
