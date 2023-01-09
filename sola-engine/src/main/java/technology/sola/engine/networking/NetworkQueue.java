package technology.sola.engine.networking;

import java.util.ArrayDeque;
import java.util.Deque;

public class NetworkQueue<T> {
  private final Deque<T> queue;

  public NetworkQueue() {
    queue = new ArrayDeque<>();
  }

  public synchronized boolean isEmpty() {
    return queue.isEmpty();
  }

  public synchronized int size() {
    return queue.size();
  }

  public synchronized T getFirst() {
    return queue.getFirst();
  }

  public synchronized T getLast() {
    return queue.getLast();
  }

  public synchronized void addFirst(T item) {
    queue.addFirst(item);
  }

  public synchronized void addLast(T item) {
    queue.addLast(item);
  }

  public synchronized void clear() {
    queue.clear();
  }

  public synchronized T removeFirst() {
    return queue.removeFirst();
  }

  public synchronized T removeLast() {
    return queue.removeLast();
  }
}
