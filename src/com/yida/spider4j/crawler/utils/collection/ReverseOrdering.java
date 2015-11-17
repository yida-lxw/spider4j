package com.yida.spider4j.crawler.utils.collection;

import java.io.Serializable;

import com.sun.istack.internal.Nullable;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/** An ordering that uses the reverse of a given order. */
@GwtCompatible(serializable = true)
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class ReverseOrdering<T> extends Ordering<T> implements
		Serializable {
	private final Ordering<? super T> forwardOrder;

	public ReverseOrdering(Ordering<? super T> forwardOrder) {
		this.forwardOrder = Preconditions.checkNotNull(forwardOrder);
	}

	@Override
	public int compare(T a, T b) {
		return forwardOrder.compare(b, a);
	}

	// how to explain?
	@Override
	public <S extends T> Ordering<S> reverse() {
		return (Ordering) forwardOrder;
	}

	// Override the six min/max methods to "hoist" delegation outside loops

	@Override
	public <E extends T> E min(E a, E b) {
		return forwardOrder.max(a, b);
	}

	@Override
	public <E extends T> E min(E a, E b, E c, E... rest) {
		return forwardOrder.max(a, b, c, rest);
	}

	@Override
	public <E extends T> E min(Iterable<E> iterable) {
		return forwardOrder.max(iterable);
	}

	@Override
	public <E extends T> E max(E a, E b) {
		return forwardOrder.min(a, b);
	}

	@Override
	public <E extends T> E max(E a, E b, E c, E... rest) {
		return forwardOrder.min(a, b, c, rest);
	}

	@Override
	public <E extends T> E max(Iterable<E> iterable) {
		return forwardOrder.min(iterable);
	}

	@Override
	public int hashCode() {
		return -forwardOrder.hashCode();
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this) {
			return true;
		}
		if (object instanceof ReverseOrdering) {
			ReverseOrdering<?> that = (ReverseOrdering<?>) object;
			return this.forwardOrder.equals(that.forwardOrder);
		}
		return false;
	}

	@Override
	public String toString() {
		return forwardOrder + ".reverse()";
	}

	private static final long serialVersionUID = 0;
}
