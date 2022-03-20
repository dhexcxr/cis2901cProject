package cis2901c.objects;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class InvoicePartTableItem extends TableItem implements DbObjectSavable{
	
	// this emulates the invoicepart DB table
	private static final int DESCRIPTION_COLUMN = 1;
	private static final int QUANTITY_COLUMN = 2;
	private static final int SOLDPRICE_COLUMN = 6;
	
	private Map<String, String> dataMap = new HashMap<>();

	public InvoicePartTableItem(Table parent, int style) {
		super(parent, style);
	}

	public InvoicePartTableItem(MyTable parent, int style, int itemCount) {
		super(parent, style, itemCount);
	}

	@Override
	public long getDbPk() {
		// when this is called in DbServices we'll always be making a new TableItem
		return -1;					// ie you can't edit a TableItem
	}
	
	@Override
	public void setDbPk(long dbPk) {
		// not used, we currently don't rebuild Invoices from the Database
	}

	@Override
	public String getPkName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "invoicepart";
	}

	@Override
	public Map<String, String> getDataMap() {
		if (dataMap.isEmpty()) {
			dataMap.put("partid", Long.toString(((Part) this.getData()).getPartId()));
			dataMap.put("description", this.getText(DESCRIPTION_COLUMN));
			dataMap.put("quantity", this.getText(QUANTITY_COLUMN));
			dataMap.put("soldprice", this.getText(SOLDPRICE_COLUMN));
		}		
		return dataMap;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
