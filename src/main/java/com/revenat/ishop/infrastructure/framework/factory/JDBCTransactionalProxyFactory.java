package com.revenat.ishop.infrastructure.framework.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.sql.DataSource;

import com.revenat.ishop.infrastructure.framework.annotation.persistence.service.Transactional;
import com.revenat.ishop.infrastructure.framework.util.ReflectionUtils;

/**
 * This factory is responsible for creating dynamic proxy for specified service,
 * which methods could be annotated with {@link Transactional} annotation. Each
 * such method will be executed inside separate transaction(in term of
 * relational database).
 * 
 * @author Vitaly Dragun
 *
 */
final class JDBCTransactionalProxyFactory {

	@SuppressWarnings("unchecked")
	public static <T> T createTransactionalProxy(DataSource dataSource, T realService) {
		return (T) Proxy.newProxyInstance(
				JDBCTransactionalProxyFactory.class.getClassLoader(),
				realService.getClass().getInterfaces(),
				new TransactionalProxyInvocationHandler(realService, dataSource));
	}
	
	static boolean isTransactionalProxyInvocationHandler(Object invocationHandler) {
		return invocationHandler.getClass() == TransactionalProxyInvocationHandler.class;
	}
	
	static Object getRealService(Object invocationHandler) throws IllegalAccessException {
		TransactionalProxyInvocationHandler handler = (TransactionalProxyInvocationHandler) invocationHandler;
		return handler.realService;
	}

	private JDBCTransactionalProxyFactory() {
	}

	private static class TransactionalProxyInvocationHandler implements InvocationHandler {
		private final JDBCTransactionalHelper transactionalHelper;
		private final Object realService;

		public TransactionalProxyInvocationHandler(Object realService, DataSource dataSource) {
			this.transactionalHelper = new JDBCTransactionalHelper(realService, dataSource);
			this.realService = realService;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				if ("toString".equals(method.getName())) {
					return "Proxy for " + realService.getClass() + " class";
				} else {
					Method m = ReflectionUtils.findMethod(method, realService.getClass());
					Transactional transactional = ReflectionUtils.findConfigAnnotaion(m, Transactional.class);
					if (transactional != null) {
						return transactionalHelper.invokeInsideTransaction(transactional, m, args);
					} else {
						return m.invoke(realService, args);
					}
				}
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}

	}

}
