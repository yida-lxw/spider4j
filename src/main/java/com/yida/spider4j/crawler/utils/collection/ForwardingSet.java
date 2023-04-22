package com.yida.spider4j.crawler.utils.collection;

import java.util.Set;

import com.sun.istack.internal.Nullable;
import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A set which forwards all its method calls to another set. Subclasses should
 * override one or more methods to modify the behavior of the backing set as
 * desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * <p><b>Warning:</b> The methods of {@code ForwardingSet} forward
 * <b>indiscriminately</b> to the methods of the delegate. For example,
 * overriding {@link #add} alone <b>will not</b> change the behavior of {@link
 * #addAll}, which can lead to unexpected behavior. In this case, you should
 * override {@code addAll} as well, either providing your own implementation, or
 * delegating to the provided {@code standardAddAll} method.
 *
 * <p>The {@code standard} methods are not guaranteed to be thread-safe, even
 * when all of the methods that they depend on are thread-safe.
 *
 * @author Kevin Bourrillion
 * @author Louis Wasserman
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
public abstract class ForwardingSet<E> extends ForwardingCollection<E>
    implements Set<E> {
  // TODO(user): identify places where thread safety is actually lost

  /** Constructor for use by subclasses. */
  protected ForwardingSet() {}

  @Override protected abstract Set<E> delegate();

  @Override public boolean equals(@Nullable Object object) {
    return object == this || delegate().equals(object);
  }

  @Override public int hashCode() {
    return delegate().hashCode();
  }

  /**
   * A sensible definition of {@link #equals} in terms of {@link #size} and
   * {@link #containsAll}. If you override either of those methods, you may wish
   * to override {@link #equals} to forward to this implementation.
   *
   * @since 7
   */
  @Beta protected boolean standardEquals(@Nullable Object object) {
    return Sets.equalsImpl(this, object);
  }

  /**
   * A sensible definition of {@link #hashCode} in terms of {@link #iterator}.
   * If you override {@link #iterator}, you may wish to override {@link #equals}
   * to forward to this implementation.
   *
   * @since 7
   */
  @Beta protected int standardHashCode() {
    return Sets.hashCodeImpl(this);
  }
}
