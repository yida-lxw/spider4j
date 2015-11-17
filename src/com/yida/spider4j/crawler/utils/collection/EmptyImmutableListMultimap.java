package com.yida.spider4j.crawler.utils.collection;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * Implementation of {@link ImmutableListMultimap} with no entries.
 *
 * @author Jared Levy
 */
@GwtCompatible(serializable = true)
public class EmptyImmutableListMultimap extends ImmutableListMultimap<Object, Object> {
  static final EmptyImmutableListMultimap INSTANCE
      = new EmptyImmutableListMultimap();

  private EmptyImmutableListMultimap() {
    super(ImmutableMap.<Object, ImmutableList<Object>>of(), 0);
  }

  private Object readResolve() {
    return INSTANCE; // preserve singleton property
  }

  private static final long serialVersionUID = 0;
}
