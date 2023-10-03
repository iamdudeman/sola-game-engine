package technology.sola.engine.assets.graphics.gui;

import technology.sola.engine.assets.Asset;
import technology.sola.engine.graphics.guiv2.GuiElement;

/**
 * GuiJsonDocument as an {@link Asset} containing a {@link GuiElement} and its children that were loaded from a JSON file.
 *
 * @param root the root {@code GuiElement}
 */
public record GuiJsonDocument(GuiElement<?> root) implements Asset {
}
