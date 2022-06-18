package technology.sola.engine.graphics.font;

import java.util.List;

public record FontInfo(
  String fontGlyphFile, String fontName,
  FontStyle fontStyle, int fontSize, int leading,
  List<FontGlyph> glyphs) {
}
