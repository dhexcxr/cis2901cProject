package cis2901c.objects;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class MyInvoiceTableItem extends TableItem implements DbObject{
	
	private final int DESCRIPTION_COLUMN = 1;
	private final int QUANTITY_COLUMN = 2;
	private final int SOLDPRICE_COLUMN = 6;
	
	private Map<String, String> dataMap = new HashMap<>();

	public MyInvoiceTableItem(Table parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	public MyInvoiceTableItem(MyTable parent, int style, int itemCount) {
		super(parent, style, itemCount);
		// TODO Auto-generated constructor stub
	}

	@Override
	public long getDbPk() {
		// when this is called in DbServices we'll always be making a new TableItem
		return -1;					// ie you can't edit a TableItem
	}

	@Override
	public String getPkName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "invoicepart";
	}

	@Override
	public Map<String, String> getDataMap() {
		dataMap.put("partid", Integer.toString(((Part) this.getData()).getPartId()));
		dataMap.put("description", this.getText(DESCRIPTION_COLUMN));
		dataMap.put("quantity", this.getText(QUANTITY_COLUMN));
		dataMap.put("soldprice", this.getText(SOLDPRICE_COLUMN));		
		return dataMap;
	}
	
	public void setDataMap(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
