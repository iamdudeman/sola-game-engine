package technology.sola.engine.assets.graphics.font;

import java.util.List;

/**
 * FontInfo contains all the information needed to render strings for a {@link Font}.
 *
 * @param fontGlyphFile the file containing the rasterized glyphs
 * @param fontFamily    the family of the font
 * @param fontStyle     the {@link FontStyle} of the font
 * @param fontSize      the size of the font
 * @param leading       the logical amount of space to be reserved between the descent of one line of text and the ascent of the next line.
 * @param glyphs        the {@link List} of {@link FontGlyph}s
 */
public record FontInfo(
  String fontGlyphFile, String fontFamily,
  FontStyle fontStyle, int fontSize, int leading,
  List<FontGlyph> glyphs) {
}
