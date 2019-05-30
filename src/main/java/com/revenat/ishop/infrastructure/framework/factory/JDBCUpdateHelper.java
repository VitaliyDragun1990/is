package com.revenat.ishop.infrastructure.framework.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.infrastructure.framework.annotation.persistence.entity.Child;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.entity.Table;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Update;
import com.revenat.ishop.infrastructure.framework.exception.FrameworkPersistenceException;
import com.revenat.ishop.infrastructure.framework.exception.FrameworkSystemException;
import com.revenat.ishop.infrastructure.framework.sql.queries.UpdateQuery;
import com.revenat.ishop.infrastructure.framework.util.ReflectionUtils;

/**
 * This component responsible for generating implementation for repository
 * methods annotated with {@link Update} annotation.
 * 
 * @author Vitaly Dragun
 *
 */
class JDBCUpdateHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(JDBCUpdateHelper.class);
	
	Object update(Method method, Object[] args) throws IllegalAccessException {
		validateMethodArgs(method, args);
		
		Object entity = args[0];
		Class<?> entityClass = entity.getClass();
		Table table = entityClass.getAnnotation(Table.class);
		validateTableAnnotation(table, entityClass);
		
		return updateEntity(entity, table);
		
	}
	
	private Object updateEntity(Object entity, Table table) throws IllegalAccessException {
		List<Field> entityFields = ReflectionUtils.getAccessibleFields(entity.getClass());
		
		UpdateQuery sql = build(entity, table, entityFields);
		LOGGER.debug("UPDATE: {}, {}", sql.getQuery(), sql.getParameters());
		
		int updatedRows = JDBCUtils.executeUpdate(
				JDBCConnectionFactory.getCurrentConnection(),
				sql.getQuery(),
				sql.getParameters());
		
		if (updatedRows == 1) {
			return Boolean.TRUE;
		} else if (updatedRows == 0) {
			return Boolean.FALSE;
		} else {
			throw new FrameworkPersistenceException("Error while updating " + table.name() + 
					": maximum allowed records to be updated per single execution = 1, but was update: " + updatedRows);
		}
	}

	private UpdateQuery build(Object entity, Table table, List<Field> entityFields) throws IllegalAccessException {
		StringBuilder sql = new StringBuilder("UPDATE ").append(table.name()).append(" SET ");
		StringBuilder whereClause = new StringBuilder(" WHERE ");
		
		String idFieldName = table.idField();
		Field idField = findIdField(entityFields, idFieldName, entity);
		Object idFieldValue = findIdFieldValue(entity, idField);
		String idColumnName = getIdColumnName(idField);
		
		whereClause.append(idColumnName).append("=?");
		
		String updateClause = generateUpdateClause(entityFields, idFieldName);
		Object[] params = generateParameters(entity, entityFields, idFieldName, idFieldValue);
		
		sql.append(updateClause).append(whereClause);
		
		return new UpdateQuery(sql.toString(), params);
	}

	private Field findIdField(List<Field> entityFields, String idFieldName, Object entity) {
		for (Field field : entityFields) {
			if (field.getName().equals(idFieldName)) {
				return field;
			}
		}
		throw new FrameworkSystemException("Id field with name=" + idFieldName + " not found for class: " + entity.getClass());
	}
	
	private Object findIdFieldValue(Object entity, Field idField) throws IllegalAccessException {
		Object idFieldValue = idField.get(entity);
		if (idFieldValue == null) {
			throw new FrameworkPersistenceException("Can not update entity " + entity.getClass() + " with null id field");
		}
		return idFieldValue;
	}

	private String generateUpdateClause(List<Field> entityFields, String idFieldName) {
		StringBuilder updateClause = new StringBuilder();
		
		for (Field field : entityFields) {
			if (notIdField(idFieldName, field)) {
				updateClause.append(getColumnName(field)).append("=?,");
			}
		}
		
		return updateClause.deleteCharAt(updateClause.length()-1).toString();
	}
	
	private Object[] generateParameters(Object entity, List<Field> entityFields, String idFieldName, Object idFieldValue) 
			throws IllegalAccessException {
		List<Object> params = new ArrayList<>();
		
		for(Field field : entityFields) {
			if (notIdField(idFieldName, field)) {
				params.add(getFieldValue(field, entity));
			}
		}
		
		params.add(idFieldValue);
		return params.toArray();
	}

	private Object getFieldValue(Field field, Object entity) throws IllegalAccessException {
		Child child = field.getAnnotation(Child.class);
		Object fieldValue = findIdFieldValue(entity, field);
		if (child != null) {
			List<Field> childFields = ReflectionUtils.getAccessibleFields(fieldValue.getClass());
			Field childIdField = ReflectionUtils.findField(fieldValue.getClass(), childFields, child.idFieldName());
			return findIdFieldValue(fieldValue, childIdField);
		}
		return fieldValue;
	}

	private String getColumnName(Field field) {
		Child child = field.getAnnotation(Child.class);
		if (child != null) {
			return child.idColumnName().toUpperCase();
		}
		return ReflectionUtils.getColumnNameForField(field);
	}

	private static boolean notIdField(String idFieldName, Field field) {
		return !field.getName().equals(idFieldName);
	}

	private String getIdColumnName(Field idField) {
		return ReflectionUtils.getColumnNameForField(idField);
	}

	private static void validateMethodArgs(Method method, Object[] args) {
		if (args.length != 1) {
			throw new FrameworkSystemException("Method with @Insert annotation: " + method + " should have one argument only!");
		}
	}
	
	private static void validateTableAnnotation(Table table, Class<?> entityClass) {
		if (table == null) {
			throw new FrameworkSystemException("Entity class " + entityClass + " does not have @Table annotation!");
		}
	}
}
