package cis2901c.listeners;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import cis2901c.objects.Customer;
import cis2901c.objects.MyTable;
import cis2901c.objects.MyText;

public class SearchTextBoxListeners implements ModifyListener {
	
	// TODO separate FocusListener from this class and combine with RoSearchBox FocusListener
	private MyText searchBox;
	private MyTable table;
	private Object searchObject;
	
	public SearchTextBoxListeners(MyText textBox, MyTable table) {
		this.searchBox = textBox;
		this.table = table;
	}
	
	public SearchTextBoxListeners(MyText textBox, MyTable table, Object searchObject) {
		this.searchBox = textBox;
		this.table = table;
		this.searchObject = searchObject;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		if (searchObject instanceof Customer) {
			int queryLength = searchBox.getText().trim().length();
			if (queryLength > 0) {
				table.removeAll();
				table.paint(DbServices.searchForObject(new Customer(searchBox.getText())));
			} else if (queryLength == 0) {
				table.removeAll();
			}
			return;
		}
		
		int queryLength = searchBox.getText().trim().length();
		if (queryLength > 0) {
			table.removeAll();
			table.paint(DbServices.searchForObject(table, searchBox.getText()));
		} else if (queryLength == 0) {
			table.removeAll();
		}
	}
}
