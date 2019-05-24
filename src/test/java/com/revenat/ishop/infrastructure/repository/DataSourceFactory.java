package com.revenat.ishop.infrastructure.repository;

import org.apache.commons.dbcp2.BasicDataSource;

public class DataSourceFactory {
	private static final int MAX_POOL_SIZE = 1;
	private static final int INIT_POOL_SIZE = 1;
	private static final String H2_JDBC_URL =
			"jdbc:h2:mem:ishop;INIT=RUNSCRIPT FROM 'classpath:script/create-ishop.sql'\\;RUNSCRIPT FROM 'classpath:script/populate-ishop.sql'";
	private static final String H2_USERNAME = "sa";
	private static final String H2_DRIVER_CLASS = "org.h2.Driver";
	private static final String H2_PASSWORD = "";
	
	
	public static BasicDataSource createH2DataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDefaultAutoCommit(false);
		ds.setRollbackOnReturn(true);
		ds.setDriverClassName(H2_DRIVER_CLASS);
		ds.setUrl(H2_JDBC_URL);
		ds.setUsername(H2_USERNAME);
		ds.setPassword(H2_PASSWORD);
		ds.setInitialSize(INIT_POOL_SIZE);
		ds.setMaxTotal(MAX_POOL_SIZE);
		
		return ds;
	}

	private DataSourceFactory() {}
}
