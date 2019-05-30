package com.revenat.ishop.infrastructure.framework.factory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.infrastructure.framework.annotation.di.Autowired;
import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.framework.annotation.di.JDBCRepository;
import com.revenat.ishop.infrastructure.framework.annotation.di.OnDestroy;
import com.revenat.ishop.infrastructure.framework.annotation.di.Value;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.service.Transactional;
import com.revenat.ishop.infrastructure.framework.exception.FrameworkSystemException;
import com.revenat.ishop.infrastructure.framework.util.ReflectionUtils;

public class DependencyInjectionManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(DependencyInjectionManager.class);
	
	private final Map<Class<?>, Object> instances = new HashMap<>();
	private final Properties applicationProperties = new Properties();
	private final Map<Class<?>, Object> externalDependecies = new HashMap<>();
	
	public DependencyInjectionManager(Properties applicationProperties, Map<Class<?>, Object> externalDependencies) {
		this.applicationProperties.putAll(applicationProperties);
		this.externalDependecies.putAll(externalDependencies);
	}
	
	public void scanPackage(String packageName) {
		try {
			List<Class<?>> classes = getAllClassesInPackage(packageName);
			for (Class<?> classObject : classes) {
				Object instance = createInstance(classObject);
				if (instance != null) {
					for (Class<?> interfaceClass : getKeysForInstance(classObject) ) {
						instances.put(interfaceClass, instance);
						LOGGER.info("Added {}.class = {}", interfaceClass.getSimpleName(), toStringInstance(instance));
					}
				}
			}
		} catch (ClassNotFoundException | IllegalAccessException | IOException e) {
			throw new FrameworkSystemException("Can not load instances from pakage: " + packageName, e);
		}
	}
	
	public void injectDependencies() {
		try {
			for (Map.Entry<Class<?>, Object> entry : instances.entrySet()) {
				Object instance = entry.getValue();
				if (Proxy.isProxyClass(instance.getClass())) {
					injectProxyDependencies(instance);
				} else {
					injectRealObjectDependencies(instance);
				}
			}
		} catch (IllegalAccessException e) {
			throw new FrameworkSystemException("Can not inject dependencies:" + e.getMessage(), e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getInstance(Class<T> classObject) {
		T instance = (T) instances.get(classObject);
		Objects.requireNonNull(instance, "Instance not found for class: " + classObject);
		return instance;
	}
	
	public void destroyInstances() {
		for (Object instance : instances.values()) {
			if (Proxy.isProxyClass(instance.getClass())) {
				destroyProxyInstance(instance);
			} else {
				destroyInstance(instance);
			}
			
		}
		instances.clear();
	}

	protected void destroyProxyInstance(Object proxyInstance) {
		List<Field> fields = ReflectionUtils.getAccessibleFields(proxyInstance.getClass());
		for (Field invocationhandlerField : fields) {
			try {
				Object invocationHandler = invocationhandlerField.get(proxyInstance);
				if (JDBCTransactionalProxyFactory.isTransactionalProxyInvocationHandler(invocationHandler)) {
					Object realInstance = JDBCTransactionalProxyFactory.getRealService(invocationHandler);
					destroyInstance(realInstance);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				LOGGER.error("Error while trying to get a real instance of class: " + proxyInstance.getClass().getSimpleName() 
					+ " from proxy to destroy it", e);
			}
		}
	}

	protected void destroyInstance(Object instance) {
		Method[] methods = instance.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(OnDestroy.class) && method.getParameterCount() == 0) {
				LOGGER.info("Invoke method annotated with @OnDestroy from class {}", instance.getClass().getSimpleName());
				try {
					method.setAccessible(true);
					method.invoke(instance);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					LOGGER.error("Invoke method annotated witn @OnDestroy failed:" + e.getMessage(), e);
				}
			}
		}
		
	}

	protected void injectProxyDependencies(Object proxyInstance) throws IllegalAccessException {
		List<Field> fields = ReflectionUtils.getAccessibleFields(proxyInstance.getClass());
		for (Field invocationhandlerField : fields) {
			Object invocationHandler = invocationhandlerField.get(proxyInstance);
			if (JDBCTransactionalProxyFactory.isTransactionalProxyInvocationHandler(invocationHandler)) {
				Object realInstance = JDBCTransactionalProxyFactory.getRealService(invocationHandler);
				injectRealObjectDependencies(realInstance);
			}
		}
	}

	protected void injectRealObjectDependencies(Object instance) throws IllegalAccessException {
		List<Field> fields = ReflectionUtils.getAccessibleFields(instance.getClass());
		for (Field field : fields) {
			injectAutowiredDependency(field, instance);
			injectValueDependency(field, instance);
		}
	}

	protected void injectAutowiredDependency(Field field, Object instance) throws IllegalAccessException {
		Autowired autowired = field.getAnnotation(Autowired.class);
		if (autowired != null) {
			Object dependency = instances.get(field.getType());
			if (dependency == null) {
				throw new FrameworkSystemException("Can not inject dependency: field=" + field 
						+ " from class=" + field.getType());
			}
			field.set(instance, dependency);
			LOGGER.info("Dependency {}.{} injected by instance {}",
					field.getDeclaringClass().getSimpleName(), field.getName(), toStringInstance(dependency));
		}
		
	}

	protected void injectValueDependency(Field field, Object instance) throws IllegalAccessException {
		Value value = field.getAnnotation(Value.class);
		if (value != null) {
			String key = value.value();
			String propertyValue = applicationProperties.getProperty(key);
			if (propertyValue == null) {
				throw new FrameworkSystemException("Property " + value.value() + " not found");
			}
			
			boolean isSystemProperty = isSystemProperty(propertyValue);
			if (isSystemProperty) {
				propertyValue = resolveSystemProperty(propertyValue);
			}
			
			field.set(instance, propertyValue);
			String loggerPropValue = isSystemProperty ? "${" + key + "}" : propertyValue;
			LOGGER.info("Value {}.{} injected by property {}",
					field.getDeclaringClass().getSimpleName(), field.getName(), loggerPropValue);
		}
		
	}

	protected boolean isSystemProperty(String propertyValue) {
		return propertyValue.startsWith("${sysEnv.");
	}
	
	protected String resolveSystemProperty(String propertyValue) {
		String systemProperty = propertyValue.replace("${sysEnv.", "").replace("}", "");
		String value = System.getProperty(systemProperty);
		if (value != null) {
			return value;
		} else {
			throw new FrameworkSystemException("System property " + systemProperty + " not found");
		}
	}

	protected Class<?>[] getKeysForInstance(Class<?> classObject) {
		if (classObject.isInterface()) {
			return new Class[] { classObject };
		} else {
			return classObject.getInterfaces();
		}
	}

	protected Object createInstance(Class<?> classObject) throws IllegalAccessException {
		if (classObject.isInterface()) {
			if (classObject.getAnnotation(JDBCRepository.class) != null) {
				return JDBCRepositoryFactory.createRepository(classObject);
			} else {
				return null;
			}
		} else {
			if (classObject.getAnnotation(Component.class) != null) {
				return createComponentInstance(classObject);
			} else {
				return null;
			}
		}
	}

	protected Object createComponentInstance(Class<?> classObject) throws IllegalAccessException {
		try {
			Object realInstance = classObject.newInstance();
			if (isConfigAnnotationExists(classObject, Transactional.class)) {
				DataSource dataSource = (DataSource) externalDependecies.get(DataSource.class);
				Objects.requireNonNull(dataSource, "DataSource instance not found");
				return JDBCTransactionalProxyFactory.createTransactionalProxy(dataSource, realInstance);
			} else {
				return realInstance;
			}
		} catch (InstantiationException e) {
			throw new FrameworkSystemException("Can not instantiate class: " + classObject + 
					"! Does it have default constructor without parameters?", e);
		}
	}


	protected List<Class<?>> getAllClassesInPackage(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> resources = classLoader.getResources(packageName.replace('.', '/'));
		List<Class<?>> classes = new ArrayList<>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			File dir = new File(resource.getFile());
			for (File file : dir.listFiles()) {
				String fileName = file.getName();
				if (fileName.endsWith(".class")) {
					classes.add(Class.forName(packageName + "." + fileName.replace(".class", "")));
				}
			}
		}
		return classes;
	}

	protected boolean isConfigAnnotationExists(Class<?> classObject, Class<? extends Annotation> annotationClass) {
		if (classObject.getAnnotation(annotationClass) != null) {
			return true;
		} else {
			Method[] methods = classObject.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getAnnotation(annotationClass) != null) {
					return true;
				}
			}
			return false;
		}
	}
	
	protected String toStringInstance(Object instance) {
		return Proxy.isProxyClass(instance.getClass()) ? instance.toString() : instance.getClass().getSimpleName();
	}
}
