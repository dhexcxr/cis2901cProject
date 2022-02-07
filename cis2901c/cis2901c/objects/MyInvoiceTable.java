package cis2901c.objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

public class MyInvoiceTable extends MyTable {

	public static final int PART_NUMBER_COLUMN = 0;
	public static final int QUANTITY_COLUMN = 2;
	public static final int PART_PRICE_COLUMN = 5;
	public static final int EXTENDED_PRICE_COLUMN = 6;
	
	public MyInvoiceTable(Composite parent, int style) {	
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void paint(Object[] invoiceResults) {
		// build each TableItem to fill Invoice Table
		for (Invoice invoice: (Invoice[]) invoiceResults) {
			if (invoice == null) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
				// TODO set date/time display to something nicer
			tableItem.setText(new String[] {Integer.toString(invoice.getInvoiceNum()), invoice.getCustomerName(),
					invoice.getCashiereDateTime().toString(), Integer.toString(invoice.getParts().length), "DUMMY"});
			tableItem.setData(invoice);
		}
	}

}
