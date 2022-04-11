package cis2901c.listeners;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Shell;

import cis2901c.main.ConfirmDialog;
import cis2901c.main.Main;
import cis2901c.main.RepairOrderDialog;

public class RepairOrderCancelListener extends MouseAdapter {

	private RepairOrderDialog roDialog;
	private Shell shell;
	
	public RepairOrderCancelListener(RepairOrderDialog roDialog) {
		this.roDialog = roDialog;
		this.shell = roDialog.getRoDialogShell();
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		boolean skipCancelConfirm = Main.getSettings().skipCancelConfirm();
		boolean cancelChanges = true;
		if (!skipCancelConfirm) {
			ConfirmDialog confirmDialogBox = new ConfirmDialog (shell, SWT.APPLICATION_MODAL);
			confirmDialogBox.setText("Cancle Repair Order Changes?");
			confirmDialogBox.setMessage("Undo all changes?");
			boolean[] response = confirmDialogBox.open();
			cancelChanges = response[0];
			skipCancelConfirm = response[1];
			
			if (skipCancelConfirm) {
				// do not ask to confirm again
				Main.getSettings().setSkipCancelConfirm(true);
			}
		}
		
		if (cancelChanges) {
			// TODO remove customer and unit
			roDialog.getTxtCustomerRepairOrder().setText("");
			roDialog.getTxtCustomerRepairOrder().setData(null);
			roDialog.getTxtUnitRepairOrder().setText("");
			roDialog.getTxtUnitRepairOrder().setData(null);
			roDialog.getTableJobsRepairOrder().removeAll();
			roDialog.disableJobTabs();
			roDialog.setDetailsToDelete(new HashMap<>());
			roDialog.reloadRo();
		}
	}

}
