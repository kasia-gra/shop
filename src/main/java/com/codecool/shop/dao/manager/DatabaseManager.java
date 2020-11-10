package com.codecool.shop.dao.manager;

import com.codecool.shop.dao.dao.*;
import com.codecool.shop.dao.jdbc.*;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseManager {
	private static DatabaseManager instance = null;

	public ProductDao productDao;
	public SupplierDao supplierDao;
	public ProductCategoryDao categoryDao;

	public LineItemDao lineItemDao;
	public AddressDao addressDao;
	public AddressDetailDao addressDetailDao;

	private DatabaseManager() {
	}

	public static DatabaseManager getInstance() {
		if (instance == null) {
			instance = new DatabaseManager();
		}
		return instance;
	}

	public void run() {
		try {
			setup();
		} catch (SQLException | IOException throwables) {
			System.err.println("Could not connect to the database.");
		}
	}

	public void setup() throws SQLException, IOException {
		DataSource dataSource = connect();;
		supplierDao = new SupplierDaoJdbc(dataSource);
		categoryDao = new ProductCategoryDaoJdbc(dataSource);
		productDao = new ProductDaoJdbc(dataSource, supplierDao, categoryDao);
		lineItemDao = new LineItemDaoJdbc(dataSource);
		addressDao = new AddressDaoJdbc(dataSource);
		addressDetailDao = new AddressDetailDaoJdbc(dataSource, addressDao);

	}

	private DataSource connect() throws SQLException, IOException {
		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		Properties db_config = new Properties();
		db_config.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db_config.properties"));
		String dbName = db_config.getProperty("dbName");
		String user = db_config.getProperty("user");
		String password = db_config.getProperty("password");

//		String dbName = System.getenv("PSQL_DB_NAME");
//		String user = System.getenv("PSQL_USER_NAME");
//		String password = System.getenv("PSQL_PASSWORD");

		dataSource.setDatabaseName(dbName);
		dataSource.setUser(user);
		dataSource.setPassword(password);

		System.out.println("Trying to connect");
		dataSource.getConnection().close();
		System.out.println("Connection ok.");

		return dataSource;
	}
}
