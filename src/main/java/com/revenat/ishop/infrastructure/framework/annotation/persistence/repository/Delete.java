package com.revenat.ishop.infrastructure.framework.annotation.persistence.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to annotate repository interface method that should
 * use SQL Select query to delete entity from underlaying relational database.
 * Can be placed on mehtods with single argument only and such argument should
 * be entity instance to be deleted.
 * 
 * @author Vitaly Dragun
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Delete {
}
