package technology.sola.engine.graphics.gui.navigation;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.gui.GuiDocument;
import technology.sola.engine.graphics.gui.GuiElement;

import java.util.HashMap;
import java.util.Map;

// todo docs

@NullMarked
public class GuiNavigator {
  private final GuiDocument guiDocument;
  private final Map<Class<?>, GuiNavigationTarget> navigationTargets = new HashMap<>();

  public GuiNavigator(GuiDocument guiDocument) {
    this.guiDocument = guiDocument;
  }

  public <T extends GuiNavigationTarget<?, ?>> void register(GuiNavigationTarget navigationTarget) {
    navigationTargets.put(navigationTarget.getClass(), navigationTarget);
  }

  public <Element extends GuiElement<?, Element>, Payload, T extends GuiNavigationTarget<Element, Payload>> void navigate(Class<T> navigationTargetClass, Payload payload) {
    GuiNavigationTarget<Element, Payload> navigationTarget = navigationTargets.get(navigationTargetClass);
    Element element = navigationTarget.element();

    guiDocument.setRootElement(element);
    navigationTarget.onNavigate(element, navigationTarget.getPayloadType().cast(payload));
  }
}
