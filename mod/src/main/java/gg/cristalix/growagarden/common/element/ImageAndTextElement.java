package gg.cristalix.growagarden.common.element;

import dev.xdark.clientapi.resource.ResourceLocation;
import gg.cristalix.enginex.color.palette.Palette;
import gg.cristalix.enginex.element.AbstractElement;
import gg.cristalix.enginex.element.Image;
import gg.cristalix.enginex.element.Text;
import gg.cristalix.enginex.element.layout.AbstractLayout;
import gg.cristalix.enginex.event.element.LayoutRescaleEvent;
import gg.cristalix.enginex.math.Relative;
import gg.cristalix.enginex.math.V3;
import gg.cristalix.growagarden.util.TextureLoader;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageAndTextElement extends AbstractLayout<ImageAndTextElement> {
  Image imageLeft = new Image()
    .setSize(16, 16, 0)
    .setAlign(Relative.CENTER);
  Text textRight = new Text();

  double spacing = 4;

  private ImageAndTextElement() {
    this.setColor(Palette.WHITE_0);
    addChild(imageLeft, textRight);
    setOriginAndAlign(Relative.CENTER);
  }

  public ImageAndTextElement(ResourceLocation left, Object right) {
    this();
    if (right == null) right = "";

    String rightAsString = right.toString();

    setLeftValue(left);
    textRight.setValue(rightAsString);
  }

  public ImageAndTextElement(String left, Object right) {
    this();
    if (left == null) left = "";
    if (right == null) right = "";

    String rightAsString = right.toString();

    setLeftValue(left);
    textRight.setValue(rightAsString);
  }

  public ImageAndTextElement setLeftValue(ResourceLocation location) {
    this.imageLeft.setTexture(location);
    return this;
  }

  public ImageAndTextElement setLeftValue(String leftValue) {
    if (leftValue != null && !leftValue.isEmpty()) TextureLoader.setTexture(leftValue, imageLeft::setTexture);
    return this;
  }

  public ImageAndTextElement setRightValue(String rightValue) {
    this.textRight.setValue(rightValue);
    return this;
  }

  public void rescale() {
    if (this.children.isEmpty()) {
      this.setSize(this.inset.multiply(2.0F));
      this.fireEvent(new LayoutRescaleEvent(this));
    } else {
      double currentOffset = 0.0F;
      double maxY = 0.0F;

      for (AbstractElement<?> child : this.children) {
        if (child.isEnabled()) {
          V3 currentOrigin = this.childOrigin == null ? this.getOrigin() : this.childOrigin;
          double offsetX = child.getScaledSize().getX() * child.getOrigin().getX();
          double offsetY = this.inset.getY() * 2.0F * (0.5F - currentOrigin.getY());
          offsetY += (child.getOrigin().getY() - currentOrigin.getY()) * child.getScaledSize().getY();
          child.setOffset(new V3(currentOffset + offsetX + this.inset.getX(), offsetY, 0.0F));
          child.setAlign(currentOrigin.setX(0.0F));
          double sizeY = child.getScaledSize().getY();
          if (sizeY > maxY) {
            maxY = sizeY;
          }

          currentOffset += child.getScaledSize().getX() + this.spacing;
        }
      }

      this.setSize(new V3(currentOffset - this.spacing + this.inset.getX() * 2.0F, maxY + this.inset.getY() * 2.0F));
      this.fireEvent(new LayoutRescaleEvent(this));
    }
  }
}
