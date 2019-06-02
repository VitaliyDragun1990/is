package com.revenat.ishop.application.service;

import java.util.Locale;

/**
 * Responsible for providing localized messages
 * according to specified {@link Locale} and {@code key} parameters.
 * 
 * @author Vitaly Dragun
 *
 */
public interface I18nService {

	String getMessage(String key, Locale locale, Object... args);
}
