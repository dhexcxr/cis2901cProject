package cis2901c.main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import cis2901c.listeners.CustomerSearchListener;
import cis2901c.listeners.DbServices;
import cis2901c.listeners.JobDetailsModifiedListener;
import cis2901c.listeners.JobNameModifiedListener;
import cis2901c.listeners.RepairOrderLaborTableListener;
import cis2901c.listeners.RepairOrderPartDeleteLineItemListener;
import cis2901c.listeners.RepairOrderPartTableListener;
import cis2901c.listeners.RequiredTextBoxModifyListener;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.listeners.UnitSearchListener;
import cis2901c.objects.Job;
import cis2901c.objects.Labor;
import cis2901c.objects.LaborTable;
import cis2901c.objects.MyText;
import cis2901c.objects.Part;
import cis2901c.objects.RepairOrder;
import cis2901c.objects.RepairOrderJobTable;
import cis2901c.objects.RepairOrderJobTableItem;
import cis2901c.objects.Unit;
import cis2901c.objects.LaborTableItem;
import cis2901c.objects.MyTable;

import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.swt.widgets.Composite;

import cis2901c.objects.Customer;
import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.InvoicePartTableItem;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RepairOrderDialog extends Dialog {

	protected Object result;
	protected Shell shlRepairOrder;
	
	private RepairOrderJobTable tableJobsRepairOrder;
	private Button btnSaveRo;
	private Button btnCloseRo;
	private Button btnAddJob;
	private Button btnDeleteJob;
	private Button btnDeleteLineItem; 
	private Button btnAddLaborLine;
	private Button btnDeleteLaborLine;
	private TabFolder tabFolderJobsRepairOrder;
	
	private MyText txtSubTotalRepairOrder;
	private MyText textTaxRepairOrder;
	private MyText textFinalTotalRepairOrder;
	
	private long roId = -1;
	private MyText txtCustomerRepairOrder;
	private MyText txtUnitRepairOrder;
	private MyText txtJobName;
	private MyText txtComplaints;
	private MyText txtResolution;
	private MyText txtReccomendations;
	private InvoicePartTable jobPartsTable;
	private LaborTable jobLaborTable;
	
	private RepairOrder currentRepairOrder;
	private long customerId;
	
	private JobNameModifiedListener jobNameModifiedListener;
	private JobDetailsModifiedListener jobDetailsModifiedListener;
	
	private static final String ONLY_DECIMALS = "[^0-9.]";		// find a better name
	
	// TODO create isModified variable in this RepairOrderDialog object to track if ANYTHING in the underlying repairOrder has been modified
		// so we can ask the user if they want to save before closing RepairOrderDialog
	

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
		setupListeners();
		shlRepairOrder.open();
		shlRepairOrder.layout();
		Display display = getParent().getDisplay();
		while (!shlRepairOrder.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public Object open(RepairOrder repairOrder) {
		createContents();
		
		// TODO set Dialog boxes and stuff from repairOrder fields
		currentRepairOrder = repairOrder;
		roId = repairOrder.getRepairOrderId();
		if (repairOrder.getCustomerId() != 0) {
			customerId = repairOrder.getCustomerId();
			// TODO make custom setData method for this txt object that pulls info from Customer automagiacally 
//			txtCustomerRepairOrder.setData(DbServices.searchForCustomer(customerId));
			txtCustomerRepairOrder.setData(DbServices.searchForObjectByPk(new Customer(customerId)));
		}
		if (repairOrder.getCustomerData() != null) {
			txtCustomerRepairOrder.setText(repairOrder.getCustomerName() + "\n" + repairOrder.getCustomerData());
		}
		
		if (repairOrder.getUnitId() != 0) {
			txtUnitRepairOrder.setText(repairOrder.getUnitYear() + " " + repairOrder.getUnitMake() + "\n" +
										repairOrder.getUnitModel() + "\n" + repairOrder.getUnitVin());
			txtUnitRepairOrder.setData(DbServices.searchForObjectByPk(new Unit(repairOrder.getUnitId())));
		}
		
		for (Job job : (Job[]) DbServices.searchForObjectsByPk(new Job(roId))) {
			if (job == null) {
				break;
			}
			RepairOrderJobTableItem jobTableItem = new RepairOrderJobTableItem(tableJobsRepairOrder, getStyle());
			List<Labor> jobLabor = new ArrayList<>(Arrays.asList((Labor[]) DbServices.searchForObjectsByPk(new Labor(job.getJobId()))));
			jobLabor.removeAll(Collections.singleton(null));
			job.setLabor(jobLabor);
			jobTableItem.setData(job);
			tableJobsRepairOrder.setSelection(0);
			tableJobsRepairOrder.notifyListeners(SWT.Selection, new Event());
			
			// TODO find and build Parts and Labor
		}
		
		setupListeners();
		
		shlRepairOrder.open();
		shlRepairOrder.layout();
		Display display = getParent().getDisplay();
		while (!shlRepairOrder.isDisposed()) {
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
		shlRepairOrder = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlRepairOrder.setSize(992, 736);
		shlRepairOrder.setText(getText());
		
		Gui.setDialogAtCenter(shlRepairOrder);
		
		roDetails();

		roControls();
		
		jobTabs();
	}
	
	private void roDetails() {
		// Customer Data
		txtCustomerRepairOrder = new MyText(shlRepairOrder, SWT.BORDER | SWT.WRAP);
		txtCustomerRepairOrder.setEditable(false);
		txtCustomerRepairOrder.setText("Customer...");
		txtCustomerRepairOrder.setBounds(10, 10, 466, 128);
		
		txtUnitRepairOrder = new MyText(shlRepairOrder, SWT.BORDER | SWT.WRAP);
		txtUnitRepairOrder.setEditable(false);
		txtUnitRepairOrder.setText("Unit...");
		txtUnitRepairOrder.setBounds(498, 10, 466, 128);
		// END Customer Data

		// Totals
		Group grpTotals = new Group(shlRepairOrder, SWT.NONE);
		grpTotals.setText("Sub Total");
		grpTotals.setBounds(540, 344, 424, 62);

		txtSubTotalRepairOrder = new MyText(grpTotals, SWT.BORDER);
		txtSubTotalRepairOrder.setBounds(10, 26, 128, 26);
		txtSubTotalRepairOrder.setEditable(false);
		txtSubTotalRepairOrder.setText("$0.00");

		Label lblTax = new Label(grpTotals, SWT.NONE);
		lblTax.setBounds(148, 0, 21, 20);
		lblTax.setText("Tax");

		textTaxRepairOrder = new MyText(grpTotals, SWT.BORDER);
		textTaxRepairOrder.setBounds(148, 26, 128, 26);
		textTaxRepairOrder.setText("$0.00");
		textTaxRepairOrder.setEditable(false);

		Label lblFinalTotal = new Label(grpTotals, SWT.NONE);
		lblFinalTotal.setBounds(286, 0, 70, 20);
		lblFinalTotal.setText("Final Total");

		textFinalTotalRepairOrder = new MyText(grpTotals, SWT.BORDER);
		textFinalTotalRepairOrder.setBounds(286, 26, 128, 26);
		textFinalTotalRepairOrder.setText("$0.00");
		textFinalTotalRepairOrder.setEditable(false);
		// END Totals
	}
	
	private void roControls() {
		// Jobs table
		tableJobsRepairOrder = new RepairOrderJobTable(shlRepairOrder, SWT.BORDER | SWT.FULL_SELECTION);
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

		btnAddJob = new Button(shlRepairOrder, SWT.NONE);
		btnAddJob.setBounds(676, 144, 140, 94);
		btnAddJob.setText("Add Job");

		btnDeleteJob = new Button(shlRepairOrder, SWT.NONE);
		btnDeleteJob.setBounds(824, 144, 140, 47);
		btnDeleteJob.setText("Delete Job");
		// END Jobs table

		// RO Controls
		Button btnCashierRo = new Button(shlRepairOrder, SWT.NONE);
		btnCashierRo.setBounds(676, 244, 140, 47);
		btnCashierRo.setText("Cashier");

		btnSaveRo = new Button(shlRepairOrder, SWT.NONE);
		btnSaveRo.setBounds(822, 197, 142, 94);
		btnSaveRo.setText("Save");

		btnCloseRo = new Button(shlRepairOrder, SWT.NONE);
		btnCloseRo.setBounds(749, 297, 140, 47);
		btnCloseRo.setText("Close");
		// END RO controls
	}
	
	private void jobTabs() {
		// Job Tabs Buttons
		// These buttons need to be before the TabFolder to be painted over top of it
		btnDeleteLineItem = new Button(shlRepairOrder, SWT.NONE);
		btnDeleteLineItem.setBounds(386, 350, 121, 56);
		btnDeleteLineItem.setText("Delete Line Item");
		btnDeleteLineItem.setVisible(false);

		btnAddLaborLine = new Button(shlRepairOrder, SWT.NONE);
		btnAddLaborLine.setBounds(255, 350, 121, 56);
		btnAddLaborLine.setText("Add Labor");
		btnAddLaborLine.setVisible(false);

		btnDeleteLaborLine = new Button(shlRepairOrder, SWT.NONE);
		btnDeleteLaborLine.setBounds(386, 350, 121, 56);
		btnDeleteLaborLine.setText("Delete Labor");
		btnDeleteLaborLine.setVisible(false);
		// END Job Tabs Buttons

		// Job tabs
		tabFolderJobsRepairOrder = new TabFolder(shlRepairOrder, SWT.NONE);
		tabFolderJobsRepairOrder.setBounds(10, 383, 954, 296);

		// Job Details Tab
		TabItem tbtmJobDetails = new TabItem(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmJobDetails.setText("Job Details");

		Composite jobDetailsComposite = new Composite(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmJobDetails.setControl(jobDetailsComposite);

		txtJobName = new MyText(jobDetailsComposite, SWT.BORDER);
		txtJobName.setText("Job Name...");
		txtJobName.setBounds(10, 10, 460, 26);
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
		// END Job Details Tab

		// Job Part tab
		TabItem tbtmParts = new TabItem(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmParts.setText("Parts");

		Composite jobPartsComposite = new Composite(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmParts.setControl(jobPartsComposite);

		jobPartsTable = new InvoicePartTable(jobPartsComposite, SWT.BORDER | SWT.FULL_SELECTION);
		jobPartsTable.setLinesVisible(true);
		jobPartsTable.setHeaderVisible(true);
		jobPartsTable.setBounds(10, 10, 915, 243);
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
		// END Job Part tab

		// Labor tab
		TabItem tbtmLabor = new TabItem(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmLabor.setText("Labor");

		Composite jobLaborComposite = new Composite(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmLabor.setControl(jobLaborComposite);

		jobLaborTable = new LaborTable(jobLaborComposite, SWT.BORDER | SWT.FULL_SELECTION);
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
		// END Labor tab
		// END Job tabs
	}
	
	private void setupListeners() {
		// LISTENERS
				txtCustomerRepairOrder.addModifyListener(new RequiredTextBoxModifyListener(txtCustomerRepairOrder));
				
				txtCustomerRepairOrder.addMouseListener(new CustomerSearchListener(txtCustomerRepairOrder));
				
				txtUnitRepairOrder.addMouseListener(new UnitSearchListener(txtUnitRepairOrder));
				
				btnAddJob.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						// TODO judging by the comment on 2 lines below I think this might should've been for tableJobsRepairOrder
							// but it seems to be working fine so I don't know that its actually needed
						tabFolderJobsRepairOrder.notifyListeners(SWT.Selection, new Event());		// trigger saving Job details
						// create new Job on Job Table
						RepairOrderJobTableItem job = new RepairOrderJobTableItem(tableJobsRepairOrder, getStyle());
						job.setData(new Job());
						tableJobsRepairOrder.setSelection(tableJobsRepairOrder.getItemCount() - 1);
						tableJobsRepairOrder.notifyListeners(SWT.Selection, new Event());
						
						txtJobName.setText("Job Name...");
						txtComplaints.setText("Complaints...");
						txtResolution.setText("Resolution...");
						txtReccomendations.setText("Reccomendations...");
						jobPartsTable.removeAll();
						@SuppressWarnings("unused")				// this adds a new, empty TableItem at the end of the Invoice Line Items
						TableItem tableItem = new InvoicePartTableItem(jobPartsTable, SWT.NONE);	// so we can add parts
						jobLaborTable.removeAll();
					}
				});
				
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
						tableJobsRepairOrder.setSelection(selectedIndex);
						tableJobsRepairOrder.notifyListeners(SWT.Selection, new Event());

						calcRoTotal();
						
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
						if (tableJobsRepairOrder.getSelectionIndex() < 0 || tableJobsRepairOrder.getSelectionIndex() >= tableJobsRepairOrder.getItemCount()) {
							return;
						}
						
						txtJobName.removeModifyListener(jobNameModifiedListener);
						txtJobName.removeModifyListener(jobDetailsModifiedListener);
						txtComplaints.removeModifyListener(jobDetailsModifiedListener);
						txtResolution.removeModifyListener(jobDetailsModifiedListener);
						txtReccomendations.removeModifyListener(jobDetailsModifiedListener);
						
						// job selected
						txtJobName.setEnabled(true);
						txtComplaints.setEnabled(true);
						txtResolution.setEnabled(true);
						txtReccomendations.setEnabled(true);
						jobPartsTable.setEnabled(true);
						jobLaborTable.setEnabled(true);
						
						Job selectedJob = (Job) tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).getData();
						// copy selected Job to Job Tabs
						if (selectedJob != null && !selectedJob.equals(tabFolderJobsRepairOrder.getData())) {
							tabFolderJobsRepairOrder.setData(selectedJob);
							// set Job data into Job Tabs
							txtJobName.setText(selectedJob.getJobName().equals("") ? "Job Name..." : selectedJob.getJobName());
							txtComplaints.setText(selectedJob.getComplaints().equals("") ? "Complaints..." : selectedJob.getComplaints());
							txtResolution.setText(selectedJob.getResolution().equals("") ? "Resolution..." : selectedJob.getResolution());
							txtReccomendations.setText(selectedJob.getReccomendations().equals("") ? "Reccomendations..." : selectedJob.getReccomendations());
							
							// TODO add other methods to set up Job Detail tabs, Part tab, and Labor tab
							jobPartsTable.removeAll();
							for (Part part : selectedJob.getParts()) {
								// TODO see how I stored quantity in Invoice, or figure out how to store quantity
								if (part == null) {
									break;
								}
								addPartToPartTableItem(part);
							}
							@SuppressWarnings("unused")				// this adds a new, empty TableItem at the end of the Invoice Line Items
							TableItem tableItem = new InvoicePartTableItem(jobPartsTable, SWT.NONE);	// so we can add parts
							
							jobLaborTable.removeAll();
							for (Labor labor : selectedJob.getLabor()) {
								if (labor == null) {
									break;
								}
								addLaborToLaborTableItem(labor);
							}
						}

						
						txtJobName.addModifyListener(jobNameModifiedListener);
						txtJobName.addModifyListener(jobDetailsModifiedListener);
						txtComplaints.addModifyListener(jobDetailsModifiedListener);
						txtResolution.addModifyListener(jobDetailsModifiedListener);
						txtReccomendations.addModifyListener(jobDetailsModifiedListener);
					}
				});
				
				btnSaveRo.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						// TODO Auto-generated method stub
//						 spawn amount due dialog box
//						if (txtCustomerRepairOrder.getData() == null) {
//							MessageBox customerRequiredBox = new MessageBox(shlRepairOrder, SWT.ICON_INFORMATION);
//							customerRequiredBox.setText("Notice");
//							customerRequiredBox.setMessage("Please select a Customer");
//							customerRequiredBox.open();
//							return;
//						} else {
							if (roId == -1) {
								saveNewRo();
							} else {
								saveRo(currentRepairOrder);
							}
//						}
					}
				});
				
				btnCloseRo.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						// TODO if there are unsaved changes, prompt to save before closing
						shlRepairOrder.close();
					}
				});
				
				btnDeleteLineItem.addMouseListener(new RepairOrderPartDeleteLineItemListener(jobPartsTable, tableJobsRepairOrder, this));
				
				btnAddLaborLine.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						// TODO add Labor to Labor Table
						@SuppressWarnings("unused")
						TableItem tableItem = new LaborTableItem(jobLaborTable, SWT.NONE);
						tableItem.setData(new Labor());
						jobLaborTable.setSelection(jobLaborTable.getItemCount() - 1);
						jobLaborTable.notifyListeners(SWT.Selection, new Event());
						
						jobLaborTable.setTotalLabor(tableJobsRepairOrder);
						calcRoTotal();
					}
				});
				
				btnDeleteLaborLine.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						// delete Labor from Labor Table
						if (jobLaborTable.getSelectionIndex() >=0 && jobLaborTable.getSelectionIndex() < jobLaborTable.getItemCount()) {
							int selectedIndex = jobLaborTable.getSelectionIndex();
							jobLaborTable.remove(selectedIndex);
							
							selectedIndex = selectedIndex == 0 ? 0 : selectedIndex - 1;
							jobLaborTable.setSelection(selectedIndex);
							
							jobLaborTable.setTotalLabor(tableJobsRepairOrder);
							calcRoTotal();
							
							// TODO recalculate total for Job labor
						}
					}
				});
				
				tabFolderJobsRepairOrder.addSelectionListener(new SelectionAdapter() {
					@Override		// set visibility of Tab function buttons
					public void widgetSelected(SelectionEvent e) {
						if (tabFolderJobsRepairOrder.getSelectionIndex() == 0) {		// Job Details tab
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
				
				jobNameModifiedListener = new JobNameModifiedListener(txtJobName, tableJobsRepairOrder);
				txtJobName.addModifyListener(jobNameModifiedListener);
				txtJobName.addFocusListener(new TextBoxFocusListener(txtJobName));
				
				txtComplaints.addFocusListener(new TextBoxFocusListener(txtComplaints));
				
				txtResolution.addFocusListener(new TextBoxFocusListener(txtResolution));
				
				txtReccomendations.addFocusListener(new TextBoxFocusListener(txtReccomendations));
				
				jobPartsTable.addListener(SWT.MouseDown, new RepairOrderPartTableListener(jobPartsTable, tableJobsRepairOrder, this));

				jobLaborTable.addListener(SWT.MouseDown, new RepairOrderLaborTableListener(jobLaborTable, tableJobsRepairOrder, this));
				// END LISTENERS
				
				// setup Job modified listener
				List<MyText> jobDetailWidgets = new ArrayList<>();
				jobDetailWidgets.add(txtJobName);
				jobDetailWidgets.add(txtComplaints);
				jobDetailWidgets.add(txtResolution);
				jobDetailWidgets.add(txtReccomendations);
				List<MyTable> jobTables = new ArrayList<>(); 
				jobTables.add(jobPartsTable);
				jobTables.add(jobLaborTable);
				jobDetailsModifiedListener = new JobDetailsModifiedListener(tabFolderJobsRepairOrder, jobDetailWidgets, jobTables);
				txtJobName.addModifyListener(jobDetailsModifiedListener);
				txtComplaints.addModifyListener(jobDetailsModifiedListener);
				txtResolution.addModifyListener(jobDetailsModifiedListener);
				txtReccomendations.addModifyListener(jobDetailsModifiedListener);
				// END setup Job modified listener
	}
	
	private void addPartToPartTableItem(Part part) {
		// used in tableJobsRepairOrder.addSelectionListener
		InvoicePartTableItem jobPart = new InvoicePartTableItem(jobPartsTable, getStyle());
		jobPart.setText(new String[] {part.getPartNumber(), part.getDescription(), Integer.toString(0), Integer.toString(part.getOnHand()),
				part.getCost().toString(), part.getRetail().toString(), part.getRetail().toString()});
	}
	
	private void addLaborToLaborTableItem(Labor labor) {
		// used in tableJobsRepairOrder.addSelectionListener
		LaborTableItem jobLabor = new LaborTableItem(jobLaborTable, getStyle());
		jobLabor.setText(new String[] {labor.getTechnician(), labor.getDescription(), labor.getHours().toString(), labor.getLaborRate().toString(),
							labor.getHours().multiply(labor.getLaborRate()).toString()});
		jobLabor.setData(labor);
	}
	
	public void calcRoTotal() {
		BigDecimal total = BigDecimal.valueOf(0);
		for (TableItem currentItem: tableJobsRepairOrder.getItems()) {
			String totalString = currentItem.getText(RepairOrderJobTable.JOB_TOTAL_COLUMN).replaceAll(ONLY_DECIMALS, "");
			total = total.add(new BigDecimal(totalString));
		}
		txtSubTotalRepairOrder.setText("$" + total.setScale(2, RoundingMode.CEILING).toString());
		BigDecimal tax = total.multiply(BigDecimal.valueOf(0.065));
		textTaxRepairOrder.setText("$" + tax.setScale(2, RoundingMode.CEILING).toString());
		textFinalTotalRepairOrder.setText("$" + total.add(tax).setScale(2, RoundingMode.CEILING).toString());
	}
	
	public void saveJob() {
		Job currentJob = (Job) tabFolderJobsRepairOrder.getData();

		for (TableItem currentPartTableItem : jobPartsTable.getItems()) {
			if (!currentJob.getParts().contains(currentPartTableItem.getData())) {
				currentJob.addPart((Part) currentPartTableItem.getData());
			}
		}

		for (TableItem currentLaborTableItem : jobLaborTable.getItems()) {
			if (!currentJob.getLabor().contains(currentLaborTableItem.getData())) {
				currentJob.addLabor((Labor) currentLaborTableItem.getData());
			}
		}
	}
	
	private void saveNewRo() {
		saveRo(new RepairOrder());
	}
	
	private void saveRo(RepairOrder repairOrder) {
		
		if (txtCustomerRepairOrder.getData() != null && ((Customer) txtCustomerRepairOrder.getData()).getCustomerId() != -1) {
			repairOrder.setCustomerId(((Customer) txtCustomerRepairOrder.getData()).getCustomerId());
			repairOrder.setCustomerData(txtCustomerRepairOrder.getText());
		} else {
			// if we haven't selected a Customer, complain - an RO Customer is required
			MessageBox customerRequiredBox = new MessageBox(shlRepairOrder, SWT.ICON_INFORMATION);
			customerRequiredBox.setText("Notice");
			customerRequiredBox.setMessage("Please select a Customer");
			customerRequiredBox.open();
			return;
		}
		
		if (txtUnitRepairOrder.getData() != null && ((Unit) txtUnitRepairOrder.getData()).getUnitId() != -1) {
			repairOrder.setUnitId(((Unit) txtUnitRepairOrder.getData()).getUnitId());
		} else {
			// if we haven't selected a Unit, complain - an RO Unit is required
			MessageBox customerRequiredBox = new MessageBox(shlRepairOrder, SWT.ICON_INFORMATION);
			customerRequiredBox.setText("Notice");
			customerRequiredBox.setMessage("Please select a Unit");
			customerRequiredBox.open();
			return;
		}
		
		if (tableJobsRepairOrder.getItemCount() > 0) {
			for (TableItem jobTableItem : tableJobsRepairOrder.getItems()) {
				repairOrder.addJob((Job) jobTableItem.getData());
			}
		}
		
		repairOrder.setCreatedDate(Timestamp.from(Instant.now()));
		// setClosedTime
		repairOrder.setTax(new BigDecimal(textTaxRepairOrder.getText().replaceAll(ONLY_DECIMALS, "")));
		repairOrder.setTotal(new BigDecimal(textFinalTotalRepairOrder.getText().replaceAll(ONLY_DECIMALS, "")));
		
		
		// send RepairOrder object to DbServices
		DbServices.saveObject(repairOrder);
		roId = repairOrder.getRepairOrderId();
		result = repairOrder;
//		shlRepairOrder.close();
	}
}
