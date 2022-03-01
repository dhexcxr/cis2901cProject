package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import cis2901c.objects.DbObjectSavable;
import cis2901c.objects.MyTable;

public class DeleteObjectListener extends SelectionAdapter {

	private MyTable table;
	
	public DeleteObjectListener(MyTable table) {
		this.table = table;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		int linesUpdated = 0;
		if (table.getSelectionIndex() >= 0 && table.getSelectionIndex() < table.getItemCount() &&
				table.getItem(table.getSelectionIndex()).getData() != null) {
			linesUpdated = DbServices.deleteObject((DbObjectSavable) table.getItem(table.getSelectionIndex()).getData());
		}
		if (linesUpdated == 0) {
			MessageBox objectDeleteError = new MessageBox(new Shell(), SWT.ERROR);
			objectDeleteError.setText("Error");
			objectDeleteError.setMessage("Unable to delete object...");
			objectDeleteError.open();
		} else {
			table.remove(table.getSelectionIndex());
		}
	}
	
//	@Override
//	public void mouseDown(MouseEvent e) {
//		if (table.getSelectionIndex() >= 0 && table.getSelectionIndex() < table.getItemCount() &&
//				table.getItem(table.getSelectionIndex()).getData() != null) {
//			DbServices.deleteObject((DbObjectSavable) table.getItem(table.getSelectionIndex()).getData());
//		}
//	}

}
