package cis2901c.listeners;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import cis2901c.objects.MyTable;
import cis2901c.objects.MyText;

public class SearchTextBoxListeners implements ModifyListener {
	
	// TODO separate FocusListener from this class and combine with RoSearchBox FocusListener
	
	private MyText searchBox;
	private MyTable table;
	
	public SearchTextBoxListeners(MyText textBox, MyTable table) {
		this.searchBox = textBox;
		this.table = table;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		table.removeAll();
		int queryLength = searchBox.getText().trim().length();
		if (queryLength > 0) {
			table.paint(DbServices.searchForObject(table, searchBox.getText()));
		}
	}
}
