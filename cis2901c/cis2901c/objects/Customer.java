package cis2901c.objects;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Table;
import cis2901c.listeners.DbServices;

public class Customer implements DbObject{
	
	// might not need this
	private long customerId = -1;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private String zipCode;
	private String homePhone;
	private String workPhone;
	private String cellPhone;
	private String email;
	
	private String searchString;
	
	private final StringBuilder searchQuery = new StringBuilder("""
			SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, 
			email, customerId FROM cis2901c.customer WHERE firstName LIKE ? OR homePhone LIKE ? OR lastName LIKE ? 
			OR workPhone LIKE ? OR address LIKE ? OR cellPhone LIKE ? OR city LIKE ? OR zipcode LIKE ? OR state LIKE ?;""");
	private final StringBuilder outerSearchQueryAppendix = new StringBuilder(" AND customerId IN (");
	private final int[] querySubStringIndecies = {7, 99};

	// this is now used here and in PartInvoiceEditorEventListener, and in PhoneNumberTextBoxModifyListener
	private static final String NOT_NUMBERS = "[^0-9]";		// find a better name
	private static final String SEVEN_DIGIT_PHONE = "$1-$2";
	private static final String TEN_DIGIT_PHONE = "$1-$2-$3";
	private static final String INTERNATIONAL_PHONE = "$1-$2-$3-$4";
	
	public Customer() {
	}
	
	public Customer(String searchString) {
		this.searchString = searchString;
	}
	
//	public Customer(long customerId, String firstName, String lastName, String address, String city, String state,
//			String zipCode, String homePhone, String workPhone, String cellPhone, String email) {
//		super();
//		this.customerId = customerId;
//		this.firstName = firstName;
//		this.lastName = lastName;
//		this.address = address;
//		this.city = city;
//		this.state = state;
//		this.zipCode = zipCode;
//		this.homePhone = homePhone;
//		this.workPhone = workPhone;
//		this.cellPhone = cellPhone;
//		this.email = email;
//	}
	
	public long getDbPk() {
		return getCustomerId();
	}
	
	public String getPkName() {
		return "customerId";
	}
	
	public String getTableName() {
		return "customer";
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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Map<String, String> getDataMap() {
		// TODO we might need to make these Maps into <String, Object>, then instanceof on the Object value
			// to see how to treat it when building PreparedStatements with setParameter (of course I already changed all Customer fields to String)
		Map<String, String> dataMap = new HashMap<>();
		if (customerId != -1) {
			dataMap.put("customerId", Long.toString(customerId));
		}
		dataMap.put("firstName", firstName);
		dataMap.put("lastName", lastName);
		dataMap.put("address", address);
		dataMap.put("city", city);
		dataMap.put("state", state);
		dataMap.put("zipCode", zipCode);
		dataMap.put("homePhone", homePhone);
		dataMap.put("workPhone", workPhone);
		dataMap.put("cellPhone", cellPhone);
		dataMap.put("email", email);
				
		return dataMap;
	}

	// START object search methods
	public String getSearchString() {
		return searchString;
	}

	public StringBuilder getSearchQuery() {
		return searchQuery;
	}

	public String getOuterSearchQuery() {
		StringBuilder outerSearchQuery = new StringBuilder(getSearchQuery());
		outerSearchQuery.delete(outerSearchQuery.length() - 1, outerSearchQuery.length());
		outerSearchQuery.append(outerSearchQueryAppendix);
		return outerSearchQuery.toString();
	}
	
	public int[] getQuerySubStringIndecies() {
		return querySubStringIndecies;
	}
	// END object search methods

	protected static void populateCustomerTable(Table table) {
		// not yet used, could populate "" in searchForObject call with current txtSearchBox.getText()
		DbServices.searchForObject(table, "");
	}
	
	public String setPhoneNumberFormat(String inputNumber) {
		if (inputNumber != null) {
			inputNumber = (inputNumber.replaceAll(NOT_NUMBERS, ""));
			if (inputNumber.length() >= 7 && inputNumber.length() < 10) {
				inputNumber = (inputNumber.replaceFirst("(\\d{3})(\\d+)", SEVEN_DIGIT_PHONE));
			} else if (inputNumber.length() < 11) {
				inputNumber = (inputNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", TEN_DIGIT_PHONE));
			} else if (inputNumber.length() == 11) {
				inputNumber = (inputNumber.replaceFirst("(\\d{1})(\\d{3})(\\d{3})(\\d+)", INTERNATIONAL_PHONE));
			} else if (inputNumber.length() == 12) {
				inputNumber = (inputNumber.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d+)", INTERNATIONAL_PHONE));
			} else if (inputNumber.length() == 13) {
				inputNumber = (inputNumber.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d+)", INTERNATIONAL_PHONE));
			}
		}
		return inputNumber;
	}
}
