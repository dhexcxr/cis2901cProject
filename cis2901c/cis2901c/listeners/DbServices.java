package cis2901c.listeners;

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
	
	private static Connection mainDbConnection = null;
	
	// START General DB methods
	public static boolean isConnected() {
		return mainDbConnection != null;
	}
	
	public static Connection getDbConnection() {
		return mainDbConnection;		
	}
	
	public static void connectToDb() {
		// offer some customization options like actual DB options

		String url = "jdbc:mysql://localhost:3306/cis2901c";
		String user = "TestUser";
		String pass = "test";
		try {
			mainDbConnection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			
			System.out.println("Error connecting to database");
			e.printStackTrace();
		}
		
		// TODO add status icon somewhere to show when we're connected to DB
	}
	
	public static void disconnectFromDb() {
		try {
			mainDbConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveObject(Object object) {
		if (object instanceof Customer) {
			saveCustomer((Customer) object);
		} else if (object instanceof Unit) {
			 saveUnit((Unit) object);
		} else if (object instanceof Part) {
			// savePart((Part) object);
		} else if (object instanceof RepairOrder) {
			// saveRepariOrder((RepairOrder) object);
		}
	}
	
	public static Object[] searchForObject(Table resultsTable, String searchQuery) throws SQLException {
		// search for searchQueary and display in resultsTable 
		if (resultsTable.getColumn(0).getText().equals("First Name")) {
			return searchForCustomer(resultsTable, searchQuery);
		} else if (resultsTable.getColumn(0).getText().equals("Owner")) {
			return searchForUnit(resultsTable, searchQuery);
		}
		return null;
	}
	
	private static void updateQueryHelper (StringBuilder queryString, String columnName, String data) {
		StringBuilder column = new StringBuilder();
		int columnInsertionPoint = queryString.lastIndexOf("WHERE");
//		if (queryString.charAt(columnInsertionPoint - 2) != 'T' && queryString.charAt(columnInsertionPoint - 1) == '\'') {
		if (queryString.charAt(columnInsertionPoint - 1) == '\'') {
			column.append(", ");
		}
		column.append(columnName + " = '" + data + "'");
		queryString.insert(columnInsertionPoint, column);		
	}
	
	private static void insertQueryHelper (StringBuilder queryString, String columnName, String data) {
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
		return searchQuery.replaceAll("[^a-zA_Z0-9%'-]", "").split(" ");		// TODO ensure we strip this out of name entry on Save Customer
	}
	
	private static String numberSanitizer(String searchQuery) {
		// TODO change to phone
		return searchQuery.replaceAll("[^0-9]", "");
//		return null;
	}
	
	// END General DB methods
	
	// START Unit object methods
	private static Unit[] searchForUnit(Table resultsTable, String searchQuery) throws SQLException {
		int MAX_RESULTS = 255;
		Connection dbConnection = DbServices.getDbConnection();
		PreparedStatement statement = null;
			String[] wordsFromQuery = sanitizer(searchQuery);
			if (wordsFromQuery.length == 0) {
				return null;
			}
			
			StringBuilder subquery = new StringBuilder("SELECT u.unitId, u.customerId, u.make, u.model, u.modelname, u.year, u.mileage, u.color, u.vin, u.notes, "
					+ "c.lastName, c.firstName FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId " 
					+ "WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR u.vin LIKE ? OR u.make LIKE ? OR u.model LIKE ? OR u.year LIKE ?;");
			
//			for (int i = 1; i <= 6; i++) {
//				// edit string with StringBuilder replace insert
//				int insertIndex = subquery.indexOf("?");
//				subquery.replace(insertIndex, insertIndex + 1, "'%" + wordsFromQuery[0] + "%'");
//			}
			
			for (int i = 0; i < wordsFromQuery.length; i++) {
				if (i > 0) {
					subquery.delete(15, 128);
					subquery.insert(0, "SELECT u.unitId, u.customerId, u.make, u.model, u.year, u.mileage, u.color, u.vin, u.notes, "
							+ "c.lastName, c.firstName FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId " 
							+ "WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR u.vin LIKE ? OR u.make LIKE ? OR u.model LIKE ? OR u.year LIKE ? "
							+ "AND unitId IN (");
					subquery.replace(subquery.length() - 1, subquery.length(), ");");
				}
								
				for (int j = 1; j <= 6; j++) {
					// edit string with StringBuilder replace insert
					int insertIndex = subquery.indexOf("?");
					subquery.replace(insertIndex, insertIndex + 1, "'%" + wordsFromQuery[i] + "%'");
//					statement.setString(i, "%" + wordsFromQuery[0] + "%");
				}
			}
			
			
			
			// finished statement
			statement = dbConnection.prepareStatement(subquery.toString());
//		} else {
//			statement = dbConnection.prepareStatement(
//					"SELECT u.unitId, u.customerId, u.make, u.model, u.year, u.mileage, u.color, u.vin, u.notes, c.lastName, c.firstName "
//					+ "FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId " 
//					+ "WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR u.vin LIKE ? OR u.make LIKE ? OR u.model LIKE ? OR u.year LIKE ?;");
//			for (int i = 1; i <= 6; i++) {
//				statement.setString(i, "%" + searchQuery + "%");
//			}
//		}
		
		
//		statement.setString(1, "%" + searchQuery + "%");
//		statement.setString(2, "%" + searchQuery + "%");
//		statement.setString(3, "%" + searchQuery + "%");
//		statement.setString(4, "%" + searchQuery + "%");
//		statement.setString(5, "%" + searchQuery + "%");
//		statement.setString(6, "%" + searchQuery + "%");
		
		ResultSet unitQueryResults = statement.executeQuery();

		Unit[] unitResults = new Unit[MAX_RESULTS];
		int i = 0;
		while (unitQueryResults.next() && i < MAX_RESULTS) {
			// TODO change to Unit fields
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
//			String lastName = unitQueryResults.getString(11);
			String owner = unitQueryResults.getString(11);
			String firstName = unitQueryResults.getString(12);
			
			if (firstName != null && firstName.length() != 0) {
				owner = owner + ", " + firstName;
			}
		
			// "" stub for modelName, we need to add modelName to this query
			Unit unit = new Unit(unitId, customerId, make, model, modelName, modelYear, mileage, color, vin, notes, owner);
			
			unitResults[i] = unit;
			i++;
//			TableItem tableItem = new TableItem(resultsTable, SWT.NONE);
//				// TODO when setData called, pull tableItem.txt from object instead of manually setting
//					// we can add a method to Customer to array up fields
//					// on second thought, this would probably require too much custom object code for each result table
//					// may as well keep it with each object class, as in this code for displaying Customer objects
//					// is in the Customer class
//			tableItem.setText(new String[] {lastName + ", " + firstName, make, model, modelYear == 0 ? "" : Integer.toString(modelYear),
//				Integer.toString(mileage), color, vin, notes});
//			tableItem.setData(unit);
		}
		return unitResults;
	}
	
	public static void saveUnit(Unit unit) {
		System.out.println("Save Unit button pressed");

		StringBuilder queryString = null;

		if (unit.getUnitId() == -1) {		// add new customer
			queryString = buildAddNewUnitQuery(unit);
		} else {							// save modifications to existing customer
			queryString = buildModifyExistingUnitQuery(unit);
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
	
	private static StringBuilder buildModifyExistingUnitQuery(Unit unit) {
		boolean isAnythingModified = false;
		
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
			return queryString;
		}
	}

	private static StringBuilder buildAddNewUnitQuery(Unit unit) {
		boolean isAnythingModified = false;
		
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
			return queryString;
		}
	}
	// END Unit object methods

	// START Customer object methods
	private static Customer[] searchForCustomer(Table resultsTable, String searchQuery) throws SQLException {
		
		// TODO refactor this to combine the duplicate for() loops that do the exact same thing 
		
		int MAX_RESULTS = 255;
		Connection dbConnection = DbServices.getDbConnection();
		PreparedStatement statement = null;
		String[] wordsFromQuery = sanitizer(searchQuery);
		if (wordsFromQuery.length == 0) {
			return null;
		}
		String phone = numberSanitizer(searchQuery);
		StringBuilder subquery = new StringBuilder("SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, "
				+ "email, customerId FROM cis2901c.customer WHERE firstName LIKE ? OR homePhone LIKE ? OR lastName LIKE ? OR workPhone LIKE ? OR address LIKE ? OR cellPhone LIKE ? "
				+ "OR city LIKE ? OR zipcode LIKE ? OR state LIKE ?;");
		
		for (int i = 0; i < wordsFromQuery.length; i++) {
			if (i > 0) {
				subquery.delete(7, 99);
				subquery.insert(0,"SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, "
						+ "email, customerId FROM cis2901c.customer WHERE firstName LIKE ? OR homePhone LIKE ? OR lastName LIKE ? OR workPhone LIKE ? OR address LIKE ? OR cellPhone LIKE ? "
						+ "OR city LIKE ? OR zipcode LIKE ? OR state LIKE ? AND customerId IN (");
				subquery.replace(subquery.length() - 1, subquery.length(), ");");
			}
			
			int insertIndex = 0;
			for (int j = 1; j <= 4; j++) {
				insertIndex = subquery.indexOf("?");
				subquery.replace(insertIndex, insertIndex + 1, "'%" + wordsFromQuery[i] + "%'");
				insertIndex = subquery.indexOf("?");
				subquery.replace(insertIndex, insertIndex + 1, "'%" + phone + "%'");
			}
			insertIndex = subquery.indexOf("?");
			subquery.replace(insertIndex, insertIndex + 1, "'%" + wordsFromQuery[i] + "%'");		// fill "state LIKE ?"
		}
		
		statement = dbConnection.prepareStatement(subquery.toString());
		ResultSet customerQueryResults = statement.executeQuery();
		
//		List<Customer> customerResults = new ArrayList<>();
		Customer[] customerResults = new Customer[MAX_RESULTS];
		int i = 0;
		while (customerQueryResults.next() && i < MAX_RESULTS) {
			String firstName = customerQueryResults.getString(1);
			String lastName = customerQueryResults.getString(2);
			String address = customerQueryResults.getString(3);
			String city = customerQueryResults.getString(4);
			String state = customerQueryResults.getString(5);			
			int zip = customerQueryResults.getInt(6);
			int homePhone = customerQueryResults.getInt(7);
			int workPhone = customerQueryResults.getInt(8); // not using this right now
			int cellPhone = customerQueryResults.getInt(9);
			
			String email = customerQueryResults.getString(10);
			long customerId = customerQueryResults.getLong(11);
			
			Customer customer = new Customer(customerId, firstName, lastName, address, city, state,
					zip, homePhone, workPhone, cellPhone, email);
			
			customerResults[i] = customer;
			i++;
		}
		return customerResults;
	}
	
	private static StringBuilder buildModifyExistingCustomerQuery(Customer customer) {
		boolean isAnythingModified = false;
//		long customerId = customer.getCustomerId();
		
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
		
		// TODO finish modifying fields
		
		if (isAnythingModified == false) {
			return null;
		} else {
			return queryString;
		}
	}
	
	private static StringBuilder buildAddNewCustomerQuery(Customer customer) {
		boolean isAnythingModified = false;
		
		StringBuilder queryString = new StringBuilder("INSERT INTO cis2901c.customer () VALUES ();");
		// use regex to build query
		//	if (txtXXX.isModified() {
		//		take queryString, find first set of parens, insert Column Name
		//		find second set of parens, insert txtXXX.getText()
		
		//		!!!!---- pull these things out into a generic buildQuery method ----!!!!
			// StringBuilder buildQuery(StringBuilder queryString, StringBuilder column, StringBuilder fieldData)
			// queryString = buildQuery(queryString, "firstName", customer.getFirstName().trim());
		// this might allow me to buildAddNewQuery and buildModifyExistingQuery
		
		
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
			return queryString;
		}
	}
	
	public static void saveCustomer(Customer customer) {		// TODO change to Object object, we'll need object.getObjectId or something
//boolean isAnythingModified = false;
		
		// this will probably be mostly moved into dbServices.SaveCustomer
		// this dialog will only build a Customer object (only programmatic functions)
		//then pass the object to the dbServies class to save user input (through the Customer object) to db
			// this will allow us to have one overloaded dbServies.saveObject method that will do all the data storage/db operations
			// this allows re-usability and plug-ability
		
//		if (!txtLastName.isModified()) {
//		if (customer.getLastName().equals("Last Name/Company Name...")) {
//			// dialog box stating last name is required
//			MessageBox lastNameRequirementBox = new MessageBox(Main.getShell(), SWT.ICON_INFORMATION);
//			lastNameRequirementBox.setText("Notice");
//			lastNameRequirementBox.setMessage("Please enter a Last Name or Company Name");
//			lastNameRequirementBox.open();
//			return;
//		}
		
		// TODO finish
		
		System.out.println("Save Customer button pressed");
		// on "Save" button press in New Customer dialog
			// get data entered into dialog
			// sanitize, regex phones to 10 digit, check email format, require at least last name
			
		StringBuilder queryString = null;
		
		if (customer.getCustomerId() == -1) {		// add new customer
			queryString = buildAddNewCustomerQuery(customer);
		} else {							// save modifications to existing customer
			queryString = buildModifyExistingCustomerQuery(customer);
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
	
	
	// first service - getCustomerObject (need to set customerId when customer object is saved)
	// build Customer object from data in db return
	
	// second service - saveCustomerObject
			// need to set customerId when customer object is saved
			// we'll probably implement this as dbServices.getPrimaryKey
			// as a separate helper function
	
	// probably actually just overloaded saveObject classed 
}
