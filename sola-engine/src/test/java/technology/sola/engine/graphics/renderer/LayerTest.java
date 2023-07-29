package technology.sola.engine.graphics.renderer;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LayerTest {
  @Test
  void shouldHaveNameSet() {
    Layer layer = new Layer("test");

    assertEquals("test", layer.getName());
  }

  @Test
  void shouldRenderDrawItemsInOrder() {
    Renderer mockRenderer = Mockito.mock(Renderer.class);
    DrawItem mockDrawItem = Mockito.mock(DrawItem.class);
    DrawItem mockDrawItem2 = Mockito.mock(DrawItem.class);
    DrawItem mockDrawItem3 = Mockito.mock(DrawItem.class);
    DrawItem mockDrawItem4 = Mockito.mock(DrawItem.class);

    Layer layer = new Layer("test");

    layer.add(mockDrawItem4, 2);
    layer.add(mockDrawItem);
    layer.add(mockDrawItem2);
    layer.add(mockDrawItem3, -1);

    layer.draw(mockRenderer);

    InOrder renderOrder = Mockito.inOrder(
      mockDrawItem3, mockDrawItem, mockDrawItem2, mockDrawItem4
    );

    renderOrder.verify(mockDrawItem3).draw(mockRenderer);
    renderOrder.verify(mockDrawItem).draw(mockRenderer);
    renderOrder.verify(mockDrawItem2).draw(mockRenderer);
    renderOrder.verify(mockDrawItem4).draw(mockRenderer);
  }

  @Test
  void shouldNotRenderDrawItemsIfInactive() {
    Renderer mockRenderer = Mockito.mock(Renderer.class);
    DrawItem mockDrawItem = Mockito.mock(DrawItem.class);
    Layer layer = new Layer("test");

    layer.add(mockDrawItem);
    layer.setActive(false);

    layer.draw(mockRenderer);

    assertFalse(layer.isActive());
    Mockito.verify(mockDrawItem, Mockito.times(0)).draw(mockRenderer);
  }

  @Test
  void shouldNotAddDrawItemsIfInactive() {
    Renderer mockRenderer = Mockito.mock(Renderer.class);
    DrawItem mockDrawItem = Mockito.mock(DrawItem.class);
    Layer layer = new Layer("test");

    layer.setActive(false);
    layer.add(mockDrawItem);
    layer.setActive(true);

    layer.draw(mockRenderer);

    Mockito.verify(mockDrawItem, Mockito.times(0)).draw(mockRenderer);
  }
}
