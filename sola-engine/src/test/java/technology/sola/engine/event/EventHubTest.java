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

    eventHub.add(TestEvent.class, testEventListener);
    eventHub.add(TestEvent.class, testEventListenerTwo);
    eventHub.add(TestEvent.class, testEventListenerThree);
    eventHub.remove(TestEvent.class, testEventListenerTwo);
    eventHub.emit(new TestEvent("test_message"));

    assertTestListener(testEventListener, "test_message");
    assertTestListener(testEventListenerTwo, null);
    assertTestListener(testEventListenerThree, "test_message");
  }

  @Test
  void whenOffCalledForEvent_shouldNotNotifyAnySubscribers() {
    TestEventListener testEventListener = new TestEventListener();
    TestEventListener testEventListenerTwo = new TestEventListener();
    TestEventListener testEventListenerThree = new TestEventListener();
    EventHub eventHub = new EventHub();

    eventHub.add(TestEvent.class, testEventListener);
    eventHub.add(TestEvent.class, testEventListenerTwo);
    eventHub.add(TestEvent.class, testEventListenerThree);
    eventHub.off(TestEvent.class);
    eventHub.emit(new TestEvent("test_message"));

    assertTestListener(testEventListener, null);
    assertTestListener(testEventListenerTwo, null);
    assertTestListener(testEventListenerThree, null);

  }

  private static void assertTestListener(TestEventListener testEventListener, String expected) {
    assertEquals(expected, testEventListener.result);
  }

  private static class TestEventListener implements EventListener<TestEvent> {
    private String result = null;

    @Override
    public void onEvent(TestEvent event) {
      result = event.message();
    }
  }

  private record TestEvent(String message) implements Event {
  }
}
