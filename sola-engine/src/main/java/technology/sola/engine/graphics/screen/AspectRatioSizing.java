package technology.sola.engine.graphics.screen;

/**
 * AspectRatioSizing contains the top, left coordinate for where rendering will begin as well as the width and height
 * of the space to render in.
 *
 * @param x      the x coordinate where rendering begins
 * @param y      the y coordinate where rendering begins
 * @param width  the width to render within
 * @param height the height to render within
 */
public record AspectRatioSizing(int x, int y, int width, int height) {
}
