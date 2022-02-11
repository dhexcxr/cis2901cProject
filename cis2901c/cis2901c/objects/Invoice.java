package cis2901c.objects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.TableItem;

public class Invoice extends DbObjectSearchable implements DbObjectSavable{

	private int invoiceNum = -1;
	private long customerId;
	private String customerName;
	private String customerData;
	private String notes;
	private BigDecimal tax;
	private Timestamp cashiereDateTime;
	private boolean cashiered;
	private BigDecimal total;
	
	private TableItem[] tableLineItems;
	private Part[] parts;
	
	public Invoice() {
	}
	
	public Invoice(String searchString) {
		super.searchString = searchString;
		super.searchQuery = new StringBuilder("""
				SELECT i.invoicenum, i.customerid, c.lastname, c.firstname, c.address, c.city, c.state, c.zipcode, 
				c.homephone, c.cellphone, c.email, i.notes, i.tax, i.cashiereddate, i.cashiered, ip.count, ip.total + i.tax 
				FROM cis2901c.invoice AS i JOIN cis2901c.customer AS c ON i.customerid = c.customerid 
				JOIN (SELECT invoicenum, COUNT(partid) AS count, SUM(soldprice) AS total FROM cis2901c.invoicepart GROUP BY invoicenum) AS ip 
				ON i.invoicenum = ip.invoicenum WHERE c.lastname LIKE ? OR c.firstname LIKE ? OR i.invoicenum LIKE ?;""");
		super.outerSearchQueryAppendix = new StringBuilder(" AND i.invoicenum IN (");
		super.querySubStringIndecies[0] = 19;
		super.querySubStringIndecies[1] = 207;
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
	
	private BigDecimal getPartsTotal() {
		// not used right now, need to populate Part[] parts before this is usable
		BigDecimal partsTotal = new BigDecimal(0);
		for (int i = 0; i < this.getParts().length; i++) {
			partsTotal.add(this.getParts()[i].getRetail());
		}
		return partsTotal;
	}
	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
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
