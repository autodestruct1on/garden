package gg.cristalix.growagarden.common.model.element;

import gg.cristalix.enginex.color.palette.ButtonColor;
import gg.cristalix.enginex.element.AbstractElement;
import gg.cristalix.enginex.element.Text;
import gg.cristalix.enginex.element.button.Button;
import gg.cristalix.enginex.element.carved.CarvedRectangle;
import gg.cristalix.enginex.element.layout.type.HorizontalLayout;
import gg.cristalix.enginex.event.element.ButtonLeftActionEvent;
import gg.cristalix.enginex.math.Relative;
import gg.cristalix.growagarden.common.menu.AbstractMenu;
import gg.cristalix.growagarden.common.model.AbstractModal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HeaderElementModal extends HorizontalLayout {

  public static final double HEADER_SPACING = 8;

  final AbstractModal<?> menu;

  @Setter
  double menuSizeX;

  final CarvedRectangle headerCarved;
  final Text headerTextElement;
  final HorizontalLayout headerRightElement;

  final Button backButton;

  @Setter
  @NonFinal
  boolean navigationButtonsEnabled = false;

  public HeaderElementModal(AbstractModal<?> menu, double menuSizeX, String headerText) {
    this.menu = menu;
    this.menuSizeX = menuSizeX;

    setSpacing(HEADER_SPACING);

    this.headerCarved = createCarvedHeader();
    this.headerTextElement = createTitleHeader(headerText);

    this.headerRightElement = new HorizontalLayout(HEADER_SPACING)
      .setOriginAndAlign(Relative.RIGHT)
      .setPosX(-AbstractMenu.DEFAULT_ELEMENT_PADDING);
    this.headerCarved.addChild(headerRightElement);

    this.backButton = createBackButton();

    addChild(backButton, headerCarved);

    updateSize();
  }

  public void setMenuSizeX(double menuSizeX) {
    this.menuSizeX = menuSizeX;
    updateSize();
  }

  public void updateSize() {
    double sizeX = menuSizeX;
    if (backButton.isEnabled()) sizeX -= AbstractMenu.DEFAULT_HEADER_HEIGHT + HEADER_SPACING;
    this.headerCarved.setSize(sizeX, AbstractMenu.DEFAULT_HEADER_HEIGHT, 0);
  }

  private CarvedRectangle createCarvedHeader() {
    if (headerCarved != null)
      return headerCarved;

    return new CarvedRectangle()
      .setColor(AbstractMenu.MENU_COLOR);
  }

  private Text createTitleHeader(String text) {
    if (headerTextElement != null)
      return headerTextElement.setValue(text);

    Text textElement = new Text(text)
      .setOriginAndAlign(Relative.LEFT)
      .setPosX(AbstractMenu.DEFAULT_ELEMENT_PADDING);

    headerCarved.addChild(textElement);

    return textElement;
  }

  public void addRightTitle(AbstractElement<?>... elements) {
    this.headerRightElement.addChild(elements);
  }


  private Button createBackButton() {
    if (backButton != null)
      return backButton;

    Button button = new Button("\uE40C")
      .setButtonColor(ButtonColor.BLUE)
      .setSize(AbstractMenu.DEFAULT_BUTTON_SIZE)
      .setOriginAndAlign(Relative.TOP_LEFT);

    button.registerEvent(ButtonLeftActionEvent.class, e -> {
      menu.close();
    });

    button.getTextElement().setPosX(2);
    return button;
  }

}
