package gg.cristalix.growagarden.component.menu.inventory;

import gg.cristalix.enginex.element.Container;
import gg.cristalix.enginex.element.input.Input;
import gg.cristalix.enginex.element.layout.type.VerticalLayout;
import gg.cristalix.enginex.element.selector.Switcher;
import gg.cristalix.enginex.event.element.ButtonLeftActionEvent;
import gg.cristalix.enginex.event.element.InputTextUpdateEvent;
import gg.cristalix.enginex.event.element.SwitcherUpdateEvent;
import gg.cristalix.enginex.math.Relative;
import gg.cristalix.enginex.math.V3;
import gg.cristalix.enginex.transfer.ModTransfer;
import gg.cristalix.growagarden.channel.ChannelHandler;
import gg.cristalix.growagarden.common.data.item.data.ItemsData;
import gg.cristalix.growagarden.common.data.item.data.ItemsDataDeserializers;
import gg.cristalix.growagarden.common.element.CustomSwitcher;
import gg.cristalix.growagarden.common.element.InventoryElement;
import gg.cristalix.growagarden.common.element.item.ItemInventoryElement;
import gg.cristalix.growagarden.common.menu.AbstractMenu;
import gg.cristalix.growagarden.common.mod.inventory.ItemEnum;
import gg.cristalix.growagarden.util.cache.GlobalCache;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryMenu extends AbstractMenu<VerticalLayout> {

  Switcher switcher;

  InventoryElement inventoryElement = new InventoryElement(14, 14, new V3(72, 72), AbstractMenu.DEFAULT_SIZE.getX(), 526);

  public InventoryMenu() {
    super("Инвентарь", AbstractMenu.DEFAULT_SIZE);

    this.switcher = new CustomSwitcher(620, false, Arrays.stream(ItemEnum.VAL).map(ItemEnum::getModName).toArray(String[]::new))
      .registerEvent(SwitcherUpdateEvent.class, event -> {
        ChannelHandler.send("inv:open", t -> t.writeInt(event.getIndex()));
      });

    Input input = new Input()
      .setPlaceholder("Поиск..")
      .setMaxLength(32)
      .setOriginAndAlign(Relative.TOP_RIGHT)
      .setSizeX(265)
      .registerEvent(InputTextUpdateEvent.class, this::itemFilter)
      .setColor(AbstractMenu.MENU_COLOR);

    bodyAddChild(
      new Container()
        .setSize(AbstractMenu.DEFAULT_SIZE.getX(), AbstractMenu.DEFAULT_BUTTON_SIZE.getY(), 0)
        .addChild(switcher, input),
      inventoryElement
    );
  }

  private void itemFilter(InputTextUpdateEvent inputTextUpdateEvent) {

  }

  @Override
  protected VerticalLayout createBodyLayout() {
    return new VerticalLayout(AbstractMenu.DEFAULT_ELEMENT_PADDING);
  }

  public void open(ModTransfer transfer) {
    int i = transfer.readInt();
    this.switcher.setSelected(i, false);

    long itemSize = transfer.readLong();
    List<ItemInventoryElement> list = new ArrayList<>();
    for (long l = 0; l < itemSize; l++) {
      ItemsData itemsData = ItemsDataDeserializers.deserialize(
        transfer,
        ItemsDataDeserializers.UUID,
        ItemsDataDeserializers.ITEM_ID,
        ItemsDataDeserializers.ITEM_STACK,
        ItemsDataDeserializers.COUNT,
        ItemsDataDeserializers.ITEM_ENUM
      );

      ItemInventoryElement itemInventoryElement = new ItemInventoryElement(itemsData);
      itemInventoryElement.registerEvent(ButtonLeftActionEvent.class, event -> {
        if (GlobalCache.checkAndUpdate("EQUIP_ITEM")) return;
        ChannelHandler.send("inv:equip", transfer1 -> transfer1.writeInt(itemsData.getItemEnum().ordinal()).writeUUID(itemsData.getUuid()));
      });
      list.add(itemInventoryElement);
    }
    inventoryElement.load(list);
    openMenu();
  }
}
