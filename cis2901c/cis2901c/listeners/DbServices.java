package cis2901c.listeners;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import cis2901c.main.Main;
import cis2901c.objects.Customer;
import cis2901c.objects.DbObjectSavable;
import cis2901c.objects.DbObjectSearchable;
import cis2901c.objects.Part;

public class DbServices {
	
	private static Connection mainDbConnection = null;
	
	private DbServices() {
	}
	
	// START General DB methods
	public static boolean isConnected() {
		return mainDbConnection != null;
	}
	
	public static Connection getDbConnection() {
		return mainDbConnection;		
	}
	
	public static void connectToDb() {
		String url = "jdbc:mysql://localhost:3306/cis2901c";
		String user = "TestUser";
		String pass = "test";
		try {
			mainDbConnection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			Main.log(Level.SEVERE, "Error connecting to database");
			e.printStackTrace();
		}
	}
	
	public static void disconnectFromDb() {
		// clean up, close connection
		try {
			mainDbConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveObject(DbObjectSavable dbObject) {
		// public Save Object interface
		if (dbObject == null) {
			return;
		}
		StringBuilder queryString = new StringBuilder();
		boolean creatingNewObject = (dbObject.getDbPk() == -1);
		// start building query string
		if (creatingNewObject) {
			queryString.append("INSERT INTO cis2901c." + dbObject.getTableName() + " () VALUES ();" );
		} else {
			queryString.append("UPDATE cis2901c." + dbObject.getTableName() + " SET WHERE " + dbObject.getPkName() + " = " + dbObject.getDbPk() + ";");
		}
		// check which object data is present and add those fields to query string
		List<String> dbFields = insertFieldsIntoQuery(queryString, dbObject, creatingNewObject);

		// build query Statement and fill parameters
		sendQueryToDb(queryString, dbFields);
	}

	private static void sendQueryToDb(StringBuilder queryString, List<String> dbFields) {
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(queryString.toString())) {
			int parameterIndex = 1;
			for(String fieldData : dbFields) {
				statement.setString(parameterIndex, fieldData);
				parameterIndex++;
			}
			statement.execute();
			Main.log(Level.INFO, "Update Count: " + statement.getUpdateCount());
		} catch (SQLException e) {
			Main.log(Level.SEVERE, "SQL Error: " + queryString);
			e.printStackTrace();
		}
	}
	
	private static List<String> insertFieldsIntoQuery(StringBuilder queryString, DbObjectSavable dbObject, boolean creatingNewObject) {
		List<String> results = new ArrayList<>();
		for (Map.Entry<String, String> entry : dbObject.getDataMap().entrySet()) {
			if (entry.getValue() != null) {
				if (creatingNewObject) {
					insertQueryHelper(queryString, entry.getKey(), "?");
				} else {
					updateQueryHelper(queryString, entry.getKey(), "?");
				}
				results.add(entry.getValue().trim());
			}
		}
		return results;
	}
	
	private static void updateQueryHelper(StringBuilder queryString, String columnName, String data) {
		StringBuilder column = new StringBuilder();
		int columnInsertionPoint = queryString.lastIndexOf("WHERE");	// find where to insert fields and data
		if (queryString.charAt(columnInsertionPoint - 2) == '?') {		// see if there are already other data
			column.append(", ");											// and we need to add a comma
		}
		column.append(columnName + " = " + data + " ");
		queryString.insert(columnInsertionPoint, column);		// insert into query
	}
	
	private static void insertQueryHelper(StringBuilder queryString, String columnName, String data) {
		StringBuilder column = new StringBuilder();
		int columnInsertionPoint = queryString.indexOf(")");		// find where to insert field
		if (queryString.charAt(columnInsertionPoint - 1) != '(') {
			column.append(", ");
		}
		column.append(columnName);
		queryString.insert(columnInsertionPoint, column);
		
		StringBuilder field = new StringBuilder();		// find where to insert data
		int fieldInsertionPoint = queryString.lastIndexOf(")");
		if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
			field.append(", ");
		}
		field.append(data);
		queryString.insert(fieldInsertionPoint, field);
	}
	
	public static String[] sanitizer(String searchQuery) {
		// simple regex to remove chars i don't want to search for
			// !!!! this is not to be taken as SQL Injection protection
		return searchQuery.replaceAll("[^a-zA-Z0-9 %'-]", "").split(" ");
	}
	
	public static Object searchForCustomer(long customerId) {
		Object results = new Object();
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement("""
				SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, 
				email, customerId FROM cis2901c.customer WHERE customerId = ?;""")) {
			statement.setLong(1, customerId);
			ResultSet queryResultSet = statement.executeQuery();
			results = buildFoundObjects(queryResultSet, new Customer(), 1)[0];
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;	
	}
	
	public static Object[] searchForObject(DbObjectSearchable object) {
		final int MAX_RESULTS = 255;		// max search return results
		Object[] results = new Object[1];
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(object.getSearchQuery())) {
			statement.setMaxRows(MAX_RESULTS);
			fillStatementParameters(statement, object);
			ResultSet queryResultSet = statement.executeQuery();
			results = buildFoundObjects(queryResultSet, object, MAX_RESULTS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	private static void fillStatementParameters(PreparedStatement statement, DbObjectSearchable object) throws SQLException {
		ParameterMetaData parameterMetaData = statement.getParameterMetaData();
		int parameterCount = parameterMetaData.getParameterCount();
		int queryWord = object.getWordsFromSearchString().length - 1;
		for (int j = 1; j <= parameterCount; j++) {
			// fill search fields in queryString
			statement.setString(j, "%" + object.getWordsFromSearchString()[queryWord] + "%");
			int parametersPerQuery = parameterCount / object.getWordsFromSearchString().length; 
			if (j % parametersPerQuery == 0) {
				queryWord--;
			}
		}
	}
	
	private static Object[] buildFoundObjects(ResultSet queryResultSet, DbObjectSearchable object, int maxResults) throws SQLException {
		Object[] results = new Object[1];
		if (object instanceof Customer) {
			results = new Customer[maxResults];
			int i = 0;
			while (queryResultSet.next()) {
				Customer customer = new Customer();
				customer.setFirstName(queryResultSet.getString(1));
				customer.setLastName(queryResultSet.getString(2));
				customer.setAddress(queryResultSet.getString(3));
				customer.setCity(queryResultSet.getString(4));
				customer.setState(queryResultSet.getString(5));			
				customer.setZipCode(queryResultSet.getString(6));
				customer.setHomePhone(queryResultSet.getString(7));
				customer.setWorkPhone(queryResultSet.getString(8));
				customer.setCellPhone(queryResultSet.getString(9));
				customer.setEmail(queryResultSet.getString(10));
				customer.setCustomerId(queryResultSet.getLong(11));

				results[i] = customer;
				i++;
			}
		} else if (object instanceof Part) {
			results = new Part[maxResults];
			int i = 0;
			while (queryResultSet.next()) {
				
				Part part = new Part();
				part.setPartId(queryResultSet.getInt(1));
				part.setPartNumber(queryResultSet.getString(2));
				part.setSupplier(queryResultSet.getString(3));
				part.setCategory(queryResultSet.getString(4));
				part.setDescription(queryResultSet.getString(5));
				part.setNotes(queryResultSet.getString(6));
				part.setCost(queryResultSet.getBigDecimal(7));
				part.setRetail(queryResultSet.getBigDecimal(8));
				part.setOnHand(queryResultSet.getInt(9));
				
				results[i] = part;
				i++;
			}
		}
		return results;
	}
}
