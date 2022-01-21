package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import cis2901c.main.Main;
import cis2901c.main.NewUnitDialog;

public class NewUnitButtonListener extends MouseAdapter {

	public NewUnitButtonListener() {
	}
	
	public void mouseDown(MouseEvent e) {
//		Window parent = SwingUtilities.windowForComponent(Main.getShell());
		NewUnitDialog addNewUnitDialog = new NewUnitDialog(Main.getShell(), SWT.NONE);
		addNewUnitDialog.open();
	}
}
