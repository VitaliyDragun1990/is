package com.revenat.ishop.config;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revenat.ishop.exception.ConfigurationException;

public class PropertiesLoaderTest {
	
	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	private PropertiesLoader loader;
	
	@Before
	public void setUp() {
		loader = new PropertiesLoader();
	}

	@Test
	public void shouldNotAllowToLoadUsingNullResourceName() throws Exception {
		expected.expect(NullPointerException.class);
		expected.expectMessage(containsString("Resource name to load properties from can not be null"));
		
		loader.load(null);
	}
	
	@Test
	public void shouldNotAllowToLoadUsingEmptyResourceName() throws Exception {
		expected.expect(IllegalArgumentException.class);
		expected.expectMessage(containsString("Resource name to load properties from can not be empty"));
		
		loader.load("");
	}
	
	@Test
	public void shouldNotAllowToLoadFromNonexistentResource() throws Exception {
		expected.expect(ConfigurationException.class);
		expected.expectMessage(containsString("Resource is not found"));
		
		loader.load("nonexistent.properties");
	}
	
	@Test
	public void shouldAllowToLoadPropertiesFromProeprtyFile() throws Exception {
		Properties props = loader.load("test.properties");
		
		assertThat(props.size(), equalTo(2));
		assertThat(props.getProperty("java"), equalTo("test"));
		assertThat(props.getProperty("ide"), equalTo("eclipse"));
	}
	
	@Test
	public void shouldThrowExceptionIfErrorDuringResourceLoading() throws Exception {
		expected.expect(ConfigurationException.class);
		expected.expectMessage(containsString("Error while loading data from resource"));
		
		loader = new PropertiesLoader(resourceName -> new InputStreamStub(resourceName));
		
		loader.load("test.properties");
	}
	
	private static class InputStreamStub extends InputStream {
		public InputStreamStub(String resourceName) {
		}

		@Override
		public int read() throws IOException {
			throw new IOException("Error during read operation");
		}
	}

}
