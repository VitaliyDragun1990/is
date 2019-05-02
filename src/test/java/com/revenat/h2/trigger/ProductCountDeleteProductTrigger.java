package com.revenat.h2.trigger;

import static com.revenat.h2.trigger.Constants.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.tools.TriggerAdapter;

public class ProductCountDeleteProductTrigger extends TriggerAdapter {

	@Override
	public void fire(Connection conn, ResultSet oldRow, ResultSet newRow) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(UPDATE_CATEGORY_DECREMENT_PRODUCT_COUNT);
		ps.setInt(1, oldRow.getInt(CATEGORY_ID));
		ps.execute();
		
		ps = conn.prepareStatement(UPDATE_PRODUCER_DECREMENT_PRODUCT_COUNT);
		ps.setInt(1, oldRow.getInt(PRODUCER_ID));
		ps.execute();
	}
}
