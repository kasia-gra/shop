package com.codecool.shop.dao;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DatabaseManager {
	public ProductDao productDao;

	public void run() {
		try {
			setup();
		} catch (SQLException throwables) {
			System.err.println("Could not connect to the database.");
		}
	}

	public void setup() throws SQLException {
		DataSource dataSource = connect();
		productDao = new ProductDaoJdbc(dataSource);
	}


	private DataSource connect() throws SQLException {
		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		String dbName = System.getenv("PSQL_DB_NAME");
		String user = System.getenv("PSQL_USER_NAME");
		String password = System.getenv("PSQL_PASSWORD");

		dataSource.setDatabaseName(dbName);
		dataSource.setUser(user);
		dataSource.setPassword(password);

		System.out.println("Trying to connect");
		dataSource.getConnection().close();
		System.out.println("Connection ok.");

		return dataSource;
	}
}
