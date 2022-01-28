package cis2901c.listeners;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Table;

import cis2901c.objects.Customer;
import cis2901c.objects.DbObject;
import cis2901c.objects.Part;
import cis2901c.objects.Unit;

public class DbServices {
	// TODO add status icon somewhere to show when we're connected to DB
		// set icon in initial connectToDb method call
		// maybe check isConnected before every getDbConnection call, and set icon then too
	
	private static Connection mainDbConnection = null;
	
	// START General DB methods
	public static boolean isConnected() {
		return mainDbConnection != null;
	}
	
	public static Connection getDbConnection() {
		return mainDbConnection;		
	}
	
	public static void connectToDb() {
		// TODO offer some customization options in application like actual DB options
		String url = "jdbc:mysql://localhost:3306/cis2901c";
		String user = "TestUser";
		String pass = "test";
		try {
			mainDbConnection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			System.out.println("Error connecting to database");
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
	
	public static void saveObject(DbObject dbObject) {
		// public Save Object interface
		StringBuilder queryString = new StringBuilder();
		List<String> fields = null;
	
		if (dbObject.getDbPk() == -1) {
			queryString.append("INSERT INTO cis2901c." + dbObject.getTableName() + " () VALUES ();" );
			fields = buildAddNewObjectQuery(dbObject, queryString);
		} else {
			queryString.append("UPDATE cis2901c." + dbObject.getTableName() + " SET WHERE " + dbObject.getPkName() + " = " + dbObject.getDbPk() + ";");
			fields = buildModifyExistingObjectQuery(dbObject, queryString);
		}
		
		if (queryString != null) {
			Connection dbConnection = DbServices.getDbConnection();
			try {
				PreparedStatement statement = dbConnection.prepareStatement(queryString.toString());
				
				int parameterIndex = 1;
				for(String fieldData : fields) {
					statement.setString(parameterIndex, fieldData);
					parameterIndex++;
				}
				
				statement.execute();
				System.out.println("Update Count: " + statement.getUpdateCount());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(queryString);
				e.printStackTrace();
			}
		}
	}
	
	private static List<String> buildAddNewObjectQuery(DbObject dbObject, StringBuilder queryString) {
		// add new object
		boolean isAnythingModified = false;
		
		// build query out of DbObject
		
		Map<String, String> dataMap = dbObject.getDataMap();
		List<String> fields = new ArrayList<>();
				
		for (Map.Entry<String, String> entry : dataMap.entrySet()) {
			if (entry.getValue() != null) {
				isAnythingModified = true;
				insertQueryHelper(queryString, entry.getKey(), "?");
				fields.add(entry.getValue().trim());
			}
		}
		if (isAnythingModified == false) {
			return null;
		} else {
			return fields;
		}
	}
	
	
	private static List<String> buildModifyExistingObjectQuery(DbObject dbObject, StringBuilder queryString) {
		// save modifications to existing unit
		boolean isAnythingModified = false;
		
		Map<String, String> dataMap = dbObject.getDataMap();
		List<String> fields = new ArrayList<>();

		for (Map.Entry<String, String> entry : dataMap.entrySet()) {
			if (entry.getValue() != null) {
				isAnythingModified = true;
				updateQueryHelper(queryString, entry.getKey(), "?");
				fields.add(entry.getValue().trim());
			}
		}
		if (isAnythingModified == false) {
			return null;
		} else {
			return fields;
		}
	}
	
	
	
	public static Object[] searchForObject(Table resultsTable, String searchQuery) {
		// public search interface
			// search for searchQuery and display in resultsTable
			// TODO there might be a better way to check what type we're searching
		if (resultsTable.getColumn(0).getText().equals("First Name")) {
			return searchForCustomer(resultsTable, searchQuery);
		} else if (resultsTable.getColumn(0).getText().equals("Owner")) {
			return searchForUnit(resultsTable, searchQuery);
		} else if (resultsTable.getColumn(0).getText().equals("Part Number")) {
			return searchForPart(resultsTable, searchQuery);
		}
		return null;
	}
	
	private static void updateQueryHelper(StringBuilder queryString, String columnName, String data) {
		// build Edit Current Object SQL query
		StringBuilder column = new StringBuilder();
		int columnInsertionPoint = queryString.lastIndexOf("WHERE");
		if (queryString.charAt(columnInsertionPoint - 2) == '?') {
			column.append(", ");
		}
		column.append(columnName + " = " + data + " ");
		queryString.insert(columnInsertionPoint, column);		
	}
	
	private static void insertQueryHelper(StringBuilder queryString, String columnName, String data) {
		// build New Object SQL query
		StringBuilder column = new StringBuilder();
		int columnInsertionPoint = queryString.indexOf(")");
		if (queryString.charAt(columnInsertionPoint - 1) != '(') {
			column.append(", ");
		}
		column.append(columnName);
		queryString.insert(columnInsertionPoint, column);
		
		StringBuilder field = new StringBuilder();
		int fieldInsertionPoint = queryString.lastIndexOf(")");
		if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
			field.append(", ");
		}
		field.append(data);
		queryString.insert(fieldInsertionPoint, field);
	}
	
	private static String[] sanitizer(String searchQuery) {
		// simple regex to remove chars i don't want to search for
			// !!!! this is not to be taken as SQL Injection protection
		return searchQuery.replaceAll("[^a-zA-Z0-9 %'-]", "").split(" ");
	}
	
	private static String numberSanitizer(String searchQuery) {
		// simple regex to remove chars i don't want to search for
			// !!!! this is not to be taken as SQL Injection protection
		return searchQuery.replaceAll("[^0-9]", "");
	}
	// END General DB methods
	
	// START Unit object methods
	private static Unit[] searchForUnit(Table resultsTable, String searchQuery) {
		final int MAX_RESULTS = 255;		// max search return results, could set limit at DB query level to lessen load on DB server

		// setup search
		List<String> wordsFromQuery = Arrays.asList(sanitizer(searchQuery));	// remove most non-alphanumerics

		Connection dbConnection = DbServices.getDbConnection();	// get connection to DB
		// build query string
		StringBuilder subquery = new StringBuilder("SELECT u.unitId, u.customerId, u.make, u.model, u.modelname, u.year, u.mileage, u.color, u.vin, u.notes, "
				+ "c.lastName, c.firstName FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId " 
				+ "WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR u.vin LIKE ? OR u.make LIKE ? OR u.model LIKE ? OR u.year LIKE ?;");

		for (int i = 0; i < wordsFromQuery.size(); i++) {
			if (i > 0) {
				// if we're looping through multiple search terms, modify query string to add multi-row  Subquery using WHERE ... IN
				subquery.delete(15, 128);
				subquery.insert(0, "SELECT u.unitId, u.customerId, u.make, u.model, u.modelname, u.year, u.mileage, u.color, u.vin, u.notes, "
						+ "c.lastName, c.firstName FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId " 
						+ "WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR u.vin LIKE ? OR u.make LIKE ? OR u.model LIKE ? OR u.year LIKE ? "
						+ "AND unitId IN (");
				subquery.replace(subquery.length() - 1, subquery.length(), ");");
			}
		}

		// build Unit[] to return DB search results
		Unit[] unitResults = new Unit[MAX_RESULTS];
		try {
			PreparedStatement statement = dbConnection.prepareStatement(subquery.toString());
			
			ParameterMetaData parameterMetaData = statement.getParameterMetaData();
			
			Collections.reverse(wordsFromQuery);
			
			int queryWord = 0;
			
			// TODO it might be better to fill the parameters from the back/from the last
				// one, this way we wouldn't have to make wordsFromQuery an ArrayList
				// and reverse it
			
			for (int j = 1; j <= parameterMetaData.getParameterCount(); j++) {
				// fill search fields in queryString
				
				statement.setString(j, "%" + wordsFromQuery.get(queryWord) + "%");
				if (j % 6 == 0) {
					queryWord++;
				}
			}
			
			ResultSet unitQueryResults = statement.executeQuery();
			int i = 0;
			while (unitQueryResults.next() && i < MAX_RESULTS) {
				long unitId = unitQueryResults.getLong(1);
				long customerId = unitQueryResults.getLong(2);
				String make = unitQueryResults.getString(3);
				String model = unitQueryResults.getString(4);
				String modelName = unitQueryResults.getString(5);
				int year = unitQueryResults.getInt(6);
				int mileage = unitQueryResults.getInt(7);
				String color = unitQueryResults.getString(8);
				String vin = unitQueryResults.getString(9);
				String notes = unitQueryResults.getString(10);
				String lastName = unitQueryResults.getString(11);
				String firstName = unitQueryResults.getString(12);
				String owner = lastName;

				if (firstName != null && firstName.length() != 0) {
					owner = lastName + ", " + firstName;
				}

				Unit unit = new Unit(unitId, customerId, make, model, modelName, year, mileage, color, vin, notes, owner);
				unitResults[i] = unit;
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unitResults;
	}
	// END Unit object methods

	// START Customer object methods
	private static Customer[] searchForCustomer(Table resultsTable, String searchQuery) {		
		final int MAX_RESULTS = 255;		// max search return results, could set limit at DB query level to lessen load on DB server
			// TODO this is do-able with the statement.SetMaxRows or somthing like that

		// setup search
		List<String> wordsFromQuery = Arrays.asList(sanitizer(searchQuery));

		
		Connection dbConnection = DbServices.getDbConnection();
		// build query string
		StringBuilder subquery = new StringBuilder("SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, "
				+ "email, customerId FROM cis2901c.customer WHERE firstName LIKE ? OR homePhone LIKE ? OR lastName LIKE ? OR workPhone LIKE ? OR address LIKE ? OR cellPhone LIKE ? "
				+ "OR city LIKE ? OR zipcode LIKE ? OR state LIKE ?;");
		
		for (int i = 0; i < wordsFromQuery.size(); i++) {
			if (i > 0) {
				// if we're looping through multiple search terms, modify query string to add multi-row  Subquery using WHERE ... IN
				subquery.delete(7, 99);
				subquery.insert(0,"SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, "
						+ "email, customerId FROM cis2901c.customer WHERE firstName LIKE ? OR homePhone LIKE ? OR lastName LIKE ? OR workPhone LIKE ? OR address LIKE ? OR cellPhone LIKE ? "
						+ "OR city LIKE ? OR zipcode LIKE ? OR state LIKE ? AND customerId IN (");
				subquery.replace(subquery.length() - 1, subquery.length(), ");");
			}
		}
		
		
		Customer[] customerResults = new Customer[MAX_RESULTS];
		try {
		PreparedStatement statement = dbConnection.prepareStatement(subquery.toString());
		
		ParameterMetaData parameterMetaData = statement.getParameterMetaData();
		
		Collections.reverse(wordsFromQuery);
		
		int queryWord = 0;
		
		// TODO it might be better to fill the parameters from the back/from the last
			// one, this way we wouldn't have to make wordsFromQuery an ArrayList
			// and reverse it
		
		for (int j = 1; j <= parameterMetaData.getParameterCount() - 1; j++) {
			String number = numberSanitizer(wordsFromQuery.get(queryWord));
			number = number.equals("") ? "-1" : number;
			
			statement.setString(j, "%" + wordsFromQuery.get(queryWord) + "%");
			j++;
			statement.setString(j, "%" + number + "%");
			// TODO i still want to refactor this, this statement building is identical
				// in all versions of this, it can return the ResultSet, then each object
				// method can build and prep objects like in below WHILE statement
				// need to pass in outer query, subquery, how much to delete for subquery
				// search term, below condition checker for j = parameterCount / wordsFromQuery.size()
			if (j % 9 == 0) {
				queryWord++;
			}
		}
		statement.setString(9, "%" + wordsFromQuery.get(queryWord) + "%");
		
		ResultSet customerQueryResults = statement.executeQuery();
		int i = 0;
		while (customerQueryResults.next() && i < MAX_RESULTS) {
			String firstName = customerQueryResults.getString(1);
			String lastName = customerQueryResults.getString(2);
			String address = customerQueryResults.getString(3);
			String city = customerQueryResults.getString(4);
			String state = customerQueryResults.getString(5);			
			String zip = customerQueryResults.getString(6);
			String homePhone = customerQueryResults.getString(7);
			String workPhone = customerQueryResults.getString(8);
			String cellPhone = customerQueryResults.getString(9);
			String email = customerQueryResults.getString(10);
			long customerId = customerQueryResults.getLong(11);
			
			Customer customer = new Customer(customerId, firstName, lastName, address, city, state,
					zip, homePhone, workPhone, cellPhone, email);
			customerResults[i] = customer;
			i++;
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customerResults;
	}
	// END Customer methods
	
	// Part methods
	private static Part[] searchForPart(Table resultsTable, String searchQuery) {
		final int MAX_RESULTS = 255;		// max search return results, could set limit at DB query level to lessen load on DB server

		// setup search
//		String[] wordsFromQuery = sanitizer(searchQuery);	// remove most non-alphanumerics
		List<String> wordsFromQuery = Arrays.asList(sanitizer(searchQuery));

		Connection dbConnection = DbServices.getDbConnection();	// connect to DB
		// build query string
		StringBuilder subquery = new StringBuilder("SELECT partId, partNumber, supplier, category, description, notes, cost, retail, onHand FROM cis2901c.part " 
				+ "WHERE partNumber LIKE ? OR supplier LIKE ? OR category LIKE ? OR description LIKE ?;");

		for (int i = 0; i < wordsFromQuery.size(); i++) {
			if (i > 0) {
				// if we're looping through multiple search terms, modify query string to add multi-row  Subquery using WHERE ... IN
				// TODO set correct queries
				subquery.delete(13, 87);
				subquery.insert(0, "SELECT partId, partNumber, supplier, category, description, notes, cost, retail, onHand FROM cis2901c.part " 
						+ "WHERE partNumber LIKE ? OR supplier LIKE ? OR category LIKE ? OR description LIKE ? "
						+ "AND partId IN (");
				subquery.replace(subquery.length() - 1, subquery.length(), ");");
			}

//			for (int j = 1; j <= 4; j++) {
//				// fill search fields in queryString
//				int insertIndex = subquery.indexOf("?");
//				subquery.replace(insertIndex, insertIndex + 1, "'%" + wordsFromQuery[i] + "%'");
//			}
		}

		// build Unit[] to return DB search results
		Part[] partResults = new Part[MAX_RESULTS];
		try {
			PreparedStatement statement = dbConnection.prepareStatement(subquery.toString());
			
			ParameterMetaData parameterMetaData = statement.getParameterMetaData();
			
			Collections.reverse(wordsFromQuery);
			
			int queryWord = 0;
			
			for (int j = 1; j <= parameterMetaData.getParameterCount(); j++) {
				// fill search fields in queryString
				
				statement.setString(j, "%" + wordsFromQuery.get(queryWord) + "%");
//				int insertIndex = subquery.indexOf("?");
//				subquery.replace(insertIndex, insertIndex + 1, "'%" + wordsFromQuery[queryWord] + "%'");
				if (j % 4 == 0) {
					queryWord++;
				}
			}
			
			ResultSet partQueryResults = statement.executeQuery();
			int i = 0;
			while (partQueryResults.next() && i < MAX_RESULTS) {
				int partId = partQueryResults.getInt(1);
				String partNumber = partQueryResults.getString(2);
				String supplier = partQueryResults.getString(3);
				String category = partQueryResults.getString(4);
				String description = partQueryResults.getString(5);
				String notes = partQueryResults.getString(6);
				BigDecimal cost = new BigDecimal(partQueryResults.getInt(7));
				BigDecimal retail = new BigDecimal(partQueryResults.getInt(8));
				int onHand = partQueryResults.getInt(9);

				Part part = new Part(partId, partNumber, supplier, category, description, notes, cost, retail, onHand);
				partResults[i] = part;
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return partResults;
	}
	// END Part Methods
}
