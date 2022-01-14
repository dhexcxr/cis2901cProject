package cis2901c;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

public class DbServices {
	
	private static Connection mainDbConnection = null;
	
	public static boolean isConnected() {
		return mainDbConnection != null;
	}
	
	public static Connection getDbConnection() {
		return mainDbConnection;		
	}
	
	public static void connectToDb() {
		// offer some custimization options like actual db options

		String url = "jdbc:mysql://localhost:3306/cis2901c";
		String user = "TestUser";
		String pass = "test";
		try {
			mainDbConnection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			
			System.out.println("Error connecting to database");
			e.printStackTrace();
		}
		
		// TODO add status icon somewhere to show when we're connected to db
	}
	
	public static void disconnectFromDb() {
		try {
			mainDbConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void saveObject(Customer customer) {
boolean isAnythingModified = false;
		
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
		
		System.out.println("Save New Cusrtoemr button pressed");
		// on "Save" button press in New Customer dialog
		// get data entered into dialog
		// sanitize, regex phones to 10 digit, check email format, require at least last name
		
		
		StringBuilder queryString = new StringBuilder("INSERT INTO cis2901c.customer () VALUES ();");
		// use regex to build query
		//	if (txtXXX.isModified() {
		//		take queryString, find first set of parens, insert Column Name
		//		find second set of parens, insert txtXXX.getText()
		
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
			
			StringBuilder firstNameField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
				firstNameField.append(", ");
			}
			firstNameField.append("'" + customer.getAddress().trim() + "'");
			queryString.insert(fieldInsertionPoint, firstNameField);
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
			
			StringBuilder firstNameField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 1) != '(') {
				firstNameField.append(", ");
			}
			firstNameField.append("'" + customer.getCity().trim() + "'");
			queryString.insert(fieldInsertionPoint, firstNameField);
		}
		
		if (isAnythingModified) {
			Connection dbConnection = DbServices.getDbConnection();
//			
//			statement.setString(1, txtFirstName.getText());
//			statement.setString(2, "%" + query + "%");
			try {
				PreparedStatement statement = dbConnection.prepareStatement(queryString.toString());
				statement.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			dbConnection.close();
		}
		
		// at the end, PreparedStatement statement = Main.getDbConnection().prepareStatement(queryString);
		// statement.execute();
	}
	
	
	// first service - getCustomerObject (need to set customerId when customer object is saved)
	// build Customer object from data in db return
	
	// second service - saveCustomerObject
			// need to set customerId when customer object is saved
			// we'll probably implement this as dbServices.getPrimaryKey
			// as a separate helper function
	
	// probably actually just overloaded saveObject classed 
}
