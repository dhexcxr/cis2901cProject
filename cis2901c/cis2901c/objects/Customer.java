package cis2901c.objects;

import org.eclipse.swt.widgets.Table;
import cis2901c.listeners.DbServices;

public class Customer {
	
	// might not need this
	private long customerId = -1;
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

	protected static void populateCustomerTable(Table table) {
		// not yet used, could populate "" in searchForObject call with current txtSearchBox.getText()
		DbServices.searchForObject(table, "");
	}
}
