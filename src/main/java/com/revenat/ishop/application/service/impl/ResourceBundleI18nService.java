package com.revenat.ishop.application.service.impl;

import java.util.Locale;
import java.util.ResourceBundle;

import com.revenat.ishop.application.service.I18nService;
import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.framework.annotation.di.Value;

@Component
public class ResourceBundleI18nService implements I18nService {
	
	@Value("i18n.bundle")
	private String bundlePath;
	
	public ResourceBundleI18nService() {
	}

	public ResourceBundleI18nService(String bundlePath) {
		this.bundlePath = bundlePath;
	}

	@Override
	public String getMessage(String key, Locale locale, Object... args) {
		String value = ResourceBundle.getBundle(bundlePath, locale).getString(key);
		for (int i = 0; i < args.length; i++) {
			value = value.replace("{" + i + "}", args[i].toString());
		}
		return value;
	}

}
