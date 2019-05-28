package com.revenat.ishop.infrastructure.framework.factory.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.infrastructure.framework.exception.FrameworkSystemException;
import com.revenat.ishop.infrastructure.framework.handler.DefaultListResultSetHandler;
import com.revenat.ishop.infrastructure.framework.handler.DefaultUniqueResultSetHandler;
import com.revenat.ishop.infrastructure.framework.handler.IntResultSetHandler;
import com.revenat.ishop.infrastructure.framework.handler.ResultSetHandler;
import com.revenat.ishop.infrastructure.framework.handler.base.DefaultResultSetHandler;

abstract class JDBCAbstractSQLHelper {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@SuppressWarnings("rawtypes")
	protected ResultSetHandler<?> createResultSetHandler(Class<? extends ResultSetHandler> resultSetHandlerClass,
			Method method, Class<?> entityClass) throws IllegalAccessException, InvocationTargetException {
		try {
			if (method.getReturnType() == Integer.TYPE) {
				return new IntResultSetHandler();
			} else {
				ResultSetHandler<?> resultSetHandler = null;
				
				if (DefaultResultSetHandler.class.isAssignableFrom(resultSetHandlerClass)) {
					resultSetHandler = resultSetHandlerClass.getConstructor(Class.class).newInstance(entityClass);
				} else {
					resultSetHandler = resultSetHandlerClass.newInstance();
				}
				
				if (Collection.class.isAssignableFrom(method.getReturnType())) {
					return new DefaultListResultSetHandler<>(resultSetHandler);
				} else {
					return new DefaultUniqueResultSetHandler<>(resultSetHandler);
				}
			}
		} catch (InstantiationException | NoSuchMethodException e) {
			throw new FrameworkSystemException("Can not create instance of resultSetHandler for class: " 
					+ resultSetHandlerClass,e);
		}
	}
}
