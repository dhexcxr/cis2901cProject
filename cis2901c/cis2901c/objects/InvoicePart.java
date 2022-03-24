package cis2901c.objects;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class InvoicePart extends DbObjectSearchable implements DbObjectSavable {
	
	// invoicepart Database Table

	private InvoicePartTableItem invoicePartTableItem;
	private Map<String, String> dataMap = new HashMap<>();
	private long invoicePartId = -1;
	private long partId;
	private String partNumber;
	private String description;
	private int quantity;
	private BigDecimal soldPrice = BigDecimal.valueOf(0);
	
	private Part part;
	
	public InvoicePart() {
		
	}
	
	public InvoicePart(Part part) {
		if (part == null) {
			return;
		}
		this.partId = part.getPartId();
		this.partNumber = part.getPartNumber();
		this.description = part.getDescription();
		this.quantity = 1;
		this.part = part;
	}
	
//	public InvoicePart(InvoicePartTableItem invoicePartTableItem) {
//		this.invoicePartTableItem = invoicePartTableItem;
//	}

	public long getInvoicePartId() {
		return invoicePartId;
	}

	public void setInvoicePartId(long invoicePartId) {
		this.invoicePartId = invoicePartId;
	}

	public long getPartId() {
		return partId;
	}

	public void setPartId(long partId) {
		this.partId = partId;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getSoldPrice() {
		return soldPrice;
	}

	public void setSoldPrice(BigDecimal soldPrice) {
		this.soldPrice = soldPrice;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.partId = part.getPartId();
		this.partNumber = part.getPartNumber();
		this.description = part.getDescription();
		if (this.part != null && this.part.getPartId() != part.getPartId()) {
			this.quantity = 1;
		}
		this.part = part;
	}

	@Override
	public long getDbPk() {
		// when this is called in DbServices we'll always be making a new TableItem
				return -1;					// ie you can't open and edit Invoices
	}

	@Override
	public void setDbPk(long dbPk) {
		return;		// similarly, we don't need to set the Primary Key
	}

	@Override
	public String getPkName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getFkName() {
		// TODO Auto-generated method stub
		return "invoicenum";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "invoicepart";
	}

	@Override
	public Map<String, String> getDataMap() {
		// TODO Auto-generated method stub
		dataMap.put("partid", Long.toString(partId));
		dataMap.put("description", description);
		dataMap.put("quantity", Integer.toString(quantity));
		dataMap.put("soldprice", soldPrice.toString());
		return dataMap;
	}
}
