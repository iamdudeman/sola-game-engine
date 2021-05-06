package technology.sola.engine.event;

import java.util.*;

public class MessageHub {
  private final Map<Class<? extends Message<?>>, List<MessageListener<?>>> messageListenersMap;

  public MessageHub() {
    messageListenersMap = new HashMap<>();
  }

  public void subscribe(MessageListener<?> messageListener, Class<Message<?>> messageClass) {
    List<MessageListener<?>> messageListeners = messageListenersMap.getOrDefault(messageClass, new LinkedList<>());

    messageListeners.add(messageListener);
  }

  public void unsubscribe() {

  }

  public void emit() {

  }
}
