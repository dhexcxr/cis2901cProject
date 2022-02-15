package cis2901c.objects;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Table;
import cis2901c.listeners.DbServices;

public class Customer extends DbObjectSearchable implements DbObjectSavable{
	
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

	// this is now used here and in PartInvoiceEditorEventListener, and in PhoneNumberTextBoxModifyListener
	private static final String NOT_NUMBERS = "[^0-9]";		// find a better name
	private static final String SEVEN_DIGIT_PHONE = "$1-$2";
	private static final String TEN_DIGIT_PHONE = "$1-$2-$3";
	private static final String INTERNATIONAL_PHONE = "$1-$2-$3-$4";
	
	public Customer() {
	}
	
	public Customer(String searchString) {
		super.searchString = searchString;
		super.searchQuery = """
				SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, 
				email, customerId FROM cis2901c.customer WHERE firstName LIKE ? OR homePhone LIKE ? OR lastName LIKE ? 
				OR workPhone LIKE ? OR address LIKE ? OR cellPhone LIKE ? OR city LIKE ? OR zipcode LIKE ? OR state LIKE ?;""";
		super.outerSearchQueryAppendix = " AND customerId IN (";
		super.querySubStringIndecies[0] = 7;
		super.querySubStringIndecies[1] = 99;
	}
	
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
		return firstName == null ? "" : firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName == null ? "" : lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address == null ? "" : address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city == null ? "" : city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state == null ? "" : state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode == null ? "" : zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getHomePhone() {
		return homePhone == null ? "" : homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getWorkPhone() {
		return workPhone == null ? "" : workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getCellPhone() {
		return cellPhone == null ? "" : cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEmail() {
		return email == null ? "" : email;
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

	protected static void populateCustomerTable(Table table) {
		// not yet used, could populate "" in searchForObject call with current txtSearchBox.getText()
		DbServices.searchForObject(new Customer(""));
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
