package technology.sola.engine.assets.graphics.font.exception;

import technology.sola.engine.assets.graphics.font.FontInfo;

import java.io.Serial;

public class MissingGlyphException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 2280054643044235363L;

  public MissingGlyphException(char character, FontInfo fontInfo) {
    super("Glyph for character [" + character + "] is not in Font [" + fontInfo.fontName() + "]");
  }
}
