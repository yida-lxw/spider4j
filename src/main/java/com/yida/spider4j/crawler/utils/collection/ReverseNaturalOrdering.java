package com.yida.spider4j.crawler.utils.collection;

import java.io.Serializable;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/** An ordering that uses the reverse of the natural order of the values. */
@GwtCompatible(serializable = true)
@SuppressWarnings({"unchecked","rawtypes"})
// TODO(kevinb): the right way to explain this??
final class ReverseNaturalOrdering extends Ordering<Comparable> implements
		Serializable {
	static final ReverseNaturalOrdering INSTANCE = new ReverseNaturalOrdering();

	@Override
	public int compare(Comparable left, Comparable right) {
		Preconditions.checkNotNull(left); // right null is caught later
		if (left == right) {
			return 0;
		}

		int result = right.compareTo(left);
		return result;
	}

	@Override
	public <S extends Comparable> Ordering<S> reverse() {
		return Ordering.natural();
	}

	// Override the six min/max methods to "hoist" delegation outside loops

	@Override
	public <E extends Comparable> E min(E a, E b) {
		return NaturalOrdering.INSTANCE.max(a, b);
	}

	@Override
	public <E extends Comparable> E min(E a, E b, E c, E... rest) {
		return NaturalOrdering.INSTANCE.max(a, b, c, rest);
	}

	@Override
	public <E extends Comparable> E min(Iterable<E> iterable) {
		return NaturalOrdering.INSTANCE.max(iterable);
	}

	@Override
	public <E extends Comparable> E max(E a, E b) {
		return NaturalOrdering.INSTANCE.min(a, b);
	}

	@Override
	public <E extends Comparable> E max(E a, E b, E c, E... rest) {
		return NaturalOrdering.INSTANCE.min(a, b, c, rest);
	}

	@Override
	public <E extends Comparable> E max(Iterable<E> iterable) {
		return NaturalOrdering.INSTANCE.min(iterable);
	}

	// preserving singleton-ness gives equals()/hashCode() for free
	private Object readResolve() {
		return INSTANCE;
	}

	@Override
	public String toString() {
		return "Ordering.natural().reverse()";
	}

	private ReverseNaturalOrdering() {
	}

	private static final long serialVersionUID = 0;
}
