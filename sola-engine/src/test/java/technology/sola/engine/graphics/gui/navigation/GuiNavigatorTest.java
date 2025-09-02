package technology.sola.engine.graphics.gui.navigation;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.engine.graphics.gui.GuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;

import static org.junit.jupiter.api.Assertions.assertEquals;

@NullMarked
class GuiNavigatorTest {
  @Test
  void test() {
    GuiDocument mockGuiDocument = Mockito.mock(GuiDocument.class);
    GuiNavigator guiNavigator = new GuiNavigator(mockGuiDocument);

    TextGuiElement element = new TextGuiElement();

    guiNavigator.register(new TestGuiNavigationTarget(element));

    guiNavigator.navigate(TestGuiNavigationTarget.class, new TestPayload("test"));

    Mockito.verify(mockGuiDocument).setRootElement(element);

    assertEquals("test", element.getText());
  }

  private record TestPayload(String message) {
  }

  private record TestGuiNavigationTarget(TextGuiElement element) implements GuiNavigationTarget<TextGuiElement, TestPayload> {
    @Override
    public Class<TestPayload> getPayloadType() {
      return TestPayload.class;
    }

    @Override
    public void onNavigate(TextGuiElement element, TestPayload testPayload) {
      element.setText(testPayload.message());
    }
  }
}
