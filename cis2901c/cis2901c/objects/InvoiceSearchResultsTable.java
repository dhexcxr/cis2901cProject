package cis2901c.objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class InvoiceSearchResultsTable extends MyTable {
	
	private static final int INVOICE_NUM_INDEX = 0;
	private static final int CUSTOMER_INDEX = 1;
	private static final int CASHIER_DATE_TIME_INDEX = 2;
	private static final int LINE_ITEMS_INDEX = 3;
	private static final int TOTAL_INDEX = 4;
	
	private int currentSortDirection = SWT.UP;
	private int currentSortedColumn = INVOICE_NUM_INDEX;
		
	public InvoiceSearchResultsTable(Composite parent, int style) {	
		super(parent, style);
	}
	@Override
	public void paint(Object object) {
		// build each TableItem to fill Unit Table
			paint(object,this.getSelectionIndex());
	}

	@Override
	public void paint(Object part, int selectedTableItemIndex) {
		this.paint(new Object[] {part});
	}
	
	@Override
	public void paint(Object[] invoiceResults) {
		// build each TableItem to fill Invoice Table
		for (Invoice invoice: (Invoice[]) invoiceResults) {
			if (invoice == null) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
			tableItem.setText(new String[] {Long.toString(invoice.getInvoiceNum()), invoice.getCustomerName(),
					invoice.getCashiereDateTime().toString(), Integer.toString(invoice.getParts().length), "$" + invoice.getTotal().toString()});
			tableItem.setData(invoice);
		}
		initialSortOnPaint(currentSortedColumn, currentSortDirection);
	}
	
	private void initialSortOnPaint(int sortColumn, int sortDirection) {
		// sort table results by 0-indexed column
		TableItem[] items = this.getItems();
		TableColumn column = this.getColumn(sortColumn);
		int index = sortColumn;
		for (int i = 1; i < items.length; i++) {
			String value1 = items[i].getText(index);
			for (int j = 0; j < i; j++) {
				String value2 = items[j].getText(index);
				if ((sortDirection ==  SWT.UP && collator.compare(value1, value2) < 0) ||
						(sortDirection ==  SWT.DOWN && collator.compare(value1, value2) > 0)) {
					String[] values = { items[i].getText(INVOICE_NUM_INDEX), items[i].getText(CUSTOMER_INDEX),
							items[i].getText(CASHIER_DATE_TIME_INDEX), items[i].getText(LINE_ITEMS_INDEX), items[i].getText(TOTAL_INDEX) };
					Object tempPart = items[i].getData();
					items[i].dispose();
					TableItem item = new TableItem(this, SWT.NONE, j);
					item.setText(values);
					item.setData(tempPart);
					items = this.getItems();
					break;
				}
			}
		}
		this.setSortColumn(column);
		this.setSortDirection(sortDirection);
	}
	
	@Override
	public void sort(int sortColumn) {
		if (currentSortedColumn == sortColumn && currentSortDirection == SWT.UP) {
			currentSortDirection = SWT.DOWN;
		} else if (currentSortedColumn == sortColumn && currentSortDirection == SWT.DOWN) {
			currentSortDirection = SWT.UP;
		} else {
			currentSortedColumn = sortColumn;
			currentSortDirection = SWT.UP;
		}
		initialSortOnPaint(currentSortedColumn, currentSortDirection);
	}
}
