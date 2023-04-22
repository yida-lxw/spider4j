package com.yida.spider4j.crawler.utils.collection.base;
import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
/**
 * @ClassName: Joiner
 * @Description: 字符串连接器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月12日 上午9:37:24
 *
 */
public class Joiner {
	/**
	   * Returns a joiner which automatically places {@code separator} between consecutive elements.
	   */
	  public static Joiner on(String separator) {
	    return new Joiner(separator);
	  }

	  /**
	   * Returns a joiner which automatically places {@code separator} between consecutive elements.
	   */
	  public static Joiner on(char separator) {
	    return new Joiner(String.valueOf(separator));
	  }

	  private final String separator;

	  private Joiner(String separator) {
	    this.separator = Preconditions.checkNotNull(separator);
	  }

	  private Joiner(Joiner prototype) {
	    this.separator = prototype.separator;
	  }

	  /**
	   * Appends the string representation of each of {@code parts}, using the previously configured
	   * separator between each, to {@code appendable}.
	   */
	  public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
	    Preconditions.checkNotNull(appendable);
	    Iterator<?> iterator = parts.iterator();
	    if (iterator.hasNext()) {
	      appendable.append(toString(iterator.next()));
	      while (iterator.hasNext()) {
	        appendable.append(separator);
	        appendable.append(toString(iterator.next()));
	      }
	    }
	    return appendable;
	  }

	  /**
	   * Appends the string representation of each of {@code parts}, using the previously configured
	   * separator between each, to {@code appendable}.
	   */
	  public final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException {
	    return appendTo(appendable, Arrays.asList(parts));
	  }

	  /**
	   * Appends to {@code appendable} the string representation of each of the remaining arguments.
	   */
	  public final <A extends Appendable> A appendTo(
	      A appendable, Object first, Object second, Object... rest)
	          throws IOException {
	    return appendTo(appendable, iterable(first, second, rest));
	  }

	  /**
	   * Appends the string representation of each of {@code parts}, using the previously configured
	   * separator between each, to {@code builder}. Identical to {@link #appendTo(Appendable,
	   * Iterable)}, except that it does not throw {@link IOException}.
	   */
	  public final StringBuilder appendTo(StringBuilder builder, Iterable<?> parts) {
	    try {
	      appendTo((Appendable) builder, parts);
	    } catch (IOException impossible) {
	      throw new AssertionError(impossible);
	    }
	    return builder;
	  }

	  /**
	   * Appends the string representation of each of {@code parts}, using the previously configured
	   * separator between each, to {@code builder}. Identical to {@link #appendTo(Appendable,
	   * Iterable)}, except that it does not throw {@link IOException}.
	   */
	  public final StringBuilder appendTo(StringBuilder builder, Object[] parts) {
	    return appendTo(builder, Arrays.asList(parts));
	  }

	  /**
	   * Appends to {@code builder} the string representation of each of the remaining arguments.
	   * Identical to {@link #appendTo(Appendable, Object, Object, Object...)}, except that it does not
	   * throw {@link IOException}.
	   */
	  public final StringBuilder appendTo(
	      StringBuilder builder, Object first, Object second, Object... rest) {
	    return appendTo(builder, iterable(first, second, rest));
	  }

	  /**
	   * Returns a string containing the string representation of each of {@code parts}, using the
	   * previously configured separator between each.
	   */
	  public final String join(Iterable<?> parts) {
	    return appendTo(new StringBuilder(), parts).toString();
	  }

	  /**
	   * Returns a string containing the string representation of each of {@code parts}, using the
	   * previously configured separator between each.
	   */
	  public final String join(Object[] parts) {
	    return join(Arrays.asList(parts));
	  }

	  /**
	   * Returns a string containing the string representation of each argument, using the previously
	   * configured separator between each.
	   */
	  public final String join(Object first, Object second, Object... rest) {
	    return join(iterable(first, second, rest));
	  }

	  /**
	   * Returns a joiner with the same behavior as this one, except automatically substituting {@code
	   * nullText} for any provided null elements.
	   */
	  public Joiner useForNull(final String nullText) {
	    Preconditions.checkNotNull(nullText);
	    return new Joiner(this) {
	      @Override CharSequence toString(Object part) {
	        return (part == null) ? nullText : Joiner.this.toString(part);
	      }

	      @Override public Joiner useForNull(String nullText) {
	        Preconditions.checkNotNull(nullText); // weird: just to satisfy NullPointerTester.
	        throw new UnsupportedOperationException("already specified useForNull");
	      }

	      @Override public Joiner skipNulls() {
	        throw new UnsupportedOperationException("already specified useForNull");
	      }
	    };
	  }

	  /**
	   * Returns a joiner with the same behavior as this joiner, except automatically skipping over any
	   * provided null elements.
	   */
	  public Joiner skipNulls() {
	    return new Joiner(this) {
	      @Override public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts)
	          throws IOException {
	        Preconditions.checkNotNull(appendable, "appendable");
	        Preconditions.checkNotNull(parts, "parts");
	        Iterator<?> iterator = parts.iterator();
	        while (iterator.hasNext()) {
	          Object part = iterator.next();
	          if (part != null) {
	            appendable.append(Joiner.this.toString(part));
	            break;
	          }
	        }
	        while (iterator.hasNext()) {
	          Object part = iterator.next();
	          if (part != null) {
	            appendable.append(separator);
	            appendable.append(Joiner.this.toString(part));
	          }
	        }
	        return appendable;
	      }

	      @Override public Joiner useForNull(String nullText) {
	        Preconditions.checkNotNull(nullText); // weird: just to satisfy NullPointerTester.
	        throw new UnsupportedOperationException("already specified skipNulls");
	      }

	      @Override public MapJoiner withKeyValueSeparator(String kvs) {
	        Preconditions.checkNotNull(kvs); // weird: just to satisfy NullPointerTester.
	        throw new UnsupportedOperationException("can't use .skipNulls() with maps");
	      }
	    };
	  }

	  /**
	   * Returns a {@code MapJoiner} using the given key-value separator, and the same configuration as
	   * this {@code Joiner} otherwise.
	   */
	  public MapJoiner withKeyValueSeparator(String keyValueSeparator) {
	    return new MapJoiner(this, keyValueSeparator);
	  }

	  /**
	   * An object that joins map entries in the same manner as {@code Joiner} joins iterables and
	   * arrays. Like {@code Joiner}, it is thread-safe and immutable.
	   *
	   * @since 2 (imported from Google Collections Library)
	   */
	  public final static class MapJoiner {
	    private final Joiner joiner;
	    private final String keyValueSeparator;

	    private MapJoiner(Joiner joiner, String keyValueSeparator) {
	      this.joiner = joiner; // only "this" is ever passed, so don't checkNotNull
	      this.keyValueSeparator = Preconditions.checkNotNull(keyValueSeparator);
	    }

	    /**
	     * Appends the string representation of each entry of {@code map}, using the previously
	     * configured separator and key-value separator, to {@code appendable}.
	     */
	    public <A extends Appendable> A appendTo(A appendable, Map<?, ?> map) throws IOException {
	      Preconditions.checkNotNull(appendable);
	      Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
	      if (iterator.hasNext()) {
	        Entry<?, ?> entry = iterator.next();
	        appendable.append(joiner.toString(entry.getKey()));
	        appendable.append(keyValueSeparator);
	        appendable.append(joiner.toString(entry.getValue()));
	        while (iterator.hasNext()) {
	          appendable.append(joiner.separator);
	          Entry<?, ?> e = iterator.next();
	          appendable.append(joiner.toString(e.getKey()));
	          appendable.append(keyValueSeparator);
	          appendable.append(joiner.toString(e.getValue()));
	        }
	      }
	      return appendable;
	    }

	    /**
	     * Appends the string representation of each entry of {@code map}, using the previously
	     * configured separator and key-value separator, to {@code builder}. Identical to {@link
	     * #appendTo(Appendable, Map)}, except that it does not throw {@link IOException}.
	     */
	    public StringBuilder appendTo(StringBuilder builder, Map<?, ?> map) {
	      try {
	        appendTo((Appendable) builder, map);
	      } catch (IOException impossible) {
	        throw new AssertionError(impossible);
	      }
	      return builder;
	    }

	    /**
	     * Returns a string containing the string representation of each entry of {@code map}, using the
	     * previously configured separator and key-value separator.
	     */
	    public String join(Map<?, ?> map) {
	      return appendTo(new StringBuilder(), map).toString();
	    }

	    /**
	     * Returns a map joiner with the same behavior as this one, except automatically substituting
	     * {@code nullText} for any provided null keys or values.
	     */
	    public MapJoiner useForNull(String nullText) {
	      return new MapJoiner(joiner.useForNull(nullText), keyValueSeparator);
	    }
	  }

	  CharSequence toString(Object part) {
	    return (part instanceof CharSequence) ? (CharSequence) part : part.toString();
	  }

	  private static Iterable<Object> iterable(
	      final Object first, final Object second, final Object[] rest) {
	    Preconditions.checkNotNull(rest);
	    return new AbstractList<Object>() {
	      @Override public int size() {
	        return rest.length + 2;
	      }

	      @Override public Object get(int index) {
	        switch (index) {
	          case 0:
	            return first;
	          case 1:
	            return second;
	          default:
	            return rest[index - 2];
	        }
	      }
	    };
	  }
}
