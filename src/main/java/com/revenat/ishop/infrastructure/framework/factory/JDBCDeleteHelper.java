package com.revenat.ishop.infrastructure.framework.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.infrastructure.framework.annotation.persistence.entity.Table;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Delete;
import com.revenat.ishop.infrastructure.framework.exception.FrameworkPersistenceException;
import com.revenat.ishop.infrastructure.framework.exception.FrameworkSystemException;
import com.revenat.ishop.infrastructure.framework.sql.queries.DeleteQuery;
import com.revenat.ishop.infrastructure.framework.util.ReflectionUtils;

/**
 * This component responsible for generating implementation for repository
 * methods annotated with {@link Delete} annotation.
 * 
 * @author Vitaly Dragun
 *
 */
class JDBCDeleteHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(JDBCDeleteHelper.class);
	
	public Object delete(Method method, Object[] args) throws IllegalAccessException {
		validateMethodArgs(method, args);
		
		Object entity = args[0];
		Class<?> entityClass = entity.getClass();
		Table table = entityClass.getAnnotation(Table.class);
		
		return deleteEntity(entity, table);
	}
	
	private Object deleteEntity(Object entity, Table table) throws IllegalAccessException {
		List<Field> entityFields = ReflectionUtils.getAccessibleFields(entity.getClass());
		
		DeleteQuery sql = build(entity, table, entityFields);
		LOGGER.debug("DELETE: {}, {}", sql.getQuery(), sql.getParameters());
		
		int deletedRows = JDBCUtils.executeUpdate(JDBCConnectionFactory.getCurrentConnection(), sql.getQuery(), sql.getParameters());
		if (deletedRows == 1) {
			return Boolean.TRUE;
		} else if (deletedRows == 0) {
			return Boolean.FALSE;
		} else {
			throw new FrameworkPersistenceException("Error while deleting " + table.name() + 
					": maximum allowed records to be deleted per single execution = 1, but was deleted: " + deletedRows);
		}
	}

	private DeleteQuery build(Object entity, Table table, List<Field> entityFields)
			throws IllegalAccessException {
		StringBuilder sql = new StringBuilder("DELETE FROM ").append(table.name()).append(" WHERE ");
		
		String idFieldName = table.id();
		Field idField = findIdField(entityFields, idFieldName, entity);
		Object idFieldValue = idField.get(entity);
		String idColumnName = getIdColumnName(idField);
		
		sql.append(idColumnName).append("=?");
		
		return new DeleteQuery(sql.toString(), new Object[] {idFieldValue});
	}

	private String getIdColumnName(Field idField) {
		return ReflectionUtils.getColumnNameForField(idField);
	}

	private Field findIdField(List<Field> entityFields, String idFieldName, Object entity) {
		for (Field field : entityFields) {
			if (field.getName().equals(idFieldName)) {
				return field;
			}
		}
		throw new FrameworkSystemException("Id field with name=" + idFieldName + " not found for class: " + entity.getClass());
	}

	private static void validateMethodArgs(Method method, Object[] args) {
		if (args.length != 1) {
			throw new FrameworkSystemException("Method with @Delete annotation: " + method + " should have one argument only!");
		}
	}

}
