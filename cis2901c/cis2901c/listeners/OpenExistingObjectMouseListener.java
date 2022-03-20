package cis2901c.listeners;

import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import cis2901c.main.Main;
import cis2901c.main.NewCustomerDialog;
import cis2901c.main.NewPartDialog;
import cis2901c.objects.Customer;
import cis2901c.objects.MyTable;
import cis2901c.objects.Part;

public class OpenExistingObjectMouseListener extends MouseAdapter {

	MyTable table = null;
	Shell shell;


	public OpenExistingObjectMouseListener(MyTable table, Shell shell) {
		this.table = table;
		this.shell = shell;
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		if (e.getSource() instanceof Button) {
			mouseDoubleClick(e);
		}
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// open saved object for editing
		Main.log(Level.INFO, "Double clicked to open an existing object");
		Object[] tableObjects = null;
		if (table.getColumn(0).getText().equals("First Name") && table.getSelection().length > 0) {
			openCustomer(table);
			tableObjects = new Customer[table.getItems().length];
		} else if (table.getColumn(0).getText().equals("Part Number") && table.getSelection().length > 0) {
			openPart(table);
			tableObjects = new Part[table.getItems().length];
		} else {		// if nothing is selected, return
			return;
		}

		// here, we repaint table with objects that are currently in it so we don't have to go out to the DB
		for (int i = 0; i < table.getItems().length; i++) {
			tableObjects[i] = table.getItems()[i].getData();
		}
		table.removeAll();
		table.paint(tableObjects);
	}

	private void openCustomer(Table table) {
		// get object saved in TableItem Data
		Customer customer = (Customer) table.getSelection()[0].getData();
		Main.log(Level.INFO, "Opened: " + customer.getFirstName());
		Main.log(Level.INFO, Long.toString(customer.getCustomerId()));

		// open customer for editing
		NewCustomerDialog modifyExistingCustomerDialog = new NewCustomerDialog(shell, SWT.NONE);
		modifyExistingCustomerDialog.open(customer);
	}
	
	private void openPart(Table table) {
		// get object saved in TableItem Data
		Part part= (Part) table.getSelection()[0].getData();
		Main.log(Level.INFO, "Open part: " + part.getPartNumber());
		Main.log(Level.INFO, part.getDescription());
		
		NewPartDialog modifyExistingPartDialog = new NewPartDialog(shell, SWT.NONE);
		modifyExistingPartDialog.open(part);
	}
}
