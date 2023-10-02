package technology.sola.engine.assets.graphics.gui;

import technology.sola.engine.assets.Asset;
import technology.sola.engine.graphics.guiv2.GuiElement;

public record GuiJsonDocument(GuiElement<?> root) implements Asset {
}
