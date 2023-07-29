package technology.sola.engine.assets.graphics.font.exception;

import technology.sola.engine.assets.graphics.font.FontInfo;

import java.io.Serial;

/**
 * Exception thrown when attempting to render a {@link technology.sola.engine.assets.graphics.font.FontGlyph} that is
 * not present in the rasterized {@link technology.sola.engine.assets.graphics.font.Font}.
 */
public class MissingGlyphException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 2280054643044235363L;

  /**
   * Creates an instance of this exception.
   *
   * @param character the character for the {@link technology.sola.engine.assets.graphics.font.FontGlyph}
   * @param fontInfo  the {@link FontInfo} for the current {@link technology.sola.engine.assets.graphics.font.Font}
   */
  public MissingGlyphException(char character, FontInfo fontInfo) {
    super("Glyph for character [" + character + "] is not in Font [" + fontInfo.fontFamily() + "]");
  }
}
