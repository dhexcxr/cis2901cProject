package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class RoSearchBoxListeners implements ModifyListener, FocusListener {
	
	private Text searchBox;
//	private List list;
	private Table table;
	
	public RoSearchBoxListeners(Text textBox, Table table) {
		this.searchBox = textBox;
//		this.list = list;
		this.table = table;
	}

	@Override
	public void modifyText(ModifyEvent e) {
//		list.setItems("Test");
		table.removeAll();
//		if (table.get)
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText(new String[] {"0ame", "other name", "other deets"} );
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		System.out.println("Search box focused gained");
		if (searchBox.getText().equals("Search...")) {
			searchBox.setText("");
		}		
	}

	@Override
	public void focusLost(FocusEvent e) {
		System.out.println("Search box focused lost");
		if (searchBox.getText().equals("")) {
			searchBox.setText("Search...");
		}
	}
}
