package cis2901c.main;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;

import cis2901c.listeners.CreateNewObjectListener;
import cis2901c.listeners.CustomerSearchListener;
import cis2901c.listeners.DbServices;
import cis2901c.listeners.DeleteObjectListener;
import cis2901c.listeners.InvoicePartDeleteLineItemListener;
import cis2901c.listeners.InfoTextBoxModifyListener;
import cis2901c.listeners.OpenExistingObjectMouseListener;
import cis2901c.listeners.InvoicePartTableListener;
import cis2901c.listeners.RequiredTextBoxModifyListener;
import cis2901c.listeners.SearchTextBoxListeners;
import cis2901c.listeners.TableColumnSortListener;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.objects.Customer;
import cis2901c.objects.Invoice;
import cis2901c.objects.InvoicePart;
import cis2901c.objects.CustomerTable;
import cis2901c.objects.InvoicePartTableItem;
import cis2901c.objects.PartInventoryTable;
import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.MyTable;
import cis2901c.objects.MyText;
import cis2901c.objects.UnitTable;
import cis2901c.objects.Part;
import cis2901c.objects.Unit;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Gui extends Composite {

	private Shell shell;

	public Gui(Composite parent, int style) {
		super(parent, style);
		
		this.shell = (Shell) parent;

		// Tab control widget
		TabFolder tabFolderGui = new TabFolder(this, SWT.NONE);
		tabFolderGui.setBounds(10, 10, 1004, 748);

		rosTab(tabFolderGui);
		partsTab(tabFolderGui);
		customersTab(tabFolderGui);
		unitsTab(tabFolderGui);
		reportsTab(tabFolderGui);
	}
	
	private void rosTab(TabFolder tabFolder) {
		TabItem tbtmRepairOrders = new TabItem(tabFolder, SWT.NONE);
		tbtmRepairOrders.setText("Repair Orders");

		Composite repairOrdersComposite = new Composite(tabFolder, SWT.NONE);
		tbtmRepairOrders.setControl(repairOrdersComposite);

		Label lblNotImplimentedYet = new Label(repairOrdersComposite, SWT.NONE);
		lblNotImplimentedYet.setText("Not Implimented Yet...");
		lblNotImplimentedYet.setBounds(10, 10, 147, 20);
	}
	
	private void partsTab(TabFolder tabFolder) {
		TabItem tbtmParts = new TabItem(tabFolder, SWT.NONE);
		tbtmParts.setText("Parts");

		Composite partsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmParts.setControl(partsComposite);
		
		TabFolder tabFolderParts = new TabFolder(partsComposite, SWT.NONE);
		tabFolderParts.setBounds(10, 10, 976, 695);
		
		// Inventory Tab
		TabItem tbtmInventory = new TabItem(tabFolderParts, SWT.NONE);
		tbtmInventory.setText("Inventory");
		
		Composite inventoryComposite = new Composite(tabFolderParts, SWT.NONE);
		tbtmInventory.setControl(inventoryComposite);
		
		MyTable partTableInventory = new PartInventoryTable(inventoryComposite, SWT.BORDER | SWT.FULL_SELECTION);
		partTableInventory.setLinesVisible(true);
		partTableInventory.setHeaderVisible(true);
		partTableInventory.setBounds(10, 42, 948, 610);
		partTableInventory.addMouseListener(new OpenExistingObjectMouseListener(partTableInventory, shell));
		
		TableColumn tblclmnPartNumberInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnPartNumberInventory.setWidth(126);
		tblclmnPartNumberInventory.setText("Part Number");
		tblclmnPartNumberInventory.addSelectionListener(new TableColumnSortListener(tblclmnPartNumberInventory));
		
		TableColumn tblclmnDescriptionInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnDescriptionInventory.setWidth(275);
		tblclmnDescriptionInventory.setText("Description");
		tblclmnDescriptionInventory.addSelectionListener(new TableColumnSortListener(tblclmnDescriptionInventory));
		
		TableColumn tblclmnOnHandInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnOnHandInventory.setWidth(100);
		tblclmnOnHandInventory.setText("On Hand");
		tblclmnOnHandInventory.addSelectionListener(new TableColumnSortListener(tblclmnOnHandInventory));
		
		TableColumn tblclmnSupplierInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnSupplierInventory.setWidth(109);
		tblclmnSupplierInventory.setText("Supplier");
		tblclmnSupplierInventory.addSelectionListener(new TableColumnSortListener(tblclmnSupplierInventory));
		
		TableColumn tblclmnCategoryInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnCategoryInventory.setWidth(115);
		tblclmnCategoryInventory.setText("Category");
		tblclmnCategoryInventory.addSelectionListener(new TableColumnSortListener(tblclmnCategoryInventory));
		
		TableColumn tblclmnCostInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnCostInventory.setWidth(100);
		tblclmnCostInventory.setText("Cost");
		tblclmnCostInventory.addSelectionListener(new TableColumnSortListener(tblclmnCostInventory));
		
		TableColumn tblclmnRetailPriceInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnRetailPriceInventory.setWidth(110);
		tblclmnRetailPriceInventory.setText("Retail Price");
		tblclmnRetailPriceInventory.addSelectionListener(new TableColumnSortListener(tblclmnRetailPriceInventory));
		
		// Delete right click menu
		Menu popupMenu = new Menu(partTableInventory);
		MenuItem deletePart = new MenuItem(popupMenu, SWT.NONE);
		deletePart.setText("Delete Part");
		deletePart.setEnabled(false);
		deletePart.addSelectionListener(new DeleteObjectListener(partTableInventory));
		partTableInventory.setMenu(popupMenu);
		
		// Inventory controls
		MyText partSearchTextBox = new MyText(inventoryComposite, SWT.BORDER);
		partSearchTextBox.setText("Search...");
		partSearchTextBox.setBounds(10, 10, 861, 26);
		partSearchTextBox.addModifyListener(new SearchTextBoxListeners(partSearchTextBox, partTableInventory, new Part()));
		partSearchTextBox.addFocusListener(new TextBoxFocusListener(partSearchTextBox));
		
		Button btnNewPart = new Button(inventoryComposite, SWT.NONE);
		btnNewPart.setText("New Part");
		btnNewPart.setBounds(877, 10, 81, 26);
		btnNewPart.addMouseListener(new CreateNewObjectListener(partTableInventory, partSearchTextBox, shell));
		// END Inventory tab
		
		// START Invoice tab
		TabItem tbtmInvoice = new TabItem(tabFolderParts, SWT.NONE);
		tbtmInvoice.setText("Invoice");
		
		Composite invoiceComposite = new Composite(tabFolderParts, SWT.NONE);
		tbtmInvoice.setControl(invoiceComposite);
		
		MyText txtCustomerInvoice = new MyText(invoiceComposite, SWT.BORDER | SWT.WRAP);
		txtCustomerInvoice.setText("Customer...");
		txtCustomerInvoice.setEditable(false);
		txtCustomerInvoice.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtCustomerInvoice.setBounds(10, 10, 554, 128);
		txtCustomerInvoice.addModifyListener(new RequiredTextBoxModifyListener(txtCustomerInvoice));
		txtCustomerInvoice.addMouseListener(new CustomerSearchListener(txtCustomerInvoice));
		
		MyText txtInvoiceNotes = new MyText(invoiceComposite, SWT.BORDER);
		txtInvoiceNotes.setText("Invoice Notes...");
		txtInvoiceNotes.setBounds(570, 10, 388, 128);
		txtInvoiceNotes.addModifyListener(new InfoTextBoxModifyListener(txtInvoiceNotes));
		txtInvoiceNotes.addFocusListener(new TextBoxFocusListener(txtInvoiceNotes));
		
		Group grpTotals = new Group(invoiceComposite, SWT.NONE);
		grpTotals.setText("Totals");
		grpTotals.setBounds(728, 501, 230, 151);
		
		MyText txtPartsTotalInvoice;
		MyText txtTaxInvoice;
		MyText txtFinalTotal;
		
		txtPartsTotalInvoice = new MyText(grpTotals, SWT.BORDER);
		txtPartsTotalInvoice.setEditable(false);
		txtPartsTotalInvoice.setBounds(87, 22, 128, 26);
		
		Label lblPartsTotal = new Label(grpTotals, SWT.NONE);
		lblPartsTotal.setBounds(10, 25, 71, 20);
		lblPartsTotal.setText("Parts Total:");
		
		txtTaxInvoice = new MyText(grpTotals, SWT.BORDER);
		txtTaxInvoice.setEditable(false);
		txtTaxInvoice.setBounds(87, 54, 128, 26);
		
		Label lblTax = new Label(grpTotals, SWT.NONE);
		lblTax.setBounds(57, 57, 24, 20);
		lblTax.setText("Tax:");
		
		txtFinalTotal = new MyText(grpTotals, SWT.BORDER);
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
		
		MyText textCategoryInvoice = new MyText(grpSelectedPart, SWT.BORDER);
		textCategoryInvoice.setEditable(false);
		textCategoryInvoice.setBounds(381, 66, 78, 26);
		
		MyText textSupplierInvoice = new MyText(grpSelectedPart, SWT.BORDER);
		textSupplierInvoice.setEditable(false);
		textSupplierInvoice.setText("");
		textSupplierInvoice.setBounds(381, 20, 78, 26);
		
		Label lblSupplier = new Label(grpSelectedPart, SWT.NONE);
		lblSupplier.setBounds(317, 23, 58, 20);
		lblSupplier.setText("Supplier:");
		
		Label lblCategory = new Label(grpSelectedPart, SWT.NONE);
		lblCategory.setBounds(312, 69, 63, 20);
		lblCategory.setText("Category:");
		
		MyText textPartNotesInvoice = new MyText(grpSelectedPart, SWT.BORDER);
		textPartNotesInvoice.setEditable(false);
		textPartNotesInvoice.setBounds(10, 51, 296, 107);
		
		Label lblNotes = new Label(grpSelectedPart, SWT.NONE);
		lblNotes.setBounds(10, 23, 42, 20);
		lblNotes.setText("Notes:");
		
		InvoicePartTable invoicePartsTable = new InvoicePartTable(invoiceComposite, SWT.BORDER | SWT.FULL_SELECTION);
		invoicePartsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				invoicePartsTable.getItemCount();
				invoicePartsTable.getSelectionIndex();
				if (invoicePartsTable.getSelectionIndex() >= 0 &&
						invoicePartsTable.getSelectionIndex() < invoicePartsTable.getItemCount() &&
							invoicePartsTable.getItem(invoicePartsTable.getSelectionIndex()).getData() != null) {
					Part selectedPart = ((InvoicePart) invoicePartsTable.getItem(invoicePartsTable.getSelectionIndex()).getData()).getPart();
					textCategoryInvoice.setText(selectedPart.getCategory());
					textSupplierInvoice.setText(selectedPart.getSupplier());
					textPartNotesInvoice.setText(selectedPart.getNotes());
				} else {
					textCategoryInvoice.setText("");
					textSupplierInvoice.setText("");
					textPartNotesInvoice.setText("");
				}
			}
		});
		
		List<MyText> invoiceDetailText = new ArrayList<>();
		invoiceDetailText.add(txtPartsTotalInvoice);
		invoiceDetailText.add(txtTaxInvoice);
		invoiceDetailText.add(txtFinalTotal);
		invoiceDetailText.add(textCategoryInvoice);
		invoiceDetailText.add(textSupplierInvoice);
		invoiceDetailText.add(textPartNotesInvoice);
	    invoicePartsTable.addListener(SWT.MouseDown, new InvoicePartTableListener(invoicePartsTable, invoiceDetailText, shell));
	    
		invoicePartsTable.setLinesVisible(true);
		invoicePartsTable.setHeaderVisible(true);
		invoicePartsTable.setBounds(10, 144, 948, 334);
		
		TableColumn tblclmnPartNumberInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnPartNumberInvoice.setWidth(126);
		tblclmnPartNumberInvoice.setText("Part Number");
		
		TableColumn tblclmnDescriptionInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnDescriptionInvoice.setWidth(275);
		tblclmnDescriptionInvoice.setText("Description");
		
		TableColumn tblclmnQuantityInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnQuantityInvoice.setWidth(100);
		tblclmnQuantityInvoice.setText("Quantity");
		
		TableColumn tblclmnOnHandInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnOnHandInvoice.setWidth(100);
		tblclmnOnHandInvoice.setText("On Hand");
		
		TableColumn tblclmnCostInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnCostInvoice.setWidth(100);
		tblclmnCostInvoice.setText("Cost");
		
		TableColumn tblclmnPartPriceInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnPartPriceInvoice.setWidth(110);
		tblclmnPartPriceInvoice.setText("Part Price");
		
		TableColumn tblclmnExtendedPriceInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnExtendedPriceInvoice.setWidth(114);
		tblclmnExtendedPriceInvoice.setText("Extended Price");
		
		Button btnCashier = new Button(invoiceComposite, SWT.NONE);
		btnCashier.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// spawn amount due dialog box
				if (txtCustomerInvoice.getData() == null) {
					MessageBox customerRequiredBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					customerRequiredBox.setText("Notice");
					customerRequiredBox.setMessage("Please select a Customer");
					customerRequiredBox.open();
					return;
				} else if (invoicePartsTable.getItemCount() == 1) {
					MessageBox noPartsBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					noPartsBox.setText("Notice");
					noPartsBox.setMessage("Enter a part to invoice");
					noPartsBox.open();
					return;
				}
				AmountDueDialog amountDueDialog = new AmountDueDialog(shell, getStyle());
				boolean cashiered = amountDueDialog.open(txtFinalTotal.getText());
				
				if (cashiered) {
					Invoice cashieredInvoice = new Invoice();
					cashieredInvoice.setCustomerId(((Customer) txtCustomerInvoice.getData()).getCustomerId());
					cashieredInvoice.setCustomerData(txtCustomerInvoice.getText());
					cashieredInvoice.setNotes(txtInvoiceNotes.getText());
					cashieredInvoice.setTax(new BigDecimal(txtTaxInvoice.getText().replace("$", "")));
					cashieredInvoice.setCashiereDateTime(Timestamp.from(Instant.now()));
					cashieredInvoice.setCashiered(true);
					cashieredInvoice.setTableLineItems(invoicePartsTable.getItems());
					
					// send invoice obj to DbServices
					DbServices.saveObject(cashieredInvoice);
					
					// clear entire Invoice Tab
					txtCustomerInvoice.setData(null);
					txtCustomerInvoice.setText("Customer...");
					txtCustomerInvoice.setBackground(SWTResourceManager.getColor(255, 102, 102));
					txtInvoiceNotes.setText("Invoice Notes...");
					txtPartsTotalInvoice.setText("0.00");
					txtTaxInvoice.setText("0.00");
					txtFinalTotal.setText("0.00");
					textCategoryInvoice.setText("");
					textSupplierInvoice.setText("");
					textPartNotesInvoice.setText("");
					invoicePartsTable.removeAll();
					@SuppressWarnings("unused")				// this adds a new, empty TableItem at the end of the Invoice Line Items
					TableItem tableItem = new InvoicePartTableItem(invoicePartsTable, SWT.NONE);	// so we can add parts
				}
			}
		});
		btnCashier.setBounds(596, 501, 126, 100);
		btnCashier.setText("Cashier");
		
		Button btnCancel = new Button(invoiceComposite, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// clear entire Invoice Tab
				txtCustomerInvoice.setData(null);
				txtCustomerInvoice.setText("Customer...");
				txtInvoiceNotes.setText("Invoice Notes...");
				txtPartsTotalInvoice.setText("0.00");
				txtTaxInvoice.setText("0.00");
				txtFinalTotal.setText("0.00");
				textCategoryInvoice.setText("");
				textSupplierInvoice.setText("");
				textPartNotesInvoice.setText("");
				invoicePartsTable.removeAll();
				@SuppressWarnings("unused")				// this adds a new, empty TableItem at the end of the Invoice Line Items
				TableItem tableItem = new InvoicePartTableItem(invoicePartsTable, SWT.NONE);	// so we can add parts
			}
		});
		btnCancel.setBounds(596, 607, 126, 45);
		btnCancel.setText("Cancel");
		
		btnDeleteLineItem.addMouseListener(new InvoicePartDeleteLineItemListener(
				invoicePartsTable, txtPartsTotalInvoice, txtTaxInvoice, txtFinalTotal));
		
		Button btnSearchForInvoice = new Button(invoiceComposite, SWT.NONE);
		btnSearchForInvoice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// open invoice search dialog
				InvoiceSearchDialog invoiceSearchDialog = new InvoiceSearchDialog(Display.getDefault().getActiveShell(), SWT.NONE);
				invoiceSearchDialog.open();
			}
		});
		btnSearchForInvoice.setBounds(485, 607, 105, 45);
		btnSearchForInvoice.setText("Invoice Search");
		// END Invoice Tab
		
		// START Order tab
		TabItem tbtmOrder = new TabItem(tabFolderParts, SWT.NONE);
		tbtmOrder.setText("Order");
		
		Composite orderComposite = new Composite(tabFolderParts, SWT.NONE);
		tbtmOrder.setControl(orderComposite);
		// END Order Tab
	}
	
	private void customersTab(TabFolder tabFolder) {
		TabItem tbtmCustomers = new TabItem(tabFolder, SWT.NONE);
		tbtmCustomers.setText("Customers");

		Composite customerComposite = new Composite(tabFolder, SWT.NONE);
		tbtmCustomers.setControl(customerComposite);

		// Table for Customer search results
		MyTable customerTable = new CustomerTable(customerComposite, SWT.BORDER | SWT.FULL_SELECTION);
		customerTable.setBounds(10, 42, 976, 663);
		customerTable.setHeaderVisible(true);
		customerTable.setLinesVisible(true);
		customerTable.addMouseListener(new OpenExistingObjectMouseListener(customerTable, shell));

		TableColumn tblclmnFirstNameCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnFirstNameCustomer.setText("First Name");
		tblclmnFirstNameCustomer.setWidth(100);
		tblclmnFirstNameCustomer.addSelectionListener(new TableColumnSortListener(tblclmnFirstNameCustomer));

		TableColumn tblclmnLastNameCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnLastNameCustomer.setText("Last Name");
		tblclmnLastNameCustomer.setWidth(100);
		tblclmnLastNameCustomer.addSelectionListener(new TableColumnSortListener(tblclmnLastNameCustomer));

		TableColumn tblclmnAddressCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnAddressCustomer.setText("Address");
		tblclmnAddressCustomer.setWidth(100);
		tblclmnAddressCustomer.addSelectionListener(new TableColumnSortListener(tblclmnAddressCustomer));
		

		TableColumn tblclmnCityCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnCityCustomer.setText("City");
		tblclmnCityCustomer.setWidth(100);
		tblclmnCityCustomer.addSelectionListener(new TableColumnSortListener(tblclmnCityCustomer));

		TableColumn tblclmnStateCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnStateCustomer.setText("State");
		tblclmnStateCustomer.setWidth(100);
		tblclmnStateCustomer.addSelectionListener(new TableColumnSortListener(tblclmnStateCustomer));

		TableColumn tblclmnZipCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnZipCustomer.setText("Zip");
		tblclmnZipCustomer.setWidth(100);
		tblclmnZipCustomer.addSelectionListener(new TableColumnSortListener(tblclmnZipCustomer));

		TableColumn tblclmnHomePhoneCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnHomePhoneCustomer.setText("Home Phone");
		tblclmnHomePhoneCustomer.setWidth(100);
		tblclmnHomePhoneCustomer.addSelectionListener(new TableColumnSortListener(tblclmnHomePhoneCustomer));

		TableColumn tblclmnCellPhoneCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnCellPhoneCustomer.setText("Cell Phone");
		tblclmnCellPhoneCustomer.setWidth(100);
		tblclmnCellPhoneCustomer.addSelectionListener(new TableColumnSortListener(tblclmnCellPhoneCustomer));

		TableColumn tblclmnEmailCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnEmailCustomer.setText("E-mail");
		tblclmnEmailCustomer.setWidth(100);
		tblclmnEmailCustomer.addSelectionListener(new TableColumnSortListener(tblclmnEmailCustomer));

		// Delete right click menu
		Menu popupMenu = new Menu(customerTable);
		MenuItem deleteCustomer = new MenuItem(popupMenu, SWT.NONE);
		deleteCustomer.setText("Delete Customer");
		deleteCustomer.setEnabled(false);
		deleteCustomer.addSelectionListener(new DeleteObjectListener(customerTable));
		customerTable.setMenu(popupMenu);
		
		// Customer controls
		MyText customerSearchTextBox = new MyText(customerComposite, SWT.BORDER);
		customerSearchTextBox.setText("Search...");
		customerSearchTextBox.setBounds(10, 10, 861, 26);
		customerSearchTextBox.addModifyListener(new SearchTextBoxListeners(customerSearchTextBox, customerTable, new Customer()));
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
		MyTable unitTable = new UnitTable(unitsComposite, SWT.BORDER | SWT.FULL_SELECTION);
		unitTable.addMouseListener(new OpenExistingObjectMouseListener(unitTable, shell));
		unitTable.setBounds(10, 42, 976, 663);
		unitTable.setHeaderVisible(true);
		unitTable.setLinesVisible(true);

		TableColumn tblclmnOwnerUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnOwnerUnit.setText("Owner");
		tblclmnOwnerUnit.setWidth(165);

		TableColumn tblclmnMakeUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnMakeUnit.setText("Make");
		tblclmnMakeUnit.setWidth(119);
		tblclmnMakeUnit.addSelectionListener(new TableColumnSortListener(tblclmnMakeUnit));

		TableColumn tblclmnModelUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnModelUnit.setText("Model");
		tblclmnModelUnit.setWidth(138);
		tblclmnModelUnit.addSelectionListener(new TableColumnSortListener(tblclmnModelUnit));

		TableColumn tblclmnModelNameUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnModelNameUnit.setText("Model Name");
		tblclmnModelNameUnit.setWidth(148);
		tblclmnModelNameUnit.addSelectionListener(new TableColumnSortListener(tblclmnModelNameUnit));

		TableColumn tblclmnYearUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnYearUnit.setText("Year");
		tblclmnYearUnit.setWidth(50);
		tblclmnYearUnit.addSelectionListener(new TableColumnSortListener(tblclmnYearUnit));

		TableColumn tblclmnMileageUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnMileageUnit.setText("Mileage");
		tblclmnMileageUnit.setWidth(85);
		tblclmnMileageUnit.addSelectionListener(new TableColumnSortListener(tblclmnMileageUnit));

		TableColumn tblclmnColorUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnColorUnit.setText("Color");
		tblclmnColorUnit.setWidth(49);
		tblclmnColorUnit.addSelectionListener(new TableColumnSortListener(tblclmnColorUnit));

		TableColumn tblclmnVinUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnVinUnit.setText("VIN");
		tblclmnVinUnit.setWidth(215);
		tblclmnVinUnit.addSelectionListener(new TableColumnSortListener(tblclmnVinUnit));

		// Delete right click menu
		Menu popupMenu = new Menu(unitTable);
		MenuItem deleteUnit = new MenuItem(popupMenu, SWT.NONE);
		deleteUnit.setText("Delete Unit");
		deleteUnit.setEnabled(false);
		deleteUnit.addSelectionListener(new DeleteObjectListener(unitTable));
		unitTable.setMenu(popupMenu);
		
		// Unit controls
		MyText unitSearchBox = new MyText(unitsComposite, SWT.BORDER);
		unitSearchBox.setText("Search...");
		unitSearchBox.setBounds(10, 10, 861, 26);
		unitSearchBox.addModifyListener(new SearchTextBoxListeners(unitSearchBox, unitTable, new Unit()));
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