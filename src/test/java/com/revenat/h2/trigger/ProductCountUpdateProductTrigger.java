package com.revenat.h2.trigger;

import static com.revenat.h2.trigger.Constants.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.tools.TriggerAdapter;

/**
 * This trigger responsible for updating 'product_count' fild in tables
 * 'category' and 'producer' when either 'product.category_id' or
 * 'product.producer_id' changes.
 * 
 * @author Vitaly Dragun
 *
 */
public class ProductCountUpdateProductTrigger extends TriggerAdapter {

	@Override
	public void fire(Connection conn, ResultSet oldRow, ResultSet newRow) throws SQLException {
		int oldCategoryId = oldRow.getInt(CATEGORY_ID);
		int newCategoryId = newRow.getInt(CATEGORY_ID);
		int oldProducerId = oldRow.getInt(PRODUCER_ID);
		int newProducerId = newRow.getInt(PRODUCER_ID);
		
		if (oldCategoryId != newCategoryId) {
			PreparedStatement ps = conn.prepareStatement(
					UPDATE_CATEGORY_DECREMENT_PRODUCT_COUNT);
			ps.setInt(1, oldCategoryId);
			ps.execute();
			ps = conn.prepareStatement(
					UPDATE_CATEGORY_INCREMENT_PRODUCT_COUNT);
			ps.setInt(1, newCategoryId);
			ps.execute();
		}
		if (oldProducerId != newProducerId) {
			PreparedStatement ps = conn.prepareStatement(
					UPDATE_PRODUCER_DECREMENT_PRODUCT_COUNT);
			ps.setInt(1, oldProducerId);
			ps.execute();
			ps = conn.prepareStatement(
					UPDATE_PRODUCER_INCREMENT_PRODUCT_COUNT);
			ps.setInt(1, newProducerId);
			ps.execute();
		}
	}
}
