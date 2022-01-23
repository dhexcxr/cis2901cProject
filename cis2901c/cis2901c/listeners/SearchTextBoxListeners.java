package cis2901c.listeners;

import java.sql.SQLException;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;

import cis2901c.objects.MyTable;

public class SearchTextBoxListeners implements ModifyListener {		//SWT imple
	
	// TODO separate FocusListener from this class and combine with RoSearchBox FocusListener
	// TODO switch Text to MyText
	
	private Text searchBox;
	private MyTable table;
//	private String textBoxText;
	
	public SearchTextBoxListeners(Text textBox, MyTable table) {
		this.searchBox = textBox;
		this.table = table;
//		this.textBoxText = textBox.getText();
	}

	@Override
	public void modifyText(ModifyEvent e) {
//		list.setItems("Test");
//		TableItem tableItem = new TableItem(table, SWT.NONE);
//		tableItem.setText(new String[] {"0ame", "other name", "other deets"} );
		table.removeAll();
		int queryLength = searchBox.getText().length();
		if (queryLength > 0) {
			try {
				table.paint(DbServices.searchForObject(table, searchBox.getText()));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
