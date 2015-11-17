package com.yida.spider4j.crawler.utils.collection;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;

import com.sun.istack.internal.Nullable;
import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A sorted set which forwards all its method calls to another sorted set.
 * Subclasses should override one or more methods to modify the behavior of the
 * backing sorted set as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * <p><em>Warning</em>: The methods of {@code ForwardingSortedSet} forward
 * <em>indiscriminately</em> to the methods of the delegate. For example,
 * overriding {@link #add} alone <em>will not</em> change the behavior of {@link
 * #addAll}, which can lead to unexpected behavior. In this case, you should
 * override {@code addAll} as well, either providing your own implementation, or
 * delegating to the provided {@code standardAddAll} method.
 *
 * <p>Each of the {@code standard} methods, where appropriate, uses the set's
 * comparator (or the natural ordering of the elements, if there is no
 * comparator) to test element equality. As a result, if the comparator is not
 * consistent with equals, some of the standard implementations may violate the
 * {@code Set} contract.
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
public abstract class ForwardingSortedSet<E> extends ForwardingSet<E>
    implements SortedSet<E> {

  /** Constructor for use by subclasses. */
  protected ForwardingSortedSet() {}

  @Override protected abstract SortedSet<E> delegate();

  public Comparator<? super E> comparator() {
    return delegate().comparator();
  }

  public E first() {
    return delegate().first();
  }

  public SortedSet<E> headSet(E toElement) {
    return delegate().headSet(toElement);
  }

  public E last() {
    return delegate().last();
  }

  public SortedSet<E> subSet(E fromElement, E toElement) {
    return delegate().subSet(fromElement, toElement);
  }

  public SortedSet<E> tailSet(E fromElement) {
    return delegate().tailSet(fromElement);
  }

  // unsafe, but worst case is a CCE is thrown, which callers will be expecting
  private int unsafeCompare(Object o1, Object o2) {
    Comparator<? super E> comparator = comparator();
    return (comparator == null) ? ((Comparable) o1).compareTo(o2)
        : ((Comparator) comparator).compare(o1, o2);
  }

  /**
   * A sensible definition of {@link #contains} in terms of the {@code first()}
   * method of {@link #tailSet}. If you override {@link #tailSet}, you may wish
   * to override {@link #contains} to forward to this implementation.
   *
   * @since 7
   */
  @Override @Beta protected boolean standardContains(@Nullable Object object) {
    try {
      // any ClassCastExceptions are caught
      SortedSet<Object> self = (SortedSet) this;
      Object ceiling = self.tailSet(object).first();
      return unsafeCompare(ceiling, object) == 0;
    } catch (ClassCastException e) {
      return false;
    } catch (NoSuchElementException e) {
      return false;
    } catch (NullPointerException e) {
      return false;
    }
  }

  /**
   * A sensible definition of {@link #remove} in terms of the {@code iterator()}
   * method of {@link #tailSet}. If you override {@link #tailSet}, you may wish
   * to override {@link #remove} to forward to this implementation.
   *
   * @since 7
   */
  @Override @Beta protected boolean standardRemove(@Nullable Object object) {
    try {
      // any ClassCastExceptions are caught
      SortedSet<Object> self = (SortedSet) this;
      Iterator<Object> iterator = self.tailSet(object).iterator();
      if (iterator.hasNext()) {
        Object ceiling = iterator.next();
        if (unsafeCompare(ceiling, object) == 0) {
          iterator.remove();
          return true;
        }
      }
    } catch (ClassCastException e) {
      return false;
    } catch (NullPointerException e) {
      return false;
    }
    return false;
  }

  /**
   * A sensible default implementation of {@link #subSet(Object, Object)} in
   * terms of {@link #headSet(Object)} and {@link #tailSet(Object)}. In some
   * situations, you may wish to override {@link #subSet(Object, Object)} to
   * forward to this implementation.
   *
   * @since 7
   */
  @Beta protected SortedSet<E> standardSubSet(E fromElement, E toElement) {
    return tailSet(fromElement).headSet(toElement);
  }
}
