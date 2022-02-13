package cis2901c.objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class UnitTable extends MyTable{
	
	private static final int OWNER_INDEX = 0;
	private static final int MAKE_INDEX = 1;
	private static final int MODEL_INDEX = 2;
	private static final int MODEL_NAME_INDEX = 3;
	private static final int YEAR_INDEX = 4;
	private static final int MILEAGE_INDEX = 5;
	private static final int COLOR_INDEX = 6;
	private static final int VIN_INDEX = 7;
	
	private int currentSortDirection = SWT.UP;
	private int currentSortedColumn = OWNER_INDEX;

	public UnitTable(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void paint(Object object) {
		// build each TableItem to fill Unit Table
			paint(object,this.getSelectionIndex());
	}

	@Override
	public void paint(Object customer, int selectedTableItemIndex) {
		// TODO compare this with MyPartInvoiceTable.paint(Object)
		this.paint(new Object[] {customer});
	}
	
	@Override
	public void paint(Object[] unitResults) {
		// build each TableItem to fill Unit Table
		for (Unit unit : (Unit[]) unitResults) {
			if (unit == null) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
			tableItem.setText(new String[] {unit.getOwner(), unit.getMake(), unit.getModel(),
				unit.getModelName(), unit.getYear() == 0 ? "" : Integer.toString(unit.getYear()),
						Integer.toString(unit.getMileage()), unit.getColor(), unit.getVin(), unit.getNotes()});
			tableItem.setData(unit);
		}
		initialSortOnPaint(currentSortedColumn, currentSortDirection);
	}
	
	
	// TODO this and sort() can be moved into MyTable class, the only thing thats different in any Table Class' version of this method
				// is the String[] values which could be moved into a custom MyTableItem class
	private void initialSortOnPaint(int sortColumn, int sortDirection) {
		// sort table results by 0-indexed column
		TableItem[] items = this.getItems();
//		Collator collator = Collator.getInstance(Locale.getDefault());
		TableColumn column = this.getColumn(sortColumn);
		int index = sortColumn;
		for (int i = 1; i < items.length; i++) {
			String value1 = items[i].getText(index);
			for (int j = 0; j < i; j++) {
				String value2 = items[j].getText(index);
				if ((sortDirection ==  SWT.UP && collator.compare(value1, value2) < 0) ||
						(sortDirection ==  SWT.DOWN && collator.compare(value1, value2) > 0)) {
					String[] values = { items[i].getText(OWNER_INDEX), items[i].getText(MAKE_INDEX),
							items[i].getText(MODEL_INDEX), items[i].getText(MODEL_NAME_INDEX), items[i].getText(YEAR_INDEX),
							items[i].getText(MILEAGE_INDEX), items[i].getText(COLOR_INDEX), items[i].getText(VIN_INDEX)};
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
