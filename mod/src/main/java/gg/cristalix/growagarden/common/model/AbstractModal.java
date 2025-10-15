package gg.cristalix.growagarden.common.model;

import gg.cristalix.enginex.element.AbstractElement;
import gg.cristalix.enginex.element.layout.type.VerticalLayout;
import gg.cristalix.enginex.element.screen.AbstractGuiScreen;
import gg.cristalix.enginex.element.screen.type.GuiModal;
import gg.cristalix.enginex.math.Relative;
import gg.cristalix.growagarden.common.menu.AbstractMenu;
import gg.cristalix.growagarden.common.model.element.HeaderElementModal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;


@Getter
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractModal<T extends AbstractElement<T>> extends GuiModal {

  UUID menuId = UUID.randomUUID();

  VerticalLayout root = new VerticalLayout(AbstractMenu.DEFAULT_ELEMENT_PADDING)
    .setOriginAndAlign(Relative.CENTER);
  HeaderElementModal header;
  T body;


  protected AbstractModal(AbstractGuiScreen menu, double sizeX) {
    this(menu, "", sizeX);
  }

  protected AbstractModal(AbstractGuiScreen menu, String headerText) {
    this(menu, headerText, 0);
  }

  protected AbstractModal(AbstractGuiScreen menu, String headerText, double sizeX) {
    setScreen(menu);

    this.header = new HeaderElementModal(this, sizeX, headerText);
    this.body = createBodyLayout();

    disableCloseAnimation();

    setNavigationButtonsEnabled(true);

    root.addChild(header, body);

    addChild(root);
  }

  protected abstract T createBodyLayout();

  public void setNavigationButtonsEnabled(boolean enable) {
    this.header.setNavigationButtonsEnabled(enable);
  }

  public T bodyAddChild(AbstractElement<?>... abstractElement) {
    return this.body.addChild(abstractElement);
  }

  public T bodyClearChildren() {
    return this.body.clearChildren();
  }

}
