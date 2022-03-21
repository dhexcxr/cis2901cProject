package cis2901c.objects;

import java.util.HashMap;
import java.util.Map;

public class InvoicePart extends DbObjectSearchable implements DbObjectSavable {
	
	// invoicepart Database Table

	private InvoicePartTableItem invoicePartTableItem;
	private Map<String, String> dataMap = new HashMap<>();
	
	public InvoicePart(InvoicePartTableItem invoicePartTableItem) {
		this.invoicePartTableItem = invoicePartTableItem;
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
		dataMap.put("partid", Long.toString(((Part) invoicePartTableItem.getData()).getPartId()));
		dataMap.put("description", invoicePartTableItem.getText(InvoicePartTableItem.DESCRIPTION_COLUMN));
		dataMap.put("quantity", invoicePartTableItem.getText(InvoicePartTableItem.QUANTITY_COLUMN));
		dataMap.put("soldprice", invoicePartTableItem.getText(InvoicePartTableItem.SOLDPRICE_COLUMN));
		return dataMap;
	}
}
