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
		this.removeAll();
		if (objects instanceof Customer[]) {
			paintCustomerTable((Customer[]) objects);
		} else if (objects instanceof Unit[]) {
			paintUnitTable((Unit[]) objects);
		} else if (objects instanceof Part[]) {
			paintPartTable((Part[]) objects);
		}
	}
	
	private void paintCustomerTable(Customer[] customerResults) {
		// TODO probably add sorting here
		// build each TableItem to fill Customer Table
		for (Customer customer : customerResults) {
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
	
	private void paintUnitTable(Unit[] unitResults) {
		// build each TableItem to fill Unit Table
		for (Unit unit : unitResults) {
			if (unit == null) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
			tableItem.setText(new String[] {unit.getOwner(), unit.getMake(), unit.getModel(), unit.getModelName(),
					unit.getYear() == 0 ? "" : Integer.toString(unit.getYear()),
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
			tableItem.setText(new String[] {part.getPartNumber(), part.getDescription(), Integer.toString(part.getOnHand() == -1 ? 0 : part.getOnHand()),
					part.getSupplier(), part.getCategory(), part.getCost().toString(), part.getRetail().toString()});
			tableItem.setData(part);
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
