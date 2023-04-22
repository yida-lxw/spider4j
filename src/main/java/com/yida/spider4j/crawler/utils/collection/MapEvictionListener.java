package com.yida.spider4j.crawler.utils.collection;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;

/**
 * An object that can receive a notification when an entry is evicted from a
 * map.
 *
 * <p>An instance may be called concurrently by multiple threads to process
 * different entries. Implementations of this interface should avoid performing
 * blocking calls or synchronizing on shared resources.
 *
 * @param <K> the type of keys being evicted
 * @param <V> the type of values being evicted
 * @author Ben Manes
 * @since 7
 */
@Beta
public interface MapEvictionListener<K, V> {

  /**
   * Notifies the listener that an eviction has occurred. Eviction may be for
   * reasons such as timed expiration, exceeding a maximum size, or due to
   * garbage collection. Eviction notification does <i>not</i> occur due to
   * manual removal.
   *
   * @param key the key of the entry that has already been evicted, or {@code
   *     null} if its reference was collected
   * @param value the value of the entry that has already been evicted, or
   *     {@code null} if its reference was collected
   */
  void onEviction(K key, V value);
}
