package cis2901c.listeners;

import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;

import cis2901c.objects.MyTable;

public class DeleteEnableListener implements MenuDetectListener {

	private MyTable table;
	
	public DeleteEnableListener(MyTable table) {
		this.table = table;
	}

	@Override
	public void menuDetected(MenuDetectEvent e) {
		if (table.getItemCount() > 0 && table.getSelectionCount() == 1) {
			table.getMenu().getItem(MyTable.DELETE_MENU_ITEM).setEnabled(true);
		} else {
			table.getMenu().getItem(MyTable.DELETE_MENU_ITEM).setEnabled(false);
		}
	}


}
