package technology.sola.engine.graphics.gui.navigation;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.gui.GuiElement;

/**
 * GuiNavigationScreen is a screen that can be navigated to via {@link GuiNavigator#navigate}. Screens define a
 * {@link Payload} type that they receive when a user navigates to them.
 *
 * @param <Element> the {@link GuiElement} type for the root element
 * @param <Payload> the payload type for the screen
 */
@NullMarked
public interface GuiNavigationScreen<Element extends GuiElement<?, Element>, Payload> {
  /**
   * @return the payload type for the screen
   */
  Class<Payload> getPayloadType();

  /**
   * @return the root element for the screen
   */
  Element rootElement();

  /**
   * Called when a user navigates to the screen.
   *
   * @param element the root element of the screen
   * @param payload the payload sent with the navigation
   */
  void onNavigate(Element element, Payload payload);
}
