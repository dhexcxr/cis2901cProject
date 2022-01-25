package cis2901c.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;

import cis2901c.listeners.OpenExistingObjectMouseListener;
import cis2901c.listeners.RoSearchBoxListeners;
import cis2901c.listeners.SearchTextBoxListeners;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.objects.MyTable;
import cis2901c.objects.MyText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Gui extends Composite {

	// TODO why are these out here? maybe move them into methods where they are used
	private Shell shell;
	private MyText roSearchBox;
	private MyTable roTable;
	private MyText customerSearchTextBox;
	private MyTable customerTable;
	private MyTable unitTable;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Gui(Composite parent, int style) {
		super(parent, style);
		
		this.shell = (Shell) parent;

		// Tab control widget
		TabFolder tabFolder_Gui = new TabFolder(this, SWT.NONE);
		tabFolder_Gui.setBounds(10, 10, 1004, 748);

		rosTab(tabFolder_Gui);
		partsTab(tabFolder_Gui);
		customersTab(tabFolder_Gui);
		unitsTab(tabFolder_Gui);
		reportsTab(tabFolder_Gui);
	}
	
	private void rosTab(TabFolder tabFolder) {
		TabItem tbtmRepairOrders = new TabItem(tabFolder, SWT.NONE);
		tbtmRepairOrders.setText("Repair Orders");

		Composite repairOrdersComposite = new Composite(tabFolder, SWT.NONE);
		tbtmRepairOrders.setControl(repairOrdersComposite);

		// Table for RO search results
		roTable = new MyTable(repairOrdersComposite, SWT.BORDER | SWT.FULL_SELECTION);
		roTable.setBounds(10, 74, 976, 631);
		roTable.setHeaderVisible(true);
		roTable.setLinesVisible(true);

		TableColumn tblclmnRo_Ro = new TableColumn(roTable, SWT.NONE);
		tblclmnRo_Ro.setText("RO #");
		tblclmnRo_Ro.setWidth(75);

		TableColumn tblclmnCustomer_Ro = new TableColumn(roTable, SWT.NONE);
		tblclmnCustomer_Ro.setText("Customer");
		tblclmnCustomer_Ro.setWidth(164);

		TableColumn tblclmnYear_Ro = new TableColumn(roTable, SWT.NONE);
		tblclmnYear_Ro.setText("Year");
		tblclmnYear_Ro.setWidth(65);

		TableColumn tblclmnMake_Ro = new TableColumn(roTable, SWT.NONE);
		tblclmnMake_Ro.setText("Make");
		tblclmnMake_Ro.setWidth(109);

		TableColumn tblclmnModel_Ro = new TableColumn(roTable, SWT.NONE);
		tblclmnModel_Ro.setText("Model");
		tblclmnModel_Ro.setWidth(113);

		TableColumn tblclmnVin_Ro = new TableColumn(roTable, SWT.NONE);
		tblclmnVin_Ro.setText("VIN");
		tblclmnVin_Ro.setWidth(222);

		TableColumn tblclmnJobs_Ro = new TableColumn(roTable, SWT.NONE);
		tblclmnJobs_Ro.setText("Jobs");
		tblclmnJobs_Ro.setWidth(212);

		// RO controls
		roSearchBox = new MyText(repairOrdersComposite, SWT.BORDER);
		roSearchBox.setText("Search...");
		roSearchBox.setBounds(10, 10, 830, 26);
		roSearchBox.addModifyListener(new RoSearchBoxListeners(roSearchBox, roTable));
		roSearchBox.addFocusListener(new TextBoxFocusListener(roSearchBox));

		Button btnNewRepairOrder = new Button(repairOrdersComposite, SWT.NONE);
		btnNewRepairOrder.setBounds(846, 10, 140, 26);
		btnNewRepairOrder.setText("New Repair Order");
		btnNewRepairOrder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// create new Repair Order
			}
		});

		Button btnNewEstimate = new Button(repairOrdersComposite, SWT.NONE);
		btnNewEstimate.setText("New Estimate");
		btnNewEstimate.setBounds(846, 42, 140, 26);

		Button btnDeleteRepairOrder = new Button(repairOrdersComposite, SWT.NONE);
		btnDeleteRepairOrder.setText("Delete Repair Order");
		btnDeleteRepairOrder.setBounds(700, 42, 140, 26);
		btnDeleteRepairOrder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// delete Repair Order
			}
		});

		Button btnShowCashiered = new Button(repairOrdersComposite, SWT.CHECK);
		btnShowCashiered.setText("Show Cashiered");
		btnShowCashiered.setBounds(10, 42, 129, 20);

		Button btnShowEstimates = new Button(repairOrdersComposite, SWT.CHECK);
		btnShowEstimates.setText("Show Estimates");
		btnShowEstimates.setBounds(145, 42, 127, 20);
	}
	
	private void partsTab(TabFolder tabFolder) {
		TabItem tbtmParts = new TabItem(tabFolder, SWT.NONE);
		tbtmParts.setText("Parts");

		Composite partsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmParts.setControl(partsComposite);
		
		TabFolder tabFolder_Parts = new TabFolder(partsComposite, SWT.NONE);
		tabFolder_Parts.setBounds(10, 10, 976, 695);
		
		// Inventory Tab
		TabItem tbtmInventory = new TabItem(tabFolder_Parts, SWT.NONE);
		tbtmInventory.setText("Inventory");
		
		Composite inventoryComposite = new Composite(tabFolder_Parts, SWT.NONE);
		tbtmInventory.setControl(inventoryComposite);
		
		MyTable partTable = new MyTable(inventoryComposite, SWT.BORDER | SWT.FULL_SELECTION);
		partTable.setLinesVisible(true);
		partTable.setHeaderVisible(true);
		partTable.setBounds(10, 42, 948, 610);
		partTable.addMouseListener(new OpenExistingObjectMouseListener(partTable, shell));
		
		TableColumn tblclmnPartNumber_Parts = new TableColumn(partTable, SWT.NONE);
		tblclmnPartNumber_Parts.setWidth(126);
		tblclmnPartNumber_Parts.setText("Part Number");
		
		TableColumn tblclmnDescription_Parts = new TableColumn(partTable, SWT.NONE);
		tblclmnDescription_Parts.setWidth(275);
		tblclmnDescription_Parts.setText("Description");
		
		TableColumn tblclmnOnHand_Parts = new TableColumn(partTable, SWT.NONE);
		tblclmnOnHand_Parts.setWidth(100);
		tblclmnOnHand_Parts.setText("On Hand");
		
		TableColumn tblclmnSupplier_Parts = new TableColumn(partTable, SWT.NONE);
		tblclmnSupplier_Parts.setWidth(109);
		tblclmnSupplier_Parts.setText("Supplier");
		
		TableColumn tblclmnCategory_Parts = new TableColumn(partTable, SWT.NONE);
		tblclmnCategory_Parts.setWidth(115);
		tblclmnCategory_Parts.setText("Category");
		
		TableColumn tblclmnCost_Parts = new TableColumn(partTable, SWT.NONE);
		tblclmnCost_Parts.setWidth(100);
		tblclmnCost_Parts.setText("Cost");
		
		TableColumn tblclmnRetailPrice_Parts = new TableColumn(partTable, SWT.NONE);
		tblclmnRetailPrice_Parts.setWidth(110);
		tblclmnRetailPrice_Parts.setText("Retail Price");
		
		// Inventory controls
		MyText partSearchTextBox = new MyText(inventoryComposite, SWT.BORDER);
		partSearchTextBox.setText("Search...");
		partSearchTextBox.setBounds(10, 10, 861, 26);
		partSearchTextBox.addModifyListener(new SearchTextBoxListeners(partSearchTextBox, partTable));
		partSearchTextBox.addFocusListener(new TextBoxFocusListener(partSearchTextBox));
		
		Button btnNewPart = new Button(inventoryComposite, SWT.NONE);
		btnNewPart.setText("New Part");
		btnNewPart.setBounds(877, 10, 81, 26);
		btnNewPart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				NewPartDialog addNewPartDialog = new NewPartDialog(shell, SWT.NONE);
				addNewPartDialog.open();
			}
		});
		// END Inventory tab
		
		TabItem tbtmInvoice = new TabItem(tabFolder_Parts, SWT.NONE);
		tbtmInvoice.setText("Invoice");
		
		Composite invoiceComposite = new Composite(tabFolder_Parts, SWT.NONE);
		tbtmInvoice.setControl(invoiceComposite);
		
		TabItem tbtmOrder = new TabItem(tabFolder_Parts, SWT.NONE);
		tbtmOrder.setText("Order");
		
		Composite orderComposite = new Composite(tabFolder_Parts, SWT.NONE);
		tbtmOrder.setControl(orderComposite);
	}
	
	private void customersTab(TabFolder tabFolder) {
		TabItem tbtmCustomers = new TabItem(tabFolder, SWT.NONE);
		tbtmCustomers.setText("Customers");

		Composite customerComposite = new Composite(tabFolder, SWT.NONE);
		tbtmCustomers.setControl(customerComposite);

		// Table for Customer search results
		customerTable = new MyTable(customerComposite, SWT.BORDER | SWT.FULL_SELECTION);
		customerTable.setBounds(10, 42, 976, 663);
		customerTable.setHeaderVisible(true);
		customerTable.setLinesVisible(true);
		customerTable.addMouseListener(new OpenExistingObjectMouseListener(customerTable, shell));

		TableColumn tblclmnFirstName_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnFirstName_Customer.setText("First Name");
		tblclmnFirstName_Customer.setWidth(100);

		TableColumn tblclmnLastName_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnLastName_Customer.setText("Last Name");
		tblclmnLastName_Customer.setWidth(100);

		TableColumn tblclmnAddress_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnAddress_Customer.setText("Address");
		tblclmnAddress_Customer.setWidth(100);

		TableColumn tblclmnCity_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnCity_Customer.setText("City");
		tblclmnCity_Customer.setWidth(100);

		TableColumn tblclmnState_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnState_Customer.setText("State");
		tblclmnState_Customer.setWidth(100);

		TableColumn tblclmnZip_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnZip_Customer.setText("Zip");
		tblclmnZip_Customer.setWidth(100);

		TableColumn tblclmnHomePhone_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnHomePhone_Customer.setText("Home Phone");
		tblclmnHomePhone_Customer.setWidth(100);

		TableColumn tblclmnCellPhone_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnCellPhone_Customer.setText("Cell Phone");
		tblclmnCellPhone_Customer.setWidth(100);

		TableColumn tblclmnEmail_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnEmail_Customer.setText("E-mail");
		tblclmnEmail_Customer.setWidth(100);

		// Customer controls
		customerSearchTextBox = new MyText(customerComposite, SWT.BORDER);
		customerSearchTextBox.setText("Search...");
		customerSearchTextBox.setBounds(10, 10, 861, 26);
		customerSearchTextBox.addModifyListener(new SearchTextBoxListeners(customerSearchTextBox, customerTable));
		customerSearchTextBox.addFocusListener(new TextBoxFocusListener(customerSearchTextBox));

		Button btnNewCustomer = new Button(customerComposite, SWT.NONE);
		btnNewCustomer.setText("New Customer");
		btnNewCustomer.setBounds(877, 10, 109, 26);
//		btnNewCustomer.addMouseListener(new NewCustomerButtonListeners());
		btnNewCustomer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				NewCustomerDialog addNewCustomerDialog = new NewCustomerDialog(shell, SWT.NONE);
				addNewCustomerDialog.open();
			}
		});
	}
	
	private void unitsTab(TabFolder tabFolder) {
		TabItem tbtmUnits = new TabItem(tabFolder, SWT.NONE);
		tbtmUnits.setText("Units");

		Composite unitsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmUnits.setControl(unitsComposite);

		// Table for Unit search results
		unitTable = new MyTable(unitsComposite, SWT.BORDER | SWT.FULL_SELECTION);
		unitTable.addMouseListener(new OpenExistingObjectMouseListener(unitTable, shell));
		unitTable.setBounds(10, 42, 976, 663);
		unitTable.setHeaderVisible(true);
		unitTable.setLinesVisible(true);

		TableColumn tblclmnOwner_Unit = new TableColumn(unitTable, SWT.NONE);
		tblclmnOwner_Unit.setText("Owner");
		tblclmnOwner_Unit.setWidth(165);

		TableColumn tblclmnMake_Unit = new TableColumn(unitTable, SWT.NONE);
		tblclmnMake_Unit.setText("Make");
		tblclmnMake_Unit.setWidth(119);

		TableColumn tblclmnModel_Unit = new TableColumn(unitTable, SWT.NONE);
		tblclmnModel_Unit.setText("Model");
		tblclmnModel_Unit.setWidth(138);

		TableColumn tblclmnModelName_Unit = new TableColumn(unitTable, SWT.NONE);
		tblclmnModelName_Unit.setText("Model Name");
		tblclmnModelName_Unit.setWidth(148);

		TableColumn tblclmnYear_Unit = new TableColumn(unitTable, SWT.NONE);
		tblclmnYear_Unit.setText("Year");
		tblclmnYear_Unit.setWidth(50);

		TableColumn tblclmnMileage_Unit = new TableColumn(unitTable, SWT.NONE);
		tblclmnMileage_Unit.setText("Mileage");
		tblclmnMileage_Unit.setWidth(85);

		TableColumn tblclmnColor_Unit = new TableColumn(unitTable, SWT.NONE);
		tblclmnColor_Unit.setText("Color");
		tblclmnColor_Unit.setWidth(49);

		TableColumn tblclmnVin_Unit = new TableColumn(unitTable, SWT.NONE);
		tblclmnVin_Unit.setText("VIN");
		tblclmnVin_Unit.setWidth(215);

		// Unit controls
		MyText unitSearchBox = new MyText(unitsComposite, SWT.BORDER);
		unitSearchBox.setText("Search...");
		unitSearchBox.setBounds(10, 10, 861, 26);
		unitSearchBox.addModifyListener(new SearchTextBoxListeners(unitSearchBox, unitTable));
		unitSearchBox.addFocusListener(new TextBoxFocusListener(unitSearchBox));
		
		Button btnNewUnit = new Button(unitsComposite, SWT.NONE);
		btnNewUnit.setText("New Unit");
		btnNewUnit.setBounds(877, 10, 109, 26);
//		btnNewUnit.addMouseListener(new NewUnitButtonListener());
		btnNewUnit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				NewUnitDialog addNewUnitDialog = new NewUnitDialog(shell, SWT.NONE);
				addNewUnitDialog.open();
			}
		});
	}
	
	private void reportsTab(TabFolder tabFolder) {
		TabItem tbtmReports = new TabItem(tabFolder, SWT.NONE);
		tbtmReports.setText("Reports");

		Composite reportsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmReports.setControl(reportsComposite);

		Label lblNotImplimentedYet = new Label(reportsComposite, SWT.NONE);
		lblNotImplimentedYet.setText("Not Implimented Yet...");
		lblNotImplimentedYet.setBounds(10, 10, 147, 20);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}