package cis2901c.objects;

import java.text.Collator;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class MyCustomerTable extends MyTable{
	
	public final int FIRST_NAME_INDEX = 0;
	public final int LAST_NAME_INDEX = 1;
	public final int ADDRESS_INDEX = 2;
	public final int CITY_INDEX = 3;
	public final int STATE_INDEX = 4;
	public final int ZIP_INDEX = 5;
	public final int HOME_PHONE_INDEX = 6;
	public final int CELL_PHONE_INDEX = 7;
	public final int EMAIL_INDEX = 8;
	private int currentSortDirection = SWT.UP;
	private int currentSortedColumn = LAST_NAME_INDEX;

	public MyCustomerTable(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	public void paint(Object[] customerResults) {
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
		
		initialSortOnPaint(LAST_NAME_INDEX, currentSortDirection);
	}
	
	private void initialSortOnPaint(int sortColumn, int sortDirection) {
		// sort table results by 0-indexed column
		TableItem[] items = this.getItems();
		Collator collator = Collator.getInstance(Locale.getDefault());
		TableColumn column = this.getColumn(sortColumn);
		int index = sortColumn;
		for (int i = 1; i < items.length; i++) {
			String value1 = items[i].getText(index);
			for (int j = 0; j < i; j++) {
				String value2 = items[j].getText(index);
				if (sortDirection ==  SWT.UP && collator.compare(value1, value2) < 0) {
					String[] values = { items[i].getText(FIRST_NAME_INDEX), items[i].getText(LAST_NAME_INDEX),
							items[i].getText(ADDRESS_INDEX), items[i].getText(CITY_INDEX), items[i].getText(STATE_INDEX),
								items[i].getText(ZIP_INDEX), items[i].getText(HOME_PHONE_INDEX),
								items[i].getText(CELL_PHONE_INDEX), items[i].getText(EMAIL_INDEX) };
					items[i].dispose();
					TableItem item = new TableItem(this, SWT.NONE, j);
					item.setText(values);
					items = this.getItems();
					break;
				} else if (sortDirection ==  SWT.DOWN && collator.compare(value1, value2) > 0) {
					String[] values = { items[i].getText(FIRST_NAME_INDEX), items[i].getText(LAST_NAME_INDEX),
							items[i].getText(ADDRESS_INDEX), items[i].getText(CITY_INDEX), items[i].getText(STATE_INDEX),
								items[i].getText(ZIP_INDEX), items[i].getText(HOME_PHONE_INDEX),
								items[i].getText(CELL_PHONE_INDEX), items[i].getText(EMAIL_INDEX) };
					items[i].dispose();
					TableItem item = new TableItem(this, SWT.NONE, j);
					item.setText(values);
					items = this.getItems();
					break;
				}
			}
		}
		this.setSortColumn(column);
		this.setSortDirection(sortDirection);
	}
	
	@Override
	public void sort(int sortColumn) {
		if (currentSortedColumn == sortColumn && currentSortDirection == SWT.UP) {
			currentSortDirection = SWT.DOWN;
		} else if (currentSortedColumn == sortColumn && currentSortDirection == SWT.DOWN) {
			currentSortDirection = SWT.UP;
		} else {
			currentSortedColumn = sortColumn;
			currentSortDirection = SWT.UP;
		}
		initialSortOnPaint(currentSortedColumn, currentSortDirection);
	}

}
