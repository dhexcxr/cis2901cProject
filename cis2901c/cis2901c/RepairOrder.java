package cis2901c;

import java.util.Date;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class RepairOrder {
	long repairOrder;
	long customerId;
	long unitId;
	Date createdDate;
	Date closedDate;

}

class RoSearchBoxListeners implements ModifyListener, FocusListener {
	
	private Text searchBox;
	private List list;
	
	public RoSearchBoxListeners(Text textBox, List list) {
		this.searchBox = textBox;
		this.list = list;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		list.setItems("Test");		
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
