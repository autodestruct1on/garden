package gg.cristalix.growagarden.common.menu;

import dev.xdark.clientapi.gui.Screen;
import gg.cristalix.enginex.color.Color;
import gg.cristalix.enginex.color.palette.ButtonColor;
import gg.cristalix.enginex.color.palette.Palette;
import gg.cristalix.enginex.element.AbstractElement;
import gg.cristalix.enginex.element.Container;
import gg.cristalix.enginex.element.screen.type.GuiScreenResize;
import gg.cristalix.enginex.math.V3;
import gg.cristalix.growagarden.common.menu.element.HeaderElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.lwjgl.input.Keyboard;

import java.util.UUID;


@Getter
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractMenu<T extends AbstractElement<T>> extends GuiScreenResize<Container> implements IAbstractMenu {
  public static final V3 DEFAULT_SIZE = new V3(1244, 658);
  public static final V3 DEFAULT_LITTLE_SIZE = new V3(622, 658);
  public static final V3 DEFAULT_BUTTON_SIZE = new V3(38, 38);
  public static final double DEFAULT_SPACING = 10;
  public static final double DEFAULT_SPACING_ELEMENT = 8;
  public static final double DEFAULT_ELEMENT_PADDING = 14;
  public static final double DEFAULT_HEADER_HEIGHT = 38;
  public static final double DEFAULT_FOOTER_HEIGHT = 38;

//  public static final Color BACKGROUND_COLOR = new Color(18, 18, 18, 1);
//  public static final Color BACKGROUND_OUTLINE_COLOR = Palette.GRAY_MIDDLE_62;

  //public static final Color MENU_COLOR = Palette.BACKGROUND_COLOR.alpha(0.82);
  //public static final Color MENU_COLOR_OUTLINE = Palette.getOutlineColor(MENU_COLOR);

  public static final Color MENU_COLOR = Palette.getOutlineColor(Palette.BACKGROUND_COLOR.alpha(0.82));
  public static final Color MENU_COLOR_OUTLINE = Palette.getOutlineColor(MENU_COLOR);
  //public static final ButtonColor MENU_COLOR_BUTTON = ButtonColor.BACKGROUND;

  //  public static final Color MENU_COLOR = BACKGROUND_COLOR;
//  public static final Color MENU_COLOR_OUTLINE = ColorUtil.lighten(BACKGROUND_COLOR, 80);
  public static final ButtonColor MENU_COLOR_BUTTON = ButtonColor.createColor("BACKGROUND_NEW", Palette.getOutlineColor(new Color(22, 24, 28, 1.0)), Palette.getOutlineColor(new Color(35, 37, 43, 1.0)), Palette.getOutlineColor(new Color(46, 49, 56, 1.0)), Palette.getOutlineColor(new Color(22, 24, 28, 0.86)));
  // public static final ButtonColor MENU_COLOR_BUTTON = ButtonColor.createColor("BACKGROUND_NEW", new Color(18, 18, 18, 1.0), new Color(31, 31, 33, 1.0), new Color(42, 43, 46, 1.0), new Color(18, 18, 18, 0.86));

  UUID menuId = UUID.randomUUID();

  HeaderElement header;
  T body;

  protected AbstractMenu(String headerText, V3 size) {
    super(new Container().setSize(size));

    setColor(Palette.BLACK.alpha(0.74));
//    setBlur(2);
    setModalAlpha(0.62);

    this.header = new HeaderElement(this, size, headerText);
    this.body = createBodyElement();

    setNavigationButtonsEnabled(true);

    rootAddChild(header, body);
  }

  protected abstract T createBodyLayout();

  private T createBodyElement() {
    T element = createBodyLayout();
    return element.setPosY(DEFAULT_HEADER_HEIGHT + DEFAULT_ELEMENT_PADDING);
  }

  public void setNavigationButtonsEnabled(boolean enable) {
    this.header.setNavigationButtonsEnabled(enable);
  }

  public T bodyAddChild(AbstractElement<?>... abstractElement) {
    return this.body.addChild(abstractElement);
  }

  public T bodyClearChildren() {
    return this.body.clearChildren();
  }

  @Override
  public void keyTyped(Screen screen, char c, int code) {
    if (code == Keyboard.KEY_ESCAPE) {
      if (tryCloseTopModalOnEscape()) return;
      if (goBack()) return;
      if (!isPlayerCloseDisabled()) {
        close();
        return;
      }
    }
    super.keyTyped(screen, c, code);
  }

  @Override
  public void openMenu() {
    header.updateNavigationButtons();
    super.open();
  }

  @Override
  @Deprecated()
  public void open() {
    super.open();
  }

  public void onClose() {
    close();
  }

  public boolean goBack() {
//    if (!menuManager.isClickAllowed()) return true;
//
//    Deque<AbstractMenu<?>> history = menuManager.getMenuHistory();
//    if (history != null && history.size() > 1) {
//      menuManager.getWada().getChannelManager().send("menu:back", new ModTransfer());
//      return true;
//    }

    return false;
  }


}
