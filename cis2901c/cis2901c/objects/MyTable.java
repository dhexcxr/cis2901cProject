package cis2901c.objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class MyTable extends Table {

	public MyTable(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	public void paint(Object[] objects) {
		if (objects == null || objects.length == 0) {
			return;
		}
		if (objects instanceof Customer[]) {
			paintCustomerTable((Customer[]) objects);
		} else if (objects instanceof Unit[]) {
			paintUnitTable((Unit[])objects);
		}
	}
	
	private void paintCustomerTable(Customer[] customerResults) {
		for (Customer customer : customerResults) {
			if (customer == null) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
			tableItem.setText(new String[] {customer.getFirstName(), customer.getLastName(), customer.getAddress(), customer.getCity(),
					customer.getState(), customer.getZipCode() == 0 ? "" : Integer.toString(customer.getZipCode()),
							customer.getHomePhone() == 0 ? "" : Integer.toString(customer.getHomePhone()),
							customer.getCellPhone() == 0 ? "" : Integer.toString(customer.getCellPhone()), customer.getEmail()} );
			tableItem.setData(customer);
		}
		
		// TODO when setData called, pull tableItem.txt from object instead of manually setting
			// we can add a method to Customer to array up fields
			// on second thought, this would probably require too much custom object code for each result table
			// may as well keep it with each object class, as in this code for displaying Customer objects
			// is in the Customer class

	}
	
	private void paintUnitTable(Unit[] unitResults) {
		for (Unit unit : unitResults) {
			if (unit == null) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
			tableItem.setText(new String[] {unit.getOwner(), unit.getMake(), unit.getModel(), unit.getModelName(), unit.getModelYear() == 0 ? "" : Integer.toString(unit.getModelYear()),
					Integer.toString(unit.getMileage()), unit.getColor(), unit.getVin(), unit.getNotes()});
				tableItem.setData(unit);
		}
		
		// TODO when setData called, pull tableItem.txt from object instead of manually setting
			// we can add a method to Customer to array up fields
			// on second thought, this would probably require too much custom object code for each result table
			// may as well keep it with each object class, as in this code for displaying Customer objects
			// is in the Customer class
	
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
