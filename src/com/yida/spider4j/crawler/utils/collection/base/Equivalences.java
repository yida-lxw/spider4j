package com.yida.spider4j.crawler.utils.collection.base;

import com.sun.istack.internal.Nullable;
import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * Contains static factory methods for creating {@code Equivalence} instances.
 *
 * <p>All methods return serializable instances.
 *
 * @author Bob Lee
 * @author Kurt Alfred Kluever
 * @since 4
 */
@Beta
@GwtCompatible
public final class Equivalences {
  private Equivalences() {}

  /**
   * Returns an equivalence that delegates to {@link Object#equals} and {@link Object#hashCode}.
   * {@link Equivalence#equivalent} returns {@code true} if both values are null, or if neither
   * value is null and {@link Object#equals} returns {@code true}. {@link Equivalence#hash} returns
   * {@code 0} if passed a null value.
   *
   * @since 8 (present null-friendly behavior)
   * @since 4 (otherwise)
   */
  public static Equivalence<Object> equals() {
    return Impl.EQUALS;
  }

  /**
   * Returns an equivalence that delegates to {@link Object#equals} and {@link Object#hashCode}.
   * {@link Equivalence#equivalent} returns {@code true} if both values are null, or if neither
   * value is null and {@link Object#equals} returns {@code true}. {@link Equivalence#hash} returns
   * {@code 0} if passed a null value.
   *
   * @deprecated use {@link Equivalences#equals}, which now has the null-aware behavior
   */
  @Deprecated
  public static Equivalence<Object> nullAwareEquals() {
    return Impl.EQUALS;
  }

  /**
   * Returns an equivalence that uses {@code ==} to compare values and {@link
   * System#identityHashCode(Object)} to compute the hash code.  {@link Equivalence#equivalent}
   * returns {@code true} if {@code a == b}, including in the case that a and b are both null.
   */
  public static Equivalence<Object> identity() {
    return Impl.IDENTITY;
  }

  private enum Impl implements Equivalence<Object> {
    EQUALS {
      public boolean equivalent(@Nullable Object a, @Nullable Object b) {
        // TODO(kevinb): use Objects.equal() after testing issue is worked out.
        return (a == null) ? (b == null) : a.equals(b);
      }

      public int hash(@Nullable Object o) {
        return (o == null) ? 0 : o.hashCode();
      }
    },
    IDENTITY {
      public boolean equivalent(@Nullable Object a, @Nullable Object b) {
        return a == b;
      }

      public int hash(@Nullable Object o) {
        return System.identityHashCode(o);
      }
    },
  }
}
