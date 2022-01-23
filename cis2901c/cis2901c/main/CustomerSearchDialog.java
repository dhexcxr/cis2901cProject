package cis2901c.main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;

import cis2901c.listeners.SearchTextBoxListeners;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.objects.MyTable;
import cis2901c.objects.MyText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

// TODO copy most of Customer tab into here, i.e. search and table display, when a selection is made in table
		// return Customer object to insert data into RO or unit info 

public class CustomerSearchDialog extends Dialog {

	protected Object result;
	protected Shell shlCustomerSearch;
	private MyTable customerTable;
	private MyText customerSearchTextBox;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CustomerSearchDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlCustomerSearch.open();
		shlCustomerSearch.layout();
		Display display = getParent().getDisplay();
		while (!shlCustomerSearch.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public Object open(String owner) {
		createContents();
		
		customerSearchTextBox.setText(owner);
		
		shlCustomerSearch.open();
		shlCustomerSearch.layout();
		Display display = getParent().getDisplay();
		while (!shlCustomerSearch.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlCustomerSearch = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlCustomerSearch.setSize(946, 410);
		shlCustomerSearch.setText("Customer Search");
		
		Button btnSelectCustomer = new Button(shlCustomerSearch, SWT.NONE);
		btnSelectCustomer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (customerTable.getSelection().length > 0) {
					result =  customerTable.getSelection()[0].getData();
					shlCustomerSearch.dispose();
				}
			}
		});
		btnSelectCustomer.setText("Select Customer");
		btnSelectCustomer.setBounds(10, 330, 256, 26);
		
		customerTable = new MyTable(shlCustomerSearch, SWT.BORDER | SWT.FULL_SELECTION);
		customerTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (customerTable.getSelection().length > 0) {
					result =  customerTable.getSelection()[0].getData();
					shlCustomerSearch.dispose();
				}
			}
		});
		customerTable.setLinesVisible(true);
		customerTable.setHeaderVisible(true);
		customerTable.setBounds(10, 42, 909, 282);
		
		TableColumn tblclmnFirstName = new TableColumn(customerTable, SWT.NONE);
		tblclmnFirstName.setWidth(100);
		tblclmnFirstName.setText("First Name");
		
		TableColumn tblclmnLastName = new TableColumn(customerTable, SWT.NONE);
		tblclmnLastName.setWidth(100);
		tblclmnLastName.setText("Last Name");
		
		TableColumn tblclmnAddress = new TableColumn(customerTable, SWT.NONE);
		tblclmnAddress.setWidth(100);
		tblclmnAddress.setText("Address");
		
		TableColumn tblclmnCity = new TableColumn(customerTable, SWT.NONE);
		tblclmnCity.setWidth(100);
		tblclmnCity.setText("City");
		
		TableColumn tblclmnState = new TableColumn(customerTable, SWT.NONE);
		tblclmnState.setWidth(100);
		tblclmnState.setText("State");
		
		TableColumn tblclmnZip = new TableColumn(customerTable, SWT.NONE);
		tblclmnZip.setWidth(100);
		tblclmnZip.setText("Zip");
		
		TableColumn tblclmnHomePhone = new TableColumn(customerTable, SWT.NONE);
		tblclmnHomePhone.setWidth(100);
		tblclmnHomePhone.setText("Home Phone");
		
		TableColumn tblclmnCellPhone = new TableColumn(customerTable, SWT.NONE);
		tblclmnCellPhone.setWidth(100);
		tblclmnCellPhone.setText("Cell Phone");
		
		TableColumn tblclmnEmail = new TableColumn(customerTable, SWT.NONE);
		tblclmnEmail.setWidth(100);
		tblclmnEmail.setText("E-mail");
		
		customerSearchTextBox = new MyText(shlCustomerSearch, SWT.BORDER);
		customerSearchTextBox.setText("Search...");
		customerSearchTextBox.setBounds(10, 10, 909, 26);
		customerSearchTextBox.addModifyListener(new SearchTextBoxListeners(customerSearchTextBox, customerTable));
		customerSearchTextBox.addFocusListener(new TextBoxFocusListener(customerSearchTextBox));
		customerSearchTextBox.setFocus();
		
		Button btnCancel = new Button(shlCustomerSearch, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// close dialog
				shlCustomerSearch.close();
			}
		});
		btnCancel.setBounds(662, 330, 256, 26);
		btnCancel.setText("Cancel");

	}
}
