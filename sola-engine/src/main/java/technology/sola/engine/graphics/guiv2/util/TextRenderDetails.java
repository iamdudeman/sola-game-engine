package technology.sola.engine.graphics.guiv2.util;

import technology.sola.engine.graphics.guiv2.GuiElementDimensions;

import java.util.List;

/**
 * TextRenderDetails contains the rendering information needed for text.
 *
 * @param lineHeight the line height
 * @param lines      the string of text in each line
 * @param dimensions the dimensions of the text for rendering
 */
public record TextRenderDetails(int lineHeight, List<String> lines, GuiElementDimensions dimensions) {
}
