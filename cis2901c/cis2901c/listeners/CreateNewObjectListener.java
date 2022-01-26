package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Shell;

import cis2901c.main.NewPartDialog;
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
		// TODO finish this for other objects and add this listener to the New buttons
		System.out.println(table.getColumn(0).getText());
		
		if (table.getColumn(0).getText().equals("First Name")) {
//			openCustomer(table);
//			tableObjects = new Customer[table.getItems().length];
		} else if (table.getColumn(0).getText().equals("Owner")) {
//			openUnit(table);
//			tableObjects = new Unit[table.getItems().length];
		} else if (table.getColumn(0).getText().equals("Part Number")) {
			NewPartDialog addNewPartDialog = new NewPartDialog(shell, SWT.NONE);
			addNewPartDialog.open();
		} 
		
		table.removeAll();
		int queryLength = searchBox.getText().trim().length();
		if (queryLength > 0) {
			table.paint(DbServices.searchForObject(table, searchBox.getText()));
		}
		// TODO add repainting to new object saves, probably put in external listener like OpenExistingObject
		
//		table.removeAll();
//		int queryLength = partSearchTextBox.getText().trim().length();
//		if (queryLength > 0) {
//			partTable.paint(DbServices.searchForObject(table, partSearchTextBox.getText()));
//		}
		
//		Object[] partTableObjects = new Object[partTable.getItems().length];
//		for (int i = 0; i < partTable.getItems().length; i++) {
//			partTableObjects[i] = partTable.getItems()[i].getData();
//		}
//		partTable.paint(partTableObjects);
	}
	
}
