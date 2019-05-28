package com.revenat.ishop.infrastructure.framework.sql;

import com.revenat.ishop.infrastructure.framework.sql.queries.SelectQuery;

/**
 * This interface represents component responsible for building complex Select
 * queries dynamically based on input {@code builderParams} values
 * 
 * @author Vitaly Dragun
 *
 */
public interface SQLBuilder {

	public SelectQuery buildSelectQuery(Object... builderParams);
}
