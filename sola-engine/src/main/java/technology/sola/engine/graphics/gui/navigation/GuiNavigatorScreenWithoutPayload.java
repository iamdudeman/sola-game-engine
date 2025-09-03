package technology.sola.engine.graphics.gui.navigation;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.graphics.gui.GuiElement;

/**
 * GuiNavigatorScreenWithoutPayload is a screen that can be navigated to via {@link GuiNavigator#navigate}. This screen
 * does not receive a payload when navigated to.
 *
 * @param <Element> the {@link GuiElement} type for the root element
 */
@NullMarked
public interface GuiNavigatorScreenWithoutPayload<Element extends GuiElement<?, Element>> extends GuiNavigationScreen<Element, Void> {
  /**
   * Called when a user navigates to the screen.
   *
   * @param element the root element of the screen
   */
  void onNavigate(Element element);

  @Override
  default void onNavigate(Element element, Void unused) {
    onNavigate(element);
  }

  @Override
  default Class<Void> getPayloadType() {
    return Void.class;
  }
}
