package cis2901c.objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class MyTable extends Table {

	public MyTable(Composite parent, int style) {
		super(parent, style);
	}
	
	public void paint(Object[] objects) {
		if (objects == null || objects.length == 0) {
			return;
		}
		if (objects instanceof Customer[]) {
			paintCustomerTable((Customer[]) objects);
		} else if (objects instanceof Unit[]) {
			paintUnitTable((Unit[])objects);
		} else if (objects instanceof Part[]) {
			paintPartTable((Part[]) objects);
		}
	}
	
	private void paintCustomerTable(Customer[] customerResults) {
		// build each TableItem to fill Customer Table
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
	}
	
	private void paintUnitTable(Unit[] unitResults) {
		// build each TableItem to fill Unit Table
		for (Unit unit : unitResults) {
			if (unit == null) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
			tableItem.setText(new String[] {unit.getOwner(), unit.getMake(), unit.getModel(), unit.getModelName(),
					unit.getModelYear() == 0 ? "" : Integer.toString(unit.getModelYear()),
					Integer.toString(unit.getMileage()), unit.getColor(), unit.getVin(), unit.getNotes()});
			tableItem.setData(unit);
		}
	}
	
	private void paintPartTable(Part[] partResults) {
		// build each TableItem to fill Unit Table
		for (Part part : partResults) {
			if (part == null) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
			tableItem.setText(new String[] {part.getPartNumber(), part.getDescription(), Integer.toString(part.getOnHand()),
					part.getSupplier(), part.getCategory(), part.getCost().toString(), part.getRetail().toString()});
			tableItem.setData(part);
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
