package cis2901c.objects;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class InvoicePartTableItem extends TableItem {
	
	public static final int PART_NUMBER_COLUMN = 0;
	public static final int DESCRIPTION_COLUMN = 1;
	public static final int QUANTITY_COLUMN = 2;
	public static final int SOLDPRICE_COLUMN = 6;

	public InvoicePartTableItem(Table parent, int style) {
		super(parent, style);
	}

	public InvoicePartTableItem(MyTable parent, int style, int itemCount) {
		super(parent, style, itemCount);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
