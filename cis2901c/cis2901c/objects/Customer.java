package cis2901c.objects;

import java.sql.SQLException;

import org.eclipse.swt.widgets.Table;
import cis2901c.listeners.DbServices;

public class Customer {
	
	// might not need this
	private long customerId = -1;
		// TODO need to set customerId when customer object is saved
			// when saving new object to DB get pk from DB after line creation
			// on second thought, we'll probably implement this as dbServices.getPrimaryKey
		// on third thought, there objects are not persistent, we retrieve it from DB each time we want to use it
		// I don't think we need a getPrimaryKey
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private int zipCode;
	private int homePhone;
	private int workPhone;
	private int cellPhone;
	private String email;
	
	public Customer() {
	}
	
	public Customer(long customerId, String firstName, String lastName, String address, String city, String state,
			int zipCode, int homePhone, int workPhone, int cellPhone, String email) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.homePhone = homePhone;
		this.workPhone = workPhone;
		this.cellPhone = cellPhone;
		this.email = email;
	}
	
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public int getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(int homePhone) {
		this.homePhone = homePhone;
	}

	public int getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(int workPhone) {
		this.workPhone = workPhone;
	}

	public int getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(int cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	protected static void populateCustomerTable(Table table) throws SQLException {
		DbServices.searchForObject(table, "");
	}
	
	// TODO look at all throws
//	public static void searchForCustomer(Table resultsTable, String searchQuery) throws SQLException {
//		// TODO move search into DbServices
//				// searchObject(query, table (maybe? we need some way to decide which table to search)) will find a customer in the customer table
//				// it will return results which we'll use to populate table
////		Statement customerQuery = Main.getDbConnection().createStatement();
//		
////		if (query.length() != 0) {
////			ResultSet customerQueryResults = customerQuery.executeQuery(
//		Connection dbConnection = DbServices.getDbConnection();
//			PreparedStatement statement = dbConnection.prepareStatement(
//					"SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, "
//							+ "email, customerId FROM cis2901c.customer WHERE firstName LIKE ? OR lastName LIKE ? OR homePhone LIKE ? OR workPhone LIKE ? OR cellPhone LIKE ?;");
//			statement.setString(1, "%" + searchQuery + "%");
//			statement.setString(2, "%" + searchQuery + "%");
//			String phone = searchQuery.replaceAll("[()\\s-]+", "");
//			statement.setString(3, "%" + phone + "%");
//			statement.setString(4, "%" + phone + "%");
//			statement.setString(5, "%" + phone + "%");
//			ResultSet customerQueryResults = statement.executeQuery();
//		
////		if (query.length() == 0) {
////			customerQueryResults =  customerQuery.executeQuery(
////					"SELECT firstName, lastName, address, city, state, zipcode, "
////							+ "homePhone, workPhone, cellPhone, email FROM cis2901c.customer;");
////		}
//		
//		while (customerQueryResults.next()) {
//			String firstName = customerQueryResults.getString(1);
//			String lastName = customerQueryResults.getString(2);
//			String address = customerQueryResults.getString(3);
//			String city = customerQueryResults.getString(4);
//			String state = customerQueryResults.getString(5);
////			String zip = Integer.toString(customerQueryResults.getInt(6));
////			String homePhone = Integer.toString(customerQueryResults.getInt(7));
////			String workPhone = Integer.toString(customerQueryResults.getInt(8)); // not using this right now
////			String cellPhone = Integer.toString(customerQueryResults.getInt(9));
//			
//			int zip = customerQueryResults.getInt(6);
//			int homePhone = customerQueryResults.getInt(7);
//			int workPhone = customerQueryResults.getInt(8); // not using this right now
//			int cellPhone = customerQueryResults.getInt(9);
//			
//			String email = customerQueryResults.getString(10);
//			int customerId = customerQueryResults.getInt(11);
//			
//			Customer customer = new Customer(customerId, firstName, lastName, address, city, state,
//					zip, homePhone, workPhone, cellPhone, email);
//					
//			TableItem tableItem = new TableItem(resultsTable, SWT.NONE);
//					// TODO when setData called, pull tableItem.txt from object instead of manually setting
//						// we can add a method to Customer to array up fields
//						// on second thought, this would probably require too much custom object code for each result table
//						// may as well keep it with each object class, as in this code for displaying Customer objects
//						// is in the Customer class
//			tableItem.setText(new String[] {firstName, lastName, address, city, state, zip == 0 ? "" : Integer.toString(zip),
//					homePhone == 0 ? "" : Integer.toString(homePhone), cellPhone == 0 ? "" : Integer.toString(cellPhone), email} );
//			tableItem.setData(customer);
//		}
//	}
	
	protected static void openCustomer() {
		// get customer object from dbService class
		
	}
}

//class NewCustomerButtonListeners extends MouseAdapter {
//	
//	public NewCustomerButtonListeners() {	
//	}
//	
//	public void mouseDown(MouseEvent e) {
////		Window parent = SwingUtilities.windowForComponent(Main.getShell());
//		NewCustomerDialog addNewCustomerDialog = new NewCustomerDialog(Main.getShell(), SWT.NONE);
//		addNewCustomerDialog.open();
//		
//	}
//}

//class CustomerSearchBoxListeners implements ModifyListener {		//SWT imple
//	
//	// TODO separate FocusListener from this class and combine with RoSearchBox FocusListener
//	// TODO switch Text to MyText
//	
//	private Text searchBox;
//	private Table table;
////	private String textBoxText;
//	
//	public CustomerSearchBoxListeners(Text textBox, Table table) {
//		this.searchBox = textBox;
//		this.table = table;
////		this.textBoxText = textBox.getText();
//	}
//
//	@Override
//	public void modifyText(ModifyEvent e) {
////		list.setItems("Test");
////		TableItem tableItem = new TableItem(table, SWT.NONE);
////		tableItem.setText(new String[] {"0ame", "other name", "other deets"} );
//		table.removeAll();
//		int queryLength = searchBox.getText().length();
//		if (queryLength > 0) {
//			try {
//				Customer.searchForCustomer(table, searchBox.getText());
//			} catch (SQLException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//	}
//}

