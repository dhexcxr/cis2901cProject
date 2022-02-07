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
import cis2901c.objects.MyCustomerTable;
import cis2901c.objects.MyTable;
import cis2901c.objects.MyText;

public class Gui extends Composite {

	private Shell shell;
	private MyText customerSearchTextBox;
	private MyTable customerTable;
	
	public Gui(Composite parent, int style) {
		super(parent, style);
		
		this.shell = (Shell) parent;

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
		
		Label lblNotImplimentedYet = new Label(repairOrdersComposite, SWT.NONE);
		lblNotImplimentedYet.setText("Not Implimented Yet...");
		lblNotImplimentedYet.setBounds(10, 10, 147, 20);
	}
	
	private void partsTab(TabFolder tabFolder) {
		TabItem tbtmParts = new TabItem(tabFolder, SWT.NONE);
		tbtmParts.setText("Parts");

		Composite partsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmParts.setControl(partsComposite);
		
		Label lblNotImplimentedYet = new Label(partsComposite, SWT.NONE);
		lblNotImplimentedYet.setText("Not Implimented Yet...");
		lblNotImplimentedYet.setBounds(10, 10, 147, 20);
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
		tblclmnFirstName_Customer.addSelectionListener(new TableColumnSortListener(tblclmnFirstName_Customer));

		TableColumn tblclmnLastName_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnLastName_Customer.setText("Last Name");
		tblclmnLastName_Customer.setWidth(100);
		tblclmnLastName_Customer.addSelectionListener(new TableColumnSortListener(tblclmnLastName_Customer));

		TableColumn tblclmnAddress_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnAddress_Customer.setText("Address");
		tblclmnAddress_Customer.setWidth(100);
		tblclmnAddress_Customer.addSelectionListener(new TableColumnSortListener(tblclmnAddress_Customer));
		

		TableColumn tblclmnCity_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnCity_Customer.setText("City");
		tblclmnCity_Customer.setWidth(100);
		tblclmnCity_Customer.addSelectionListener(new TableColumnSortListener(tblclmnCity_Customer));

		TableColumn tblclmnState_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnState_Customer.setText("State");
		tblclmnState_Customer.setWidth(100);
		tblclmnState_Customer.addSelectionListener(new TableColumnSortListener(tblclmnState_Customer));

		TableColumn tblclmnZip_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnZip_Customer.setText("Zip");
		tblclmnZip_Customer.setWidth(100);
		tblclmnZip_Customer.addSelectionListener(new TableColumnSortListener(tblclmnZip_Customer));

		TableColumn tblclmnHomePhone_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnHomePhone_Customer.setText("Home Phone");
		tblclmnHomePhone_Customer.setWidth(100);
		tblclmnHomePhone_Customer.addSelectionListener(new TableColumnSortListener(tblclmnHomePhone_Customer));

		TableColumn tblclmnCellPhone_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnCellPhone_Customer.setText("Cell Phone");
		tblclmnCellPhone_Customer.setWidth(100);
		tblclmnCellPhone_Customer.addSelectionListener(new TableColumnSortListener(tblclmnCellPhone_Customer));

		TableColumn tblclmnEmail_Customer = new TableColumn(customerTable, SWT.NONE);
		tblclmnEmail_Customer.setText("E-mail");
		tblclmnEmail_Customer.setWidth(100);
		tblclmnEmail_Customer.addSelectionListener(new TableColumnSortListener(tblclmnEmail_Customer));

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
		
		Label lblNotImplimentedYet = new Label(unitsComposite, SWT.NONE);
		lblNotImplimentedYet.setText("Not Implimented Yet...");
		lblNotImplimentedYet.setBounds(10, 10, 147, 20);
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