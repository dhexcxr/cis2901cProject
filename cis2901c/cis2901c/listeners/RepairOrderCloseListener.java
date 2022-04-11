package cis2901c.listeners;

import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import cis2901c.main.ConfirmDialog;
import cis2901c.main.Main;
import cis2901c.main.RepairOrderDialog;

public class RepairOrderCloseListener implements Listener {
	
	private RepairOrderDialog roDialog;
	private Shell shell;

	public RepairOrderCloseListener(RepairOrderDialog roDialog) {
		this.roDialog = roDialog;
		this.shell = roDialog.getRoDialogShell();
	}

	@Override
	public void handleEvent(Event event) {
		Main.getLogger().log(Level.INFO, "Close RO {0}", event.widget);
		boolean skipCloseConfirm = Main.getSettings().skipCloseConfirm();
		boolean close = true;
		if (!skipCloseConfirm && !roDialog.cashiered()) {
			ConfirmDialog confirmDialogBox = new ConfirmDialog (shell, SWT.APPLICATION_MODAL);
			boolean[] response = confirmDialogBox.open();
			close = response[0];
			skipCloseConfirm = response[1];
			
			if (skipCloseConfirm) {
				// do not ask to confirm again
				Main.getSettings().setSkipCloseConfirm(true);
			}
		}
		event.doit = close;
	}

}
