package cis2901c.listeners;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

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
			// saveUnit((Unit) object);
		} else if (object instanceof Part) {
			// savePart((Part) object);
		} else if (object instanceof RepairOrder) {
			// saveRepariOrder((RepairOrder) object);
		}
	}
	
	public static void searchForObject(Table resultsTable, String searchQuery) throws SQLException {
		// search for searchQueary and display in resultsTable 
		if (resultsTable.getColumn(0).getText().equals("First Name")) {
			searchForCustomer(resultsTable, searchQuery);
		} else if (resultsTable.getColumn(0).getText().equals("Owner")) {
			searchForUnit(resultsTable, searchQuery);
		}
	}
	// END General DB methods
	
	// START Unit object methods
	private static void searchForUnit(Table resultsTable, String searchQuery) throws SQLException {
		Connection dbConnection = DbServices.getDbConnection();
		PreparedStatement statement = dbConnection.prepareStatement(
				// change to unit query
				"SELECT u.unitId, u.customerId, u.make, u.model, u.year, u.mileage, u.color, u.vin, u.notes, c.lastName, c.firstName "
				+ "FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId " 
				+ "WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR u.vin LIKE ? OR u.make LIKE ? OR u.model LIKE ? OR u.year LIKE ?;");
		statement.setString(1, "%" + searchQuery + "%");
		statement.setString(2, "%" + searchQuery + "%");
		statement.setString(3, "%" + searchQuery + "%");
		statement.setString(4, "%" + searchQuery + "%");
		statement.setString(5, "%" + searchQuery + "%");
		statement.setString(6, "%" + searchQuery + "%");
		
		ResultSet unitQueryResults = statement.executeQuery();

		while (unitQueryResults.next()) {
			// TODO change to Unit fields
			long unitId = unitQueryResults.getLong(1);
			long customerId = unitQueryResults.getLong(2);
			String make = unitQueryResults.getString(3);
			String model = unitQueryResults.getString(4);
			int modelYear = unitQueryResults.getInt(5);
			int mileage = unitQueryResults.getInt(6);
			String color = unitQueryResults.getString(7);
			String vin = unitQueryResults.getString(8);
			String notes = unitQueryResults.getString(9);
			String lastName = unitQueryResults.getString(10);
			String firstName = unitQueryResults.getString(11);
		
			Unit unit = new Unit(unitId, customerId, make, model, modelYear, mileage, color, vin, notes);
				
			TableItem tableItem = new TableItem(resultsTable, SWT.NONE);
				// TODO when setData called, pull tableItem.txt from object instead of manually setting
					// we can add a method to Customer to array up fields
					// on second thought, this would probably require too much custom object code for each result table
					// may as well keep it with each object class, as in this code for displaying Customer objects
					// is in the Customer class
			tableItem.setText(new String[] {lastName + ", " + firstName, make, model, modelYear == 0 ? "" : Integer.toString(modelYear),
				Integer.toString(mileage), color, vin, notes});
			tableItem.setData(unit);
		}
	}
	
	
	// START Customer object methods
	private static void searchForCustomer(Table resultsTable, String searchQuery) throws SQLException {
		// TODO move search into DbServices
				// searchObject(query, table (maybe? we need some way to decide which table to search)) will find a customer in the customer table
				// it will return results which we'll use to populate table
//		Statement customerQuery = Main.getDbConnection().createStatement();
		
//		if (query.length() != 0) {
//			ResultSet customerQueryResults = customerQuery.executeQuery(
		Connection dbConnection = DbServices.getDbConnection();
			PreparedStatement statement = dbConnection.prepareStatement(
					"SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, "
							+ "email, customerId FROM cis2901c.customer WHERE firstName LIKE ? OR lastName LIKE ? OR homePhone LIKE ? OR workPhone LIKE ? OR cellPhone LIKE ?;");
			statement.setString(1, "%" + searchQuery + "%");
			statement.setString(2, "%" + searchQuery + "%");
			String phone = searchQuery.replaceAll("[()\\s-]+", "");
			statement.setString(3, "%" + phone + "%");
			statement.setString(4, "%" + phone + "%");
			statement.setString(5, "%" + phone + "%");
			ResultSet customerQueryResults = statement.executeQuery();
		
			// shows results in table even if search box is empty
//		if (query.length() == 0) {
//			customerQueryResults =  customerQuery.executeQuery(
//					"SELECT firstName, lastName, address, city, state, zipcode, "
//							+ "homePhone, workPhone, cellPhone, email FROM cis2901c.customer;");
//		}
		
		while (customerQueryResults.next()) {
			String firstName = customerQueryResults.getString(1);
			String lastName = customerQueryResults.getString(2);
			String address = customerQueryResults.getString(3);
			String city = customerQueryResults.getString(4);
			String state = customerQueryResults.getString(5);
//			String zip = Integer.toString(customerQueryResults.getInt(6));
//			String homePhone = Integer.toString(customerQueryResults.getInt(7));
//			String workPhone = Integer.toString(customerQueryResults.getInt(8)); // not using this right now
//			String cellPhone = Integer.toString(customerQueryResults.getInt(9));
			
			int zip = customerQueryResults.getInt(6);
			int homePhone = customerQueryResults.getInt(7);
			int workPhone = customerQueryResults.getInt(8); // not using this right now
			int cellPhone = customerQueryResults.getInt(9);
			
			String email = customerQueryResults.getString(10);
			long customerId = customerQueryResults.getLong(11);
			
			Customer customer = new Customer(customerId, firstName, lastName, address, city, state,
					zip, homePhone, workPhone, cellPhone, email);
					
			TableItem tableItem = new TableItem(resultsTable, SWT.NONE);
					// TODO when setData called, pull tableItem.txt from object instead of manually setting
						// we can add a method to Customer to array up fields
						// on second thought, this would probably require too much custom object code for each result table
						// may as well keep it with each object class, as in this code for displaying Customer objects
						// is in the Customer class
			tableItem.setText(new String[] {firstName, lastName, address, city, state, zip == 0 ? "" : Integer.toString(zip),
					homePhone == 0 ? "" : Integer.toString(homePhone), cellPhone == 0 ? "" : Integer.toString(cellPhone), email} );
			tableItem.setData(customer);
		}
	}
	
	private static StringBuilder buildModifyExistingCustomerQuery(Customer customer) {
		boolean isAnythingModified = false;
		long customerId = customer.getCustomerId();
		
		StringBuilder queryString = new StringBuilder("UPDATE cis2901c.customer SET WHERE customerId = " + customerId + ";");
		
		if (customer.getFirstName() != null && customer.getFirstName().trim().length() > 0) {
			isAnythingModified = true;
			StringBuilder firstName = new StringBuilder();
			int insertionPoint = queryString.lastIndexOf("WHERE");
			if (queryString.charAt(insertionPoint - 2) != 'T') {
				firstName.append(", ");
			}
			firstName.append("firstName = '" + customer.getFirstName().trim() + "'");
			queryString.insert(insertionPoint, firstName);
		}
		
		if (customer.getLastName() != null && customer.getLastName().trim().length() > 0) {
			isAnythingModified = true;
			StringBuilder lastName = new StringBuilder();
			int insertionPoint = queryString.lastIndexOf("WHERE");
			if (queryString.charAt(insertionPoint - 2) != 'T') {
				lastName.append(", ");
			}
			lastName.append("lastName = '" + customer.getLastName().trim() + "'");
			queryString.insert(insertionPoint, lastName);
		}
		
		if (customer.getAddress() != null && customer.getAddress().trim().length() > 0) {
			isAnythingModified = true;
			StringBuilder address = new StringBuilder();
			int insertionPoint = queryString.lastIndexOf("WHERE");
			if (queryString.charAt(insertionPoint - 2) != 'T') {
				address.append(", ");
			}
			address.append("address = '" + customer.getAddress().trim() + "'");
			queryString.insert(insertionPoint, address);
		}
		
		if (customer.getCity() != null && customer.getCity().trim().length() > 0) {
			isAnythingModified = true;
			StringBuilder city = new StringBuilder();
			int insertionPoint = queryString.lastIndexOf("WHERE");
			if (queryString.charAt(insertionPoint - 2) != 'T') {
				city.append(", ");
			}
			city.append("city = '" + customer.getCity().trim() + "'");
			queryString.insert(insertionPoint, city);
		}
		
		if (customer.getState() != null && customer.getState().trim().length() > 0) {
			isAnythingModified = true;
			StringBuilder state = new StringBuilder();
			int insertionPoint = queryString.lastIndexOf("WHERE");
			if (queryString.charAt(insertionPoint - 2) != 'T') {
				state.append(", ");
			}
			state.append("state = '" + customer.getState().trim() + "'");
			queryString.insert(insertionPoint, state);
		}
		
		if (customer.getZipCode() != 0) {
			isAnythingModified = true;
			StringBuilder zipCode = new StringBuilder();
			int insertionPoint = queryString.lastIndexOf("WHERE");
			if (queryString.charAt(insertionPoint - 2) != 'T') {
				zipCode.append(", ");
			}
			zipCode.append("zipCode = '" + customer.getZipCode() + "'");
			queryString.insert(insertionPoint, zipCode);
		}
		
		if (customer.getHomePhone() != 0) {
			isAnythingModified = true;
			StringBuilder homePhone = new StringBuilder();
			int insertionPoint = queryString.lastIndexOf("WHERE");
			if (queryString.charAt(insertionPoint - 2) != 'T') {
				homePhone.append(", ");
			}
			homePhone.append("homePhone = '" + customer.getHomePhone() + "'");
			queryString.insert(insertionPoint, homePhone);
		}
		
		if (customer.getWorkPhone() != 0) {
			isAnythingModified = true;
			StringBuilder workPhone = new StringBuilder();
			int insertionPoint = queryString.lastIndexOf("WHERE");
			if (queryString.charAt(insertionPoint - 2) != 'T') {
				workPhone.append(", ");
			}
			workPhone.append("workPhone = '" + customer.getWorkPhone() + "'");
			queryString.insert(insertionPoint, workPhone);
		}
		
		if (customer.getCellPhone() != 0) {
			isAnythingModified = true;
			StringBuilder cellPhone = new StringBuilder();
			int insertionPoint = queryString.lastIndexOf("WHERE");
			if (queryString.charAt(insertionPoint - 2) != 'T') {
				cellPhone.append(", ");
			}
			cellPhone.append("cellPhone = '" + customer.getCellPhone() + "'");
			queryString.insert(insertionPoint, cellPhone);
		}
		
		if (customer.getEmail() != null && customer.getEmail().trim().length() > 0) {
			isAnythingModified = true;
			StringBuilder email = new StringBuilder();
			int insertionPoint = queryString.lastIndexOf("WHERE");
			if (queryString.charAt(insertionPoint - 2) != 'T') {
				email.append(", ");
			}
			email.append("email = '" + customer.getEmail().trim() + "'");
			queryString.insert(insertionPoint, email);
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
			StringBuilder firstNameColumn  = new StringBuilder();
			int columnInsertionPoint = queryString.indexOf(")");
			if (queryString.charAt(columnInsertionPoint - 1) != '(') {
				firstNameColumn.append(", ");
			}
			firstNameColumn.append("firstName");
			queryString.insert(columnInsertionPoint, firstNameColumn);
			
			StringBuilder firstNameField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
				firstNameField.append(", ");
			}
			firstNameField.append("'" + customer.getFirstName().trim() + "'");
			queryString.insert(fieldInsertionPoint, firstNameField);
		}
		
		if (customer.getLastName() != null && customer.getLastName().trim().length() > 0) {
			isAnythingModified = true;
			StringBuilder lastNameColumn  = new StringBuilder();
			int columnInsertionPoint = queryString.indexOf(")");
			if (queryString.charAt(columnInsertionPoint - 1) != '(') {
				lastNameColumn.append(", ");
			}
			lastNameColumn.append("lastName");
			queryString.insert(columnInsertionPoint, lastNameColumn);
			
			StringBuilder lastNameField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
				lastNameField.append(", ");
			}
			lastNameField.append("'" + customer.getLastName().trim() + "'");
			queryString.insert(fieldInsertionPoint, lastNameField);
		}
		
		if (customer.getAddress() != null && customer.getAddress().trim().length() > 0) {
			isAnythingModified = true;
			StringBuilder addressColumn  = new StringBuilder();
			int columnInsertionPoint = queryString.indexOf(")");
			if (queryString.charAt(columnInsertionPoint - 1) != '(') {
				addressColumn.append(", ");
			}
			addressColumn.append("address");
			queryString.insert(columnInsertionPoint, addressColumn);
			
			StringBuilder addressField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
				addressField.append(", ");
			}
			addressField.append("'" + customer.getAddress().trim() + "'");
			queryString.insert(fieldInsertionPoint, addressField);
		}
		
		if (customer.getCity() != null && customer.getCity().trim().length() > 0) {
			isAnythingModified = true;
			StringBuilder cityColumn  = new StringBuilder();
			int columnInsertionPoint = queryString.indexOf(")");
			if (queryString.charAt(columnInsertionPoint - 1) != '(') {
				cityColumn.append(", ");
			}
			cityColumn.append("city");
			queryString.insert(columnInsertionPoint, cityColumn);
			
			StringBuilder cityField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
				cityField.append(", ");
			}
			cityField.append("'" + customer.getCity().trim() + "'");
			queryString.insert(fieldInsertionPoint, cityField);
		}
		
		if (customer.getState() != null && customer.getState().trim().length() > 0) {
			isAnythingModified = true;
			StringBuilder stateColumn  = new StringBuilder();
			int columnInsertionPoint = queryString.indexOf(")");
			if (queryString.charAt(columnInsertionPoint - 1) != '(') {
				stateColumn.append(", ");
			}
			stateColumn.append("state");
			queryString.insert(columnInsertionPoint, stateColumn);
			
			StringBuilder stateField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
				stateField.append(", ");
			}
			stateField.append("'" + customer.getState().trim() + "'");
			queryString.insert(fieldInsertionPoint, stateField);
		}
		
		if (customer.getZipCode() != 0) {
			isAnythingModified = true;
			StringBuilder zipCodeColumn  = new StringBuilder();
			int columnInsertionPoint = queryString.indexOf(")");
			if (queryString.charAt(columnInsertionPoint - 1) != '(') {
				zipCodeColumn.append(", ");
			}
			zipCodeColumn.append("zipcode");
			queryString.insert(columnInsertionPoint, zipCodeColumn);
			
			StringBuilder zipCodeField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
				zipCodeField.append(", ");
			}
			zipCodeField.append("'" + customer.getZipCode() + "'");
			queryString.insert(fieldInsertionPoint, zipCodeField);
		}
		
		if (customer.getHomePhone() != 0) {
			isAnythingModified = true;
			StringBuilder homePhoneColumn  = new StringBuilder();
			int columnInsertionPoint = queryString.indexOf(")");
			if (queryString.charAt(columnInsertionPoint - 1) != '(') {
				homePhoneColumn.append(", ");
			}
			homePhoneColumn.append("homephone");
			queryString.insert(columnInsertionPoint, homePhoneColumn);
			
			StringBuilder homePhoneField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
				homePhoneField.append(", ");
			}
			homePhoneField.append("'" + customer.getHomePhone() + "'");
			queryString.insert(fieldInsertionPoint, homePhoneField);
		}
		
		if (customer.getWorkPhone() != 0) {
			isAnythingModified = true;
			StringBuilder workPhoneColumn  = new StringBuilder();
			int columnInsertionPoint = queryString.indexOf(")");
			if (queryString.charAt(columnInsertionPoint - 1) != '(') {
				workPhoneColumn.append(", ");
			}
			workPhoneColumn.append("workphone");
			queryString.insert(columnInsertionPoint, workPhoneColumn);
			
			StringBuilder workPhoneField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
				workPhoneField.append(", ");
			}
			workPhoneField.append("'" + customer.getWorkPhone() + "'");
			queryString.insert(fieldInsertionPoint, workPhoneField);
		}
		
		if (customer.getCellPhone() != 0) {
			isAnythingModified = true;
			StringBuilder cellPhoneColumn  = new StringBuilder();
			int columnInsertionPoint = queryString.indexOf(")");
			if (queryString.charAt(columnInsertionPoint - 1) != '(') {
				cellPhoneColumn.append(", ");
			}
			cellPhoneColumn.append("cellphone");
			queryString.insert(columnInsertionPoint, cellPhoneColumn);
			
			StringBuilder cellPhoneField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
				cellPhoneField.append(", ");
			}
			cellPhoneField.append("'" + customer.getCellPhone() + "'");
			queryString.insert(fieldInsertionPoint, cellPhoneField);
		}
		
		if (customer.getEmail() != null && customer.getEmail().trim().length() > 0) {
			isAnythingModified = true;
			StringBuilder emailColumn  = new StringBuilder();
			int columnInsertionPoint = queryString.indexOf(")");
			if (queryString.charAt(columnInsertionPoint - 1) != '(') {
				emailColumn.append(", ");
			}
			emailColumn.append("email");
			queryString.insert(columnInsertionPoint, emailColumn);
			
			StringBuilder emailField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
				emailField.append(", ");
			}
			emailField.append("'" + customer.getEmail().trim() + "'");
			queryString.insert(fieldInsertionPoint, emailField);
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
