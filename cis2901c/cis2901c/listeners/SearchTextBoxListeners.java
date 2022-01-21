package cis2901c.listeners;

import java.sql.SQLException;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

public class SearchTextBoxListeners implements ModifyListener {		//SWT imple
	
	// TODO separate FocusListener from this class and combine with RoSearchBox FocusListener
	// TODO switch Text to MyText
	
	private Text searchBox;
	private Table table;
//	private String textBoxText;
	
	public SearchTextBoxListeners(Text textBox, Table table) {
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
				DbServices.searchForObject(table, searchBox.getText());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
