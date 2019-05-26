package com.revenat.ishop.infrastructure.framework.annotation.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation may be placed on method or on the whole class. If
 * {@link #readOnly()} == {@code true} then commit won't be called
 * automatically. If {@link RuntimeException} happens, then rollback will be
 * called. If {@link Exception} happens and {@link #readOnly()} == {@code false}
 * then commit will be called.
 * 
 * @author Vitaly Dragun
 *
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transactional {
	boolean readOnly() default true;
}
