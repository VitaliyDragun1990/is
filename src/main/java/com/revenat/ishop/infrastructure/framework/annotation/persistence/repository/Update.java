package com.revenat.ishop.infrastructure.framework.annotation.persistence.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to annotate repository interface method that should
 * use SQL Update query to update entity in underlaying relational database. Can
 * be placed on methods with single argument only. Such argument should be
 * entity to be updated.
 * 
 * @author Vitaly Dragun
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Update {
}
