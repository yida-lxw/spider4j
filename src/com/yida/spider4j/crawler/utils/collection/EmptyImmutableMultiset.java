package com.yida.spider4j.crawler.utils.collection;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * An empty immutable multiset.
 * 
 * @author Jared Levy
 */
@GwtCompatible(serializable = true)
final class EmptyImmutableMultiset extends ImmutableMultiset<Object> {
  static final EmptyImmutableMultiset INSTANCE = new EmptyImmutableMultiset();

  private EmptyImmutableMultiset() {
    super(ImmutableMap.<Object, Integer>of(), 0);
  }

  Object readResolve() {
    return INSTANCE; // preserve singleton property
  }

  private static final long serialVersionUID = 0;
}
