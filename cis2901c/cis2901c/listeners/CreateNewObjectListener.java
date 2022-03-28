package cis2901c.listeners;

import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Shell;

import cis2901c.main.Main;
import cis2901c.main.NewCustomerDialog;
import cis2901c.main.NewPartDialog;
import cis2901c.main.NewUnitDialog;
import cis2901c.objects.Customer;
import cis2901c.objects.MyTable;
import cis2901c.objects.MyText;
import cis2901c.objects.Part;
import cis2901c.objects.Unit;

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
		
		Main.log(Level.INFO, "Mouse down event on a table, first column: " + table.getColumn(0).getText());
		
		Object newObject = null;
		Object[] newTableContents =  new Object[0];
		if (table.getColumn(0).getText().equals("First Name")) {
			NewCustomerDialog addNewCustomerDialog = new NewCustomerDialog(shell, SWT.NONE);
			newObject = addNewCustomerDialog.open();
			newTableContents = new Customer[table.getItems().length + 1];
		} else if (table.getColumn(0).getText().equals("Owner")) {
			NewUnitDialog addNewUnitDialog = new NewUnitDialog(shell, SWT.NONE);
			newObject = addNewUnitDialog.open();
			newTableContents = new Unit[table.getItems().length + 1];
		} else if (table.getColumn(0).getText().equals("Part Number")) {
			NewPartDialog addNewPartDialog = new NewPartDialog(shell, SWT.NONE);
			newObject = addNewPartDialog.open();
			newTableContents = new Part[table.getItems().length + 1];
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
