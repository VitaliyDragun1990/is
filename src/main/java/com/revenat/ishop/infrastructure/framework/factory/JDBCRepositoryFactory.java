package com.revenat.ishop.infrastructure.framework.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Delete;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Insert;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Select;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Update;

/**
 * This factory is responsible for creating dynamic proxy object for specified
 * repository interface which methods should be annotation with appropriate
 * annotations like {@link Select}, {@link Insert}, {@link Update},
 * {@link Delete} and so on. Proxy, created by this factory is responsible for
 * dynamically generate such methods implementations according to information
 * presented in such annotations. Thus jdbc repositories can be generated based
 * solely on interfaces with appropriate metainformation.
 * 
 * 
 * @author Vitaly Dragun
 *
 */
final class JDBCRepositoryFactory {

	@SuppressWarnings("unchecked")
	static <T> T createRepository(Class<T> interfaceClass) {
		return (T) Proxy.newProxyInstance(
				JDBCRepositoryFactory.class.getClassLoader(),
				new Class[] { interfaceClass },
				new JDBCRepositoryInvocationHandler(interfaceClass));
	}

	private JDBCRepositoryFactory() {
	}

	private static class JDBCRepositoryInvocationHandler implements InvocationHandler {
		private final JDBCSelectHelper selectSQLHelper = new JDBCSelectHelper();
		private final JDBCInsertHelper insertSQLHelper = new JDBCInsertHelper();
		private final JDBCDeleteHelper deleteSQLHelper = new JDBCDeleteHelper();
		private final JDBCUpdateHelper updateSQLHelper = new JDBCUpdateHelper();
		private final Class<?> interfaceClass;

		private JDBCRepositoryInvocationHandler(Class<?> interfaceClass) {
			this.interfaceClass = interfaceClass;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				Select select = method.getAnnotation(Select.class);
				if (select != null) {
					return selectSQLHelper.select(select, method, args);
				}
				Insert insert = method.getAnnotation(Insert.class);
				if (insert != null) {
					return insertSQLHelper.insert(insert, method, args);
				}
				Delete delete = method.getAnnotation(Delete.class);
				if (delete != null) {
					return deleteSQLHelper.delete(method, args);
				}
				Update update = method.getAnnotation(Update.class);
				if (update != null) {
					return updateSQLHelper.update(method, args);
				}
				if ("toString".equals(method.getName())) {
					return "Proxy for " + interfaceClass + " class";
				}
				throw new UnsupportedOperationException("Can not execute method: " + method);
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}

	}
}
