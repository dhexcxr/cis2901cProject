package cis2901c.listeners;

import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cis2901c.main.Main;

public class RepairOrderSearchBoxListeners implements ModifyListener, FocusListener {
	
	// TODO combine with SearchBoxListener
	private Text searchBox;
	private Table table;
	
	public RepairOrderSearchBoxListeners(Text textBox, Table table) {
		this.searchBox = textBox;
		this.table = table;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		// TODO stub, waiting for RO Tab implementation
		table.removeAll();
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText(new String[] {"0ame", "other name", "other deets"} );
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO stub, waiting for RO Tab implementation
		Main.log(Level.INFO, "Search box focused gained");
		if (searchBox.getText().equals("Search...")) {
			searchBox.setText("");
		}		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO stub, waiting for RO Tab implementation
		Main.log(Level.INFO, "Search box focused lost");
		if (searchBox.getText().equals("")) {
			searchBox.setText("Search...");
		}
	}
}
