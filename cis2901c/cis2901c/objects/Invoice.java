package cis2901c.objects;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.TableItem;

public class Invoice implements DbObject{

	private int invoiceNum = -1;
	private long customerId;
	private String customerName;
	private String customerData;
	private String notes;
	private BigDecimal tax;
	private Timestamp cashiereDateTime;
	private boolean cashiered;
	
	private TableItem[] tableLineItems;
	private Part[] parts;
	
	public Invoice() {
	}
	
	public Invoice(int invoiceNum, long customerId, String lastname, String firstname, String customerData, String notes, BigDecimal tax,
			Timestamp cashiereDateTime, boolean cashiered, Part[] parts) {
		super();
		this.invoiceNum = invoiceNum;
		this.customerId = customerId;
		this.setCustomerName(lastname, firstname);
		this.customerData = customerData;
		this.notes = notes;
		this.tax = tax;
		this.cashiereDateTime = cashiereDateTime;
		this.cashiered = cashiered;
		this.parts = parts;
	}

	public int getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(int invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String lastname, String firstname) {
		if (firstname != null) {
			firstname = firstname.equals("") ? firstname : ", " + firstname;
		}
		this.customerName = lastname + firstname;
	}

	public String getCustomerData() {
		return customerData;
	}

	public void setCustomerData(String customerData) {
		this.customerData = customerData;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public Timestamp getCashiereDateTime() {
		return cashiereDateTime;
	}

	public void setCashiereDateTime(Timestamp cashiereDateTime) {
		this.cashiereDateTime = cashiereDateTime;
	}

	public boolean isCashiered() {
		return cashiered;
	}

	public void setCashiered(boolean cashiered) {
		this.cashiered = cashiered;
	}

	public TableItem[] getTableLineItems() {
		return tableLineItems;
	}

	public void setTableLineItems(TableItem[] tableItems) {
		this.tableLineItems = tableItems;
	}

	public Part[] getParts() {
		return parts;
	}

	public void setParts(Part[] parts) {
		this.parts = parts;
	}

	@Override
	public long getDbPk() {
//		return invoiceNum;		// when this is called in DbServices we'll always be making a new invoice
		return -1;					// ie you can't edit an old invoice
	}

	@Override
	public String getPkName() {
		return "invoicenum";
	}

	@Override
	public String getTableName() {
		return "invoice";
	}

	@Override
	public Map<String, String> getDataMap() {
		// TODO finish field map method
		Map<String, String> dataMap = new HashMap<>();
		if (invoiceNum != -1) {
			dataMap.put("invoicenum", Long.toString(customerId));
		}
		dataMap.put("customerid", Long.toString(customerId));
		dataMap.put("customerdata", customerData);
		dataMap.put("notes", notes);
		dataMap.put("tax", tax.toString());
		dataMap.put("cashiereddate", cashiereDateTime.toString());
		dataMap.put("cashiered", cashiered ? "1" : "0");
		return dataMap;
	}	
}
