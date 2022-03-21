package cis2901c.objects;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class InvoicePartTableItem extends TableItem implements DbObjectSavable{
	
	// TODO clean this up, most of this is no longer needed
	// this emulates the invoicepart DB table
	
	public static final int PART_NUMBER_COLUMN = 0;
	public static final int DESCRIPTION_COLUMN = 1;
	public static final int QUANTITY_COLUMN = 2;
	public static final int SOLDPRICE_COLUMN = 6;
	
	private Map<String, String> dataMap = new HashMap<>();

	public InvoicePartTableItem(Table parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	public InvoicePartTableItem(MyTable parent, int style, int itemCount) {
		super(parent, style, itemCount);
		// TODO Auto-generated constructor stub
	}

	@Override
	public long getDbPk() {
		// when this is called in DbServices we'll always be making a new TableItem
		return -1;					// ie you can't edit a TableItem
	}
	
	@Override
	public void setDbPk(long dbPk) {
		return;
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
		// TODO i think I need to put these into the constructor, as it stands these will be added to 
			// dataMap every time this function is called, check every instance of getDataMap in all Objects
		// TODO cont, I can't create this in constructor, I don't necessarily know all these details at object instatiation
			// if I create a new map in here, it will return this new object, but then I can't add invoicenum in DbServices
//		Map<String, String> dataMap = new HashMap<>();
		if (dataMap.isEmpty()) {
			dataMap.put("partid", Long.toString(((Part) this.getData()).getPartId()));
			dataMap.put("description", this.getText(DESCRIPTION_COLUMN));
			dataMap.put("quantity", this.getText(QUANTITY_COLUMN));
			dataMap.put("soldprice", this.getText(SOLDPRICE_COLUMN));
		}		
		return dataMap;
	}
	
//	public void setDataMap(Map<String, String> dataMap) {
//		this.dataMap = dataMap;
//	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
