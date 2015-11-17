package com.yida.spider4j.crawler.utils.collection;

import java.util.Map;
import java.util.Map.Entry;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Objects;

/**
 * A map entry which forwards all its method calls to another map entry.
 * Subclasses should override one or more methods to modify the behavior of the
 * backing map entry as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 * 
 * <p>
 * <em>Warning</em>: The methods of {@code ForwardingMapEntry} forward
 * <em>indiscriminately</em> to the methods of the delegate. For example,
 * overriding {@link #getValue} alone <em>will not</em> change the behavior of
 * {@link #equals}, which can lead to unexpected behavior. In this case, you
 * should override {@code equals} as well, either providing your own
 * implementation, or delegating to the provided {@code standardEquals} method.
 * 
 * <p>
 * Each of the {@code standard} methods, where appropriate, use
 * {@link Objects#equal} to test equality for both keys and values. This may not
 * be the desired behavior for map implementations that use non-standard notions
 * of key equality, such as the entry of a {@code SortedMap} whose comparator is
 * not consistent with {@code equals}.
 * 
 * <p>
 * The {@code standard} methods are not guaranteed to be thread-safe, even when
 * all of the methods that they depend on are thread-safe.
 * 
 * @author Mike Bostock
 * @author Louis Wasserman
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible
public abstract class ForwardingMapEntry<K, V> extends ForwardingObject
		implements Map.Entry<K, V> {
	// TODO(user): identify places where thread safety is actually lost

	/** Constructor for use by subclasses. */
	protected ForwardingMapEntry() {
	}

	@Override
	protected abstract Map.Entry<K, V> delegate();

	public K getKey() {
		return delegate().getKey();
	}

	public V getValue() {
		return delegate().getValue();
	}

	public V setValue(V value) {
		return delegate().setValue(value);
	}

	@Override
	public boolean equals(Object object) {
		return delegate().equals(object);
	}

	@Override
	public int hashCode() {
		return delegate().hashCode();
	}

	/**
	 * A sensible definition of {@link #equals(Object)} in terms of
	 * {@link #getKey()} and {@link #getValue()}. If you override either of
	 * these methods, you may wish to override {@link #equals(Object)} to
	 * forward to this implementation.
	 * 
	 * @since 7
	 */
	@Beta
	protected boolean standardEquals(Object object) {
		if (object instanceof Entry) {
			Entry<?, ?> that = (Entry<?, ?>) object;
			return Objects.equal(this.getKey(), that.getKey())
					&& Objects.equal(this.getValue(), that.getValue());
		}
		return false;
	}

	/**
	 * A sensible definition of {@link #hashCode()} in terms of
	 * {@link #getKey()} and {@link #getValue()}. If you override either of
	 * these methods, you may wish to override {@link #hashCode()} to forward to
	 * this implementation.
	 * 
	 * @since 7
	 */
	@Beta
	protected int standardHashCode() {
		K k = getKey();
		V v = getValue();
		return ((k == null) ? 0 : k.hashCode())
				^ ((v == null) ? 0 : v.hashCode());
	}

	/**
	 * A sensible definition of {@link #toString} in terms of {@link #getKey}
	 * and {@link #getValue}. If you override either of these methods, you may
	 * wish to override {@link #equals} to forward to this implementation.
	 * 
	 * @since 7
	 */
	@Beta
	protected String standardToString() {
		return getKey() + "=" + getValue();
	}
}
