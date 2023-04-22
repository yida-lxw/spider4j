package com.yida.spider4j.crawler.utils.collection;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedMap;

import com.sun.istack.internal.Nullable;
import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A sorted map which forwards all its method calls to another sorted map.
 * Subclasses should override one or more methods to modify the behavior of
 * the backing sorted map as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * <p><em>Warning</em>: The methods of {@code ForwardingSortedMap} forward
 * <em>indiscriminately</em> to the methods of the delegate. For example,
 * overriding {@link #put} alone <em>will not</em> change the behavior of {@link
 * #putAll}, which can lead to unexpected behavior. In this case, you should
 * override {@code putAll} as well, either providing your own implementation, or
 * delegating to the provided {@code standardPutAll} method.
 *
 * <p>Each of the {@code standard} methods, where appropriate, use the
 * comparator of the map to test equality for both keys and values, unlike
 * {@code ForwardingMap}.
 *
 * <p>The {@code standard} methods and the collection views they return are not
 * guaranteed to be thread-safe, even when all of the methods that they depend
 * on are thread-safe.
 *
 * @author Mike Bostock
 * @author Louis Wasserman
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class ForwardingSortedMap<K, V> extends ForwardingMap<K, V>
    implements SortedMap<K, V> {
  // TODO(user): identify places where thread safety is actually lost

  /** Constructor for use by subclasses. */
  protected ForwardingSortedMap() {}

  @Override protected abstract SortedMap<K, V> delegate();

  public Comparator<? super K> comparator() {
    return delegate().comparator();
  }

  public K firstKey() {
    return delegate().firstKey();
  }

  public SortedMap<K, V> headMap(K toKey) {
    return delegate().headMap(toKey);
  }

  public K lastKey() {
    return delegate().lastKey();
  }

  public SortedMap<K, V> subMap(K fromKey, K toKey) {
    return delegate().subMap(fromKey, toKey);
  }

  public SortedMap<K, V> tailMap(K fromKey) {
    return delegate().tailMap(fromKey);
  }

  // unsafe, but worst case is a CCE is thrown, which callers will be expecting
  private int unsafeCompare(Object k1, Object k2) {
    Comparator<? super K> comparator = comparator();
    if (comparator == null) {
      return ((Comparable) k1).compareTo(k2);
    } else {
      return ((Comparator) comparator).compare(k1, k2);
    }
  }

  /**
   * A sensible definition of {@link #containsKey} in terms of the {@code
   * firstKey()} method of {@link #tailMap}. If you override {@link #tailMap},
   * you may wish to override {@link #containsKey} to forward to this
   * implementation.
   *
   * @since 7
   */
  @Override @Beta protected boolean standardContainsKey(@Nullable Object key) {
    try {
      // any CCE will be caught
      SortedMap<Object, V> self = (SortedMap) this;
      Object ceilingKey = self.tailMap(key).firstKey();
      return unsafeCompare(ceilingKey, key) == 0;
    } catch (ClassCastException e) {
      return false;
    } catch (NoSuchElementException e) {
      return false;
    } catch (NullPointerException e) {
      return false;
    }
  }

  /**
   * A sensible definition of {@link #remove} in terms of the {@code
   * iterator()} of the {@code entrySet()} of {@link #tailMap}. If you override
   * {@link #tailMap}, you may wish to override {@link #remove} to forward
   * to this implementation.
   *
   * @since 7
   */
  @Override @Beta protected V standardRemove(@Nullable Object key) {
    try {
      // any CCE will be caught
      SortedMap<Object, V> self = (SortedMap) this;
      Iterator<Entry<Object, V>> entryIterator =
          self.tailMap(key).entrySet().iterator();
      if (entryIterator.hasNext()) {
        Entry<Object, V> ceilingEntry = entryIterator.next();
        if (unsafeCompare(ceilingEntry.getKey(), key) == 0) {
          V value = ceilingEntry.getValue();
          entryIterator.remove();
          return value;
        }
      }
    } catch (ClassCastException e) {
      return null;
    } catch (NullPointerException e) {
      return null;
    }
    return null;
  }

  /**
   * A sensible default implementation of {@link #subMap(Object, Object)} in
   * terms of {@link #headMap(Object)} and {@link #tailMap(Object)}. In some
   * situations, you may wish to override {@link #subMap(Object, Object)} to
   * forward to this implementation.
   *
   * @since 7
   */
  @Beta protected SortedMap<K, V> standardSubMap(K fromKey, K toKey) {
    return tailMap(fromKey).headMap(toKey);
  }
}
