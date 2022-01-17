package cis2901c;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;
import cis2901c.listeners.CustomerSearchBoxListeners;
import cis2901c.listeners.NewCustomerButtonListeners;
import cis2901c.listeners.TextBoxFocusListener;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Gui extends Composite {
	private MyText roSearchBox;
	public List listOfRos = null;
	private Table roTable;
	private MyText customerSearchBox;
	private Table customerTable;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Gui(Composite parent, int style) {
		super(parent, style);
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setBounds(10, 10, 1004, 748);
		
		TabItem tbtmRepairOrders = new TabItem(tabFolder, SWT.NONE);
		tbtmRepairOrders.setText("Repair Orders");
		
		Composite repairOrderListing = new Composite(tabFolder, SWT.NONE);
		tbtmRepairOrders.setControl(repairOrderListing);
		
		listOfRos = new List(repairOrderListing, SWT.BORDER);
		listOfRos.setBounds(10, 74, 976, 130);

		roTable = new Table(repairOrderListing, SWT.BORDER | SWT.FULL_SELECTION);
		roTable.setBounds(10, 210, 976, 495);
		roTable.setHeaderVisible(true);
		roTable.setLinesVisible(true);
		
		roSearchBox = new MyText(repairOrderListing, SWT.BORDER);
		roSearchBox.setText("Search...");
		roSearchBox.setBounds(10, 10, 830, 26);
		RoSearchBoxListeners roSearchBoxListener = new RoSearchBoxListeners(roSearchBox, roTable);
		TextBoxFocusListener testBoxFocusListener = new TextBoxFocusListener(roSearchBox);
		roSearchBox.addModifyListener(roSearchBoxListener);
		roSearchBox.addFocusListener(testBoxFocusListener);
		
		Button btnNewRepairOrder = new Button(repairOrderListing, SWT.NONE);
		btnNewRepairOrder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewRepairOrder.setBounds(846, 10, 140, 26);
		btnNewRepairOrder.setText("New Repair Order");
		
		Button btnNewEstimate = new Button(repairOrderListing, SWT.NONE);
		btnNewEstimate.setBounds(846, 42, 140, 26);
		btnNewEstimate.setText("New Estimate");
		
		Button btnDeleteRepairOrder = new Button(repairOrderListing, SWT.NONE);
		btnDeleteRepairOrder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnDeleteRepairOrder.setText("Delete Repair Order");
		btnDeleteRepairOrder.setBounds(700, 42, 140, 26);
		
		Button btnShowCashiered = new Button(repairOrderListing, SWT.CHECK);
		btnShowCashiered.setBounds(10, 42, 129, 20);
		btnShowCashiered.setText("Show Cashiered");
		
		Button btnShowEstimates = new Button(repairOrderListing, SWT.CHECK);
		btnShowEstimates.setBounds(145, 42, 127, 20);
		btnShowEstimates.setText("Show Estimates");
		
		TableColumn tblclmnRo = new TableColumn(roTable, SWT.NONE);
		tblclmnRo.setWidth(75);
		tblclmnRo.setText("RO #");
		
		TableColumn tblclmnCustomer = new TableColumn(roTable, SWT.NONE);
		tblclmnCustomer.setWidth(164);
		tblclmnCustomer.setText("Customer");
		
		TableColumn tblclmnYear = new TableColumn(roTable, SWT.NONE);
		tblclmnYear.setWidth(65);
		tblclmnYear.setText("Year");
		
		TableColumn tblclmnMake = new TableColumn(roTable, SWT.NONE);
		tblclmnMake.setWidth(109);
		tblclmnMake.setText("Make");
		
		TableColumn tblclmnModel = new TableColumn(roTable, SWT.NONE);
		tblclmnModel.setWidth(113);
		tblclmnModel.setText("Model");
		
		TableColumn tblclmnVin = new TableColumn(roTable, SWT.NONE);
		tblclmnVin.setWidth(222);
		tblclmnVin.setText("VIN");
		
		TableColumn tblclmnJobs = new TableColumn(roTable, SWT.NONE);
		tblclmnJobs.setWidth(212);
		tblclmnJobs.setText("Jobs");
		
		TabItem tbtmParts = new TabItem(tabFolder, SWT.NONE);
		tbtmParts.setText("Parts");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmParts.setControl(composite);
		
		TabItem tbtmCustomers = new TabItem(tabFolder, SWT.NONE);
		tbtmCustomers.setText("Customers");
		
		Composite customerListing = new Composite(tabFolder, SWT.NONE);
		tbtmCustomers.setControl(customerListing);
	
		Button btnNewCustomer = new Button(customerListing, SWT.NONE);
		NewCustomerButtonListeners newCustomerButtonListeners = new NewCustomerButtonListeners();
		btnNewCustomer.addMouseListener(newCustomerButtonListeners);
		btnNewCustomer.setBounds(877, 10, 109, 26);
		btnNewCustomer.setText("New Customer");
		
		customerTable = new Table(customerListing, SWT.BORDER | SWT.FULL_SELECTION);
		customerTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// open saved customer for editing
				System.out.println("Double click");
				Customer customer = (Customer) customerTable.getSelection()[0].getData();
				System.out.println(customer.getFirstName());
				System.out.println(customer.getCustomerId());
				
				NewCustomerDialog modifyExistingCustomerDialog = new NewCustomerDialog(Main.getShell(), SWT.NONE);
				modifyExistingCustomerDialog.open(customer);
				// TODO get table to refresh on Customer Save
			}
		});
		customerTable.setBounds(10, 42, 976, 663);
		customerTable.setHeaderVisible(true);
		customerTable.setLinesVisible(true);
		
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

		customerSearchBox = new MyText(customerListing, SWT.BORDER);
		customerSearchBox.setText("Search...");
		customerSearchBox.setBounds(10, 10, 861, 26);
		CustomerSearchBoxListeners customerSearchBoxListener = new CustomerSearchBoxListeners(customerSearchBox, customerTable);
		testBoxFocusListener = new TextBoxFocusListener(customerSearchBox);
		customerSearchBox.addModifyListener(customerSearchBoxListener);
		customerSearchBox.addFocusListener(testBoxFocusListener);
		
		TabItem tbtmUnits = new TabItem(tabFolder, SWT.NONE);
		tbtmUnits.setText("Units");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmUnits.setControl(composite_2);
		
		TabItem tbtmReports = new TabItem(tabFolder, SWT.NONE);
		tbtmReports.setText("Reports");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmReports.setControl(composite_3);
		
		Label lblNotImplimentedYet = new Label(composite_3, SWT.NONE);
		lblNotImplimentedYet.setBounds(10, 10, 147, 20);
		lblNotImplimentedYet.setText("Not Implimented Yet...");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

//class TextBoxFocusListener implements FocusListener {				//SWT imple
//	private MyText txtBox;
//	private String textBoxText;
//	
//	public TextBoxFocusListener(MyText textBox) {
//		this.txtBox = textBox;
//		this.textBoxText = textBox.getText();
//	}
//	
//	@Override
//	public void focusGained(FocusEvent e) {
//		System.out.println(textBoxText + " box focused gained");
//		if (txtBox.getText().equals(textBoxText)) {
//			txtBox.setText("");
//		}		
//	}
//
//	@Override
//	public void focusLost(FocusEvent e) {
//		System.out.println(textBoxText + " box focused lost");
//		if (txtBox.getText().equals("")) {
//			txtBox.setText(textBoxText);
////			txtBox.setModified(false);
////		} else {
////			txtBox.setModified(true);
//		}
//	}
//}

//class InfoTextBoxModifyListener implements ModifyListener {			// SWT implementation
//	
//	private MyText text;
//	private String textBoxText;
//	
//	public InfoTextBoxModifyListener(MyText text) {
//		// TODO Auto-generated constructor stub
//		this.text = text;
//		this.textBoxText = text.getText();
//	}
//
//	@Override
//	public void modifyText(ModifyEvent e) {
//		if (text.getText().length() > 0 && !text.getText().equals(textBoxText)) {
//			text.setModified(true);
//		} else {
//			text.setModified(false);
//		}	
//	}
//}

//class MyText extends Text {
//	
//	private boolean modified = false;
//
//	public MyText(Composite parent, int style) {
//		super(parent, style);
//		// TODO Auto-generated constructor stub
//	}
//	
//	public boolean isModified() {
//		return modified;
//	}
//
//	public void setModified(boolean modified) {
//		this.modified = modified;
//	}
//
//	@Override
//	protected void checkSubclass() {
//		// Disable the check that prevents subclassing of SWT components
//	}	
//}