package cis2901c.listeners;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.swt.widgets.TableItem;

import cis2901c.main.Main;
import cis2901c.objects.Customer;
import cis2901c.objects.DbObjectSavable;
import cis2901c.objects.DbObjectSearchable;
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
		
		if (dbObject instanceof Invoice) {		// TODO turn off auto commit until all invoice queries have completed
			// TODO get invoicenum, SELECT MAX(invoicenum) FROM cis2901c.invoice;
			int invoiceNumber = getLastInvoicenum();
			// insert individual part invoice line items into invoicepart table
				// in invoicepart section, update onHand of part table
			TableItem[] invoiceLineItems = ((Invoice) dbObject).getTableLineItems();
			for (TableItem invoiceLineItem : invoiceLineItems) {
				if (invoiceLineItem.getData() == null) {
					break;
				}
				MyInvoiceTableItem myInvoiceLineItem = (MyInvoiceTableItem) invoiceLineItem;
				myInvoiceLineItem.getDataMap().put("invoicenum", Integer.toString(invoiceNumber));
				saveObject(myInvoiceLineItem);
			}
			sellInvoicePartsFromDb(invoiceNumber);
		}
	}
	
	private static void sellInvoicePartsFromDb(int invoiceNumber) {
		StringBuilder query = new StringBuilder("UPDATE part p JOIN invoicepart ip ON p.partid = ip.partid "
				+ "SET p.onhand = p.onhand - ip.quantity WHERE ip.invoicenum = ?;");
		sendQueryToDb(query, new ArrayList<>(Arrays.asList(Integer.toString(invoiceNumber))));
	}
	
	private static int getLastInvoicenum() {
		final int SQL_FAILURE = -1;
		ResultSet results = null;
		try (PreparedStatement lastInvoiceNumStatement = DbServices.getDbConnection().prepareStatement("SELECT MAX(invoicenum) FROM cis2901c.invoice;")) {
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
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(queryString.toString())) {
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
	
	private static List<String> insertFieldsIntoQuery(StringBuilder queryString, DbObjectSavable dbObject, boolean creatingNewObject) {
		List<String> results = new ArrayList<>();
		for (Map.Entry<String, String> entry : dbObject.getDataMap().entrySet()) {
			if (entry.getValue() != null) {
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
	
	public static String[] sanitizer(String searchQuery) {
		// simple regex to remove chars i don't want to search for
			// !!!! this is not to be taken as SQL Injection protection
		return searchQuery.replaceAll("[^a-zA-Z0-9 %'-]", "").split(" ");
	}
	
			// TODO see if we need this anywhere
	private static String numberSanitizer(String searchQuery) {		// originally used in searchForCustomer, refactoring may have made it unnecessary
		// simple regex to remove chars i don't want to search for
			// !!!! this is not to be taken as SQL Injection protection
		return searchQuery.replaceAll("[^0-9]", "");
	}
	
	public static Object[] searchForObject(DbObjectSearchable object) {
		final int MAX_RESULTS = 255;		// max search return results
		Object[] results = null;
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(object.getSearchQuery())) {
			statement.setMaxRows(MAX_RESULTS);
			fillStatementParameters(statement, object);
			ResultSet queryResultSet = statement.executeQuery();
			results = buildFoundObjects(queryResultSet, object, MAX_RESULTS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;		// TODO make sure everything that calls this handles the null return condition
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
		// TODO delegate this out to individual buildFoundCustomers/Units/Parts/Invoices etc methods
		Object[] results = null;
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
		} else if (object instanceof Unit) {
			results = new Unit[maxResults];
			int i = 0;
			while (queryResultSet.next()) {
				
				Unit unit = new Unit();
				unit.setUnitId(queryResultSet.getLong(1));
				unit.setCustomerId(queryResultSet.getLong(2));
				unit.setMake(queryResultSet.getString(3));
				unit.setModel(queryResultSet.getString(4));
				unit.setModelName(queryResultSet.getString(5));
				unit.setYear(queryResultSet.getInt(6));
				unit.setMileage(queryResultSet.getInt(7));
				unit.setColor(queryResultSet.getString(8));
				unit.setVin(queryResultSet.getString(9));
				unit.setNotes(queryResultSet.getString(10));
				
				String lastName = queryResultSet.getString(11);
				String firstName = queryResultSet.getString(12);
				String owner = lastName;
				if (firstName != null && firstName.length() != 0) {
					owner = lastName + ", " + firstName;
				}
				unit.setOwner(owner);
				
				results[i] = unit;
				i++;
			}
		} else if (object instanceof Invoice) {
			results = new Invoice[maxResults];
			int i = 0;
			while (queryResultSet.next()) {
				Invoice invoice = new Invoice();
				invoice.setInvoiceNum(queryResultSet.getInt(1));
				invoice.setCustomerId(queryResultSet.getLong(2));
				
				String lastname = queryResultSet.getString(3);
				String firstname = queryResultSet.getString(4);
				invoice.setCustomerName(lastname, firstname);
				
				// TODO see if we can use TextBlocks here
				String customerData = queryResultSet.getString(5) + "\n" + queryResultSet.getString(6) + ", " +
						queryResultSet.getString(7) + " " + queryResultSet.getString(8) + "\n" + queryResultSet.getString(9) + "\n" +
							queryResultSet.getString(10) + "\n" + queryResultSet.getString(11);
				invoice.setCustomerData(customerData);
				
				invoice.setNotes(queryResultSet.getString(12));
				invoice.setTax(queryResultSet.getBigDecimal(13));
				invoice.setCashiereDateTime(queryResultSet.getTimestamp(14));
				invoice.setCashiered(queryResultSet.getBoolean(15));
				// TODO populate invoice.parts[]
				int lineItemCount = queryResultSet.getInt(16);
				invoice.setParts(new Part[lineItemCount]);
				
				invoice.setTotal(queryResultSet.getBigDecimal(17)); 
				results[i] = invoice;
				i++;
			}
		}
		return results;
	}
}
