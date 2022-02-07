package cis2901c.listeners;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import cis2901c.main.Main;
import cis2901c.objects.Customer;
import cis2901c.objects.DbObject;
import cis2901c.objects.Invoice;
import cis2901c.objects.MyInvoiceTableItem;
import cis2901c.objects.Part;
import cis2901c.objects.Unit;

public class DbServices {
	// TODO add status icon somewhere to show when we're connected to DB
		// set icon in initial connectToDb method call
		// maybe check isConnected before every getDbConnection call, and set icon then too
	
	private DbServices() {
	}
	
	private static Connection mainDbConnection = null;
	
	// START General DB methods
	public static boolean isConnected() {
		return mainDbConnection != null;
	}
	
	public static Connection getDbConnection() {
		return mainDbConnection;		
	}
	
	public static void connectToDb() {
		// TODO decide if I want to setAutoCommit to false before returning Connection, every call would have to connection.commit
			// or rollback if an exception is caught
		// TODO offer some customization options in application like actual DB options
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
	
//	public static void saveObject(Invoice cashieredInvoice) {
//		saveInvoice(cashieredInvoice);
//	}
//	
//	private static void saveInvoice(Invoice cashieredInvoice) {
//		// insert data into invoice table
//		StringBuilder queryString = new StringBuilder("INSERT INTO cis2901c.invoice () VALUES ();" );
//		
//		// insert individual part invoice line items into invoicepart table
//			// in invoicepart section, update onHand of part table
//	}
	
	public static void saveObject(DbObject dbObject) {
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
		
		if (dbObject instanceof Invoice) {		// TODO turn off auto commit until all invoice queries have completed
//			saveInvoiceParts(dbObject);
			// TODO get invoicenum, SELECT MAX(invoicenum) FROM cis2901c.invoice;
			int invoicenum = getLastInvoicenum();
			// insert individual part invoice line items into invoicepart table
				// in invoicepart section, update onHand of part table
			TableItem[] invoiceLineItems = ((Invoice) dbObject).getTableLineItems();
			for (TableItem invoiceLineItem : invoiceLineItems) {
				if (invoiceLineItem.getData() == null) {
					return;
				}
				MyInvoiceTableItem myInvoiceLineItem = (MyInvoiceTableItem) invoiceLineItem;
				myInvoiceLineItem.getDataMap().put("invoicenum", Integer.toString(invoicenum));
				saveObject(myInvoiceLineItem);
			}
		}
	}
	
	private static int getLastInvoicenum() {
		final int SQL_FAILURE = -1;
		ResultSet results = null;
		try (PreparedStatement lastInvoiceNumStatement = DbServices.getDbConnection().prepareStatement("SELECT MAX(invoicenum) FROM cis2901c.invoice;")) {
//			PreparedStatement lastInvoiceNumStatement = DbServices.getDbConnection().prepareStatement("SELECT MAX(invoicenum) FROM cis2901c.invoice;");
			results = lastInvoiceNumStatement.executeQuery();
			while (results.next()) {
				return results.getInt(1);	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SQL_FAILURE;
	}

	private static void sendQueryToDb(StringBuilder queryString, List<String> dbFields) {
//		Connection dbConnection = DbServices.getDbConnection();
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(queryString.toString())) {
//			PreparedStatement statement = dbConnection.prepareStatement(queryString.toString());
			int parameterIndex = 1;
			for(String fieldData : dbFields) {
				statement.setString(parameterIndex, fieldData);
				parameterIndex++;
			}
			statement.execute();
			Main.log(Level.INFO, "Update Count: " + statement.getUpdateCount());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Main.log(Level.SEVERE, "SQL Error: " + queryString);
			e.printStackTrace();
		}
	}
	
	private static List<String> insertFieldsIntoQuery(StringBuilder queryString, DbObject dbObject, boolean creatingNewObject) {
		List<String> results = new ArrayList<>();
//		boolean isAnythingModified = false;
		for (Map.Entry<String, String> entry : dbObject.getDataMap().entrySet()) {
			if (entry.getValue() != null) {
//				isAnythingModified = true;
				if (creatingNewObject) {
					insertQueryHelper(queryString, entry.getKey(), "?");
				} else {
					updateQueryHelper(queryString, entry.getKey(), "?");
				}
				results.add(entry.getValue().trim());		// TODO if we're always saving with .trim() there will be no spaces, trim() isn't necessary here
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
	
	private static void saveInvoiceParts(DbObject dbObject) {
		TableItem[] invoiceLineItems = ((Invoice) dbObject).getTableLineItems();
		StringBuilder queryString = new StringBuilder();
		for (TableItem invoiceLineItem : invoiceLineItems) {
			queryString.append("INSERT INTO cis2901c.invoicepart () VALUES ();" );
		}
	}
	
	private static String[] sanitizer(String searchQuery) {
		// simple regex to remove chars i don't want to search for
			// !!!! this is not to be taken as SQL Injection protection
		return searchQuery.replaceAll("[^a-zA-Z0-9 %'-]", "").split(" ");
	}
	
	@SuppressWarnings("unused")		// TODO see if we need this anywhere
	private static String numberSanitizer(String searchQuery) {		// originally used in searchForCustomer, refactoring may have made it unnecessary
		// simple regex to remove chars i don't want to search for
			// !!!! this is not to be taken as SQL Injection protection
		return searchQuery.replaceAll("[^0-9]", "");
	}	
	
	public static Object[] searchForObject(Table resultsTable, String searchQuery) {
		// public search interface
		
		StringBuilder query = new StringBuilder();
		StringBuilder outerQuery = new StringBuilder();
		int deleteStart = 0;
		int deleteEnd = 0;
		boolean isCustomerSearch = false;
		boolean isUnitSearch = false;
		boolean isPartSearch = false;
		boolean isInvoiceSearch = false;
// TODO break these up into searchFor[eachIndividualObject] so we only hafta condition check this once
		// each searchFor[eachIndividualObject] can then call buildSearchQuery method I'll refactor out 
		
			// search for searchQuery and display in resultsTable
			// TODO there might be a better way to check what type we're searching
		if (resultsTable.getColumn(0).getText().equals("First Name")) {
			isCustomerSearch = true;
			query.append("""
					SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, 
					email, customerId FROM cis2901c.customer WHERE firstName LIKE ? OR homePhone LIKE ? OR lastName LIKE ? 
					OR workPhone LIKE ? OR address LIKE ? OR cellPhone LIKE ? OR city LIKE ? OR zipcode LIKE ? OR state LIKE ?;""");
			outerQuery.append("""
					SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, email, 
					customerId FROM cis2901c.customer WHERE firstName LIKE ? OR homePhone LIKE ? OR lastName LIKE ? OR workPhone LIKE ? 
					OR address LIKE ? OR cellPhone LIKE ? OR city LIKE ? OR zipcode LIKE ? OR state LIKE ? AND customerId IN (""");
			deleteStart = 7;
			deleteEnd = 99;
		} else if (resultsTable.getColumn(0).getText().equals("Owner")) {
			isUnitSearch = true;
			query.append("""
					SELECT u.unitId, u.customerId, u.make, u.model, u.modelname, u.year, u.mileage, u.color, u.vin, u.notes, 
					c.lastName, c.firstName FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId 
					WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR u.vin LIKE ? OR u.make LIKE ? OR u.model LIKE ? OR u.year LIKE ?;""");
			outerQuery.append("""
					SELECT u.unitId, u.customerId, u.make, u.model, u.modelname, u.year, u.mileage, u.color, u.vin, u.notes, 
					c.lastName, c.firstName FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId 
					WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR u.vin LIKE ? OR u.make LIKE ? OR u.model LIKE ? OR u.year LIKE ? 
					AND unitId IN (""");
			deleteStart = 15;
			deleteEnd = 128;
		} else if (resultsTable.getColumn(0).getText().equals("Part Number")) {
			isPartSearch = true;
			query.append("""
				SELECT partId, partNumber, supplier, category, description, notes, cost, retail, onHand FROM cis2901c.part  
				WHERE partNumber LIKE ? OR supplier LIKE ? OR category LIKE ? OR description LIKE ?;""");
			outerQuery.append("""
					SELECT partId, partNumber, supplier, category, description, notes, cost, retail, onHand FROM cis2901c.part  
					WHERE partNumber LIKE ? OR supplier LIKE ? OR category LIKE ? OR description LIKE ? 
					AND partId IN (""");
			deleteStart = 13;
			deleteEnd = 87;
		} else if (resultsTable.getColumn(0).getText().equals("Invoice #")) {
			// TODO build invoice search results
			isInvoiceSearch = true;
			query.append("""
					SELECT i.invoicenum, i.customerid, c.lastname, c.firstname, c.address, c.city, c.state, c.zipcode, c.homephone, c.cellphone, c.email, 
					i.notes, i.tax, i.cashiereddate, i.cashiered, COUNT(ip.partid), SUM(ip.soldprice) + i.tax FROM 
					cis2901c.invoice AS i JOIN cis2901c.customer AS c ON i.customerid = c.customerid JOIN cis2901c.invoicepart as ip ON 
					i.invoicenum = ip.invoicenum WHERE c.lastname LIKE ? OR c.firstname LIKE ?;""");
			outerQuery.append("""
					SELECT i.invoicenum, i.customerid, c.lastname, c.firstname, c.address, c.city, c.state, c.zipcode, c.homephone, c.cellphone, c.email, 
					i.notes, i.tax, i.cashiereddate, i.cashiered, COUNT(ip.partid), SUM(ip.soldprice) + i.tax FROM 
					cis2901c.invoice AS i JOIN cis2901c.customer AS c ON i.customerid = c.customerid JOIN cis2901c.invoicepart as ip ON 
					i.invoicenum = ip.invoicenum WHERE c.lastname LIKE ? OR c.firstname LIKE ? AND i.invoicenum IN (;""");
			deleteStart = 19;
			deleteEnd = 218;
		}

		
// TODO refactor to buildSearchQuery method here 
		// build sub-queries if we have more than one search term
		String[] wordsFromQuery = sanitizer(searchQuery);
		for (int i = 1; i < wordsFromQuery.length; i++) {
			query.delete(deleteStart, deleteEnd);
			query.insert(0, outerQuery);
			query.replace(query.length() - 1, query.length(), ");");
		}
		
		// prep DB Statement, fill Statement Parameters
		final int MAX_RESULTS = 255;		// max search return results
		Object[] results = null;
		ResultSet queryResultSet = null;
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(query.toString())) {
//			Connection dbConnection = DbServices.getDbConnection();
//			PreparedStatement statement = dbConnection.prepareStatement(query.toString());
			statement.setMaxRows(MAX_RESULTS);
			ParameterMetaData parameterMetaData = statement.getParameterMetaData();
			int parameterCount = parameterMetaData.getParameterCount();
			int queryWord = wordsFromQuery.length - 1;
			for (int j = 1; j <= parameterCount; j++) {
				// fill search fields in queryString
				statement.setString(j, "%" + wordsFromQuery[queryWord] + "%");
				int parametersPerQuery = parameterCount / wordsFromQuery.length; 
				if (j % parametersPerQuery == 0) {
					queryWord--;
				}
			}
			queryResultSet = statement.executeQuery();
// TODO refactor to buildSearchQuery method here END
			
			
			// build objects from results and return them
			if (isCustomerSearch) {
				results = new Customer[MAX_RESULTS];
				int i = 0;
				while (queryResultSet.next() && i < MAX_RESULTS) {
					String firstName = queryResultSet.getString(1);
					String lastName = queryResultSet.getString(2);
					String address = queryResultSet.getString(3);
					String city = queryResultSet.getString(4);
					String state = queryResultSet.getString(5);			
					String zip = queryResultSet.getString(6);
					String homePhone = queryResultSet.getString(7);
					String workPhone = queryResultSet.getString(8);
					String cellPhone = queryResultSet.getString(9);
					String email = queryResultSet.getString(10);
					long customerId = queryResultSet.getLong(11);
					
					Customer customer = new Customer(customerId, firstName, lastName, address, city, state,
							zip, homePhone, workPhone, cellPhone, email);
					results[i] = customer;
					i++;
				}
			} else if (isUnitSearch) {
				results = new Unit[MAX_RESULTS];
				int i = 0;
				while (queryResultSet.next() && i < MAX_RESULTS) {
					long unitId = queryResultSet.getLong(1);
					long customerId = queryResultSet.getLong(2);
					String make = queryResultSet.getString(3);
					String model = queryResultSet.getString(4);
					String modelName = queryResultSet.getString(5);
					int year = queryResultSet.getInt(6);
					int mileage = queryResultSet.getInt(7);
					String color = queryResultSet.getString(8);
					String vin = queryResultSet.getString(9);
					String notes = queryResultSet.getString(10);
					String lastName = queryResultSet.getString(11);
					String firstName = queryResultSet.getString(12);
					String owner = lastName;
					if (firstName != null && firstName.length() != 0) {
						owner = lastName + ", " + firstName;
					}
					
					Unit unit = new Unit(unitId, customerId, make, model, modelName, year, mileage, color, vin, notes, owner);
					results[i] = unit;
					i++;
				}
			} else if (isPartSearch) {
				results = new Part[MAX_RESULTS];
				int i = 0;
				while (queryResultSet.next() && i < MAX_RESULTS) {
					int partId = queryResultSet.getInt(1);
					String partNumber = queryResultSet.getString(2);
					String supplier = queryResultSet.getString(3);
					String category = queryResultSet.getString(4);
					String description = queryResultSet.getString(5);
					String notes = queryResultSet.getString(6);
					BigDecimal cost = queryResultSet.getBigDecimal(7);
					BigDecimal retail = queryResultSet.getBigDecimal(8);
					int onHand = queryResultSet.getInt(9);

					Part part = new Part(partId, partNumber, supplier, category, description, notes, cost, retail, onHand);
					results[i] = part;
					i++;
				}
			} else if (isInvoiceSearch) {
				results = new Invoice[MAX_RESULTS];
				int i = 0;
				while (queryResultSet.next() && i < MAX_RESULTS) {
					int invoiceNum = queryResultSet.getInt(1);
					long customerId = queryResultSet.getLong(2);
					String lastname = queryResultSet.getString(3);
					String firstname = queryResultSet.getString(4);
					String customerData = queryResultSet.getString(5) + "\n" + queryResultSet.getString(6) + ", " +
							queryResultSet.getString(7) + " " + queryResultSet.getString(8) + "\n" + queryResultSet.getString(9) + "\n" +
								queryResultSet.getString(10) + "\n" + queryResultSet.getString(11);
					String notes = queryResultSet.getString(12);
					BigDecimal tax = queryResultSet.getBigDecimal(13);
					Timestamp cashieredDateTime = queryResultSet.getTimestamp(14);
					boolean cashiered = queryResultSet.getBoolean(15);
					int lineItemCount = queryResultSet.getInt(16);
					BigDecimal finalTotal = queryResultSet.getBigDecimal(17); 
					
					Invoice invoice = new Invoice(invoiceNum, customerId, lastname, firstname, customerData, notes, tax, cashieredDateTime, cashiered, new Part[lineItemCount]);
					// TODO populate invoice.parts[]
					results[i] = invoice;
					i++;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return results;
	}
}
