package com.yida.spider4j.crawler.utils.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.anno.GwtIncompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * Multiset implementation backed by an {@link EnumMap}.
 *
 * @author Jared Levy
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible(emulated = true)
public final class EnumMultiset<E extends Enum<E>>
    extends AbstractMapBasedMultiset<E> {
  /** Creates an empty {@code EnumMultiset}. */
  public static <E extends Enum<E>> EnumMultiset<E> create(Class<E> type) {
    return new EnumMultiset<E>(type);
  }

  /**
   * Creates a new {@code EnumMultiset} containing the specified elements.
   *
   * @param elements the elements that the multiset should contain
   * @throws IllegalArgumentException if {@code elements} is empty
   */
  public static <E extends Enum<E>> EnumMultiset<E> create(
      Iterable<E> elements) {
    Iterator<E> iterator = elements.iterator();
    Preconditions.checkArgument(iterator.hasNext(),
        "EnumMultiset constructor passed empty Iterable");
    EnumMultiset<E> multiset
        = new EnumMultiset<E>(iterator.next().getDeclaringClass());
    Iterables.addAll(multiset, elements);
    return multiset;
  }

  private transient Class<E> type;

  /** Creates an empty {@code EnumMultiset}. */
  private EnumMultiset(Class<E> type) {
    super(new EnumMap<E, AtomicInteger>(type));
    this.type = type;
  }

  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    stream.writeObject(type);
    Serialization.writeMultiset(this, stream);
  }

  /**
   * @serialData the {@code Class<E>} for the enum type, the number of distinct
   *     elements, the first element, its count, the second element, its count,
   *     and so on
   */
  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream stream)
      throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    @SuppressWarnings("unchecked") // reading data stored by writeObject
    Class<E> localType = (Class<E>) stream.readObject();
    type = localType;
    setBackingMap(new EnumMap<E, AtomicInteger>(type));
    Serialization.populateMultiset(this, stream);
  }

  @GwtIncompatible("Not needed in emulated source")
  private static final long serialVersionUID = 0;
}
