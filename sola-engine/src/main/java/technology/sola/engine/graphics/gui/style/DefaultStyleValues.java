package technology.sola.engine.graphics.gui.style;

import technology.sola.engine.assets.graphics.font.DefaultFont;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.gui.elements.TextStyles;
import technology.sola.engine.graphics.gui.style.property.*;

public final class DefaultStyleValues {
  public static final Direction DIRECTION = Direction.COLUMN;

  public static final Visibility VISIBILITY = Visibility.VISIBLE;

  public static final Position POSITION = Position.NONE;

  public static final MainAxisChildren MAIN_AXIS_CHILDREN = MainAxisChildren.START;

  public static final CrossAxisChildren CROSS_AXIS_CHILDREN = CrossAxisChildren.START;

  public static final Border BORDER = Border.NONE;

  public static final Padding PADDING = Padding.NONE;

  public static final StyleValue WIDTH = StyleValue.FULL;

  public static final StyleValue HEIGHT = StyleValue.FULL;

  public static final TextStyles.TextAlignment TEXT_ALIGNMENT = TextStyles.TextAlignment.START;

  public static final Color TEXT_COLOR = Color.BLACK;

  public static final String FONT_ASSET_ID = DefaultFont.ASSET_ID;

  private DefaultStyleValues() {
  }
}
