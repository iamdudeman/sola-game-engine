package technology.sola.engine.graphics.guiv2.event;

public interface GuiEventListener<E extends GuiEvent> {
  void onEvent(E event);
}
