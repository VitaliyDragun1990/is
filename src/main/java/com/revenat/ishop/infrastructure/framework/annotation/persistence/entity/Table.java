package com.revenat.ishop.infrastructure.framework.annotation.persistence.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
	/**
	 * Name of the table
	 */
	String name();

	/**
	 * Name of the id field of the target entity
	 * 
	 * @return
	 */
	String idField() default "id";

	/**
	 * Expression to generate next id value, may be left empty if database use
	 * auto-generated keys.
	 * 
	 */
	String nextIdExpression() default "";
}
