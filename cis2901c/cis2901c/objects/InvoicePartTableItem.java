package cis2901c.objects;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class InvoicePartTableItem extends TableItem implements DbObjectSavable{
	
	public static final int PART_NUMBER_COLUMN = 0;
	public static final int DESCRIPTION_COLUMN = 1;
	public static final int QUANTITY_COLUMN = 2;
	public static final int SOLDPRICE_COLUMN = 6;
	
	private Map<String, String> dataMap = new HashMap<>();

	public InvoicePartTableItem(Table parent, int style) {
		super(parent, style);
	}

	public InvoicePartTableItem(MyTable parent, int style, int itemCount) {
		super(parent, style, itemCount);
	}

	@Override
	public long getDbPk() {
		return -1;		// when this is called in DbServices we'll always be making a new TableItem, ie you can't edit a TableItem
	}
	
	@Override
	public void setDbPk(long dbPk) {
		return;
	}

	@Override
	public String getPkName() {
		return null;
	}
	
	public String getFkName() {
		return "invoicenum";
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
