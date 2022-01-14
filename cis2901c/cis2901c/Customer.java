package cis2901c;

import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.JTextField;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class Customer {
	
	// might not need this
	
	private long customerId;
		// TODO need to set customerId when customer object is saved
			// when saving new object to db get pk from db after line creation
			// on second thought, we'll probably impliment this as dbServices.gerPrimaryKey
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
		searchForCustomer(table, "");
	}
	
	// TODO look at all throws
	protected static void searchForCustomer(Table table, String query) throws SQLException {
		// TODO move search into DbServices
				// searchObject(query, table (maybe? some way to decide which table to search)) will find a customer in the customer table
				// it will return results which we'll use to populate table
//		Statement customerQuery = Main.getDbConnection().createStatement();
		
//		if (query.length() != 0) {
//			ResultSet customerQueryResults = customerQuery.executeQuery(
		Connection dbConnection = DbServices.getDbConnection();
			PreparedStatement statement = dbConnection.prepareStatement(
					"SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, "
							+ "email FROM cis2901c.customer WHERE firstName LIKE ? OR lastName LIKE ? OR homePhone LIKE ? OR workPhone LIKE ? OR cellPhone LIKE ?;");
			statement.setString(1, "%" + query + "%");
			statement.setString(2, "%" + query + "%");
			String phone = query.replaceAll("[()\\s-]+", "");
			statement.setString(3, "%" + phone + "%");
			statement.setString(4, "%" + phone + "%");
			statement.setString(5, "%" + phone + "%");
			ResultSet customerQueryResults = statement.executeQuery();
		
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
			String zip = Integer.toString(customerQueryResults.getInt(6));
			String homePhone = Integer.toString(customerQueryResults.getInt(7));
//			String workPhone = Integer.toString(customerQueryResults.getInt(8));
			String cellPhone = Integer.toString(customerQueryResults.getInt(9));
			String email = customerQueryResults.getString(10);
			
			TableItem tableItem = new TableItem(table, SWT.NONE);																		// SWT implementation
			tableItem.setText(new String[] {firstName, lastName, address, city, state, zip, homePhone, cellPhone, email} );				// SWT implementation
		}
//		}
//		dbConnection.close();
	}
	
	protected static void openCustomer() {
		// get customer object from dbService class
		
	}
}

class NewCustomerButtonListeners extends MouseAdapter {
	
	public NewCustomerButtonListeners() {	
	}
	
	public void mouseDown(MouseEvent e) {
//		Window parent = SwingUtilities.windowForComponent(Main.getShell());
		NewCustomerDialog addNewCustomerDialog = new NewCustomerDialog(Main.getShell(), SWT.NONE);
		addNewCustomerDialog.open();
	}
}

class CustomerSearchBoxListeners implements ModifyListener {		//SWT imple
	
	// TODO separate FocusListener from this class and combine with RoSearchBox FocusListener
	// TODO switch Text to MyText
	
	private Text searchBox;
	private Table table;
	private String textBoxText;
	
	public CustomerSearchBoxListeners(Text textBox, Table table) {
		this.searchBox = textBox;
		this.table = table;
		this.textBoxText = textBox.getText();
	}

	@Override
	public void modifyText(ModifyEvent e) {
//		list.setItems("Test");
//		TableItem tableItem = new TableItem(table, SWT.NONE);
//		tableItem.setText(new String[] {"0ame", "other name", "other deets"} );
		table.removeAll();
		int queryLength = searchBox.getText().length();
		if (queryLength > 0) {
			try {
				Customer.searchForCustomer(table, searchBox.getText());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}

