package technology.sola.engine.networking;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * NetworkQueue is a data structure for holding network messages in a first in first out basis.
 *
 * @param <T> the type of the data
 */
public class NetworkQueue<T> {
  private final Deque<T> queue = new ArrayDeque<>();

  /**
   * @return true if there are no messages
   */
  public synchronized boolean isEmpty() {
    return queue.isEmpty();
  }

  /**
   * @return the number of messages in the queue
   */
  public synchronized int size() {
    return queue.size();
  }

  /**
   * @return the first message in the queue
   */
  public synchronized T getFirst() {
    return queue.getFirst();
  }

  /**
   * @return the last message in the queue
   */
  public synchronized T getLast() {
    return queue.getLast();
  }

  /**
   * Adds message to the front of the queue.
   *
   * @param item the message to add
   */
  public synchronized void addFirst(T item) {
    queue.addFirst(item);
  }

  /**
   * Adds a message to the end of the queue.
   *
   * @param item the message to add
   */
  public synchronized void addLast(T item) {
    queue.addLast(item);
  }

  /**
   * Clears all messages from the queue.
   */
  public synchronized void clear() {
    queue.clear();
  }

  /**
   * Removes the first message from the queue and returns it.
   *
   * @return the first message in the queue
   */
  public synchronized T removeFirst() {
    return queue.removeFirst();
  }

  /**
   * Removes the last message from the queue and returns it.
   *
   * @return the last message in the queue
   */
  public synchronized T removeLast() {
    return queue.removeLast();
  }
}
