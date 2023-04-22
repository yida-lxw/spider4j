package com.yida.spider4j.crawler.utils.collection;

import java.util.concurrent.Executor;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;

/**
 * A collection of common eviction listeners.
 *
 * @author Charles Fry
 * @since 7
 */
@Beta
public final class EvictionListeners {

  private EvictionListeners() {}

  /**
   * Returns an asynchronous {@code MapEvictionListener} which processes all
   * eviction notifications asynchronously, using {@code executor}.
   *
   * @param listener the backing listener
   * @param executor the executor with which eviciton notifications are
   *     asynchronously executed
   */
  public static <K, V> MapEvictionListener<K, V> asynchronous(
      final MapEvictionListener<K, V> listener, final Executor executor) {
    return new MapEvictionListener<K, V>() {
      @Override
      public void onEviction(final K key, final V value) {
        executor.execute(new Runnable() {
          public void run() {
            listener.onEviction(key, value);
          }
        });
      }
    };
  }

}
