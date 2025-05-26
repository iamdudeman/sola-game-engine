package technology.sola.engine.graphics.gui;

import org.jspecify.annotations.NullMarked;

/**
 * GuiElementDimensions holds the width and height of a {@link GuiElement} measurement.
 *
 * @param width  the width
 * @param height the height
 */
@NullMarked
public record GuiElementDimensions(int width, int height) {
}
