package com.revenat.ishop.infrastructure.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.infrastructure.exception.ConfigurationException;
import com.revenat.ishop.infrastructure.util.Checks;

/**
 * This component is responsible for loading configuration in form of
 * {@link Properties} from a resource with particular name.
 * 
 * @author Vitaly Dragun
 *
 */
public class PropertiesLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoader.class);

	private final Function<String, InputStream> resourceInputStreamBuilder;

	/**
	 * Creates new instance of the {@link PropertiesLoader} which uses
	 * {@link ClassLoader#getResourceAsStream(String)} mechanism to get input stream
	 * to read resource from within classpath.
	 */
	public PropertiesLoader() {
		this(PropertiesLoader::getClassPathResourceInputStream);
	}

	/**
	 * Creates new instance of the {@link PropertiesLoader}, which uses provided
	 * {@code resourceInputStreamBuilder} function to get input stream to read
	 * resource.
	 * 
	 * @param resourceInputStreamBuilder function that represents abstract mechanism
	 *                                   of building {@link InputStream} to read
	 *                                   resource data from.
	 */
	public PropertiesLoader(Function<String, InputStream> resourceInputStreamBuilder) {
		this.resourceInputStreamBuilder = resourceInputStreamBuilder;
	}

	/**
	 * Loads configuration from resource with specified name and returns it in form
	 * of {@link Properties}
	 * 
	 * @param resourceName name of the resource to load proeprties from.
	 * @return {@link Properties} object populated from resource with given name.
	 */
	public Properties load(String resourceName) {
		validateResourceName(resourceName);

		try (InputStream input = resourceInputStreamBuilder.apply(resourceName)) {
			Properties props = new Properties();
			if (input != null) {
				props.load(input);
				LOGGER.debug("Successfully loaded properties from resource: {}", resourceName);
				return props;
			} else {
				throw new ConfigurationException("Resource is not found: " + resourceName);
			}
		} catch (IOException e) {
			throw new ConfigurationException("Error while loading data from resource with name: " + resourceName, e);
		}
	}

	private static InputStream getClassPathResourceInputStream(String resourceName) {
		return PropertiesLoader.class.getClassLoader().getResourceAsStream(resourceName);
	}

	private void validateResourceName(String resourceName) {
		Checks.checkParam(resourceName != null && resourceName.trim().length() > 0,
				"Resource name to load properties from can not be null or empty");
	}
}
