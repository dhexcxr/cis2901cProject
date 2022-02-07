package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Shell;

import cis2901c.main.NewCustomerDialog;
import cis2901c.objects.Customer;
import cis2901c.objects.MyTable;
import cis2901c.objects.MyText;

public class CreateNewObjectListener extends MouseAdapter{
	
	MyTable table = null;
	MyText searchBox = null;
	Shell shell;
	
	public CreateNewObjectListener(MyTable table, MyText searchBox, Shell shell) {
		this.table = table;
		this.searchBox = searchBox;
		this.shell = shell;
	}
	
	
	@Override
	public void mouseDown(MouseEvent e) {
		System.out.println(table.getColumn(0).getText());
		
		Object newObject = null;
		Object newTableContents[] =  null;
		if (table.getColumn(0).getText().equals("First Name")) {
			NewCustomerDialog addNewCustomerDialog = new NewCustomerDialog(shell, SWT.NONE);
			newObject = (Customer) addNewCustomerDialog.open();
			newTableContents = new Customer[table.getItems().length + 1];
		}
		
		// repaint table - get current data in TableItems, insert the new data at the end, then call table.paint
		for (int i = 0; i < table.getItems().length; i++) {
			newTableContents[i] = table.getItems()[i].getData();
		}
		newTableContents[table.getItems().length] = newObject;
		table.removeAll();
		table.paint(newTableContents);
	}	
}
