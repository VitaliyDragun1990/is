package com.revenat.ishop.application.service.impl;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.revenat.ishop.application.service.I18nService;


@Service
public class ResourceBundleI18nService implements I18nService {
	private String bundlePath;

	@Autowired
	public ResourceBundleI18nService(@Value("${i18n.bundle}") String bundlePath) {
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
