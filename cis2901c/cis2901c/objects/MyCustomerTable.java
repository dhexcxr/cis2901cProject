package cis2901c.objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

public class MyCustomerTable extends MyTable{

	public MyCustomerTable(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void paint(Object[] customerResults) {
		// TODO probably add sorting here
		// build each TableItem to fill Customer Table
		for (Customer customer : (Customer[]) customerResults) {
			if (customer == null) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
			tableItem.setText(new String[] {customer.getFirstName(), customer.getLastName(), customer.getAddress(), customer.getCity(),
					customer.getState(), customer.getZipCode(),	customer.setPhoneNumberFormat(customer.getHomePhone()),
						customer.setPhoneNumberFormat(customer.getCellPhone()), customer.getEmail()} );
			tableItem.setData(customer);
		}
	}

}
