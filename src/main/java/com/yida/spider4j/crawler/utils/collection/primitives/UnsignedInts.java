package com.yida.spider4j.crawler.utils.collection.primitives;

import java.util.Arrays;
import java.util.Comparator;

import com.yida.spider4j.crawler.utils.collection.anno.Beta;
import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * Static utility methods pertaining to {@code int} primitives that interpret values as
 * <i>unsigned</i> (that is, any negative value {@code x} is treated as the positive value
 * {@code 2^32 + x}). The methods for which signedness is not an issue are in {@link Ints}, as well
 * as signed versions of methods for which signedness is an issue.
 * 
 * <p>In addition, this class provides several static methods for converting an {@code int} to a
 * {@code String} and a {@code String} to an {@code int} that treat the {@code int} as an unsigned
 * number.
 * 
 * <p>Users of these utilities must be <i>extremely careful</i> not to mix up signed and unsigned
 * {@code int} values. When possible, it is recommended that the {@link UnsignedInteger} wrapper
 * class be used, at a small efficiency penalty, to enforce the distinction in the type system.
 * 
 * @author Louis Wasserman
 * @since 11.0
 */
@Beta
@GwtCompatible
public final class UnsignedInts {
  static final long INT_MASK = 0xffffffffL;

  private UnsignedInts() {}

  static int flip(int value) {
    return value ^ Integer.MIN_VALUE;
  }

  /**
   * Compares the two specified {@code int} values, treating them as unsigned values between
   * {@code 0} and {@code 2^32 - 1} inclusive.
   * 
   * @param a the first unsigned {@code int} to compare
   * @param b the second unsigned {@code int} to compare
   * @return a negative value if {@code a} is less than {@code b}; a positive value if {@code a} is
   *         greater than {@code b}; or zero if they are equal
   */
  public static int compare(int a, int b) {
    return Ints.compare(flip(a), flip(b));
  }

  /**
   * Returns the value of the given {@code int} as a {@code long}, when treated as unsigned.
   */
  public static long toLong(int value) {
    return value & INT_MASK;
  }

  /**
   * Returns the least value present in {@code array}, treating values as unsigned.
   * 
   * @param array a <i>nonempty</i> array of unsigned {@code int} values
   * @return the value present in {@code array} that is less than or equal to every other value in
   *         the array according to {@link #compare}
   * @throws IllegalArgumentException if {@code array} is empty
   */
  public static int min(int... array) {
    Preconditions.checkArgument(array.length > 0);
    int min = flip(array[0]);
    for (int i = 1; i < array.length; i++) {
      int next = flip(array[i]);
      if (next < min) {
        min = next;
      }
    }
    return flip(min);
  }

  /**
   * Returns the greatest value present in {@code array}, treating values as unsigned.
   * 
   * @param array a <i>nonempty</i> array of unsigned {@code int} values
   * @return the value present in {@code array} that is greater than or equal to every other value
   *         in the array according to {@link #compare}
   * @throws IllegalArgumentException if {@code array} is empty
   */
  public static int max(int... array) {
    Preconditions.checkArgument(array.length > 0);
    int max = flip(array[0]);
    for (int i = 1; i < array.length; i++) {
      int next = flip(array[i]);
      if (next > max) {
        max = next;
      }
    }
    return flip(max);
  }

  /**
   * Returns a string containing the supplied unsigned {@code int} values separated by
   * {@code separator}. For example, {@code join("-", 1, 2, 3)} returns the string {@code "1-2-3"}.
   * 
   * @param separator the text that should appear between consecutive values in the resulting
   *        string (but not at the start or end)
   * @param array an array of unsigned {@code int} values, possibly empty
   */
  public static String join(String separator, int... array) {
    Preconditions.checkNotNull(separator);
    if (array.length == 0) {
      return "";
    }

    // For pre-sizing a builder, just get the right order of magnitude
    StringBuilder builder = new StringBuilder(array.length * 5);
    builder.append(array[0]);
    for (int i = 1; i < array.length; i++) {
      builder.append(separator).append(toString(array[i]));
    }
    return builder.toString();
  }

  /**
   * Returns a comparator that compares two arrays of unsigned {@code int} values lexicographically.
   * That is, it compares, using {@link #compare(int, int)}), the first pair of values that follow
   * any common prefix, or when one array is a prefix of the other, treats the shorter array as the
   * lesser. For example, {@code [] < [1] < [1, 2] < [2] < [1 << 31]}.
   * 
   * <p>The returned comparator is inconsistent with {@link Object#equals(Object)} (since arrays
   * support only identity equality), but it is consistent with {@link Arrays#equals(int[], int[])}.
   * 
   * @see <a href="http://en.wikipedia.org/wiki/Lexicographical_order"> Lexicographical order
   *      article at Wikipedia</a>
   */
  public static Comparator<int[]> lexicographicalComparator() {
    return LexicographicalComparator.INSTANCE;
  }

  enum LexicographicalComparator implements Comparator<int[]> {
    INSTANCE;

    @Override
    public int compare(int[] left, int[] right) {
      int minLength = Math.min(left.length, right.length);
      for (int i = 0; i < minLength; i++) {
        if (left[i] != right[i]) {
          return UnsignedInts.compare(left[i], right[i]);
        }
      }
      return left.length - right.length;
    }
  }

  /**
   * Returns dividend / divisor, where the dividend and divisor are treated as unsigned 32-bit
   * quantities.
   * 
   * @param dividend the dividend (numerator)
   * @param divisor the divisor (denominator)
   * @throws ArithmeticException if divisor is 0
   */
  public static int divide(int dividend, int divisor) {
    return (int) (toLong(dividend) / toLong(divisor));
  }

  /**
   * Returns dividend % divisor, where the dividend and divisor are treated as unsigned 32-bit
   * quantities.
   * 
   * @param dividend the dividend (numerator)
   * @param divisor the divisor (denominator)
   * @throws ArithmeticException if divisor is 0
   */
  public static int remainder(int dividend, int divisor) {
    return (int) (toLong(dividend) % toLong(divisor));
  }

  /**
   * Returns the unsigned {@code int} value represented by the given decimal string.
   * 
   * @throws NumberFormatException if the string does not contain a valid unsigned integer, or if
   *         the value represented is too large to fit in an unsigned {@code int}.
   * @throws NullPointerException if {@code s} is null
   */
  public static int parseUnsignedInt(String s) {
    return parseUnsignedInt(s, 10);
  }

  /**
   * Returns the unsigned {@code int} value represented by a string with the given radix.
   * 
   * @param string the string containing the unsigned integer representation to be parsed.
   * @param radix the radix to use while parsing {@code s}; must be between
   *        {@link Character#MIN_RADIX} and {@link Character#MAX_RADIX}.
   * @throws NumberFormatException if the string does not contain a valid unsigned {@code int}, or
   *         if supplied radix is invalid.
   */
  public static int parseUnsignedInt(String string, int radix) {
    Preconditions.checkNotNull(string);
    long result = Long.parseLong(string, radix);
    if ((result & INT_MASK) != result) {
      throw new NumberFormatException("Input " + string + " in base " + radix
          + " is not in the range of an unsigned integer");
    }
    return (int) result;
  }

  /**
   * Returns a string representation of x, where x is treated as unsigned.
   */
  public static String toString(int x) {
    return toString(x, 10);
  }

  /**
   * Returns a string representation of {@code x} for the given radix, where {@code x} is treated
   * as unsigned.
   * 
   * @param x the value to convert to a string.
   * @param radix the radix to use while working with {@code x}
   * @throws IllegalArgumentException if {@code radix} is not between {@link Character#MIN_RADIX}
   *         and {@link Character#MAX_RADIX}.
   */
  public static String toString(int x, int radix) {
    long asLong = x & INT_MASK;
    return Long.toString(asLong, radix);
  }
}