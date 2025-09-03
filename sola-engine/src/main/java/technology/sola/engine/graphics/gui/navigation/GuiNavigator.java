package technology.sola.engine.graphics.gui.navigation;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.gui.GuiDocument;
import technology.sola.engine.graphics.gui.GuiElement;

import java.util.HashMap;
import java.util.Map;

/**
 * GuiNavigator is a class that manages navigation between {@link GuiNavigationScreen}s.
 */
@NullMarked
public class GuiNavigator {
  private final GuiDocument guiDocument;
  private final Map<Class<?>, GuiNavigationScreen<?, ?>> navigationScreenMap = new HashMap<>();

  /**
   * Creates an instance for a {@link GuiDocument}.
   *
   * @param guiDocument the {@link GuiDocument} that will update when navigating
   */
  public GuiNavigator(GuiDocument guiDocument) {
    this.guiDocument = guiDocument;
  }

  /**
   * Registers a {@link GuiNavigationScreen} to be navigated to.
   *
   * @param navigationScreen the {@code GuiNavigationScreen} to register
   */
  public void register(GuiNavigationScreen<?, ?> navigationScreen) {
    navigationScreenMap.put(navigationScreen.getClass(), navigationScreen);
  }

  /**
   * Navigates to a registered {@link GuiNavigatorScreenWithoutPayload}.
   *
   * @param navigationScreenClass the class of the {@code GuiNavigatorScreenWithoutPayload} to navigate to
   * @param <Element>             the type of {@link GuiElement} the {@code GuiNavigatorScreenWithoutPayload} uses
   * @param <T>                   the type of {@code GuiNavigatorScreenWithoutPayload} to navigate to
   */
  public <Element extends GuiElement<?, Element>, T extends GuiNavigatorScreenWithoutPayload<Element>> void navigate(
    Class<T> navigationScreenClass
  ) {
    @SuppressWarnings("unchecked")
    var navigationTarget = (GuiNavigatorScreenWithoutPayload<Element>) navigationScreenMap.get(navigationScreenClass);

    if (navigationTarget == null) {
      throw new IllegalArgumentException("No navigation screen registered for class: " + navigationScreenClass.getName());
    }

    var element = navigationTarget.rootElement();

    guiDocument.setRootElement(element);
    navigationTarget.onNavigate(element);
  }

  /**
   * Navigates to a registered {@link GuiNavigationScreen} passing it a payload.
   *
   * @param navigationScreenClass the class of the {@code GuiNavigationScreen} to navigate to
   * @param payload               the payload to pass to the {@code GuiNavigationScreen}
   * @param <Element>             the type of {@link GuiElement} the {@code GuiNavigationScreen} uses
   * @param <Payload>             the type of payload to pass to the {@code GuiNavigationScreen}
   * @param <T>                   the type of {@code GuiNavigationScreen} to navigate to
   */
  public <Element extends GuiElement<?, Element>, Payload, T extends GuiNavigationScreen<Element, Payload>> void navigate(
    Class<T> navigationScreenClass,
    Payload payload
  ) {
    @SuppressWarnings("unchecked")
    var navigationTarget = (GuiNavigationScreen<Element, Payload>) navigationScreenMap.get(navigationScreenClass);

    if (navigationTarget == null) {
      throw new IllegalArgumentException("No navigation screen registered for class: " + navigationScreenClass.getName());
    }

    var element = navigationTarget.rootElement();

    guiDocument.setRootElement(element);
    navigationTarget.onNavigate(element, navigationTarget.getPayloadType().cast(payload));
  }
}
