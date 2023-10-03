package technology.sola.engine.graphics.guiv2.json.exception;

import java.io.Serial;

/**
 * Exception thrown when reading a GUI JSON file that uses a tag that has not been registered in
 * the {@link technology.sola.engine.graphics.guiv2.json.GuiJsonDocumentBuilder}.
 */
public class MissingGuiElementJsonBlueprintException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -7182205961188768948L;

  /**
   * Creates an instance of this exception for the tag that was not registered.
   *
   * @param tag the element tag
   */
  public MissingGuiElementJsonBlueprintException(String tag) {
    super("GuiElementJsonBlueprint with tag " + tag + " was not found.");
  }
}
