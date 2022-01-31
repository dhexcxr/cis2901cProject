package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;

import cis2901c.main.CustomerSearchDialog;
import cis2901c.objects.Customer;
import cis2901c.objects.MyText;

public class CustomerSearchListener extends MouseAdapter{
	
	MyText txtBox;
	String txtBoxStartingText;
	
	public CustomerSearchListener(MyText txtBox) {
		this.txtBox = txtBox;
		this.txtBoxStartingText = txtBox.getText();
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// open Customer Search dialog with Owner text box double click
		CustomerSearchDialog customerSearchDialog = new CustomerSearchDialog(Display.getDefault().getActiveShell(), SWT.NONE);
		Customer selectedCustomer = new Customer();
		if (!txtBox.getText().equals(txtBoxStartingText)) {
			// if we're editing a current unit, pass Owner name to Customer Search Dialog
			selectedCustomer = (Customer) customerSearchDialog.open(txtBox.getText());
		} else {
			// open normal, empty Customer Search Dialog
			selectedCustomer = (Customer) customerSearchDialog.open();
		}
		if (selectedCustomer instanceof Customer) {
			txtBox.setText(selectedCustomer.getLastName() + ", " + selectedCustomer.getFirstName());
			txtBox.setData(selectedCustomer);
//			customerId = selectedCustomer.getCustomerId();
		}
	}

}
