package cis2901c.listeners;

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
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import cis2901c.main.Main;
import cis2901c.objects.Customer;
import cis2901c.objects.DbObjectSavable;
import cis2901c.objects.DbObjectSearchable;
import cis2901c.objects.Invoice;
import cis2901c.objects.InvoicePart;
import cis2901c.objects.Job;
import cis2901c.objects.JobLabor;
import cis2901c.objects.JobPart;
import cis2901c.objects.Part;
import cis2901c.objects.RepairOrder;
import cis2901c.objects.Unit;

public class DbServices {
	// TODO add status icon somewhere to show when we're connected to DB
		// set icon in initial connectToDb method call
		// maybe check isConnected before every getDbConnection call, and set icon then too
	
	private static Connection mainDbConnection = null;
	private static final int SQL_FAILURE = -1;
	private static final int MAX_RESULTS = 255;		// max search return results
	
	private DbServices() {
	}
	
	// START General DB methods
	public static boolean isConnected() {
		boolean isConnected = false;
		try {
			isConnected = mainDbConnection.isValid(5);
		} catch (SQLException e) {
			Main.getLogger().log(Level.FINER, "DbServices.isConnected: mainDbConnection SQL Exception");
			e.printStackTrace();
		} catch (NullPointerException e) {
			Main.getLogger().log(Level.SEVERE, "DbServices.isConnected: mainDbConnection Null Pointer Exception");
			e.printStackTrace();
		}
		return isConnected;
	}
	
	public static Connection getDbConnection() {
		return mainDbConnection;		
	}
	
	public static void connectToDb() {
		// TODO decide if I want to setAutoCommit to false before returning Connection, every call would have to connection.commit
			// or rollback if an exception is caught
		// TODO offer some customization options in application like actual DB options
		Main.getLogger().log(Level.INFO, "Connecting to Database");
		String url = Main.getSettings().getDatabase();
		String user = "TestUser";
		String pass = "test";
		
		long splashTime = System.currentTimeMillis();
		Shell connectingToDb = new Shell(Main.display(), SWT.APPLICATION_MODAL | SWT.TOOL | SWT.NO_FOCUS);
		connectingToDb.setSize(980, 640);
		connectingToDb.setBackgroundImage(new Image(Main.display(), DbServices.class.getClassLoader().getResourceAsStream("resources/splash.jpg")));
		connectingToDb.setText("Connecting to database...");
		connectingToDb.open();
		
		int i = 0;
		while (!isConnected() && i < 3) {
			try {
				mainDbConnection = DriverManager.getConnection(url, user, pass);
			} catch (SQLException e) {
				Main.getLogger().log(Level.SEVERE, "Error connecting to database");
				e.printStackTrace();
			}
			i++;
		}
		if (!isConnected()) {
			Main.getLogger().log(Level.SEVERE, "DB Connection error dialog box");
			MessageBox dbConnectionError = new MessageBox(new Shell(), SWT.ERROR);
			dbConnectionError.setText("DB Error");
			dbConnectionError.setMessage("Unable to connect to database...");
			dbConnectionError.open();
		}
		// TODO enable splash delay in release 
//		while (System.currentTimeMillis() - splashTime < 2000) {};		// leave splash screen open for at least 2 seconds, but no more than 2 seconds
		connectingToDb.close();
	}
	
	public static void disconnectFromDb() {
		// clean up, close connection
		try {
			mainDbConnection.close();
		} catch (SQLException e) {
			Main.getLogger().log(Level.FINER, "DbServices.disconnectFromDb: mainDbConnection SQL Exception");
			e.printStackTrace();
		} catch (NullPointerException e) {
			Main.getLogger().log(Level.SEVERE, "DbServices.disconnectFromDb: mainDbConnection Null Pointer Exception");
			e.printStackTrace();
		}
	}
	
	public static int deleteObject(DbObjectSavable dbObject) {
		if (!isConnected()) {
			connectToDb();
		}
		if (dbObject == null) {
			return 0;		// no object passed in, so no object deleted
		}
		StringBuilder queryString = new StringBuilder();		// TODO this query (below) is potentially dangerous, i'm not sanitizing getDbPk() with PrepareStatement
		queryString.append("DELETE FROM cis2901c." + dbObject.getTableName() + " WHERE " + dbObject.getPkName() + " = " + dbObject.getDbPk());
		return sendQueryToDb(queryString);
	}
	
	public static void saveObject(DbObjectSavable dbObject) {
		// TODO have saveObject also return updateCount from sendQueryToDb
		if (!isConnected()) {
			connectToDb();
		}

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
		
		if (creatingNewObject) {
			// get latest dbObject.Table.dbPk, set dbObject.setPk
			dbObject.setDbPk(getLastSavedDbObject(dbObject));
		}
		
		if (dbObject instanceof Invoice) {		// TODO turn off auto commit until all invoice queries have completed
			saveInvoiceLineItems(dbObject);
		} else if (dbObject instanceof RepairOrder) {
			if (dbObject.getDbPk() == -1 ) {
				dbObject.setDbPk(getLastRoNum());
			}
			saveRoJobs(dbObject);
			RepairOrder thisRo = (RepairOrder) dbObject;
			if (thisRo.getClosedDate() != null) {
				sellRoPartsOutOfInventory(thisRo.getRepairOrderId());
			}
		} else if (dbObject instanceof Job) {
			if (dbObject.getDbPk() == -1 ) {
				dbObject.setDbPk(getLastJobId());
			}
			saveJobParts(dbObject);
			saveJobLabor(dbObject);
		}
	}
	
	private static long getLastSavedDbObject(DbObjectSavable dbObject) {
		ResultSet results = null;
		try (PreparedStatement lastInvoiceNumStatement = DbServices.getDbConnection().prepareStatement(
									"SELECT MAX(" + dbObject.getPkName() + ") FROM cis2901c." + dbObject.getTableName() + ";")) {
			results = lastInvoiceNumStatement.executeQuery();
			while (results.next()) {
				return results.getLong(1);		// we should always return here
			}
		} catch (SQLException e) {
			Main.getLogger().log(Level.SEVERE, "SQL Error: getLastSavedDbObject");
			e.printStackTrace();
		}
		// if we get here something went wrong
		return SQL_FAILURE;
	}
	
	private static void saveRoJobs(DbObjectSavable dbObject ) {
		long roNum = dbObject.getDbPk();
		if (roNum == -1) {
			roNum = getLastRoNum();
		}
		List<Job> roJobs = ((RepairOrder) dbObject).getJobs();
		for (Job job : roJobs) {
			job.getDataMap().put("roid", Long.toString(roNum));
			DbServices.saveObject(job);
		}
	}
	
	private static int getLastRoNum() {
		ResultSet results = null;
		try (PreparedStatement lastInvoiceNumStatement = DbServices.getDbConnection().prepareStatement("SELECT MAX(roid) FROM cis2901c.ro;")) {
			results = lastInvoiceNumStatement.executeQuery();
			while (results.next()) {
				return results.getInt(1);		// we should always return here
			}
		} catch (SQLException e) {
			Main.getLogger().log(Level.SEVERE, "SQL Error: getLastRoNum");
			e.printStackTrace();
		}
		// if we get here something went wrong
		return SQL_FAILURE;
	}
	
	private static void saveJobParts(DbObjectSavable dbObject) {
		long jobId = dbObject.getDbPk();
		if (jobId == -1) {
			jobId = getLastJobId();
		}
		List<JobPart> jobParts  = ((Job) dbObject).getJobParts();
		for (JobPart jobPart : jobParts) {
			jobPart.getDataMap().put("jobid", Long.toString(jobId));
			DbServices.saveObject(jobPart);
		}
	}
	
	private static void saveJobLabor(DbObjectSavable dbObject) {
		long jobId = dbObject.getDbPk();
		if (jobId == -1) {
			jobId = getLastJobId();
		}
		List<JobLabor> jobLabor = ((Job) dbObject).getLabor();
		for (JobLabor labor : jobLabor) {
			labor.getDataMap().put("jobid", Long.toString(jobId));
			DbServices.saveObject(labor);
		}
	}
	
	private static int getLastJobId() {
		ResultSet results = null;
		try (PreparedStatement lastInvoiceNumStatement = DbServices.getDbConnection().prepareStatement("SELECT MAX(jobid) FROM cis2901c.job;")) {
			results = lastInvoiceNumStatement.executeQuery();
			while (results.next()) {
				return results.getInt(1);		// we should always return here
			}
		} catch (SQLException e) {
			Main.getLogger().log(Level.SEVERE, "SQL Error: getLastJobId");
			e.printStackTrace();
		}
		// if we get here something went wrong
		return SQL_FAILURE;
	}
	
	private static void saveInvoiceLineItems(DbObjectSavable dbObject) {
		int invoiceNumber = getLastInvoiceNum();
		// insert individual part invoice line items into invoicepart table
		// in invoicepart section, update onHand of part table
		TableItem[] invoiceLineItems = ((Invoice) dbObject).getTableLineItems();
		for (TableItem invoiceLineItem : invoiceLineItems) {
			if (invoiceLineItem.getData() == null) {
				break;
			}
			InvoicePart invoicePart = (InvoicePart) invoiceLineItem.getData();
			invoicePart.getDataMap().put("invoicenum", Integer.toString(invoiceNumber));
			saveObject(invoicePart);
		}
		sellInvoicePartsOutOfInventory(invoiceNumber);
	}
	
	private static int getLastInvoiceNum() {
		ResultSet results = null;
		try (PreparedStatement lastInvoiceNumStatement = DbServices.getDbConnection().prepareStatement("SELECT MAX(invoicenum) FROM cis2901c.invoice;")) {
			results = lastInvoiceNumStatement.executeQuery();
			while (results.next()) {
				return results.getInt(1);		// we should always return here
			}
		} catch (SQLException e) {
			Main.getLogger().log(Level.SEVERE, "SQL Error: getLastInvoiceNum");
			e.printStackTrace();
		}
		// if we get here something went wrong
		return SQL_FAILURE;
	}
	
	private static void sellInvoicePartsOutOfInventory(int invoiceNumber) {
		StringBuilder query = new StringBuilder("UPDATE part p JOIN invoicepart ip ON p.partid = ip.partid "
				+ "SET p.onhand = p.onhand - ip.quantity WHERE ip.invoicenum = ?;");
		sendQueryToDb(query, new ArrayList<>(List.of(Integer.toString(invoiceNumber))));
	}
	
	private static void sellRoPartsOutOfInventory(long roNumber) {
		StringBuilder query = new StringBuilder("UPDATE cis2901c.part p JOIN cis2901c.jobpart jp ON p.partid = jp.partid "
				+ "JOIN cis2901c.job j ON jp.jobid = j.jobid "
				+ "SET p.onhand = p.onhand - jp.quantity WHERE j.roid = ?;");
		sendQueryToDb(query, new ArrayList<>(List.of(Long.toString(roNumber))));
	}
	
	private static int sendQueryToDb(StringBuilder queryString) {
		return sendQueryToDb(queryString, new ArrayList<>());
	}

	private static int sendQueryToDb(StringBuilder queryString, List<String> dbFields) {
		int updateCount = 0;
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(queryString.toString())) {
			int parameterIndex = 1;
			for(String fieldData : dbFields) {
				statement.setString(parameterIndex, fieldData);
				parameterIndex++;
			}
			statement.execute();
			updateCount = statement.getUpdateCount();
			Main.getLogger().log(Level.INFO, "Update Count: {0}", updateCount);
		} catch (SQLException e) {
			Main.getLogger().log(Level.SEVERE, "SQL Error: {0}", queryString);
			e.printStackTrace();
		}
		return updateCount;
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
	
	public static int deleteDetailsFromRo(Map<String, List<Long>> detailsToDelete) {
		int updateCount = 0;
		for (Map.Entry<String, List<Long>> entry : detailsToDelete.entrySet()) {
			for (Long primaryKey : entry.getValue()) {
				updateCount += deleteObjectsByPk(entry.getKey(), entry.getKey() + "id", primaryKey);
			}
		}
		
		return updateCount;
	}
	
	private static int deleteObjectsByPk(String tableName, String primaryKeyName, Long primaryKey) {
		int updateCount = 0;
		if (!isConnected()) {
			connectToDb();
		}
		String deleteQuery = "DELETE FROM " + tableName + " WHERE " + primaryKeyName + " = ?;";
		
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(deleteQuery)) {
			statement.setLong(1, primaryKey);
			statement.execute();
			updateCount = statement.getUpdateCount();
			Main.getLogger().log(Level.INFO, "Delete from {0}. Count: {1}", new Object[] {tableName, updateCount});
		} catch (SQLException e) {
			// TODO print deleteQuery or statement
			e.printStackTrace();
		}
		return updateCount;
	}
	
	public static String[] sanitizer(String searchQuery) {
		// simple regex to remove chars i don't want to search for
			// !!!! this is not to be taken as SQL Injection protection
		return searchQuery.replaceAll("[^a-zA-Z0-9 %'-]", "").split(" ");
	}
	
	public static Object searchForObjectByPk(DbObjectSearchable object) {
		return searchForObjectsByPk(object)[0];
	}
	
	// TODO rename to searchForManyObjectsByPk
	public static Object[] searchForObjectsByPk(DbObjectSearchable object) {
		if (!isConnected()) {
			connectToDb();
		}
		
		Object[] results = new Object[1];		// TODO we can move this into each DbSearchableObjects in our application, make an object.getSelectedIdQuery()
											// this will allow us to make a polymorphic select by Primary Key search - MOSTLY DONE
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(object.getSearchQuery())) {
			statement.setLong(1, Long.parseLong(object.getSearchString()));
			ResultSet queryResultSet = statement.executeQuery();
			results = buildFoundObjects(queryResultSet, object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;	
	}
	
	// TODO rename to searchByQueryString
	public static Object[] searchForObject(DbObjectSearchable object) {
		if (!isConnected()) {
			connectToDb();
		}
		
		Object[] results = new Object[1];
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(object.getSearchQuery())) {
			statement.setMaxRows(MAX_RESULTS);
			fillStatementParameters(statement, object);
			ResultSet queryResultSet = statement.executeQuery();
			results = buildFoundObjects(queryResultSet, object);
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
	
	private static Object[] buildFoundObjects(ResultSet queryResultSet, DbObjectSearchable object) throws SQLException {
		Object[] results = new Object[1];
		if (object instanceof Customer) {
			results = buildCustomers(queryResultSet);
		} else if (object instanceof Part) {
			results = buildParts(queryResultSet);
		} else if (object instanceof Unit) {
			results = buildUnits(queryResultSet);
		} else if (object instanceof Invoice) {
			results = buildInvoices(queryResultSet);
		} else if (object instanceof RepairOrder) {
			results = buildRepairOrders(queryResultSet);
		} else if (object instanceof Job) {
			results = buildJobs(queryResultSet);
		} else if (object instanceof JobLabor) {
			results = buildJobLabors(queryResultSet);
		} else if (object instanceof JobPart) {
			results = buildJobParts(queryResultSet);
		}
		return results;
	}
	
	private static Customer[] buildCustomers(ResultSet queryResultSet) throws SQLException {
		Customer[] results = new Customer[MAX_RESULTS];
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
		return results;
	}
	
	private static Part[] buildParts(ResultSet queryResultSet) throws SQLException {
		Part[] results = new Part[MAX_RESULTS];
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
		return results;
	}
	
	private static Unit[] buildUnits(ResultSet queryResultSet) throws SQLException {
		Unit[] results = new Unit[MAX_RESULTS];
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
		return results;
	}
	
	private static Invoice[] buildInvoices(ResultSet queryResultSet) throws SQLException {
		Invoice[] results = new Invoice[MAX_RESULTS];
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
		return results;
	}
	
	private static RepairOrder[] buildRepairOrders(ResultSet queryResultSet) throws SQLException {
		RepairOrder[] results = new RepairOrder[MAX_RESULTS];
		int i = 0;
		while (queryResultSet.next()) {
			RepairOrder repairOrder = new RepairOrder();
			repairOrder.setRepairOrderId(queryResultSet.getLong(1));
			repairOrder.setCustomerId(queryResultSet.getLong(2));
			
			String lastname = queryResultSet.getString(3);
			String firstname = queryResultSet.getString(4);
			repairOrder.setCustomerName(lastname, firstname);
			
			// TODO see if we can use TextBlocks here
			String customerData = queryResultSet.getString(5) + "\n" + queryResultSet.getString(6) + ", " +
					queryResultSet.getString(7) + " " + queryResultSet.getString(8) + "\n" + queryResultSet.getString(9) + "\n" +
					queryResultSet.getString(10) + "\n" + queryResultSet.getString(11);
			repairOrder.setCustomerData(customerData);
			
			repairOrder.setUnitId(queryResultSet.getLong(12));
			repairOrder.setUnitYear(queryResultSet.getString(13));
			repairOrder.setUnitMake(queryResultSet.getString(14));
			repairOrder.setUnitModel(queryResultSet.getString(15));
			repairOrder.setUnitVin(queryResultSet.getString(16));
			
			repairOrder.setCreatedDate(queryResultSet.getTimestamp(17));
			repairOrder.setClosedDate(queryResultSet.getTimestamp(18));
			
			List<Job> roJobs =  new ArrayList<>(Arrays.asList((Job[]) DbServices.searchForObjectsByPk(new Job(repairOrder.getRepairOrderId()))));
			roJobs.removeAll(Collections.singleton(null));
			repairOrder.setJobs(roJobs);
						
			results[i] = repairOrder;
			i++;
		}
		return results;
	}
	
	private static Job[] buildJobs(ResultSet queryResultSet) throws SQLException {
		Job[] results = new Job[MAX_RESULTS];
		int i = 0;
		while (queryResultSet.next()) {
			Job job = new Job();
			job.setJobId(queryResultSet.getLong(1));
			job.setRoId(queryResultSet.getLong(2));
			job.setJobName(queryResultSet.getString(3));
			job.setComplaints(queryResultSet.getString(4));
			job.setResolution(queryResultSet.getString(5));
			job.setReccomendations(queryResultSet.getString(6));
			
			results[i] = job;
			i++;
		}
		return results;
	}
	
	private static JobLabor[] buildJobLabors(ResultSet queryResultSet) throws SQLException {
		JobLabor[] results = new JobLabor[MAX_RESULTS];
		int i = 0;
		while (queryResultSet.next()) {
			JobLabor jobLabor = new JobLabor();
			jobLabor.setJobLaborId(queryResultSet.getLong(1));
			jobLabor.setJobId(queryResultSet.getLong(2));
			jobLabor.setDescription(queryResultSet.getString(3));
			jobLabor.setHours(queryResultSet.getBigDecimal(4));
			jobLabor.setLaborRate(queryResultSet.getBigDecimal(5));
			jobLabor.setTechnician(queryResultSet.getString(6));
						
			results[i] = jobLabor;
			i++;
		}
		return results;
	}
	
	private static JobPart[] buildJobParts(ResultSet queryResultSet) throws SQLException {
		JobPart[] results = new JobPart[MAX_RESULTS];
		int i = 0;
		while (queryResultSet.next()) {
			JobPart jobPart = new JobPart();
			jobPart.setJobPartId(queryResultSet.getLong(1));
			jobPart.setJobId(queryResultSet.getLong(2));
			jobPart.setPartId(queryResultSet.getLong(3));
			jobPart.setPartNumber(queryResultSet.getString(4));
			jobPart.setDescription(queryResultSet.getString(5));
			jobPart.setQuantity(queryResultSet.getInt(6));
			jobPart.setSoldPrice(queryResultSet.getBigDecimal(7));
			jobPart.setPart((Part) DbServices.searchForObjectByPk(new Part(jobPart.getPartId())));
			
			results[i] = jobPart;
			i++;
		}
		return results;
	}
}
