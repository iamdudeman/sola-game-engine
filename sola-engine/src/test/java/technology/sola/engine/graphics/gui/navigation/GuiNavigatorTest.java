package technology.sola.engine.graphics.gui.navigation;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.engine.graphics.gui.GuiDocument;
import technology.sola.engine.graphics.gui.elements.TextGuiElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@NullMarked
class GuiNavigatorTest {
  @Nested
  class navigate {
    @Test
    void whenRegistered_shouldNavigate() {
      GuiDocument mockGuiDocument = Mockito.mock(GuiDocument.class);
      GuiNavigator guiNavigator = new GuiNavigator(mockGuiDocument);

      TextGuiElement element = new TextGuiElement();

      guiNavigator.register(new TestGuiNavigationScreen(element));

      guiNavigator.navigate(TestGuiNavigationScreen.class, new TestPayload("test"));

      Mockito.verify(mockGuiDocument).setRootElement(element);
      assertEquals("test", element.getText());
    }

    @Test
    void whenRegisteredWithoutPayload_shouldNavigate() {
      GuiDocument mockGuiDocument = Mockito.mock(GuiDocument.class);
      GuiNavigator guiNavigator = new GuiNavigator(mockGuiDocument);

      TextGuiElement element = new TextGuiElement();

      guiNavigator.register(new TestGuiNavigatorScreenWithoutPayload(element));

      guiNavigator.navigate(TestGuiNavigatorScreenWithoutPayload.class);

      Mockito.verify(mockGuiDocument).setRootElement(element);
      assertEquals("no payload", element.getText());
    }

    @Test
    void whenNotRegistered_shouldThrow() {
      GuiDocument mockGuiDocument = Mockito.mock(GuiDocument.class);
      GuiNavigator guiNavigator = new GuiNavigator(mockGuiDocument);

      assertThrows(
        IllegalArgumentException.class,
        () -> guiNavigator.navigate(TestGuiNavigationScreen.class, new TestPayload("test"))
      );
    }
  }

  private record TestPayload(String message) {
  }

  private record TestGuiNavigationScreen(TextGuiElement rootElement) implements GuiNavigationScreen<TextGuiElement, TestPayload> {
    @Override
    public Class<TestPayload> getPayloadType() {
      return TestPayload.class;
    }

    @Override
    public void onNavigate(TextGuiElement element, TestPayload testPayload) {
      element.setText(testPayload.message());
    }
  }

  private record TestGuiNavigatorScreenWithoutPayload(TextGuiElement rootElement) implements GuiNavigatorScreenWithoutPayload<TextGuiElement> {
    @Override
    public void onNavigate(TextGuiElement element) {
      rootElement.setText("no payload");
    }
  }
}
