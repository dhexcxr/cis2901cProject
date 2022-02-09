package cis2901c.objects;

import java.text.Collator;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class MyPartInventoryTable extends MyTable{
	private static final int PART_NUMBER_INDEX = 0;
	private static final int DESCRIPTION_INDEX = 1;
	private static final int ON_HAND_INDEX = 2;
	private static final int SUPPLIER_INDEX = 3;
	private static final int CATEGORY_INDEX = 4;
	private static final int COST_INDEX = 5;
	private static final int RETAIL_INDEX = 6;
	
	private int currentSortDirection = SWT.UP;
	private int currentSortedColumn = PART_NUMBER_INDEX;

	public MyPartInventoryTable(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void paint(Object part) {
		// TODO compare this with MyPartInvoiceTable.paint(Object)
		this.paint(new Object[] {part});
	}
	
	@Override
	public void paint(Object[] partResults) {
		// build each TableItem to fill Unit Table
		for (Part part : (Part[]) partResults) {
			if (part == null) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
				// TODO break out all tables into classes so we can correctly paint all without conditional paint
			tableItem.setText(new String[] {part.getPartNumber(), part.getDescription(),
					Integer.toString(part.getOnHand() == -1 ? 0 : part.getOnHand()), part.getSupplier(),
								part.getCategory(), part.getCost().toString(), part.getRetail().toString()});
			tableItem.setData(part);
		}
		initialSortOnPaint(currentSortedColumn, currentSortDirection);
	}
	
	// TODO this and sort() can be moved into MyTable class, the only thing thats different in any Table Class' version of this method
				// is the String[] values which could be moved into a custom MyTableItem class
	private void initialSortOnPaint(int sortColumn, int sortDirection) {
		// sort table results by 0-indexed column
		TableItem[] items = this.getItems();
		Collator collator = Collator.getInstance(Locale.getDefault());
		TableColumn column = this.getColumn(sortColumn);
		int index = sortColumn;
		for (int i = 1; i < items.length; i++) {
			String value1 = items[i].getText(index);
			for (int j = 0; j < i; j++) {
				String value2 = items[j].getText(index);
				if ((sortDirection ==  SWT.UP && collator.compare(value1, value2) < 0) ||
						(sortDirection ==  SWT.DOWN && collator.compare(value1, value2) > 0)) {
					String[] values = { items[i].getText(PART_NUMBER_INDEX), items[i].getText(DESCRIPTION_INDEX),
							items[i].getText(ON_HAND_INDEX), items[i].getText(SUPPLIER_INDEX), items[i].getText(CATEGORY_INDEX),
								items[i].getText(COST_INDEX), items[i].getText(RETAIL_INDEX)};
					items[i].dispose();
					TableItem item = new TableItem(this, SWT.NONE, j);
					item.setText(values);
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
