package cis2901c.main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;

import cis2901c.listeners.SearchTextBoxListeners;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.objects.MyCustomerTable;
import cis2901c.objects.MyText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;


public class CustomerSearchDialog extends Dialog {

	protected Object result;
	protected Shell shlCustomerSearch;
	private MyCustomerTable customerTable;
	private MyText customerSearchTextBox;
	
	public CustomerSearchDialog(Shell parent, int style) {
		super(parent, style);
	}

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
		
		// populate Search Text Box with selected Owner
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

	private void createContents() {
		shlCustomerSearch = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlCustomerSearch.setText("Customer Search");
		shlCustomerSearch.setSize(946, 410);
		
		Gui.setDialogAtCenter(shlCustomerSearch);
						
		// search results table
		customerTable = new MyCustomerTable(shlCustomerSearch, SWT.BORDER | SWT.FULL_SELECTION);
		customerTable.setBounds(10, 42, 909, 282);
		customerTable.setLinesVisible(true);
		customerTable.setHeaderVisible(true);
		customerTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (customerTable.getSelection().length > 0) {
					// return Customer selected from Customer Search Dialog table on double click
					result =  customerTable.getSelection()[0].getData();
					shlCustomerSearch.close();
				}
			}
		});
		
		TableColumn tblclmnFirstName = new TableColumn(customerTable, SWT.NONE);
		tblclmnFirstName.setText("First Name");
		tblclmnFirstName.setWidth(100);
		
		TableColumn tblclmnLastName = new TableColumn(customerTable, SWT.NONE);
		tblclmnLastName.setText("Last Name");
		tblclmnLastName.setWidth(100);
		
		TableColumn tblclmnAddress = new TableColumn(customerTable, SWT.NONE);
		tblclmnAddress.setText("Address");
		tblclmnAddress.setWidth(100);
		
		TableColumn tblclmnCity = new TableColumn(customerTable, SWT.NONE);
		tblclmnCity.setText("City");
		tblclmnCity.setWidth(100);
		
		TableColumn tblclmnState = new TableColumn(customerTable, SWT.NONE);
		tblclmnState.setText("State");
		tblclmnState.setWidth(100);
		
		TableColumn tblclmnZip = new TableColumn(customerTable, SWT.NONE);
		tblclmnZip.setText("Zip");
		tblclmnZip.setWidth(100);
		
		TableColumn tblclmnHomePhone = new TableColumn(customerTable, SWT.NONE);
		tblclmnHomePhone.setText("Home Phone");
		tblclmnHomePhone.setWidth(100);
		
		TableColumn tblclmnCellPhone = new TableColumn(customerTable, SWT.NONE);
		tblclmnCellPhone.setText("Cell Phone");
		tblclmnCellPhone.setWidth(100);
		
		TableColumn tblclmnEmail = new TableColumn(customerTable, SWT.NONE);
		tblclmnEmail.setText("E-mail");
		tblclmnEmail.setWidth(100);
		
		customerSearchTextBox = new MyText(shlCustomerSearch, SWT.BORDER);
		customerSearchTextBox.setText("Search...");
		customerSearchTextBox.setBounds(10, 10, 909, 26);
		customerSearchTextBox.setFocus();
		customerSearchTextBox.addModifyListener(new SearchTextBoxListeners(customerSearchTextBox, customerTable));
		customerSearchTextBox.addFocusListener(new TextBoxFocusListener(customerSearchTextBox));

		// dialog  controls
		Button btnSelectCustomer = new Button(shlCustomerSearch, SWT.NONE);
		btnSelectCustomer.setText("Select Customer");
		btnSelectCustomer.setBounds(10, 330, 256, 26);
		btnSelectCustomer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (customerTable.getSelection().length > 0) {
					result =  customerTable.getSelection()[0].getData();
					shlCustomerSearch.close();
				}
			}
		});

		Button btnCancel = new Button(shlCustomerSearch, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(662, 330, 256, 26);
		shlCustomerSearch.setTabList(new Control[]{customerSearchTextBox, customerTable, btnSelectCustomer, btnCancel});
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// close dialog
				shlCustomerSearch.close();
			}
		});
	}
}
