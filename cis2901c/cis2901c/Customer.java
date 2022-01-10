package cis2901c;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class Customer {
	long customerId;
	String firstName;
	String lastName;
	String address;
	String city;
	String state;
	int zipCode;
	int homePhone;
	int workPhone;
	int cellPhone;
	String email;
	
	protected static void populateCustomerTable(Table table) throws SQLException {
		populateCustomerTable(table, "");
	}
	
	protected static void populateCustomerTable(Table table, String query) throws SQLException {
		Statement customerQuery = Main.getDbConnection().createStatement();
		
		ResultSet customerQueryResults = customerQuery.executeQuery(
				"SELECT firstName, lastName, address, city, state, zipcode, "
				+ "homePhone, workPhone, cellPhone, email FROM cis2901c.customer WHERE lastName LIKE \"" + query + "%\" OR firstName LIKE \"" + query + "%\" ;");
		
		if (query.length() == 0) {
			customerQueryResults =  customerQuery.executeQuery(
					"SELECT firstName, lastName, address, city, state, zipcode, "
							+ "homePhone, workPhone, cellPhone, email FROM cis2901c.customer;");
		}
		
		while (customerQueryResults.next()) {
			String firstName = customerQueryResults.getString(1);
			String lastName = customerQueryResults.getString(2);
			String address = customerQueryResults.getString(3);
			String city = customerQueryResults.getString(4);
			String state = customerQueryResults.getString(5);
			String zip = Integer.toString(customerQueryResults.getInt(6));
			String homePhone = Integer.toString(customerQueryResults.getInt(7));
			String workPhone = Integer.toString(customerQueryResults.getInt(8));
			String cellPhone = Integer.toString(customerQueryResults.getInt(9));
			String email = customerQueryResults.getString(10);
			
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(new String[] {firstName, lastName, address, city, state, zip, homePhone, cellPhone, email} );
		}
	}
	
}

class CustomerSearchBoxListeners implements ModifyListener, FocusListener {
	
	private Text searchBox;
//	private List list;
	private Table table;
	
	public CustomerSearchBoxListeners(Text textBox, Table table) {
		this.searchBox = textBox;
//		this.list = list;
		this.table = table;
	}

	@Override
	public void modifyText(ModifyEvent e) {
//		list.setItems("Test");
//		TableItem tableItem = new TableItem(table, SWT.NONE);
//		tableItem.setText(new String[] {"0ame", "other name", "other deets"} );
		table.removeAll();
		try {
			Customer.populateCustomerTable(table, searchBox.getText());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		System.out.println("Search box focused gained");
		if (searchBox.getText().equals("Search...")) {
			searchBox.setText("");
		}		
	}

	@Override
	public void focusLost(FocusEvent e) {
		System.out.println("Search box focused lost");
		if (searchBox.getText().equals("")) {
			searchBox.setText("Search...");
		}
	}
}

