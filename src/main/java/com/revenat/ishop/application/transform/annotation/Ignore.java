package com.revenat.ishop.application.transform.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Fields with this annotation will be skipped
 * during field-to-field copying
 * 
 * @author Vitaly Dragun
 *
 */
@Retention(RUNTIME)
@Target({ TYPE, FIELD })
public @interface Ignore {

}
