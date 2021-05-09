package technology.sola.engine.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventHubTest {
  @Test
  void whenEventEmitted_shouldNotifyAllActiveSubscribers() {
    TestEventListener testEventListener = new TestEventListener();
    TestEventListener testEventListenerTwo = new TestEventListener();
    TestEventListener testEventListenerThree = new TestEventListener();
    EventHub eventHub = new EventHub();

    eventHub.subscribe(testEventListener);
    eventHub.subscribe(testEventListenerTwo);
    eventHub.subscribe(testEventListenerThree);
    eventHub.unsubscribe(testEventListenerTwo);
    eventHub.emit(new TestEvent("Hello"));

    assertTestListener(testEventListener, "Hello");
    assertTestListener(testEventListenerTwo, null);
    assertTestListener(testEventListenerThree, "Hello");
  }

  private static void assertTestListener(TestEventListener testEventListener, String expected) {
    assertEquals(expected, testEventListener.result);
  }

  private static class TestEventListener implements EventListener<TestEvent> {
    private String result = null;

    @Override
    public Class<TestEvent> getEventClass() {
      return TestEvent.class;
    }

    @Override
    public void onEvent(TestEvent event) {
      result = event.getMessage();
    }
  }

  private static class TestEvent implements Event<String> {
    private final String message;

    public TestEvent(String message) {
      this.message = message;
    }

    @Override
    public String getMessage() {
      return message;
    }
  }
}
