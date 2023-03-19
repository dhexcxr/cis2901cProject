package cis2901c.objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class RepairOrderTable extends MyTable {
	private static final int RO_NUMBER_INDEX = 0;
	private static final int CUSTOMER_INDEX = 1;
	private static final int YEAR_INDEX = 2;
	private static final int MAKE_INDEX = 3;
	private static final int MODEL_INDEX = 4;
	private static final int VIN_INDEX = 5;
	private static final int JOBS_INDEX = 6;
	
	private int currentSortDirection = SWT.UP;
	private int currentSortedColumn = RO_NUMBER_INDEX;
	
	
	public RepairOrderTable(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void paint(Object object) {
		// build each TableItem to fill Unit Table
			paint(object, this.getSelectionIndex());
	}

	@Override
	public void paint(Object object, int selectedTableItemIndex) {
		// TODO compare this with PartInventoryTable.paint(part, slectedTableItemIndex)
		if (object == null) {
			return;
		}
		RepairOrder repairOrder= (RepairOrder) object;
		TableItem tableItem = this.getItem(selectedTableItemIndex);	
		tableItem.setText(new String[] {Long.toString(repairOrder.getRepairOrderId()), repairOrder.getCustomerName(),
				repairOrder.getUnitYear(), repairOrder.getUnitMake(), repairOrder.getUnitModel(), repairOrder.getUnitVin(), repairOrder.getJobNames()});
		tableItem.setData(repairOrder);
	}

	@Override
	public void paint(Object[] repairOrderResults) {
		for (RepairOrder repairOrder : (RepairOrder[]) repairOrderResults) {
			if (repairOrder == null) {
				break;
			}
			TableItem tableItem =  new TableItem(this, SWT.NONE);	
			tableItem.setText(new String[] {Long.toString(repairOrder.getRepairOrderId()), repairOrder.getCustomerName(),
					repairOrder.getUnitYear(), repairOrder.getUnitMake(), repairOrder.getUnitModel(), repairOrder.getUnitVin(), repairOrder.getJobNames()});
			tableItem.setData(repairOrder);
		}
		initialSortOnPaint(currentSortedColumn, currentSortDirection);
	}

	// TODO this and sort() can be moved into MyTable class, the only thing thats different in any Table Class' version of this method
		// is the String[] values which could be moved into a custom MyTableItem class
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
					String[] values = { items[i].getText(RO_NUMBER_INDEX), items[i].getText(CUSTOMER_INDEX),
							items[i].getText(YEAR_INDEX), items[i].getText(MAKE_INDEX), items[i].getText(MODEL_INDEX),
							items[i].getText(VIN_INDEX), items[i].getText(JOBS_INDEX)};
					Object tempRepairOrder = items[i].getData();
					items[i].dispose();
					TableItem item = new TableItem(this, SWT.NONE, j);
					item.setText(values);
					item.setData(tempRepairOrder);
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
		// TODO change currentSortedColumn to a TableColumn, then we can pass that TableColumn to initialSortOnPaint
			// and avoid the TableColumn column = this.getColumn(sortColumn); in initialSortOnPaint
			// as well as the for loop in TableColumnSortListener.widgetSelected()
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
