package com.yida.spider4j.crawler.utils.collection.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The presence of this annotation on a method indicates that the method may
 * <em>not</em> be used with the
 * <a href="http://code.google.com/webtoolkit/">Google Web Toolkit</a> (GWT),
 * even though its type is annotated as {@link GwtCompatible} and accessible in
 * GWT.  They can cause GWT compilation errors or simply unexpected exceptions
 * when used in GWT.
 *
 * <p>Note that this annotation should only be applied to methods, fields, or
 * inner classes of types which are annotated as {@link GwtCompatible}.
 *
 * @author Charles Fry
 */
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Documented
@GwtCompatible
public @interface GwtIncompatible {

  /**
   * Describes why the annotated element is incompatible with GWT. Since this is
   * generally due to a dependence on a type/method which GWT doesn't support,
   * it is sufficient to simply reference the unsupported type/method. E.g.
   * "Class.isInstance".
   */
  String value();

}
