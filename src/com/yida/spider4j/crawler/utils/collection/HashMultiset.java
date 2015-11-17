package com.yida.spider4j.crawler.utils.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.anno.GwtIncompatible;

/**
 * Multiset implementation backed by a {@link HashMap}.
 *
 * @author Kevin Bourrillion
 * @author Jared Levy
 * @since 2 (imported from Google Collections Library)
 */
@GwtCompatible(serializable = true, emulated = true)
public final class HashMultiset<E> extends AbstractMapBasedMultiset<E> {

  /**
   * Creates a new, empty {@code HashMultiset} using the default initial
   * capacity.
   */
  public static <E> HashMultiset<E> create() {
    return new HashMultiset<E>();
  }

  /**
   * Creates a new, empty {@code HashMultiset} with the specified expected
   * number of distinct elements.
   *
   * @param distinctElements the expected number of distinct elements
   * @throws IllegalArgumentException if {@code distinctElements} is negative
   */
  public static <E> HashMultiset<E> create(int distinctElements) {
    return new HashMultiset<E>(distinctElements);
  }

  /**
   * Creates a new {@code HashMultiset} containing the specified elements.
   *
   * @param elements the elements that the multiset should contain
   */
  public static <E> HashMultiset<E> create(Iterable<? extends E> elements) {
    HashMultiset<E> multiset =
        create(Multisets.inferDistinctElements(elements));
    Iterables.addAll(multiset, elements);
    return multiset;
  }

  private HashMultiset() {
    super(new HashMap<E, AtomicInteger>());
  }

  private HashMultiset(int distinctElements) {
    super(new HashMap<E, AtomicInteger>(Maps.capacity(distinctElements)));
  }

  /**
   * @serialData the number of distinct elements, the first element, its count,
   *     the second element, its count, and so on
   */
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    Serialization.writeMultiset(this, stream);
  }

  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream stream)
      throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    int distinctElements = Serialization.readCount(stream);
    setBackingMap(
        Maps.<E, AtomicInteger>newHashMapWithExpectedSize(distinctElements));
    Serialization.populateMultiset(this, stream, distinctElements);
  }

  @GwtIncompatible("Not needed in emulated source.")
  private static final long serialVersionUID = 0;
}
