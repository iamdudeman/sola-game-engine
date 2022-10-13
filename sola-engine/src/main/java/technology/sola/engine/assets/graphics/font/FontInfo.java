package technology.sola.engine.assets.graphics.font;

import java.util.List;

public record FontInfo(
  String fontGlyphFile, String fontFamily,
  FontStyle fontStyle, int fontSize, int leading,
  List<FontGlyph> glyphs) {
}
