package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Table;

import cis2901c.main.Main;
import cis2901c.main.NewCustomerDialog;
import cis2901c.objects.Customer;

public class OpenExistingCustomerMouseListener extends MouseAdapter {
	
	Table customerTable = null;


	public OpenExistingCustomerMouseListener(Table customerTable) {
		this.customerTable = customerTable;
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// open saved customer for editing
		System.out.println("Double click");
		Customer customer = (Customer) customerTable.getSelection()[0].getData();
		System.out.println(customer.getFirstName());
		System.out.println(customer.getCustomerId());

		NewCustomerDialog modifyExistingCustomerDialog = new NewCustomerDialog(Main.getShell(), SWT.NONE);
		modifyExistingCustomerDialog.open(customer);
		// TODO get table to refresh on Customer Save
	}


}
