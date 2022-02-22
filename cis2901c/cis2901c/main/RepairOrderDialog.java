package cis2901c.main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import cis2901c.listeners.CustomerSearchListener;
import cis2901c.listeners.DeleteLineItemListener;
import cis2901c.listeners.JobNameModifiedListener;
import cis2901c.listeners.RepairOrderPartTableListener;
import cis2901c.listeners.RequiredTextBoxModifyListener;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.listeners.UnitSearchListener;
import cis2901c.objects.Job;
import cis2901c.objects.JobLaborTable;
import cis2901c.objects.MyText;
import cis2901c.objects.Part;
import cis2901c.objects.RepairOrderJobTable;
import cis2901c.objects.RepairOrderJobTableItem;

import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import java.util.logging.Level;

import org.eclipse.swt.widgets.Composite;
import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.InvoicePartTableItem;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RepairOrderDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private MyText txtCustomerRepairOrder;
	private MyText txtUnitRepairOrder;
	private RepairOrderJobTable tableJobsRepairOrder;
	private MyText txtSubTotalRepairOrder;
	private MyText textTaxRepairOrder;
	private MyText textFinalTotalRepairOrder;
	private MyText txtJobName;
	private JobNameModifiedListener jobNameModifiedListener;
	private MyText txtComplaints;
	private MyText txtResolution;
	private MyText txtReccomendations;
	private InvoicePartTable jobPartsTable;
	private JobLaborTable jobLaborTable;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RepairOrderDialog(Shell parent, int style) {
		super(parent, style);		// TODO set customer name or RO number in setText
		setText("Repair Order Details");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
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
		shell = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(992, 736);
		shell.setText(getText());
		
		Gui.setDialogAtCenter(shell);
		
//		InvoicePartTable jobPartsTable;
		
		txtCustomerRepairOrder = new MyText(shell, SWT.BORDER | SWT.WRAP);
		txtCustomerRepairOrder.setEditable(false);
		txtCustomerRepairOrder.setText("Customer...");
		txtCustomerRepairOrder.setBounds(10, 10, 466, 128);
		txtCustomerRepairOrder.addModifyListener(new RequiredTextBoxModifyListener(txtCustomerRepairOrder));
		txtCustomerRepairOrder.addMouseListener(new CustomerSearchListener(txtCustomerRepairOrder));
		
		txtUnitRepairOrder = new MyText(shell, SWT.BORDER);
		txtUnitRepairOrder.setEditable(false);
		txtUnitRepairOrder.setText("Unit...");
		txtUnitRepairOrder.setBounds(498, 10, 466, 128);
		txtUnitRepairOrder.addMouseListener(new UnitSearchListener(txtUnitRepairOrder));
		// TODO Unit Search Dialog
		
		tableJobsRepairOrder = new RepairOrderJobTable(shell, SWT.BORDER | SWT.FULL_SELECTION);
		tableJobsRepairOrder.setBounds(10, 144, 660, 200);
		tableJobsRepairOrder.setHeaderVisible(true);
		tableJobsRepairOrder.setLinesVisible(true);
		
		TableColumn tblclmnJobName = new TableColumn(tableJobsRepairOrder, SWT.NONE);
		tblclmnJobName.setWidth(330);
		tblclmnJobName.setText("Job Name");
		
		TableColumn tblclmnPartsTotal = new TableColumn(tableJobsRepairOrder, SWT.NONE);
		tblclmnPartsTotal.setWidth(100);
		tblclmnPartsTotal.setText("Parts Total");
		
		TableColumn tblclmnLaborTotal = new TableColumn(tableJobsRepairOrder, SWT.NONE);
		tblclmnLaborTotal.setWidth(100);
		tblclmnLaborTotal.setText("Labor Total");
		
		TableColumn tblclmnJobTotal = new TableColumn(tableJobsRepairOrder, SWT.NONE);
		tblclmnJobTotal.setWidth(111);
		tblclmnJobTotal.setText("Job Total");
		
		Button btnCashier = new Button(shell, SWT.NONE);
		btnCashier.setBounds(676, 244, 140, 47);
		btnCashier.setText("Cashier");
		
		Button btnSave = new Button(shell, SWT.NONE);
		btnSave.setBounds(822, 197, 142, 94);
		btnSave.setText("Save");
		
		Button btnClose = new Button(shell, SWT.NONE);
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO if there are unsaved changes, prompt to save before closing
				shell.close();
			}
		});
		btnClose.setBounds(749, 297, 140, 47);
		btnClose.setText("Close");
		
		Group grpTotals = new Group(shell, SWT.NONE);
		grpTotals.setText("Sub Total");
		grpTotals.setBounds(540, 344, 424, 62);
		
		txtSubTotalRepairOrder = new MyText(grpTotals, SWT.BORDER);
		txtSubTotalRepairOrder.setBounds(10, 26, 128, 26);
		txtSubTotalRepairOrder.setEditable(false);
		txtSubTotalRepairOrder.setText("SubTotal");
		
		Label lblTax = new Label(grpTotals, SWT.NONE);
		lblTax.setBounds(148, 0, 21, 20);
		lblTax.setText("Tax");
		
		textTaxRepairOrder = new MyText(grpTotals, SWT.BORDER);
		textTaxRepairOrder.setBounds(148, 26, 128, 26);
		textTaxRepairOrder.setText("Tax");
		textTaxRepairOrder.setEditable(false);
		
		Label lblFinalTotal = new Label(grpTotals, SWT.NONE);
		lblFinalTotal.setBounds(286, 0, 70, 20);
		lblFinalTotal.setText("Final Total");
		
		textFinalTotalRepairOrder = new MyText(grpTotals, SWT.BORDER);
		textFinalTotalRepairOrder.setBounds(286, 26, 128, 26);
		textFinalTotalRepairOrder.setText("Final Total");
		textFinalTotalRepairOrder.setEditable(false);
		
		Button btnDeleteLineItem = new Button(shell, SWT.NONE);
		btnDeleteLineItem.setBounds(386, 350, 121, 56);
		btnDeleteLineItem.setText("Delete Line Item");
		btnDeleteLineItem.setVisible(false);
//		btnDeleteLineItem.addMouseListener(new DeleteLineItemListener(jobPartsTable));
		
		Button btnAddLaborLine = new Button(shell, SWT.NONE);
		btnAddLaborLine.setBounds(255, 350, 121, 56);
		btnAddLaborLine.setText("Add Labor");
		btnAddLaborLine.setVisible(false);
		
		Button btnDeleteLaborLine = new Button(shell, SWT.NONE);
		btnDeleteLaborLine.setBounds(386, 350, 121, 56);
		btnDeleteLaborLine.setText("Delete Labor");
		btnDeleteLaborLine.setVisible(false);
		
		// START Job Tabs
		TabFolder tabFolderJobsRepairOrder = new TabFolder(shell, SWT.NONE);
		tabFolderJobsRepairOrder.addSelectionListener(new SelectionAdapter() {
			@Override		// set visibility of Tab function buttons
			public void widgetSelected(SelectionEvent e) {
				if (tabFolderJobsRepairOrder.getSelectionIndex() == 0) {		// Parts tab
					btnDeleteLineItem.setVisible(false);
					btnAddLaborLine.setVisible(false);
					btnDeleteLaborLine.setVisible(false);
				} else if (tabFolderJobsRepairOrder.getSelectionIndex() == 1) {		// Parts tab
					Main.log(Level.INFO, "Repair Order Parts tab selected");
					btnDeleteLineItem.setVisible(true);
					btnAddLaborLine.setVisible(false);
					btnDeleteLaborLine.setVisible(false);
				} else if (tabFolderJobsRepairOrder.getSelectionIndex() == 2) {		// Labor tab
					Main.log(Level.INFO, "Repair Order Labor tab selected");
					btnDeleteLineItem.setVisible(false);
					btnAddLaborLine.setVisible(true);
					btnDeleteLaborLine.setVisible(true);
				}
			}
		});
		tabFolderJobsRepairOrder.setBounds(10, 383, 954, 296);
		
		TabItem tbtmJobDetails = new TabItem(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmJobDetails.setText("Job Details");
		
		Composite jobDetailsComposite = new Composite(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmJobDetails.setControl(jobDetailsComposite);
//		jobDetailsComposite.setEnabled(false);
		
		txtJobName = new MyText(jobDetailsComposite, SWT.BORDER);
		txtJobName.setText("Job Name...");
		txtJobName.setBounds(10, 10, 460, 26);
		jobNameModifiedListener = new JobNameModifiedListener(txtJobName, tableJobsRepairOrder);
		txtJobName.addModifyListener(jobNameModifiedListener);
//		txtJobName.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent e) {
//				// TODO copy entered text into Selected Job Table Item
//				Main.log(Level.INFO, "Job Name Txt modified");
//			}
//		});
		txtJobName.addFocusListener(new TextBoxFocusListener(txtJobName));
//		txtJobName.addModifyListener(new RequiredTextBoxModifyListener(txtJobName));
		txtJobName.setEnabled(false);
		
		txtComplaints = new MyText(jobDetailsComposite, SWT.BORDER | SWT.WRAP);
		txtComplaints.setText("Complaints...");
		txtComplaints.setBounds(10, 42, 460, 211);
		txtComplaints.setEnabled(false);
		
		txtResolution = new MyText(jobDetailsComposite, SWT.BORDER | SWT.WRAP);
		txtResolution.setText("Resolution...");
		txtResolution.setBounds(476, 42, 460, 100);
		txtResolution.setEnabled(false);
		
		txtReccomendations = new MyText(jobDetailsComposite, SWT.BORDER | SWT.WRAP);
		txtReccomendations.setText("Reccomendations...");
		txtReccomendations.setBounds(476, 148, 460, 105);
		txtReccomendations.setEnabled(false);
		
		TabItem tbtmParts = new TabItem(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmParts.setText("Parts");
		
		Composite jobPartsComposite = new Composite(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmParts.setControl(jobPartsComposite);
		
		jobPartsTable = new InvoicePartTable(jobPartsComposite, SWT.BORDER | SWT.FULL_SELECTION);
		jobPartsTable.setLinesVisible(true);
		jobPartsTable.setHeaderVisible(true);
		jobPartsTable.setBounds(10, 10, 915, 243);
		jobPartsTable.addListener(SWT.MouseDown, new RepairOrderPartTableListener(jobPartsTable, tableJobsRepairOrder));		// TODO extend this listener so we can show totals in Job Table
		jobPartsTable.setEnabled(false);
		
		TableColumn tblclmnPartNumberInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnPartNumberInvoice.setWidth(126);
		tblclmnPartNumberInvoice.setText("Part Number");
		
		TableColumn tblclmnDescriptionInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnDescriptionInvoice.setWidth(262);
		tblclmnDescriptionInvoice.setText("Description");
		
		TableColumn tblclmnQuantityInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnQuantityInvoice.setWidth(91);
		tblclmnQuantityInvoice.setText("Quantity");
		
		TableColumn tblclmnOnHandInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnOnHandInvoice.setWidth(100);
		tblclmnOnHandInvoice.setText("On Hand");
		
		TableColumn tblclmnCostInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnCostInvoice.setWidth(100);
		tblclmnCostInvoice.setText("Cost");
		
		TableColumn tblclmnPartPriceInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnPartPriceInvoice.setWidth(110);
		tblclmnPartPriceInvoice.setText("Part Price");
		
		TableColumn tblclmnExtendedPriceInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnExtendedPriceInvoice.setWidth(114);
		tblclmnExtendedPriceInvoice.setText("Extended Price");
		
		TabItem tbtmLabor = new TabItem(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmLabor.setText("Labor");
		
		Composite jobLaborComposite = new Composite(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmLabor.setControl(jobLaborComposite);
		
		jobLaborTable = new JobLaborTable(jobLaborComposite, SWT.BORDER | SWT.FULL_SELECTION);
		jobLaborTable.setBounds(10, 10, 926, 243);
		jobLaborTable.setHeaderVisible(true);
		jobLaborTable.setLinesVisible(true);
		jobLaborTable.setEnabled(false);
		
		TableColumn tblclmnTechnician = new TableColumn(jobLaborTable, SWT.NONE);
		tblclmnTechnician.setWidth(100);
		tblclmnTechnician.setText("Technician");
		
		TableColumn tblclmnDescription = new TableColumn(jobLaborTable, SWT.NONE);
		tblclmnDescription.setWidth(507);
		tblclmnDescription.setText("Description");
		
		TableColumn tblclmnHours = new TableColumn(jobLaborTable, SWT.NONE);
		tblclmnHours.setWidth(100);
		tblclmnHours.setText("Hours");
		
		TableColumn tblclmnRate = new TableColumn(jobLaborTable, SWT.NONE);
		tblclmnRate.setWidth(100);
		tblclmnRate.setText("Rate");
		
		TableColumn tblclmnTotal = new TableColumn(jobLaborTable, SWT.NONE);
		tblclmnTotal.setWidth(100);
		tblclmnTotal.setText("Total");
		
		btnDeleteLineItem.addMouseListener(new DeleteLineItemListener(jobPartsTable));
		
		Button btnAddJob = new Button(shell, SWT.NONE);
		btnAddJob.setBounds(676, 144, 140, 94);
		btnAddJob.setText("Add Job");
		btnAddJob.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// create new Job on Job Table
				RepairOrderJobTableItem job = new RepairOrderJobTableItem(tableJobsRepairOrder, getStyle());
				job.setData(new Job());
			}
		});
		
		Button btnDeleteJob = new Button(shell, SWT.NONE);
		btnDeleteJob.setBounds(824, 144, 140, 47);
		btnDeleteJob.setText("Delete Job");
		btnDeleteJob.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// create new Job on Job Table
				int selectedIndex = -1;
				if (tableJobsRepairOrder.getSelectionIndex() >= 0 && tableJobsRepairOrder.getSelectionIndex() < tableJobsRepairOrder.getItemCount()
						&& tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).getData() != null) {
					selectedIndex = tableJobsRepairOrder.getSelectionIndex();
					tableJobsRepairOrder.remove(selectedIndex);
				}
				selectedIndex = selectedIndex == 0 ? 0 : selectedIndex - 1;
//				selectedIndex--;
				tableJobsRepairOrder.setSelection(selectedIndex);
				tableJobsRepairOrder.notifyListeners(SWT.Selection, new Event());
				
				// disable Job Tabs if no jobs on Job Table
				if (tableJobsRepairOrder.getItemCount() == 0) {
					txtJobName.setEnabled(false);
					txtJobName.setText("Job Name...");
					txtComplaints.setEnabled(false);
					txtComplaints.setText("Complaints...");
					txtResolution.setEnabled(false);
					txtResolution.setText("Resolution...");
					txtReccomendations.setEnabled(false);
					txtReccomendations.setText("Reccomendations...");
					jobPartsTable.setEnabled(false);
					jobPartsTable.removeAll();
					jobLaborTable.setEnabled(false);
					jobLaborTable.removeAll();
				} 
			}
		});
		
		tableJobsRepairOrder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtJobName.removeModifyListener(jobNameModifiedListener);
				// job selected
				txtJobName.setEnabled(true);
				txtComplaints.setEnabled(true);
				txtResolution.setEnabled(true);
				txtReccomendations.setEnabled(true);
				jobPartsTable.setEnabled(true);
				jobLaborTable.setEnabled(true);
				// TODO protect agains't invalid indices
				if (tableJobsRepairOrder.getSelectionIndex() < 0 || tableJobsRepairOrder.getSelectionIndex() >= tableJobsRepairOrder.getItemCount()) {
					return;
				}
				Job selectedJob = (Job) tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).getData();
				// copy selected Job to Job Tabs
				if (selectedJob != null && !selectedJob.equals(tabFolderJobsRepairOrder.getData())) {
					tabFolderJobsRepairOrder.setData(selectedJob);
					// set Job data into Job Tabs
					txtJobName.setText(selectedJob.getJobName().equals("") ? "Job Name..." : selectedJob.getJobName());
					txtComplaints.setText(selectedJob.getComplaints().equals("") ? "Complaints..." : selectedJob.getComplaints());
					txtResolution.setText(selectedJob.getResolution().equals("") ? "Resolution..." : selectedJob.getResolution());
					txtReccomendations.setText(selectedJob.getReccomendations().equals("") ? "Reccomendations..." : selectedJob.getReccomendations());
					
					for (Part part : selectedJob.getParts()) {
						// TODO see how I stored quantity in Invoice, or figure out how to store quantity
						addPartToPartTableItem(part);
					}
				}

				
				txtJobName.addModifyListener(jobNameModifiedListener);
			}
		});
	}
	
	private void addPartToPartTableItem(Part part) {
		// used in tableJobsRepairOrder.addSelectionListener
		InvoicePartTableItem jobPart = new InvoicePartTableItem(jobPartsTable, getStyle());
		jobPart.setText(new String[] {part.getPartNumber(), part.getDescription(), Integer.toString(0), Integer.toString(part.getOnHand()),
				part.getCost().toString(), part.getRetail().toString(), part.getRetail().toString()});
	}
}
