package gg.cristalix.growagarden.common.menu.element;

import gg.cristalix.enginex.color.palette.ButtonColor;
import gg.cristalix.enginex.element.AbstractElement;
import gg.cristalix.enginex.element.Text;
import gg.cristalix.enginex.element.button.Button;
import gg.cristalix.enginex.element.carved.CarvedRectangle;
import gg.cristalix.enginex.element.layout.type.HorizontalLayout;
import gg.cristalix.enginex.event.element.ButtonLeftActionEvent;
import gg.cristalix.enginex.math.Relative;
import gg.cristalix.enginex.math.V3;
import gg.cristalix.growagarden.common.menu.AbstractMenu;
import gg.cristalix.growagarden.common.menu.IAbstractMenu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HeaderElement extends HorizontalLayout {

  public static final double HEADER_SPACING = 8;

  final IAbstractMenu menu;

  @Setter
  V3 menuSize;

  final CarvedRectangle headerCarved;
  final Text headerTextElement;
  final HorizontalLayout headerRightElement;

  //Button backButton;
  final Button closeButton;

  @Setter
  @NonFinal
  boolean navigationButtonsEnabled = false;

  public HeaderElement(IAbstractMenu menu, V3 menuSize, String headerText) {
    this.menu = menu;
    this.menuSize = menuSize;

    setSpacing(HEADER_SPACING);

    this.headerCarved = createCarvedHeader();
    this.headerTextElement = createTitleHeader(headerText);

    this.headerRightElement = new HorizontalLayout(HEADER_SPACING)
      .setOriginAndAlign(Relative.RIGHT)
      .setPosX(-AbstractMenu.DEFAULT_ELEMENT_PADDING);
    this.headerCarved.addChild(headerRightElement);

    //this.backButton = createBackButton();
    this.closeButton = createCloseButton();

    addChild(headerCarved, closeButton);

    updateSize();
  }

  public void updateSize() {
    double sizeX = menuSize.getX();

    //if (backButton.isEnabled()) sizeX -= AbstractMenu.DEFAULT_HEADER_HEIGHT + HEADER_SPACING;
    if (closeButton.isEnabled()) sizeX -= AbstractMenu.DEFAULT_HEADER_HEIGHT + HEADER_SPACING;

    this.headerCarved.setSize(sizeX, AbstractMenu.DEFAULT_HEADER_HEIGHT, 0);
  }

  public void updateNavigationButtons() {
    //backButton.setEnabled(false);
    closeButton.setEnabled(false);

    if (!navigationButtonsEnabled) return;

//    Deque<AbstractMenu<?>> history = menuManager.getMenuHistory();
//    if (history != null && history.size() > 1) {
//      rootAddChild(backButton);
//    }

    //backButton.setEnabled(true);
    closeButton.setEnabled(true);

    updateSize();
  }

  private CarvedRectangle createCarvedHeader() {
    if (headerCarved != null)
      return headerCarved;

    return new CarvedRectangle()
      .setColor(AbstractMenu.MENU_COLOR);
    // .setOutlineColor(AbstractMenu.MENU_COLOR_OUTLINE);
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

  public void clearChildrenRightTitle() {
    this.headerRightElement.clearChildren();
  }
  //  private Button createBackButton() {
//    if (backButton != null)
//      return backButton;
//
//    Button button = new Button("\uE40C")
//      .setSize(AbstractMenu.DEFAULT_BUTTON_SIZE);
//
//    button.registerEvent(ButtonLeftActionEvent.class, e -> menu.goBack());
//
//    return button;
//  }

  private Button createCloseButton() {
    if (closeButton != null)
      return closeButton;

    Button button = new Button("\uE0EC")
      .setButtonColor(ButtonColor.RED)
      .setSize(AbstractMenu.DEFAULT_BUTTON_SIZE)
      .setOriginAndAlign(Relative.TOP_RIGHT);

    button.registerEvent(ButtonLeftActionEvent.class, e -> {
      menu.onClose();
    });

    button.getTextElement().setPosX(2);
    return button;
  }

}
