package com.revenat.ishop.infrastructure.framework.handler.base;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revenat.ishop.infrastructure.framework.annotation.jdbc.Child;
import com.revenat.ishop.infrastructure.framework.converter.Converter;
import com.revenat.ishop.infrastructure.framework.converter.impl.DefaultConverter;
import com.revenat.ishop.infrastructure.framework.exception.FrameworkSystemException;
import com.revenat.ishop.infrastructure.framework.util.ReflectionUtils;
import com.revenat.ishop.infrastructure.framework.util.JDBCUtils.ResultSetHandler;

public class DefaultResultSetHandler<T> implements ResultSetHandler<T> {
	protected final Class<T> entityClass;
	protected final Converter converter;

	public DefaultResultSetHandler(Class<T> entityClass, Converter converter) {
		this.entityClass = entityClass;
		this.converter = converter;
	}
	
	public DefaultResultSetHandler(Class<T> entityClass) {
		this.entityClass = entityClass;
		this.converter = new DefaultConverter();
	}

	@Override
	public T handle(ResultSet rs) throws SQLException {
		try {
			T entity = entityClass.newInstance();
			List<Field> entityFields = ReflectionUtils.getAccessibleEntityFields(entityClass);
			List<String> columns = getAllColumns(rs);
			populateFields(entityFields, entity, columns, rs);
			return entity;
		} catch (InstantiationException e) {
			throw new FrameworkSystemException("Accessible constructor without parameters not found or class is abstract",e);
		} catch (IllegalAccessException e) {
			throw new FrameworkSystemException(e);
		}
	}
	
	protected List<String> getAllColumns(ResultSet rs) throws SQLException {
		List<String> columns = new ArrayList<>();
		for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
			columns.add(rs.getMetaData().getColumnLabel(i+1).toUpperCase());
		}
		return columns;
	}
	
	protected void populateFields(List<Field> fields, Object entity, List<String> columns, ResultSet rs)
			throws InstantiationException, IllegalAccessException, SQLException {
		for (Field field : fields) {
			Class<?> fieldClass = field.getType();
			Child child = field.getAnnotation(Child.class);
			if (child != null) {
				populateChildField(child, fieldClass, field, entity, rs, columns);
			} else {
				populateSimpleField(fieldClass, field, entity, rs, columns);
			}
		}
	}

	protected void populateChildField(Child child, Class<?> fieldClass, Field field, Object entity, ResultSet rs,
			List<String> columns) throws InstantiationException, IllegalAccessException, SQLException {
		Object embeddedInstance = fieldClass.newInstance();
		field.set(entity, embeddedInstance);
		List<Field> embeddedInstanceFields = ReflectionUtils.getAccessibleEntityFields(fieldClass);
		populateFields(embeddedInstanceFields, embeddedInstance, columns, rs);
		
		Field idField = ReflectionUtils.findField(fieldClass, embeddedInstanceFields, child.idFieldName());
		String idColumnName = child.idColumnName().toUpperCase();
		if (columns.contains(idColumnName)) {
			setChildIdField(idColumnName, rs, embeddedInstance, idField);
		}
	}

	private void setChildIdField(String idColumnName, ResultSet rs, Object embeddedInstance, Field idField)
			throws SQLException, IllegalAccessException {
		try {
			Object idValue = rs.getObject(idColumnName);
			idField.set(embeddedInstance, idValue);
		} catch (IllegalArgumentException | SQLException e) {
			// If result set does not contain such column or contains but in incompatible type
			idField.set(embeddedInstance, null);
		}
	}
	
	protected void populateSimpleField(Class<?> fieldClass, Field field, Object entity, ResultSet rs,
			List<String> columns) throws SQLException, IllegalAccessException {
		String columnName = ReflectionUtils.getColumnNameForField(field);
		if (columns.contains(columnName)) {
			Object valueFromDB = rs.getObject(columnName);
			Object convertedValueFromDB = converter.convert(fieldClass, valueFromDB);
			field.set(entity, convertedValueFromDB);
		}
	}
}
