package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import cis2901c.main.NewCustomerDialog;
import cis2901c.objects.Customer;
import cis2901c.objects.MyTable;

public class OpenExistingObjectMouseListener extends MouseAdapter {

	MyTable table = null;
	Shell shell;


	public OpenExistingObjectMouseListener(MyTable table, Shell shell) {
		this.table = table;
		this.shell = shell;
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// open saved object for editing
		System.out.println("Double click");
		Object[] tableObjects = null;
		if (table.getColumn(0).getText().equals("First Name") && table.getSelection().length > 0) {
			openCustomer(table);
			tableObjects = new Customer[table.getItems().length];
		} else {		// if nothing is selected, return
			return;
		}

		// we repaint table with objects that are currently in table so we don't have to go out to the DB
		for (int i = 0; i < table.getItems().length; i++) {
			tableObjects[i] = table.getItems()[i].getData();
		}
		table.removeAll();
		table.paint(tableObjects);
	}

	private void openCustomer(Table table) {
		// get object saved in TableItem Data
		Customer customer = (Customer) table.getSelection()[0].getData();
		System.out.println(customer.getFirstName());
		System.out.println(customer.getCustomerId());

		// open customer for editing
		NewCustomerDialog modifyExistingCustomerDialog = new NewCustomerDialog(shell, SWT.NONE);
		modifyExistingCustomerDialog.open(customer);
	}
}
