package gg.cristalix.growagarden.common.element.tabs;

import gg.cristalix.enginex.animation.Animation;
import gg.cristalix.enginex.color.Color;
import gg.cristalix.enginex.color.palette.Palette;
import gg.cristalix.enginex.element.AbstractElement;
import gg.cristalix.enginex.element.Rectangle;
import gg.cristalix.enginex.element.Text;
import gg.cristalix.enginex.element.layout.type.HorizontalLayout;
import gg.cristalix.enginex.event.HoverEvent;
import gg.cristalix.enginex.event.input.MouseLeftClickEvent;
import gg.cristalix.enginex.math.Relative;
import gg.cristalix.enginex.sound.Sound;
import gg.cristalix.enginex.sound.SoundPlayer;
import gg.cristalix.growagarden.common.element.tabs.event.CustomTabsUpdateEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CustomTabs extends AbstractElement<CustomTabs> {
  private static final double ANIMATION_DURATION = 0.26;
  private static final double COLOR_CHANGE_DURATION = 0.13;
  private static final double LINE_WIDTH_MIN = 18;
  private static final double LINE_WIDTH_MAX = 76;

  @Getter
  private HorizontalLayout container;
  private Rectangle bottomLine;

  @Getter
  private CustomTabsElement selected;
  @Getter
  private int selectedIndex = 0;
  @Getter
  private Color selectedColor = Palette.BLUE_LIGHT;
  @Getter
  private Color defaultColor = Palette.WHITE;
  @Getter
  private double spacing = 10;

  private CustomTabs() {
  }

  public CustomTabs(double spacing, String... titles) {
    this(spacing, Arrays.asList(titles));
  }

  public CustomTabs(double spacing, Collection<String> titles) {
    this.spacing = spacing;
    setElements(titles);
  }

  public CustomTabs(double spacing, CustomTabsElement... elements) {
    this(spacing, Arrays.asList(elements));
  }

  public CustomTabs(double spacing, List<CustomTabsElement> elements) {
    this.spacing = spacing;
    initElements(elements);
  }

  public void setElements(Collection<String> titles) {
    List<CustomTabsElement> elements = new ArrayList<>();

    for (String title : titles) {
      elements.add(new CustomTabsElement(title));
    }

    initElements(elements);
  }

  public void setElements(List<CustomTabsElement> elements) {
    initElements(elements);
  }

  private void initElements(Collection<CustomTabsElement> elements) {
    this.container = new HorizontalLayout(spacing);
    addChild(this.container);

    elements.forEach(this::initTabElement);

    this.bottomLine = new Rectangle()
      .setAlign(Relative.BOTTOM_LEFT)
      .setOrigin(Relative.BOTTOM)
      .setSize(LINE_WIDTH_MIN, 2, 0)
      .setColor(selectedColor);

    addChild(bottomLine);

    setSize(container.getSize().getX(), 22, 0);
    setSelected(selectedIndex, false, false);
    setStartPosLine();
  }

  private void initTabElement(CustomTabsElement element) {
    container.addChild(element);
    Text tabsText = element.getTextElement();

    tabsText.registerEvent(MouseLeftClickEvent.class, event -> handleTabClick(element));
    tabsText.registerEvent(HoverEvent.class, event -> handleTabHover(element, event));
  }

  private void handleTabClick(CustomTabsElement element) {
    if (!element.isActive()) return;

    int clickedIndex = getElementIndex(element);
    if (clickedIndex != selectedIndex) {
      SoundPlayer.play(Sound.RANDOM_CLICK, 0.6f);
      setSelected(clickedIndex);
    }
  }

  private void handleTabHover(CustomTabsElement element, HoverEvent event) {
    if (!element.isActive()) return;

    int hoveredIndex = getElementIndex(element);
    if (hoveredIndex == selectedIndex) return;

    Color targetColor = event.isHover() ? selectedColor : defaultColor;
    smoothChangeColor(element.getTextElement(), targetColor);
  }

  private void updateSize() {
    setSize(container.getSize().getX(), 22, 0);
    setStartPosLine();
  }


  public CustomTabs setSelectedColor(Color color) {
    this.selectedColor = color;
    bottomLine.setColor(color);
    return this;
  }

  public CustomTabs setDefaultColor(Color color) {
    this.defaultColor = color;
    return this;
  }

  public CustomTabs setSpacing(double spacing) {
    container.setSpacing(spacing);
    updateSize();
    return this;
  }

  public CustomTabsElement getElement(int index) {
    return (CustomTabsElement) container.getChildren().get(index);
  }

  public void setActive(int index, boolean active) {
    CustomTabsElement element = getElement(index);
    if (element != null) {
      element.setActive(active);
    }
  }

  public CustomTabs setSelected(int index) {
    return setSelected(index, true, true);
  }

  public CustomTabs setSelected(int index, boolean shouldTriggerEvent) {
    return setSelected(index, shouldTriggerEvent, true);
  }

  public CustomTabs setSelected(int index, boolean shouldTriggerEvent, boolean animated) {
    CustomTabsElement element = getElement(index);
    return setSelected(element, shouldTriggerEvent, animated);
  }

  public CustomTabs setSelectedInstant(int index) {
    return setSelected(index, true, false);
  }

  public CustomTabs setSelectedInstant(int index, boolean shouldTriggerEvent) {
    return setSelected(index, shouldTriggerEvent, false);
  }

  public CustomTabs setSelected(CustomTabsElement element) {
    return setSelected(element, true, true);
  }

  public CustomTabs setSelected(CustomTabsElement element, boolean shouldTriggerEvent) {
    return setSelected(element, shouldTriggerEvent, true);
  }

  public CustomTabs setSelectedInstant(CustomTabsElement element) {
    return setSelected(element, true, false);
  }

  public CustomTabs setSelectedInstant(CustomTabsElement element, boolean shouldTriggerEvent) {
    return setSelected(element, shouldTriggerEvent, false);
  }

  public CustomTabs setSelected(CustomTabsElement element, boolean shouldTriggerEvent, boolean animated) {
    if (element == null || element.equals(this.selected)) return this;
    if (!element.isActive()) return this;

    if (shouldTriggerEvent) {
      CustomTabsUpdateEvent event = new CustomTabsUpdateEvent(this, getElementIndex(element));
      fireEvent(event);
      if (event.isCancelled()) {
        return this;
      }
    }

    if (this.selected != null) {
      Color color = selected.isActive() ? defaultColor : defaultColor.alpha(0.62);
      selected.getTextElement().setColor(color);
    }

    this.selected = element;
    this.selectedIndex = getElementIndex(element);

    changeLinePosition(element, animated);
    selected.getTextElement().setColor(selectedColor);

    return this;
  }

  public int getElementIndex(CustomTabsElement element) {
    return container.getChildren().indexOf(element);
  }

  @Override
  public void render(double partialTicks, double mouseX, double mouseY) {

  }

  private void changeLinePosition(CustomTabsElement target, boolean animated) {
    double targetX = target.getOffset().getX() + target.getSize().getX() / 2;
    double startX = bottomLine.getPos().getX();
    double delta = targetX - startX;

    if (animated) {
      Animation.play(bottomLine, "move", ANIMATION_DURATION, Animation.DEFAULT_OUT_ANIMATION,
        (element, progress) -> {
          element.setPosX(startX + delta * progress);
          double newWidth = progress < 0.5
            ? LINE_WIDTH_MIN + (LINE_WIDTH_MAX - LINE_WIDTH_MIN) * progress * 2
            : LINE_WIDTH_MAX - (LINE_WIDTH_MAX - LINE_WIDTH_MIN) * (progress - 0.5) * 2;
          element.setSize(newWidth, 2, 0);
        });
    } else {
      bottomLine.setPosX(targetX);
    }
  }

  private void setStartPosLine() {
    double targetX = selected.getOffset().getX() + selected.getSize().getX() / 2;
    bottomLine.setPosX(targetX);
  }

  private void smoothChangeColor(Text tab, Color target) {
    Animation.stop(tab, "tabs_color");
    Color current = tab.getColor();
    if (current.equals(target)) return;

    Animation.play(tab, "tabs_color", COLOR_CHANGE_DURATION, Animation.DEFAULT_OUT_ANIMATION,
        (element, progress) -> element.setColor(current.interpolateProgressColor(target, progress)))
      .onComplete(text -> {
        text.setColor(target);
        markDirty();
      });
  }

  @Override
  public CustomTabs copy(CustomTabs element) {
    super.copy(element);
    element.setSpacing(getSpacing());
    element.setSelectedColor(getSelectedColor());
    element.setDefaultColor(getDefaultColor());
    return element;
  }

  @Override
  public CustomTabs clone() {
    return copy(new CustomTabs());
  }
}