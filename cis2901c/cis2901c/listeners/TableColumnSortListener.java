package cis2901c.listeners;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;

import cis2901c.objects.MyTable;

public class TableColumnSortListener extends SelectionAdapter{
	
	private MyTable table;
	private TableColumn column;
	
	public TableColumnSortListener(TableColumn column) {
		super();
		this.table = (MyTable) column.getParent();
		this.column = column;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO pass this.column to table.sort() after changing MyTable.currentSortedColumn to a TableColumn...hopefully this makes sense later
		for (int i = 0; i < table.getColumnCount(); i++) {
			if (column == table.getColumn(i)) {
				table.sort(i);
			}
		}
	}
}
