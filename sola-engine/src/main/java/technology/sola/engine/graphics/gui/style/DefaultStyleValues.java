package technology.sola.engine.graphics.gui.style;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.gui.style.property.*;

/**
 * Class containing constants for default style values for various properties used
 * by {@link technology.sola.engine.graphics.gui.GuiElement} for rendering and layout calculations.
 */
@NullMarked
public final class DefaultStyleValues {
  /**
   * Default layout value for {@link BaseStyles#direction()}.
   */
  public static final Direction DIRECTION = Direction.COLUMN;

  /**
   * Default layout and rendering value for {@link BaseStyles#visibility()}.
   */
  public static final Visibility VISIBILITY = Visibility.VISIBLE;

  /**
   * Default layout value for {@link BaseStyles#position()}.
   */
  public static final Position POSITION = Position.NONE;

  /**
   * Default layout value for {@link BaseStyles#mainAxisChildren()}.
   */
  public static final MainAxisChildren MAIN_AXIS_CHILDREN = MainAxisChildren.START;

  /**
   * Default layout value for {@link BaseStyles#crossAxisChildren()}.
   */
  public static final CrossAxisChildren CROSS_AXIS_CHILDREN = CrossAxisChildren.START;

  /**
   * Default layout and rendering value for {@link BaseStyles#border()}.
   */
  public static final Border BORDER = Border.NONE;

  /**
   * Default layout value for {@link BaseStyles#padding()} ()}.
   */
  public static final Padding PADDING = Padding.NONE;

  /**
   * Default layout value for {@link BaseStyles#width()}.
   */
  public static final StyleValue WIDTH = StyleValue.FULL;

  /**
   * Default layout value for {@link BaseStyles#height()}.
   */
  public static final StyleValue HEIGHT = StyleValue.FULL;

  /**
   * Default rendering value for {@link TextStyles#textAlignment()}.
   */
  public static final TextStyles.TextAlignment TEXT_ALIGNMENT = TextStyles.TextAlignment.START;

  /**
   * Default rendering value for {@link TextStyles#textColor()}.
   */
  public static final Color TEXT_COLOR = Color.BLACK;

  /**
   * Default rendering value for {@link TextStyles#fontAssetId()}.
   */
  public static final String FONT_ASSET_ID = DefaultFont.ASSET_ID;

  private DefaultStyleValues() {
  }
}
