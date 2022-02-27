package cis2901c.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;

import cis2901c.listeners.CreateNewObjectListener;
import cis2901c.listeners.OpenExistingObjectMouseListener;
import cis2901c.listeners.SearchTextBoxListeners;
import cis2901c.listeners.TableColumnSortListener;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.objects.Customer;
import cis2901c.objects.CustomerTable;
import cis2901c.objects.PartInventoryTable;
import cis2901c.objects.MyTable;
import cis2901c.objects.MyText;
import cis2901c.objects.Part;

public class Gui extends Composite {

	private Shell shell;
	private String noImplementationMsg = "Not Implimented Yet...";

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
		lblNotImplimentedYet.setText(noImplementationMsg);
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
		
		Label lblInvoiceNotImplimentedYet = new Label(invoiceComposite, SWT.NONE);
		lblInvoiceNotImplimentedYet.setText(noImplementationMsg);
		lblInvoiceNotImplimentedYet.setBounds(10, 10, 147, 20);
		// END Invoice Tab
		
		// START Order tab
		TabItem tbtmOrder = new TabItem(tabFolderParts, SWT.NONE);
		tbtmOrder.setText("Order");
		
		Composite orderComposite = new Composite(tabFolderParts, SWT.NONE);
		tbtmOrder.setControl(orderComposite);
		
		Label lblOrderNotImplimentedYet = new Label(orderComposite, SWT.NONE);
		lblOrderNotImplimentedYet.setText(noImplementationMsg);
		lblOrderNotImplimentedYet.setBounds(10, 10, 147, 20);
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
		
		Label lblNotImplimentedYet = new Label(unitsComposite, SWT.NONE);
		lblNotImplimentedYet.setText(noImplementationMsg);
		lblNotImplimentedYet.setBounds(10, 10, 147, 20);
	}
	
	private void reportsTab(TabFolder tabFolder) {
		TabItem tbtmReports = new TabItem(tabFolder, SWT.NONE);
		tbtmReports.setText("Reports");

		Composite reportsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmReports.setControl(reportsComposite);

		Label lblNotImplimentedYet = new Label(reportsComposite, SWT.NONE);
		lblNotImplimentedYet.setText(noImplementationMsg);
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