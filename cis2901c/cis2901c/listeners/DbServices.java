package cis2901c.listeners;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.eclipse.swt.widgets.Table;

import cis2901c.objects.Customer;
import cis2901c.objects.Part;
import cis2901c.objects.RepairOrder;
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
	
	public static void saveObject(Object object) {
		// public Save Object interface
		String queryString = null;
		if (object instanceof Customer) {
			queryString = saveCustomer((Customer) object);
		} else if (object instanceof Unit) {
			 queryString = saveUnit((Unit) object);
		} else if (object instanceof Part) {
			queryString = savePart((Part) object);
		} else if (object instanceof RepairOrder) {
			// saveRepariOrder((RepairOrder) object);
		}
		
		if (queryString != null) {
			Connection dbConnection = DbServices.getDbConnection();
			try {
				PreparedStatement statement = dbConnection.prepareStatement(queryString.toString());
				statement.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.print(queryString);
				e.printStackTrace();
			}
		}
	}
	
	public static Object[] searchForObject(Table resultsTable, String searchQuery) {
		// public search interface
			// search for searchQueary and display in resultsTable
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
		if (queryString.charAt(columnInsertionPoint - 1) == '\'') {
			column.append(", ");
		}
		column.append(columnName + " = '" + data + "'");
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
		field.append("'" + data + "'");
		queryString.insert(fieldInsertionPoint, field);
	}
	
	private static String[] sanitizer(String searchQuery) {
		// simple regex to remove chars i don't want to search for
			// !!!! this is not to be taken as SQL Injection proof
		return searchQuery.replaceAll("[^a-zA-Z0-9 %'-]", "").split(" ");
	}
	
	private static String numberSanitizer(String searchQuery) {
		// simple regex to remove chars i don't want to search for
			// !!!! this is not to be taken as SQL Injection proof
		return searchQuery.replaceAll("[^0-9]", "");
	}
	// END General DB methods
	
	// START Unit object methods
	private static Unit[] searchForUnit(Table resultsTable, String searchQuery) {
		final int MAX_RESULTS = 255;		// max search return results, could set limit at DB query level to lessen load on DB server

		// setup search
		String[] wordsFromQuery = sanitizer(searchQuery);	// remove most non-alphanumerics
//		if (wordsFromQuery.length == 0) {	// if there is no search term, return without searching DB
//			return null;
//		}

		Connection dbConnection = DbServices.getDbConnection();	// connect to DB
		// build query string
		StringBuilder subquery = new StringBuilder("SELECT u.unitId, u.customerId, u.make, u.model, u.modelname, u.year, u.mileage, u.color, u.vin, u.notes, "
				+ "c.lastName, c.firstName FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId " 
				+ "WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR u.vin LIKE ? OR u.make LIKE ? OR u.model LIKE ? OR u.year LIKE ?;");

		for (int i = 0; i < wordsFromQuery.length; i++) {
			if (i > 0) {
				// if we're looping through multiple search terms, modify query string to add multi-row  Subquery using WHERE ... IN
				subquery.delete(15, 128);
				subquery.insert(0, "SELECT u.unitId, u.customerId, u.make, u.model, u.year, u.mileage, u.color, u.vin, u.notes, "
						+ "c.lastName, c.firstName FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId " 
						+ "WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR u.vin LIKE ? OR u.make LIKE ? OR u.model LIKE ? OR u.year LIKE ? "
						+ "AND unitId IN (");
				subquery.replace(subquery.length() - 1, subquery.length(), ");");
			}

			for (int j = 1; j <= 6; j++) {
				// fill search fields in queryString
				int insertIndex = subquery.indexOf("?");
				subquery.replace(insertIndex, insertIndex + 1, "'%" + wordsFromQuery[i] + "%'");
			}
		}

		// build Unit[] to return DB search results
		Unit[] unitResults = new Unit[MAX_RESULTS];
		try {
			PreparedStatement statement = dbConnection.prepareStatement(subquery.toString());
			ResultSet unitQueryResults = statement.executeQuery();
			int i = 0;
			while (unitQueryResults.next() && i < MAX_RESULTS) {
				long unitId = unitQueryResults.getLong(1);
				long customerId = unitQueryResults.getLong(2);
				String make = unitQueryResults.getString(3);
				String model = unitQueryResults.getString(4);
				String modelName = unitQueryResults.getString(5);
				int modelYear = unitQueryResults.getInt(6);
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

				Unit unit = new Unit(unitId, customerId, make, model, modelName, modelYear, mileage, color, vin, notes, owner);
				unitResults[i] = unit;
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unitResults;
	}
	
	private static String saveUnit(Unit unit) {
		System.out.println("Save Unit button pressed");

		String queryString = null;
		if (unit.getUnitId() == -1) {		// add new customer
			queryString = buildAddNewUnitQuery(unit);
		} else {							// save modifications to existing customer
			queryString = buildModifyExistingUnitQuery(unit);
		}
		
		return queryString;

//		if (queryString != null) {
//			Connection dbConnection = DbServices.getDbConnection();
//			try {
//				PreparedStatement statement = dbConnection.prepareStatement(queryString.toString());
//				statement.execute();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				System.out.print(queryString);
//				e.printStackTrace();
//			}
//		}
	}
	
	private static String buildModifyExistingUnitQuery(Unit unit) {
		// save modifications to existing unit
		boolean isAnythingModified = false;
		
		// build query out of Unit unit
		StringBuilder queryString = new StringBuilder("UPDATE cis2901c.unit SET WHERE unitId = " + unit.getUnitId() + ";");
		
		if (unit.getCustomerId() != -1) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "customerId", Long.toString(unit.getCustomerId()));
		}
		
		if (unit.getMake() != null && unit.getMake().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "make", unit.getMake().trim());
		}
		
		if (unit.getModel() != null && unit.getModel().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "model", unit.getModel().trim());
		}
		
		if (unit.getModelName() != null && unit.getModelName().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "modelName", unit.getModelName().trim());
		}
		
		if (unit.getModelYear() != 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "year", Integer.toString(unit.getModelYear()));
		}
		
		if (unit.getMileage() != 0) {		// I think this is silly because mileage will always be 0 in a new object
			isAnythingModified = true;
			updateQueryHelper(queryString, "mileage", Integer.toString(unit.getMileage()));
		}
		
		if (unit.getColor() != null && unit.getColor().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "color", unit.getColor().trim());
		}
		
		if (unit.getVin() != null && unit.getVin().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "vin", unit.getVin().trim());
		}
		
		if (unit.getNotes() != null && unit.getNotes().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "notes", unit.getNotes().trim());
		}
		
		if (isAnythingModified == false) {
			return null;
		} else {
			return queryString.toString();
		}
	}

	private static String buildAddNewUnitQuery(Unit unit) {
		// add new unit
		boolean isAnythingModified = false;
		
		// build query out of Unit unit
		StringBuilder queryString = new StringBuilder("INSERT INTO cis2901c.unit () VALUES ();");
		if (unit.getCustomerId() != 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "customerId", Long.toString(unit.getCustomerId()));
		}
		
		if (unit.getMake() != null && unit.getMake().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "make", unit.getMake().trim());
		}
		
		if (unit.getModel() != null && unit.getModel().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "model", unit.getModel().trim());
		}
		
		if (unit.getModelName() != null && unit.getModelName().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "modelName", unit.getModelName().trim());
		}
		
		if (unit.getModelYear() != 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "year", Integer.toString(unit.getModelYear()));
		}
		
		if (unit.getMileage() != 0) {		// I think this is silly because mileage will always be 0 in a new object
			isAnythingModified = true;
			insertQueryHelper(queryString, "mileage", Integer.toString(unit.getMileage()));
		}
		
		if (unit.getColor() != null && unit.getColor().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "color", unit.getColor().trim());
		}
		
		if (unit.getVin() != null && unit.getVin().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "vin", unit.getVin().trim());
		}
		
		if (unit.getNotes() != null && unit.getNotes().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "notes", unit.getNotes().trim());
		}
		
		if (isAnythingModified == false) {
			return null;
		} else {
			return queryString.toString();
		}
	}
	// END Unit object methods

	// START Customer object methods
	private static Customer[] searchForCustomer(Table resultsTable, String searchQuery) {		
		final int MAX_RESULTS = 255;		// max search return results, could set limit at DB query level to lessen load on DB server
//		PreparedStatement statement = null;
		// setup search
		String[] wordsFromQuery = sanitizer(searchQuery);
//		if (wordsFromQuery.length == 0) {
//			return null;
//		}
		
		Connection dbConnection = DbServices.getDbConnection();
		// build query string
		StringBuilder subquery = new StringBuilder("SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, "
				+ "email, customerId FROM cis2901c.customer WHERE firstName LIKE ? OR homePhone LIKE ? OR lastName LIKE ? OR workPhone LIKE ? OR address LIKE ? OR cellPhone LIKE ? "
				+ "OR city LIKE ? OR zipcode LIKE ? OR state LIKE ?;");
		
		for (int i = 0; i < wordsFromQuery.length; i++) {
			if (i > 0) {
				// if we're looping through multiple search terms, modify query string to add multi-row  Subquery using WHERE ... IN
				subquery.delete(7, 99);
				subquery.insert(0,"SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, "
						+ "email, customerId FROM cis2901c.customer WHERE firstName LIKE ? OR homePhone LIKE ? OR lastName LIKE ? OR workPhone LIKE ? OR address LIKE ? OR cellPhone LIKE ? "
						+ "OR city LIKE ? OR zipcode LIKE ? OR state LIKE ? AND customerId IN (");
				subquery.replace(subquery.length() - 1, subquery.length(), ");");
			}
			
			int insertIndex = 0;
			for (int j = 1; j <= 4; j++) {
				// fill search fields in queryString
				String number = numberSanitizer(wordsFromQuery[i]);
				number = number.equals("") ? "-1" : number;
				insertIndex = subquery.indexOf("?");
				subquery.replace(insertIndex, insertIndex + 1, "'%" + wordsFromQuery[i] + "%'");
				insertIndex = subquery.indexOf("?");
				subquery.replace(insertIndex, insertIndex + 1, "'%" + number + "%'");
			}
			insertIndex = subquery.indexOf("?");
			subquery.replace(insertIndex, insertIndex + 1, "'%" + wordsFromQuery[i] + "%'");		// fill "state LIKE ?"
		}
		
		
		Customer[] customerResults = new Customer[MAX_RESULTS];
		try {
			PreparedStatement statement = dbConnection.prepareStatement(subquery.toString());
		ResultSet customerQueryResults = statement.executeQuery();
		int i = 0;
		while (customerQueryResults.next() && i < MAX_RESULTS) {
			String firstName = customerQueryResults.getString(1);
			String lastName = customerQueryResults.getString(2);
			String address = customerQueryResults.getString(3);
			String city = customerQueryResults.getString(4);
			String state = customerQueryResults.getString(5);			
			int zip = customerQueryResults.getInt(6);
			int homePhone = customerQueryResults.getInt(7);
			int workPhone = customerQueryResults.getInt(8);
			int cellPhone = customerQueryResults.getInt(9);
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
	
	private static String saveCustomer(Customer customer) {
		System.out.println("Save Customer button pressed");
		// TODO finalize sanitization, regex phones to 10 digit, check email format, require at least last name

		String queryString = null;
		if (customer.getCustomerId() == -1) {		// add new customer
			queryString = buildAddNewCustomerQuery(customer);
		} else {							// save modifications to existing customer
			queryString = buildModifyExistingCustomerQuery(customer);
		}
		
		return queryString;

//		if (queryString != null) {
//			Connection dbConnection = DbServices.getDbConnection();
//			try {
//				PreparedStatement statement = dbConnection.prepareStatement(queryString.toString());
//				statement.execute();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				System.out.print(queryString);
//				e.printStackTrace();
//			}
//		}
	}

	private static String buildModifyExistingCustomerQuery(Customer customer) {
		// save modifications to existing customer
		boolean isAnythingModified = false;
		
		// build query out of Customer customer
		StringBuilder queryString = new StringBuilder("UPDATE cis2901c.customer SET WHERE customerId = " + customer.getCustomerId() + ";");
		
		if (customer.getFirstName() != null && customer.getFirstName().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "firstName", customer.getFirstName().trim());
		}
		
		if (customer.getLastName() != null && customer.getLastName().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "lastName", customer.getLastName().trim());
		}
		
		if (customer.getAddress() != null && customer.getAddress().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "address", customer.getAddress().trim());
		}
		
		if (customer.getCity() != null && customer.getCity().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "city", customer.getCity().trim());
		}
		
		if (customer.getState() != null && customer.getState().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "state", customer.getState().trim());
		}
		
		if (customer.getZipCode() != 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "zipCode", Integer.toString(customer.getZipCode()));
		}
		
		if (customer.getHomePhone() != 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "homePhone", Integer.toString(customer.getHomePhone()));
		}
		
		if (customer.getWorkPhone() != 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "workPhone", Integer.toString(customer.getWorkPhone()));
		}
		
		if (customer.getCellPhone() != 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "cellPhone", Integer.toString(customer.getCellPhone()));
		}
		
		if (customer.getEmail() != null && customer.getEmail().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "email", customer.getEmail().trim());
		}
		
		if (isAnythingModified == false) {
			return null;
		} else {
			return queryString.toString();
		}
	}
	
	private static String buildAddNewCustomerQuery(Customer customer) {
		// add new customer
		boolean isAnythingModified = false;
		
		// build query out of Customer customer
		StringBuilder queryString = new StringBuilder("INSERT INTO cis2901c.customer () VALUES ();");		
		if (customer.getFirstName() != null && customer.getFirstName().trim().length() > 0) {		// TODO add && clause to all these
			isAnythingModified = true;
			insertQueryHelper(queryString, "firstName", customer.getFirstName().trim());
		}
		
		if (customer.getLastName() != null && customer.getLastName().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "lastName", customer.getLastName().trim());
		}
		
		if (customer.getAddress() != null && customer.getAddress().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "address", customer.getAddress().trim());
		}
		
		if (customer.getCity() != null && customer.getCity().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "city", customer.getCity().trim());
		}
		
		if (customer.getState() != null && customer.getState().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "state", customer.getState().trim());
		}
		
		if (customer.getZipCode() != 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "zipCode", Integer.toString(customer.getZipCode()));
		}
		
		if (customer.getHomePhone() != 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "homePhone", Integer.toString(customer.getHomePhone()));
		}
		
		if (customer.getWorkPhone() != 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "workPhone", Integer.toString(customer.getWorkPhone()));
		}
		
		if (customer.getCellPhone() != 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "cellPhone", Integer.toString(customer.getCellPhone()));
		}
		
		if (customer.getEmail() != null && customer.getEmail().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "email", customer.getEmail().trim());
		}
		
		if (isAnythingModified == false) {
			return null;
		} else {
			return queryString.toString();
		}
	}
	// END Customer methods
	
	// Part methods
	private static Part[] searchForPart(Table resultsTable, String searchQuery) {
		final int MAX_RESULTS = 255;		// max search return results, could set limit at DB query level to lessen load on DB server

		// setup search
		String[] wordsFromQuery = sanitizer(searchQuery);	// remove most non-alphanumerics

		Connection dbConnection = DbServices.getDbConnection();	// connect to DB
		// build query string
		StringBuilder subquery = new StringBuilder("SELECT partId, partNumber, supplier, category, description, notes, cost, retail, onHand FROM cis2901c.part " 
				+ "WHERE partNumber LIKE ? OR supplier LIKE ? OR category LIKE ? OR description LIKE ?;");

		for (int i = 0; i < wordsFromQuery.length; i++) {
			if (i > 0) {
				// if we're looping through multiple search terms, modify query string to add multi-row  Subquery using WHERE ... IN
				// TODO set correct queries
				subquery.delete(27, 79);
				subquery.insert(0, "SELECT partId, partNumber, supplier, category, description, notes, cost, retail, onHand FROM cis2901c.part " 
						+ "WHERE partNumber LIKE ? OR supplier LIKE ? OR category LIKE ? OR description LIKE ? "
						+ "AND (partNumber, supplier) IN (");
				subquery.replace(subquery.length() - 1, subquery.length(), ");");
			}

			for (int j = 1; j <= 4; j++) {
				// fill search fields in queryString
				int insertIndex = subquery.indexOf("?");
				subquery.replace(insertIndex, insertIndex + 1, "'%" + wordsFromQuery[i] + "%'");
			}
		}

		// build Unit[] to return DB search results
		Part[] partResults = new Part[MAX_RESULTS];
		try {
			PreparedStatement statement = dbConnection.prepareStatement(subquery.toString());
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

				Part part = new Part(partId, partNumber, supplier, category, description, notes, cost, retail, onHand, false);
				partResults[i] = part;
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return partResults;
	}
	
	private static String savePart(Part part) {
		System.out.println("Save Part button pressed");

		String queryString = null;
		if (part.getPartId() == -1) {		// save a new Part
			queryString = buildAddNewPartQuery(part);
		} else {							// save modifications to existing Part
			queryString = buildModifyExistingPartQuery(part);
		}
		
		return queryString;

//		if (queryString != null) {
//			Connection dbConnection = DbServices.getDbConnection();
//			try {
//				PreparedStatement statement = dbConnection.prepareStatement(queryString.toString());
//				statement.execute();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				System.out.print(queryString);
//				e.printStackTrace();
//			}
//		}
	}
	
	private static String buildModifyExistingPartQuery(Part part) {
		// save modifications to existing part 
		boolean isAnythingModified = false;
		
		// build query out of Customer customer
		StringBuilder queryString = new StringBuilder("UPDATE cis2901c.part SET WHERE partId = " + part.getPartId() + ";");		
				
		if (part.getPartNumber() != null && part.getPartNumber().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "partNumber", part.getPartNumber().trim());
		}
		
		if (part.getSupplier() != null && part.getPartNumber().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "supplier", part.getSupplier().trim());
		}
		
		if (part.getCategory() != null && part.getCategory().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "category", part.getCategory().trim());
		}
		
		if (part.getDescription() != null && part.getDescription().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "description", part.getDescription().trim());
		}
		
		if (part.getNotes() != null && part.getNotes().trim().length() > 0) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "notes", part.getNotes().trim());
		}
		
		if (!part.getRetail().equals(new BigDecimal(0))) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "retail", part.getRetail().toString());
		}
		
		if (!part.getCost().equals(new BigDecimal(0))) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "cost", part.getCost().toString());
		}
		
		if (part.getOnHand() != -1) {
			isAnythingModified = true;
			updateQueryHelper(queryString, "onHand", Integer.toString(part.getOnHand()));
		}
		
		if (isAnythingModified == false) {
			return null;
		} else {
			return queryString.toString();
		}
	}
	
	private static String buildAddNewPartQuery(Part part) {
		// add new part
		boolean isAnythingModified = false;
		
		// build query out of Customer customer
		StringBuilder queryString = new StringBuilder("INSERT INTO cis2901c.part () VALUES ();");		
		if (part.getPartNumber() != null && part.getPartNumber().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "partNumber", part.getPartNumber());
		}
		
		if (part.getSupplier() != null && part.getSupplier().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "supplier", part.getSupplier().trim());
		}
		
		if (part.getCategory() != null && part.getCategory().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "category", part.getCategory().trim());
		}
		
		if (part.getDescription() != null && part.getDescription().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "description", part.getDescription().trim());
		}
		
		if (part.getNotes() != null && part.getNotes().trim().length() > 0) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "notes", part.getNotes().trim());
		}
		
		if (!part.getRetail().equals(new BigDecimal(0))) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "retail", part.getRetail().toString());
		}
		
		if (!part.getCost().equals(new BigDecimal(0))) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "cost", part.getCost().toString());
		}
		
		if (part.getOnHand() != -1) {
			isAnythingModified = true;
			insertQueryHelper(queryString, "onHand", Integer.toString(part.getOnHand()));
		}
		
		if (isAnythingModified == false) {
			return null;
		} else {
			return queryString.toString();
		}
	}
	
	
	// END Part Methods
}
