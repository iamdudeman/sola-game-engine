package technology.sola.engine.assets.graphics.font;

/**
 * FontGlyph holds information on a glyph's placement in the rasterized {@link Font}
 *
 * @param glyph  the rasterized glyph
 * @param x      the left coordinate of the glyph
 * @param y      the top coordinate of the glyph
 * @param width  the width of the glyph
 * @param height the height of the glyph
 */
public record FontGlyph(char glyph, int x, int y, int width, int height) {
}
