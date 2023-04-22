package com.yida.spider4j.crawler.utils.collection;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * List returned by {@link ImmutableCollection#asList} when the collection isn't
 * an {@link ImmutableList} or an {@link ImmutableSortedSet}.
 *
 * @author Jared Levy
 */
@GwtCompatible(serializable = true, emulated = true)
@SuppressWarnings("serial")
final class ImmutableAsList<E> extends RegularImmutableList<E> {
  private final transient ImmutableCollection<E> collection;

  ImmutableAsList(Object[] array, ImmutableCollection<E> collection) {
    super(array, 0, array.length);
    this.collection = collection;
  }

  @Override public boolean contains(Object target) {
    // The collection's contains() is at least as fast as RegularImmutableList's
    // and is often faster.
    return collection.contains(target);
  }

  /**
   * Serialized form that leads to the same performance as the original list.
   */
  static class SerializedForm implements Serializable {
    final ImmutableCollection<?> collection;
    SerializedForm(ImmutableCollection<?> collection) {
      this.collection = collection;
    }
    Object readResolve() {
      return collection.asList();
    }
    private static final long serialVersionUID = 0;
  }

  private void readObject(ObjectInputStream stream)
      throws InvalidObjectException {
    throw new InvalidObjectException("Use SerializedForm");
  }

  @Override Object writeReplace() {
    return new SerializedForm(collection);
  }
}
