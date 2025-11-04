package technology.sola.engine.graphics.modules;

/**
 * Contains render order constants for {@link SolaGraphicsModule}s.
 */
public class RenderOrders {
  /**
   * The default render order for {@link SolaGraphicsModule}s.
   */
  public static final int DEFAULT = 0;

  /**
   * The render order for {@link ScreenSpaceLightMapGraphicsModule}.
   */
  public static final int SCREEN_SPACE_LIGHT_MAP = Integer.MAX_VALUE - 10;

  /**
   * The render order for {@link GuiDocumentGraphicsModule}.
   */
  public static final int GUI = SCREEN_SPACE_LIGHT_MAP + 1;

  /**
   * The render order for {@link technology.sola.engine.debug.DebugGraphicsModule}.
   */
  public static final int DEBUG = GUI + 1;

  private RenderOrders() {
  }
}
