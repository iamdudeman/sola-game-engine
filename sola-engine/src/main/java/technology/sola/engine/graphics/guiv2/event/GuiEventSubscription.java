package technology.sola.engine.graphics.guiv2.event;

@FunctionalInterface
public interface GuiEventSubscription {
  void unsubscribe();
}
