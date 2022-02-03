package cis2901c.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;

import cis2901c.listeners.CreateNewObjectListener;
import cis2901c.listeners.CustomerSearchListener;
import cis2901c.listeners.DeleteLineItemListener;
import cis2901c.listeners.InfoTextBoxModifyListener;
import cis2901c.listeners.OpenExistingObjectMouseListener;
import cis2901c.listeners.PartInvoiceEditorEventListener;
import cis2901c.listeners.RequiredTextBoxModifyListener;
import cis2901c.listeners.RoSearchBoxListeners;
import cis2901c.listeners.SearchTextBoxListeners;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.objects.MyCustomerTable;
import cis2901c.objects.MyPartInventoryTable;
import cis2901c.objects.MyPartInvoiceTable;
import cis2901c.objects.MyTable;
import cis2901c.objects.MyText;
import cis2901c.objects.MyUnitTable;
import cis2901c.objects.Part;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.custom.TableEditor;
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
	private MyText txtInvoiceNotes;
	private Text textCategory_Invoice;
	private Text textSupplier_Invoice;
	private Text textNotes_Invoice;
	private MyTable partTable_Invoice;
	
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
		
		MyTable partTable_inventory = new MyPartInventoryTable(inventoryComposite, SWT.BORDER | SWT.FULL_SELECTION);
		partTable_inventory.setLinesVisible(true);
		partTable_inventory.setHeaderVisible(true);
		partTable_inventory.setBounds(10, 42, 948, 610);
		partTable_inventory.addMouseListener(new OpenExistingObjectMouseListener(partTable_inventory, shell));
		
		TableColumn tblclmnPartNumber_Inventory = new TableColumn(partTable_inventory, SWT.NONE);
		tblclmnPartNumber_Inventory.setWidth(126);
		tblclmnPartNumber_Inventory.setText("Part Number");
		
		TableColumn tblclmnDescription_Inventory = new TableColumn(partTable_inventory, SWT.NONE);
		tblclmnDescription_Inventory.setWidth(275);
		tblclmnDescription_Inventory.setText("Description");
		
		TableColumn tblclmnOnHand_Inventory = new TableColumn(partTable_inventory, SWT.NONE);
		tblclmnOnHand_Inventory.setWidth(100);
		tblclmnOnHand_Inventory.setText("On Hand");
		
		TableColumn tblclmnSupplier_Inventory = new TableColumn(partTable_inventory, SWT.NONE);
		tblclmnSupplier_Inventory.setWidth(109);
		tblclmnSupplier_Inventory.setText("Supplier");
		
		TableColumn tblclmnCategory_Inventory = new TableColumn(partTable_inventory, SWT.NONE);
		tblclmnCategory_Inventory.setWidth(115);
		tblclmnCategory_Inventory.setText("Category");
		
		TableColumn tblclmnCost_Inventory = new TableColumn(partTable_inventory, SWT.NONE);
		tblclmnCost_Inventory.setWidth(100);
		tblclmnCost_Inventory.setText("Cost");
		
		TableColumn tblclmnRetailPrice_Inventory = new TableColumn(partTable_inventory, SWT.NONE);
		tblclmnRetailPrice_Inventory.setWidth(110);
		tblclmnRetailPrice_Inventory.setText("Retail Price");
		
		// Inventory controls
		MyText partSearchTextBox = new MyText(inventoryComposite, SWT.BORDER);
		partSearchTextBox.setText("Search...");
		partSearchTextBox.setBounds(10, 10, 861, 26);
		partSearchTextBox.addModifyListener(new SearchTextBoxListeners(partSearchTextBox, partTable_inventory));
		partSearchTextBox.addFocusListener(new TextBoxFocusListener(partSearchTextBox));
		
		Button btnNewPart = new Button(inventoryComposite, SWT.NONE);
		btnNewPart.setText("New Part");
		btnNewPart.setBounds(877, 10, 81, 26);
		btnNewPart.addMouseListener(new CreateNewObjectListener(partTable_inventory, partSearchTextBox, shell));
		// END Inventory tab
		
		// START Invoice tab
		TabItem tbtmInvoice = new TabItem(tabFolder_Parts, SWT.NONE);
		tbtmInvoice.setText("Invoice");
		
		Composite invoiceComposite = new Composite(tabFolder_Parts, SWT.NONE);
		tbtmInvoice.setControl(invoiceComposite);
		
		MyText txtCustomer_invoice = new MyText(invoiceComposite, SWT.BORDER);
		txtCustomer_invoice.setText("Customer...");
		txtCustomer_invoice.setEditable(false);
		txtCustomer_invoice.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtCustomer_invoice.setBounds(10, 10, 554, 128);
		txtCustomer_invoice.addModifyListener(new RequiredTextBoxModifyListener(txtCustomer_invoice));
		txtCustomer_invoice.addMouseListener(new CustomerSearchListener(txtCustomer_invoice));
		
		txtInvoiceNotes = new MyText(invoiceComposite, SWT.BORDER);
		txtInvoiceNotes.setText("Invoice Notes...");
		txtInvoiceNotes.setBounds(570, 10, 388, 128);
		txtInvoiceNotes.addModifyListener(new InfoTextBoxModifyListener(txtInvoiceNotes));
		txtInvoiceNotes.addFocusListener(new TextBoxFocusListener(txtInvoiceNotes));
		
		Group grpTotals = new Group(invoiceComposite, SWT.NONE);
		grpTotals.setText("Totals");
		grpTotals.setBounds(728, 501, 230, 151);
		
		Text txtPartsTotal_Invoice;
		Text txtTax_Invoice;
		Text txtFinalTotal;
		
		txtPartsTotal_Invoice = new Text(grpTotals, SWT.BORDER);
		txtPartsTotal_Invoice.setEditable(false);
		txtPartsTotal_Invoice.setBounds(87, 22, 128, 26);
		
		Label lblPartsTotal = new Label(grpTotals, SWT.NONE);
		lblPartsTotal.setBounds(10, 25, 71, 20);
		lblPartsTotal.setText("Parts Total:");
		
		txtTax_Invoice = new Text(grpTotals, SWT.BORDER);
		txtTax_Invoice.setEditable(false);
		txtTax_Invoice.setBounds(87, 54, 128, 26);
		
		Label lblTax = new Label(grpTotals, SWT.NONE);
		lblTax.setBounds(57, 57, 24, 20);
		lblTax.setText("Tax:");
		
		txtFinalTotal = new Text(grpTotals, SWT.BORDER);
		txtFinalTotal.setEditable(false);
		txtFinalTotal.setBounds(87, 86, 128, 26);
		
		Label lblFinalTotal = new Label(grpTotals, SWT.NONE);
		lblFinalTotal.setBounds(10, 89, 71, 20);
		lblFinalTotal.setText("Final Total:");
		
		Group grpSelectedPart = new Group(invoiceComposite, SWT.NONE);
		grpSelectedPart.setText("Selected Part");
		grpSelectedPart.setBounds(10, 484, 469, 168);
		
		Button btnDeleteLineItem = new Button(grpSelectedPart, SWT.NONE);
		btnDeleteLineItem.setBounds(333, 106, 126, 52);
		btnDeleteLineItem.setText("Delete Line Item");
		
		textCategory_Invoice = new Text(grpSelectedPart, SWT.BORDER);
		textCategory_Invoice.setEditable(false);
		textCategory_Invoice.setBounds(381, 66, 78, 26);
		
		textSupplier_Invoice = new Text(grpSelectedPart, SWT.BORDER);
		textSupplier_Invoice.setEditable(false);
		textSupplier_Invoice.setText("");
		textSupplier_Invoice.setBounds(381, 20, 78, 26);
		
		Label lblSupplier = new Label(grpSelectedPart, SWT.NONE);
		lblSupplier.setBounds(317, 23, 58, 20);
		lblSupplier.setText("Supplier:");
		
		Label lblCategory = new Label(grpSelectedPart, SWT.NONE);
		lblCategory.setBounds(312, 69, 63, 20);
		lblCategory.setText("Category:");
		
		textNotes_Invoice = new Text(grpSelectedPart, SWT.BORDER);
		textNotes_Invoice.setEditable(false);
		textNotes_Invoice.setBounds(10, 51, 296, 107);
		
		Label lblNotes = new Label(grpSelectedPart, SWT.NONE);
		lblNotes.setBounds(10, 23, 42, 20);
		lblNotes.setText("Notes:");
		
		partTable_Invoice = new MyPartInvoiceTable(invoiceComposite, SWT.BORDER | SWT.FULL_SELECTION);
		partTable_Invoice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				partTable_Invoice.getItemCount();
				partTable_Invoice.getSelectionIndex();
				if (partTable_Invoice.getSelectionIndex() >= 0 &&
						partTable_Invoice.getSelectionIndex() < partTable_Invoice.getItemCount() &&
							partTable_Invoice.getItem(partTable_Invoice.getSelectionIndex()).getData() != null) {
					Part selectedPart = (Part) partTable_Invoice.getItem(partTable_Invoice.getSelectionIndex()).getData();
					textCategory_Invoice.setText(selectedPart.getCategory());
					textSupplier_Invoice.setText(selectedPart.getSupplier());
					textNotes_Invoice.setText(selectedPart.getNotes());
				} else {
					textCategory_Invoice.setText("");
					textSupplier_Invoice.setText("");
					textNotes_Invoice.setText("");
				}
			}
		});
		
		final TableEditor editor = new TableEditor(partTable_Invoice);		// TODO why am I building this out here
	    editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	    partTable_Invoice.addListener(SWT.MouseDown, new PartInvoiceEditorEventListener(	// and passing it in here
	    		partTable_Invoice, editor, txtPartsTotal_Invoice, txtTax_Invoice, txtFinalTotal));
	    
		partTable_Invoice.setLinesVisible(true);
		partTable_Invoice.setHeaderVisible(true);
		partTable_Invoice.setBounds(10, 144, 948, 334);
		
		TableColumn tblclmnPartNumber_Invoice = new TableColumn(partTable_Invoice, SWT.NONE);
		tblclmnPartNumber_Invoice.setWidth(126);
		tblclmnPartNumber_Invoice.setText("Part Number");
		
		TableColumn tblclmnDescription_Invoice = new TableColumn(partTable_Invoice, SWT.NONE);
		tblclmnDescription_Invoice.setWidth(275);
		tblclmnDescription_Invoice.setText("Description");
		
		TableColumn tblclmnQuantity_Invoice = new TableColumn(partTable_Invoice, SWT.NONE);
		tblclmnQuantity_Invoice.setWidth(100);
		tblclmnQuantity_Invoice.setText("Quantity");
		
		TableColumn tblclmnOnHand = new TableColumn(partTable_Invoice, SWT.NONE);
		tblclmnOnHand.setWidth(100);
		tblclmnOnHand.setText("On Hand");
		
		TableColumn tblclmnCost_Parts_1 = new TableColumn(partTable_Invoice, SWT.NONE);
		tblclmnCost_Parts_1.setWidth(100);
		tblclmnCost_Parts_1.setText("Cost");
		
		TableColumn tblclmnPartPrice_Parts = new TableColumn(partTable_Invoice, SWT.NONE);
		tblclmnPartPrice_Parts.setWidth(110);
		tblclmnPartPrice_Parts.setText("Part Price");
		
		TableColumn tblclmnExtendedPrice_Invoice = new TableColumn(partTable_Invoice, SWT.NONE);
		tblclmnExtendedPrice_Invoice.setWidth(114);
		tblclmnExtendedPrice_Invoice.setText("Extended Price");
		
		@SuppressWarnings("unused")				// this adds a new, empty TableItem at the end of the Invoice Line Items
		TableItem tableItem = new TableItem(partTable_Invoice, SWT.NONE);	// so we can add parts
		
		Button btnCashier = new Button(invoiceComposite, SWT.NONE);
		btnCashier.setBounds(596, 501, 126, 100);
		btnCashier.setText("Cashier");
		
		Button btnCancel = new Button(invoiceComposite, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO clear entire Invoice Tab
				txtCustomer_invoice.setData(null);
				txtCustomer_invoice.setText("Customer...");
				txtInvoiceNotes.setText("Invoice Notes...");
				txtPartsTotal_Invoice.setText("0.00");
				txtTax_Invoice.setText("0.00");
				txtFinalTotal.setText("0.00");
				textCategory_Invoice.setText("");
				textSupplier_Invoice.setText("");
				textNotes_Invoice.setText("");
				partTable_Invoice.removeAll();
				@SuppressWarnings("unused")				// this adds a new, empty TableItem at the end of the Invoice Line Items
				TableItem tableItem = new TableItem(partTable_Invoice, SWT.NONE);	// so we can add parts
			}
		});
		btnCancel.setBounds(596, 607, 126, 45);
		btnCancel.setText("Cancel");
		
		btnDeleteLineItem.addMouseListener(new DeleteLineItemListener(
				partTable_Invoice, txtPartsTotal_Invoice, txtTax_Invoice, txtFinalTotal));
		// END Invoice Tab
		
		// START Order tab
		TabItem tbtmOrder = new TabItem(tabFolder_Parts, SWT.NONE);
		tbtmOrder.setText("Order");
		
		Composite orderComposite = new Composite(tabFolder_Parts, SWT.NONE);
		tbtmOrder.setControl(orderComposite);
		// END Order Tab
	}
	
	private void customersTab(TabFolder tabFolder) {
		TabItem tbtmCustomers = new TabItem(tabFolder, SWT.NONE);
		tbtmCustomers.setText("Customers");

		Composite customerComposite = new Composite(tabFolder, SWT.NONE);
		tbtmCustomers.setControl(customerComposite);

		// Table for Customer search results
		customerTable = new MyCustomerTable(customerComposite, SWT.BORDER | SWT.FULL_SELECTION);
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
		btnNewCustomer.addMouseListener(new CreateNewObjectListener(customerTable, customerSearchTextBox, shell));
	}
	
	private void unitsTab(TabFolder tabFolder) {
		TabItem tbtmUnits = new TabItem(tabFolder, SWT.NONE);
		tbtmUnits.setText("Units");

		Composite unitsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmUnits.setControl(unitsComposite);

		// Table for Unit search results
		unitTable = new MyUnitTable(unitsComposite, SWT.BORDER | SWT.FULL_SELECTION);
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
		btnNewUnit.addMouseListener(new CreateNewObjectListener(unitTable, unitSearchBox, shell));
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

	public static void setDialogAtCenter(Shell dialogShell) {
		Rectangle parentRec = dialogShell.getParent().getBounds();
		Point parentCenter = new Point(parentRec.x + parentRec.width / 2, parentRec.y + parentRec.height / 2);
		dialogShell.setLocation(parentCenter.x - dialogShell.getBounds().width / 2,
				parentCenter.y - dialogShell.getBounds().height / 2);
	}
}