package com.revenat.ishop.infrastructure.framework.factory.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.CollectionItem;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Select;
import com.revenat.ishop.infrastructure.framework.exception.FrameworkSystemException;
import com.revenat.ishop.infrastructure.framework.factory.transaction.JDBCConnectionFactory;
import com.revenat.ishop.infrastructure.framework.handler.ResultSetHandler;
import com.revenat.ishop.infrastructure.framework.sql.SQLBuilder;
import com.revenat.ishop.infrastructure.framework.sql.queries.SelectQuery;

/**
 * This component responsible for generating implementation for repository
 * methods annotated with {@link Select} annotation.
 * 
 * @author Vitaly Dragun
 *
 */
class JDBCSelectHelper extends JDBCAbstractSQLHelper {

	public Object select(Select select, Method method, Object[] args)
			throws IllegalAccessException, InvocationTargetException {
		Class<?> entityClass = getResultEntityClass(method);
		ResultSetHandler<?> resultSetHandler = createResultSetHandler(select.resultSetHandlerClass(), method,
				entityClass);
		Class<? extends SQLBuilder> sqlBuilderClass = select.sqlBuilderClass();

		if (sqlBuilderClass == SQLBuilder.class) {
			return JDBCUtils.select(JDBCConnectionFactory.getCurrentConnection(), select.value(), resultSetHandler,
					args);
		} else {
			return select(sqlBuilderClass, resultSetHandler, args);
		}
	}

	private Object select(Class<? extends SQLBuilder> sqlBuilderClass, ResultSetHandler<?> resultSetHandler,
			Object[] args) throws IllegalAccessException {
		try {
			SQLBuilder sqlBuilder = sqlBuilderClass.newInstance();
			SelectQuery sqlQuery = sqlBuilder.buildSelectQuery(args);
			LOGGER.debug("Custom SELECT: {}, {}", sqlQuery.getQuery(), sqlQuery.getParameters());
			return JDBCUtils.select(JDBCConnectionFactory.getCurrentConnection(), sqlQuery.getQuery(), resultSetHandler,
					sqlQuery.getParameters());
		} catch (InstantiationException e) {
			throw new FrameworkSystemException("Can not create instance of SQLBuilder for class: " + sqlBuilderClass,
					e);
		}
	}

	private Class<?> getResultEntityClass(Method method) {
		CollectionItem collectionItem = method.getAnnotation(CollectionItem.class);
		if (collectionItem != null) {
			return collectionItem.value();
		} else {
			Class<?> returnType = method.getReturnType();
			if (returnType.isArray()) {
				throw new FrameworkSystemException(
						"Use collections instead of array as return type for method: " + method);
			} else if (Collection.class.isAssignableFrom(returnType)) {
				throw new FrameworkSystemException("Use @CollectionItem annotation to specify collection item type"
						+ " for method return type for method: " + method);
			} else {
				return returnType;
			}
		}
	}

}
