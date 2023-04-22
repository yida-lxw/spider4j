package com.yida.spider4j.crawler.utils.collection.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import com.yida.spider4j.crawler.utils.collection.Predicate;

/**
 * Static utility methods pertaining to {@code Predicate} instances.
 *
 * <p>All methods returns serializable predicates as long as they're given
 * serializable parameters.
 *
 * @author Kevin Bourrillion
 * @since 2 (imported from Google Collections Library)
 */
public final class Predicates {
  private Predicates() {}

  // TODO(kevinb): considering having these implement a VisitablePredicate
  // interface which specifies an accept(PredicateVisitor) method.

  /**
   * Returns a predicate that always evaluates to {@code true}.
   */
  public static <T> Predicate<T> alwaysTrue() {
    return ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
  }

  /**
   * Returns a predicate that always evaluates to {@code false}.
   */
  public static <T> Predicate<T> alwaysFalse() {
    return ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
  }

  /**
   * Returns a predicate that evaluates to {@code true} if the object reference
   * being tested is null.
   */
  public static <T> Predicate<T> isNull() {
    return ObjectPredicate.IS_NULL.withNarrowedType();
  }

  /**
   * Returns a predicate that evaluates to {@code true} if the object reference
   * being tested is not null.
   */
  public static <T> Predicate<T> notNull() {
    return ObjectPredicate.NOT_NULL.withNarrowedType();
  }

  /**
   * Returns a predicate that evaluates to {@code true} if the given predicate
   * evaluates to {@code false}.
   */
  public static <T> Predicate<T> not(Predicate<T> predicate) {
    return new NotPredicate<T>(predicate);
  }

  /**
   * Returns a predicate that evaluates to {@code true} if each of its
   * components evaluates to {@code true}. The components are evaluated in
   * order, and evaluation will be "short-circuited" as soon as a false
   * predicate is found. It defensively copies the iterable passed in, so future
   * changes to it won't alter the behavior of this predicate. If {@code
   * components} is empty, the returned predicate will always evaluate to {@code
   * true}.
   */
  public static <T> Predicate<T> and(
      Iterable<? extends Predicate<? super T>> components) {
    return new AndPredicate<T>(defensiveCopy(components));
  }

  /**
   * Returns a predicate that evaluates to {@code true} if each of its
   * components evaluates to {@code true}. The components are evaluated in
   * order, and evaluation will be "short-circuited" as soon as a false
   * predicate is found. It defensively copies the array passed in, so future
   * changes to it won't alter the behavior of this predicate. If {@code
   * components} is empty, the returned predicate will always evaluate to {@code
   * true}.
   */
  public static <T> Predicate<T> and(Predicate<? super T>... components) {
    return new AndPredicate<T>(defensiveCopy(components));
  }

  /**
   * Returns a predicate that evaluates to {@code true} if both of its
   * components evaluate to {@code true}. The components are evaluated in
   * order, and evaluation will be "short-circuited" as soon as a false
   * predicate is found.
   */
  public static <T> Predicate<T> and(Predicate<? super T> first,
      Predicate<? super T> second) {
    return new AndPredicate<T>(Predicates.<T>asList(
        Preconditions.checkNotNull(first), Preconditions.checkNotNull(second)));
  }

  /**
   * Returns a predicate that evaluates to {@code true} if any one of its
   * components evaluates to {@code true}. The components are evaluated in
   * order, and evaluation will be "short-circuited" as soon as a
   * true predicate is found. It defensively copies the iterable passed in, so
   * future changes to it won't alter the behavior of this predicate. If {@code
   * components} is empty, the returned predicate will always evaluate to {@code
   * false}.
   */
  public static <T> Predicate<T> or(
      Iterable<? extends Predicate<? super T>> components) {
    return new OrPredicate<T>(defensiveCopy(components));
  }

  /**
   * Returns a predicate that evaluates to {@code true} if any one of its
   * components evaluates to {@code true}. The components are evaluated in
   * order, and evaluation will be "short-circuited" as soon as a
   * true predicate is found. It defensively copies the array passed in, so
   * future changes to it won't alter the behavior of this predicate. If {@code
   * components} is empty, the returned predicate will always evaluate to {@code
   * false}.
   */
  public static <T> Predicate<T> or(Predicate<? super T>... components) {
    return new OrPredicate<T>(defensiveCopy(components));
  }

  /**
   * Returns a predicate that evaluates to {@code true} if either of its
   * components evaluates to {@code true}. The components are evaluated in
   * order, and evaluation will be "short-circuited" as soon as a
   * true predicate is found.
   */
  public static <T> Predicate<T> or(
      Predicate<? super T> first, Predicate<? super T> second) {
    return new OrPredicate<T>(Predicates.<T>asList(
        Preconditions.checkNotNull(first), Preconditions.checkNotNull(second)));
  }

  /**
   * Returns a predicate that evaluates to {@code true} if the object being
   * tested {@code equals()} the given target or both are null.
   */
  public static <T> Predicate<T> equalTo(T target) {
    return (target == null)
        ? Predicates.<T>isNull()
        : new IsEqualToPredicate<T>(target);
  }

  /**
   * Returns a predicate that evaluates to {@code true} if the object being
   * tested is an instance of the given class. If the object being tested
   * is {@code null} this predicate evaluates to {@code false}.
   *
   * <p>If you want to filter an {@code Iterable} to narrow its type, consider
   * using {@link com.google.common.collect.Iterables#filter(Iterable, Class)}
   * in preference.
   *
   * <p><b>Warning:</b> contrary to the typical assumptions about predicates (as
   * documented at {@link Predicate#apply}), the returned predicate may not be
   * <i>consistent with equals</i>. For example, {@code
   * instanceOf(ArrayList.class)} will yield different results for the two equal
   * instances {@code Lists.newArrayList(1)} and {@code Arrays.asList(1)}.
   */
  public static Predicate<Object> instanceOf(Class<?> clazz) {
    return new InstanceOfPredicate(clazz);
  }

  /**
   * Returns a predicate that evaluates to {@code true} if the object reference
   * being tested is a member of the given collection. It does not defensively
   * copy the collection passed in, so future changes to it will alter the
   * behavior of the predicate.
   *
   * This method can technically accept any Collection<?>, but using a typed
   * collection helps prevent bugs. This approach doesn't block any potential
   * users since it is always possible to use {@code Predicates.<Object>in()}.
   *
   * @param target the collection that may contain the function input
   */
  public static <T> Predicate<T> in(Collection<? extends T> target) {
    return new InPredicate<T>(target);
  }

  /**
   * Returns the composition of a function and a predicate. For every {@code x},
   * the generated predicate returns {@code predicate(function(x))}.
   *
   * @return the composition of the provided function and predicate
   */
  public static <A, B> Predicate<A> compose(
      Predicate<B> predicate, Function<A, ? extends B> function) {
    return new CompositionPredicate<A, B>(predicate, function);
  }

  /**
   * Returns a predicate that evaluates to {@code true} if the
   * {@code CharSequence} being tested contains any match for the given
   * regular expression pattern. The test used is equivalent to
   * {@code Pattern.compile(pattern).matcher(arg).find()}
   *
   * @throws java.util.regex.PatternSyntaxException if the pattern is invalid
   * @since 3
   */
  public static Predicate<CharSequence> containsPattern(String pattern) {
    return new ContainsPatternPredicate(pattern);
  }

  /**
   * Returns a predicate that evaluates to {@code true} if the
   * {@code CharSequence} being tested contains any match for the given
   * regular expression pattern. The test used is equivalent to
   * {@code regex.matcher(arg).find()}
   *
   * @since 3
   */
  public static Predicate<CharSequence> contains(Pattern pattern) {
    return new ContainsPatternPredicate(pattern);
  }

  // End public API, begin private implementation classes.

  // Package private for GWT serialization.
  enum ObjectPredicate implements Predicate<Object> {
    ALWAYS_TRUE {
      @Override public boolean apply(Object o) {
        return true;
      }
    },
    ALWAYS_FALSE {
      @Override public boolean apply(Object o) {
        return false;
      }
    },
    IS_NULL {
      @Override public boolean apply(Object o) {
        return o == null;
      }
    },
    NOT_NULL {
      @Override public boolean apply(Object o) {
        return o != null;
      }
    };
    
    @SuppressWarnings("unchecked") // these Object predicates work for any T
    <T> Predicate<T> withNarrowedType() {
      return (Predicate<T>) this;
    }
  }

  /** @see Predicates#not(Predicate) */
  private static class NotPredicate<T> implements Predicate<T>, Serializable {
    final Predicate<T> predicate;

    NotPredicate(Predicate<T> predicate) {
      this.predicate = Preconditions.checkNotNull(predicate);
    }
    public boolean apply(T t) {
      return !predicate.apply(t);
    }
    @Override public int hashCode() {
      return ~predicate.hashCode();
    }
    @Override public boolean equals(Object obj) {
      if (obj instanceof NotPredicate) {
        NotPredicate<?> that = (NotPredicate<?>) obj;
        return predicate.equals(that.predicate);
      }
      return false;
    }
    @Override public String toString() {
      return "Not(" + predicate.toString() + ")";
    }
    private static final long serialVersionUID = 0;
  }

  private static final Joiner COMMA_JOINER = Joiner.on(",");

  /** @see Predicates#and(Iterable) */
  private static class AndPredicate<T> implements Predicate<T>, Serializable {
    private final List<? extends Predicate<? super T>> components;

    private AndPredicate(List<? extends Predicate<? super T>> components) {
      this.components = components;
    }
    public boolean apply(T t) {
      for (Predicate<? super T> predicate : components) {
        if (!predicate.apply(t)) {
          return false;
        }
      }
      return true;
    }
    @Override public int hashCode() {
      // 0x12472c2c is a random number to help avoid collisions with OrPredicate
      return components.hashCode() + 0x12472c2c;
    }
    @Override public boolean equals(Object obj) {
      if (obj instanceof AndPredicate) {
        AndPredicate<?> that = (AndPredicate<?>) obj;
        return components.equals(that.components);
      }
      return false;
    }
    @Override public String toString() {
      return "And(" + COMMA_JOINER.join(components) + ")";
    }
    private static final long serialVersionUID = 0;
  }

  /** @see Predicates#or(Iterable) */
  private static class OrPredicate<T> implements Predicate<T>, Serializable {
    private final List<? extends Predicate<? super T>> components;

    private OrPredicate(List<? extends Predicate<? super T>> components) {
      this.components = components;
    }
    public boolean apply(T t) {
      for (Predicate<? super T> predicate : components) {
        if (predicate.apply(t)) {
          return true;
        }
      }
      return false;
    }
    @Override public int hashCode() {
      // 0x053c91cf is a random number to help avoid collisions with AndPredicate
      return components.hashCode() + 0x053c91cf;
    }
    @Override public boolean equals(Object obj) {
      if (obj instanceof OrPredicate) {
        OrPredicate<?> that = (OrPredicate<?>) obj;
        return components.equals(that.components);
      }
      return false;
    }
    @Override public String toString() {
      return "Or(" + COMMA_JOINER.join(components) + ")";
    }
    private static final long serialVersionUID = 0;
  }

  /** @see Predicates#equalTo(Object) */
  private static class IsEqualToPredicate<T>
      implements Predicate<T>, Serializable {
    private final T target;

    private IsEqualToPredicate(T target) {
      this.target = target;
    }
    public boolean apply(T t) {
      return target.equals(t);
    }
    @Override public int hashCode() {
      return target.hashCode();
    }
    @Override public boolean equals(Object obj) {
      if (obj instanceof IsEqualToPredicate) {
        IsEqualToPredicate<?> that = (IsEqualToPredicate<?>) obj;
        return target.equals(that.target);
      }
      return false;
    }
    @Override public String toString() {
      return "IsEqualTo(" + target + ")";
    }
    private static final long serialVersionUID = 0;
  }

  /** @see Predicates#instanceOf(Class) */
  private static class InstanceOfPredicate
      implements Predicate<Object>, Serializable {
    private final Class<?> clazz;

    private InstanceOfPredicate(Class<?> clazz) {
      this.clazz = Preconditions.checkNotNull(clazz);
    }
    public boolean apply(Object o) {
      return Platform.isInstance(clazz, o);
    }
    @Override public int hashCode() {
      return clazz.hashCode();
    }
    @Override public boolean equals(Object obj) {
      if (obj instanceof InstanceOfPredicate) {
        InstanceOfPredicate that = (InstanceOfPredicate) obj;
        return clazz == that.clazz;
      }
      return false;
    }
    @Override public String toString() {
      return "IsInstanceOf(" + clazz.getName() + ")";
    }
    private static final long serialVersionUID = 0;
  }

  /** @see Predicates#in(Collection) */
  private static class InPredicate<T> implements Predicate<T>, Serializable {
    private final Collection<?> target;

    private InPredicate(Collection<?> target) {
      this.target = Preconditions.checkNotNull(target);
    }

    public boolean apply(T t) {
      try {
        return target.contains(t);
      } catch (NullPointerException e) {
        return false;
      } catch (ClassCastException e) {
        return false;
      }
    }

    @Override public boolean equals(Object obj) {
      if (obj instanceof InPredicate) {
        InPredicate<?> that = (InPredicate<?>) obj;
        return target.equals(that.target);
      }
      return false;
    }

    @Override public int hashCode() {
      return target.hashCode();
    }

    @Override public String toString() {
      return "In(" + target + ")";
    }
    private static final long serialVersionUID = 0;
  }

  /** @see Predicates#compose(Predicate, Function) */
  private static class CompositionPredicate<A, B>
      implements Predicate<A>, Serializable {
    final Predicate<B> p;
    final Function<A, ? extends B> f;

    private CompositionPredicate(Predicate<B> p, Function<A, ? extends B> f) {
      this.p = Preconditions.checkNotNull(p);
      this.f = Preconditions.checkNotNull(f);
    }

    public boolean apply(A a) {
      return p.apply(f.apply(a));
    }

    @Override public boolean equals(Object obj) {
      if (obj instanceof CompositionPredicate) {
        CompositionPredicate<?, ?> that = (CompositionPredicate<?, ?>) obj;
        return f.equals(that.f) && p.equals(that.p);
      }
      return false;
    }

    @Override public int hashCode() {
      return f.hashCode() ^ p.hashCode();
    }

    @Override public String toString() {
      return p.toString() + "(" + f.toString() + ")";
    }

    private static final long serialVersionUID = 0;
  }

  /**
   * @see Predicates#contains(Pattern)
   * @see Predicates#containsPattern(String)
   */
  private static class ContainsPatternPredicate
      implements Predicate<CharSequence>, Serializable {
    final Pattern pattern;

    ContainsPatternPredicate(Pattern pattern) {
      this.pattern = Preconditions.checkNotNull(pattern);
    }

    ContainsPatternPredicate(String patternStr) {
      this(Pattern.compile(patternStr));
    }

    public boolean apply(CharSequence t) {
      return pattern.matcher(t).find();
    }

    @Override public int hashCode() {
      // Pattern uses Object.hashCode, so we have to reach
      // inside to build a hashCode consistent with equals.

      return Objects.hashCode(pattern.pattern(), pattern.flags());
    }

    @Override public boolean equals(Object obj) {
      if (obj instanceof ContainsPatternPredicate) {
        ContainsPatternPredicate that = (ContainsPatternPredicate) obj;

        // Pattern uses Object (identity) equality, so we have to reach
        // inside to compare individual fields.
        return Objects.equal(pattern.pattern(), that.pattern.pattern())
            && Objects.equal(pattern.flags(), that.pattern.flags());
      }
      return false;
    }

    @Override public String toString() {
      return Objects.toStringHelper(this)
          .add("pattern", pattern)
          .add("pattern.flags", Integer.toHexString(pattern.flags()))
          .toString();
    }

    private static final long serialVersionUID = 0;
  }

  @SuppressWarnings("unchecked")
  private static <T> List<Predicate<? super T>> asList(
      Predicate<? super T> first, Predicate<? super T> second) {
    return Arrays.<Predicate<? super T>>asList(first, second);
  }

  private static <T> List<T> defensiveCopy(T... array) {
    return defensiveCopy(Arrays.asList(array));
  }

  static <T> List<T> defensiveCopy(Iterable<T> iterable) {
    ArrayList<T> list = new ArrayList<T>();
    for (T element : iterable) {
      list.add(Preconditions.checkNotNull(element));
    }
    return list;
  }
}
