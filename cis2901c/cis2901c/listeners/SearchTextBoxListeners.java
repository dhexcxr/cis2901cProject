package cis2901c.listeners;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import cis2901c.objects.Customer;
import cis2901c.objects.DbObjectSearchable;
import cis2901c.objects.Invoice;
import cis2901c.objects.MyTable;
import cis2901c.objects.MyText;
import cis2901c.objects.Part;

public class SearchTextBoxListeners implements ModifyListener {
	
	private MyText searchBox;
	private MyTable table;
	private DbObjectSearchable searchObject;
	private static final int DELETE_MENU_ITEM = 0;
	
	public SearchTextBoxListeners(MyText textBox, MyTable table) {
		this.searchBox = textBox;
		this.table = table;
	}
	
	public SearchTextBoxListeners(MyText textBox, MyTable table, DbObjectSearchable searchObject) {
		this.searchBox = textBox;
		this.table = table;
		this.searchObject = searchObject;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		int queryLength = searchBox.getText().trim().length();
		if (queryLength > 0) {
			table.removeAll();
			if (searchObject instanceof Customer) {
				searchObject = new Customer(searchBox.getText());
			} else if (searchObject instanceof Part) {
				searchObject = new Part(searchBox.getText());
			} else if (searchObject instanceof Invoice) {
				searchObject = new Invoice(searchBox.getText());
			}
			if (table.getMenu() != null) {		// enable Delete right click menu when there are things in the table
				table.getMenu().getItem(DELETE_MENU_ITEM).setEnabled(true);
			}
			table.paint(DbServices.searchForObject(searchObject));
		} else if (queryLength == 0) {
			table.removeAll();
		}
	}
}
