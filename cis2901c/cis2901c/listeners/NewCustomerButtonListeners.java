package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import cis2901c.Main;
import cis2901c.NewCustomerDialog;

public class NewCustomerButtonListeners extends MouseAdapter {
	
	public NewCustomerButtonListeners() {	
	}
	
	public void mouseDown(MouseEvent e) {
//		Window parent = SwingUtilities.windowForComponent(Main.getShell());
		NewCustomerDialog addNewCustomerDialog = new NewCustomerDialog(Main.getShell(), SWT.NONE);
		addNewCustomerDialog.open();
		
	}
}
