package cis2901c.objects;

import java.util.Date;

public class RepairOrder {
	long repairOrder;
	long customerId;
	long unitId;
	Date createdDate;
	Date closedDate;

}

//class RoSearchBoxListeners implements ModifyListener, FocusListener {
//	
//	private Text searchBox;
////	private List list;
//	private Table table;
//	
//	public RoSearchBoxListeners(Text textBox, Table table) {
//		this.searchBox = textBox;
////		this.list = list;
//		this.table = table;
//	}
//
//	@Override
//	public void modifyText(ModifyEvent e) {
////		list.setItems("Test");
//		table.removeAll();
////		if (table.get)
//		TableItem tableItem = new TableItem(table, SWT.NONE);
//		tableItem.setText(new String[] {"0ame", "other name", "other deets"} );
//		
//	}
//
//	@Override
//	public void focusGained(FocusEvent e) {
//		System.out.println("Search box focused gained");
//		if (searchBox.getText().equals("Search...")) {
//			searchBox.setText("");
//		}		
//	}
//
//	@Override
//	public void focusLost(FocusEvent e) {
//		System.out.println("Search box focused lost");
//		if (searchBox.getText().equals("")) {
//			searchBox.setText("Search...");
//		}
//	}
//}

