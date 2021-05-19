package com.volatil.core.utils;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue<T> {
  private Queue<T> queue = new LinkedList<T>();

  /**
   * Checks if the queue is empty.
   * 
   * @return boolean
   */
  public synchronized boolean isEmpty() {
    return queue.isEmpty();
  }

  /**
   * Adds an object to the queue.
   * 
   * @param obj
   */
  public synchronized void add(T obj) {
    queue.add(obj);
  }

  /**
   * Retrieves and removes the head of the queue.
   * 
   * @return T
   */
  public synchronized T poll() {
    return queue.poll();
  }
}
