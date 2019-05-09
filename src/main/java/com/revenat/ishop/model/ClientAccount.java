package com.revenat.ishop.model;

import java.io.Serializable;

/**
 * This interface represents currently logged in user in the application.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ClientAccount extends Serializable {

	Integer getId();

	String getDescription();
}