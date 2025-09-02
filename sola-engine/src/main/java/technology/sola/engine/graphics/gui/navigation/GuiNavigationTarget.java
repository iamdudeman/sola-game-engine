package technology.sola.engine.graphics.gui.navigation;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.gui.GuiElement;

// todo docs

@NullMarked
public interface GuiNavigationTarget<Element extends GuiElement<?, Element>, Payload> {
  Class<Payload> getPayloadType();

  Element element();

  void onNavigate(Element element, Payload payload);
}
