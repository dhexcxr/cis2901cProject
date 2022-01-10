package cis2901c;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;

public class Gui extends Composite {
	private Text roSearchBox;
	public List listOfRos = null;

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
		listOfRos.setBounds(10, 74, 976, 631);
		
		roSearchBox = new Text(repairOrderListing, SWT.BORDER);
		roSearchBox.setText("Search...");
		roSearchBox.setBounds(10, 10, 830, 26);
//		roSearchBox.addModifyListener(new TypingInRoSearchBox());		// typingInRoSearchBox
		RoSearchBoxListeners roSearchBoxListeners = new RoSearchBoxListeners(roSearchBox, listOfRos);
//		roSearchBox.addModifyListener(new ModifyListener() {
//			
//			@Override
//			public void modifyText(ModifyEvent e) {
//				Main.getGui().listOfRos.setItems("Test");
//				
//			}
//		});		// typingInRoSearchBox
		roSearchBox.addModifyListener(roSearchBoxListeners);
		roSearchBox.addFocusListener(roSearchBoxListeners);
		
//		roSearchBox.addFocusListener(new FocusListener() {
//			
//			@Override
//			public void focusLost(FocusEvent e) {
//				System.out.println("Search box focused lost");
//				if (roSearchBox.getText().equals("")) {
//					roSearchBox.setText("Search...");
//				}
//				
//			}
//			
//			@Override
//			public void focusGained(FocusEvent e) {
//				System.out.println("Search box focused gained");
//				if (roSearchBox.getText().equals("Search...")) {
//					roSearchBox.setText("");
//				}
//				
//			}
//		});
		
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
		
		TabItem tbtmParts = new TabItem(tabFolder, SWT.NONE);
		tbtmParts.setText("Parts");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmParts.setControl(composite);
		
		TabItem tbtmCustomers = new TabItem(tabFolder, SWT.NONE);
		tbtmCustomers.setText("Customers");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmCustomers.setControl(composite_1);
		
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
