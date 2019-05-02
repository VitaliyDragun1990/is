package com.revenat.h2.trigger;

import static com.revenat.h2.trigger.Constants.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.tools.TriggerAdapter;

public class ProductCountInsertProductTrigger extends TriggerAdapter {

	@Override
	public void fire(Connection conn, ResultSet oldRow, ResultSet newRow) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(UPDATE_CATEGORY_INCREMENT_PRODUCT_COUNT);
		ps.setInt(1, newRow.getInt(CATEGORY_ID));
		ps.execute();
		
		ps = conn.prepareStatement(UPDATE_PRODUCER_INCREMENT_PRODUCT_COUNT);
		ps.setInt(1, newRow.getInt(PRODUCER_ID));
		ps.execute();
	}
}
