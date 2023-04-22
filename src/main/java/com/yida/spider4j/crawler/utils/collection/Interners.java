package com.yida.spider4j.crawler.utils.collection;

import java.util.concurrent.ConcurrentMap;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.base.FinalizableReferenceQueue;
import com.yida.spider4j.crawler.utils.collection.base.FinalizableWeakReference;
import com.yida.spider4j.crawler.utils.collection.base.Function;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * Contains static methods pertaining to instances of {@link Interner}.
 * 
 * @author Kevin Bourrillion
 * @since 3
 */
@Beta
@SuppressWarnings("rawtypes")
public final class Interners {
	private Interners() {
	}

	/**
	 * Returns a new thread-safe interner which retains a strong reference to
	 * each instance it has interned, thus preventing these instances from being
	 * garbage-collected. If this retention is acceptable, this implementation
	 * may perform better than {@link #newWeakInterner}. Note that unlike
	 * {@link String#intern}, using this interner does not consume memory in the
	 * permanent generation.
	 */
	public static <E> Interner<E> newStrongInterner() {
		final ConcurrentMap<E, E> map = new MapMaker().makeMap();
		return new Interner<E>() {
			public E intern(E sample) {
				E canonical = map.putIfAbsent(
						Preconditions.checkNotNull(sample), sample);
				return (canonical == null) ? sample : canonical;
			}
		};
	}

	/**
	 * Returns a new thread-safe interner which retains a weak reference to each
	 * instance it has interned, and so does not prevent these instances from
	 * being garbage-collected. This most likely does not perform as well as
	 * {@link #newStrongInterner}, but is the best alternative when the memory
	 * usage of that implementation is unacceptable. Note that unlike
	 * {@link String#intern}, using this interner does not consume memory in the
	 * permanent generation.
	 */
	public static <E> Interner<E> newWeakInterner() {
		return new WeakInterner<E>();
	}

	private static class WeakInterner<E> implements Interner<E> {
		private final ConcurrentMap<InternReference, InternReference> map = new MapMaker()
				.makeMap();

		public E intern(final E sample) {
			final int hashCode = sample.hashCode();

			// TODO(kevinb): stop using the dummy instance; use custom
			// Equivalence?
			Object fakeReference = new Object() {
				@Override
				public int hashCode() {
					return hashCode;
				}

				@Override
				public boolean equals(Object object) {
					if (object.hashCode() != hashCode) {
						return false;
					}
					/*
					 * Implicitly an unchecked cast to
					 * WeakInterner<?>.InternReference, though until OpenJDK 7,
					 * the compiler doesn't recognize this. If we could
					 * explicitly cast to the wildcard type
					 * WeakInterner<?>.InternReference, that would be sufficient
					 * for our purposes. The compiler, however, rejects such
					 * casts (or rather, it does until OpenJDK 7).
					 * 
					 * See Sun bug 6665356.
					 */
					@SuppressWarnings("unchecked")
					InternReference that = (InternReference) object;
					return sample.equals(that.get());
				}
			};

			// Fast-path; avoid creating the reference if possible
			InternReference existingRef = map.get(fakeReference);
			if (existingRef != null) {
				E canonical = existingRef.get();
				if (canonical != null) {
					return canonical;
				}
			}

			InternReference newRef = new InternReference(sample, hashCode);
			while (true) {
				InternReference sneakyRef = map.putIfAbsent(newRef, newRef);
				if (sneakyRef == null) {
					return sample;
				} else {
					E canonical = sneakyRef.get();
					if (canonical != null) {
						return canonical;
					}
				}
			}
		}

		private static final FinalizableReferenceQueue frq = new FinalizableReferenceQueue();

		class InternReference extends FinalizableWeakReference<E> {
			final int hashCode;

			InternReference(E key, int hash) {
				super(key, frq);
				hashCode = hash;
			}

			public void finalizeReferent() {
				map.remove(this);
			}

			@Override
			public E get() {
				E referent = super.get();
				if (referent == null) {
					finalizeReferent();
				}
				return referent;
			}

			@Override
			public int hashCode() {
				return hashCode;
			}

			@Override
			public boolean equals(Object object) {
				if (object == this) {
					return true;
				}
				if (object instanceof WeakInterner.InternReference) {
					/*
					 * On the following line, Eclipse wants a type parameter,
					 * producing WeakInterner<?>.InternReference. The problem is
					 * that javac rejects that form. Omitting WeakInterner
					 * satisfies both, though this seems odd, since we are
					 * inside a WeakInterner<E> and thus the WeakInterner<E> is
					 * implied, yet there is no reason to believe that the other
					 * object's WeakInterner has type E. That's right -- we've
					 * found a way to perform an unchecked cast without
					 * receiving a warning from either Eclipse or javac. Taking
					 * advantage of that seems questionable, even though we
					 * don't depend upon the type of that.get(), so we'll just
					 * suppress the warning.
					 */
					WeakInterner.InternReference that = (WeakInterner.InternReference) object;
					if (that.hashCode != hashCode) {
						return false;
					}
					E referent = super.get();
					return referent != null && referent.equals(that.get());
				}
				return object.equals(this);
			}
		}
	}

	/**
	 * Returns a function that delegates to the {@link Interner#intern} method
	 * of the given interner.
	 * 
	 * @since 8
	 */
	public static <E> Function<E, E> asFunction(Interner<E> interner) {
		return new InternerFunction<E>(Preconditions.checkNotNull(interner));
	}

	private static class InternerFunction<E> implements Function<E, E> {

		private final Interner<E> interner;

		public InternerFunction(Interner<E> interner) {
			this.interner = interner;
		}

		@Override
		public E apply(E input) {
			return interner.intern(input);
		}

		@Override
		public int hashCode() {
			return interner.hashCode();
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof InternerFunction<?>) {
				InternerFunction<?> that = (InternerFunction<?>) other;
				return interner.equals(that.interner);
			}

			return false;
		}
	}
}
