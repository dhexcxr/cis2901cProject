package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import cis2901c.main.NewCustomerDialog;
import cis2901c.main.NewPartDialog;
import cis2901c.main.NewUnitDialog;
import cis2901c.objects.Customer;
import cis2901c.objects.MyTable;
import cis2901c.objects.Part;
import cis2901c.objects.Unit;

public class OpenExistingObjectMouseListener extends MouseAdapter {

	MyTable table = null;
	Shell shell;


	public OpenExistingObjectMouseListener(MyTable customerTable, Shell shell) {
		this.table = customerTable;
		this.shell = shell;
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// open saved object for editing
		System.out.println("Double click");
		// TODO there might be a better way to check what type we're searching
			// just like in DbServices.searchForObject
		if (table.getColumn(0).getText().equals("First Name") && table.getSelection().length > 0) {
			openCustomer(table);
		} else if (table.getColumn(0).getText().equals("Owner") && table.getSelection().length > 0) {
			openUnit(table);
		} else if (table.getColumn(0).getText().equals("Part Number") && table.getSelection().length > 0) {
			openPart(table);
		}
		// TODO get table to refresh on Save
	}

	// TODO take out print statements, combine openCustomer and openUnit
	private void openCustomer(Table table) {
		// get object saved in TableItem Data
		Customer customer = (Customer) table.getSelection()[0].getData();
		System.out.println(customer.getFirstName());
		System.out.println(customer.getCustomerId());

		// open customer for editing
		NewCustomerDialog modifyExistingCustomerDialog = new NewCustomerDialog(shell, SWT.NONE);
		modifyExistingCustomerDialog.open(customer);
	}

	private void openUnit(Table table) {
		// get object saved in TableItem Data
		Unit unit = (Unit) table.getSelection()[0].getData();
		System.out.println(unit.getMake());
		System.out.println(unit.getModel());
		System.out.println(unit.getVin());

		NewUnitDialog modifyExistingUnitDialog = new NewUnitDialog(shell, SWT.NONE);
		modifyExistingUnitDialog.open(unit);
	}
	
	private void openPart(Table table) {
		// get object saved in TableItem Data
		Part part= (Part) table.getSelection()[0].getData();
		System.out.println(part.getPartNumber());
		System.out.println(part.getDescription());
		
		NewPartDialog modifyExistingPartDialog = new NewPartDialog(shell, SWT.NONE);
		modifyExistingPartDialog.open(part);
	}
}
