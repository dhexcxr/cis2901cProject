package cis2901c.main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import cis2901c.listeners.CustomerSearchListener;
import cis2901c.listeners.DbServices;
import cis2901c.listeners.InvoicePartTableListener;
import cis2901c.listeners.JobDetailsModifiedListener;
import cis2901c.listeners.JobNameModifiedListener;
import cis2901c.listeners.RepairOrderCancelListener;
import cis2901c.listeners.RepairOrderCloseListener;
import cis2901c.listeners.RepairOrderJobAddListener;
import cis2901c.listeners.RepairOrderJobDeleteListener;
import cis2901c.listeners.RepairOrderJobSelectedListener;
import cis2901c.listeners.RepairOrderLaborAddListener;
import cis2901c.listeners.RepairOrderLaborDeleteListener;
import cis2901c.listeners.RepairOrderLaborTableListener;
import cis2901c.listeners.RepairOrderPartDeleteLineItemListener;
import cis2901c.listeners.RequiredTextBoxModifyListener;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.listeners.UnitSearchListener;
import cis2901c.objects.Job;
import cis2901c.objects.JobLabor;
import cis2901c.objects.JobLaborTable;
import cis2901c.objects.MyText;
import cis2901c.objects.RepairOrder;
import cis2901c.objects.RepairOrderJobTable;
import cis2901c.objects.RepairOrderJobTableItem;
import cis2901c.objects.Unit;
import cis2901c.objects.JobPart;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.swt.widgets.Composite;

import cis2901c.objects.Customer;
import cis2901c.objects.InvoicePartTable;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class RepairOrderDialog extends Dialog {

	protected Object result;
	protected Shell shlRepairOrder;
	
	private RepairOrderJobTable tableJobsRepairOrder;
	
	private Button btnSaveRo;
	private Button btnCancel;
	private Button btnClose;
	private Button btnAddJob;
	private Button btnDeleteJob;
	private Button btnCashierRo;
	private Button btnDeleteLineItem; 
	private Button btnAddLaborLine;
	private Button btnDeleteLaborLine;
	private TabFolder tabFolderJobsRepairOrder;

	private MyText txtSubTotalRepairOrder;
	private MyText textTaxRepairOrder;
	private MyText textFinalTotalRepairOrder;
	
	private long roId = -1;
	private Text textRoNum;
	private Text textCreatedDate;
	private Text textCashieredDate;
	private MyText txtCustomerRepairOrder;
	private MyText txtUnitRepairOrder;
	private MyText txtJobName;
	private MyText txtComplaints;
	private MyText txtResolution;
	private MyText txtReccomendations;
	
	private InvoicePartTable jobPartsTable;
	private JobLaborTable jobLaborTable;
	
	private Map<String, List<Long>> detailsToDelete = new HashMap<>();
	
	private RepairOrder currentRepairOrder;
	private boolean cashiered = false;
	
	private CustomerSearchListener customerSearchListener;
	private UnitSearchListener unitSearchListener;
	private JobNameModifiedListener jobNameModifiedListener;
	private InvoicePartTableListener repairOrderPartTableListener;
	private RepairOrderLaborTableListener repairOrderLaborTableListener;
	private JobDetailsModifiedListener jobDetailsModifiedListener;
	private TextBoxFocusListener jobNameFocusListener;
	private TextBoxFocusListener complaintsFocusListener;
	private TextBoxFocusListener resolutionFocusListener;
	private TextBoxFocusListener reccomendationsFocusListener;
	
	
	private static final String ONLY_DECIMALS = "[^0-9.]";		// find a better name
	
	// TODO create isModified variable in this RepairOrderDialog object to track if ANYTHING in the underlying repairOrder has been modified
		// so we can ask the user if they want to save before closing RepairOrderDialog
	
	// RO getters
	public Shell getRoDialogShell() {
		return shlRepairOrder;
	}
	
	public Text getTextCashieredDate() {
		return textCashieredDate;
	}
	
	public MyText getTxtCustomerRepairOrder() {
		return txtCustomerRepairOrder;
	}

	public MyText getTxtUnitRepairOrder() {
		return txtUnitRepairOrder;
	}

	public RepairOrderJobTable getTableJobsRepairOrder() {
		return tableJobsRepairOrder;
	}
	
	public TabFolder getTabFolderJobsRepairOrder() {
		return tabFolderJobsRepairOrder;
	}
	
	public List<MyText> getJobDetailsText() {
		return List.of(txtJobName, txtComplaints, txtResolution, txtReccomendations);
	}
	
	public List<Button> getJobDetailsButtons() {
		return List.of(btnDeleteLineItem, btnAddLaborLine, btnDeleteLaborLine);
	}
	
	public InvoicePartTable getJobPartsTable() {
		return jobPartsTable;
	}
	
	public JobLaborTable getJobLaborTable() {
		return jobLaborTable;
	}
	
	public JobNameModifiedListener getJobNameModifiedListener() {
		return jobNameModifiedListener;
	}

	public JobDetailsModifiedListener getJobDetailsModifiedListener() {
		return jobDetailsModifiedListener;
	}

	public Map<String, List<Long>> getDetailsToDelete() {
		return detailsToDelete;
	}
	
	public void setDetailsToDelete(Map<String, List<Long>> newDetailsToDelete) {
		detailsToDelete = newDetailsToDelete;
	}
	
	public void addDetailsToDelete(String table, Long primaryKey) {
		detailsToDelete.computeIfAbsent(table, t -> new ArrayList<>()).add(primaryKey);
	}
	
	public boolean cashiered() {
		return cashiered;
	}
	// END RO getters

	public RepairOrderDialog(Shell parent, int style) {
		super(parent, style);		// TODO set customer name or RO number in setText
		setText("Repair Order Details");
	}

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
		setupListeners();
		
		// TODO at this point in code, I'm pretty sure the DB has already built us a complete RO object and we
			// don't need to load anything fro the DB again, maybe from here make a method that just sets everything from the current RO
			// object and only call loadRoFromDb() when hitting the Cancel button and we need to reset to the last known saved state
		loadRoFromDb(repairOrder);
		
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
		Group grpRepairOrderNum = new Group(shlRepairOrder, SWT.NONE);
		grpRepairOrderNum.setText("Repair Order");
		grpRepairOrderNum.setBounds(861, 10, 103, 61);
		
		textRoNum = new Text(grpRepairOrderNum, SWT.BORDER);
		textRoNum.setEditable(false);
		textRoNum.setBounds(10, 25, 83, 26);
		
		Group grpCreatedDate = new Group(shlRepairOrder, SWT.NONE);
		grpCreatedDate.setText("Created Date");
		grpCreatedDate.setBounds(676, 77, 288, 62);
		
		textCreatedDate = new Text(grpCreatedDate, SWT.BORDER);
		textCreatedDate.setEditable(false);
		textCreatedDate.setBounds(10, 26, 268, 26);
		
		Group grpCashieredDate = new Group(shlRepairOrder, SWT.NONE);
		grpCashieredDate.setText("Cashiered Date");
		grpCashieredDate.setBounds(676, 144, 288, 62);
		
		textCashieredDate = new Text(grpCashieredDate, SWT.BORDER);
		textCashieredDate.setEditable(false);
		textCashieredDate.setBounds(10, 26, 268, 26);
		
		// Customer Data
		txtCustomerRepairOrder = new MyText(shlRepairOrder, SWT.BORDER | SWT.WRAP);
		txtCustomerRepairOrder.setEditable(false);
		txtCustomerRepairOrder.setText("Customer...");
		txtCustomerRepairOrder.setBounds(10, 10, 334, 128);
		txtCustomerRepairOrder.setBackground(SWTResourceManager.getColor(255, 102, 102));		// RED
		
		txtUnitRepairOrder = new MyText(shlRepairOrder, SWT.BORDER | SWT.WRAP);
		txtUnitRepairOrder.setEditable(false);
		txtUnitRepairOrder.setText("Unit...");
		txtUnitRepairOrder.setBounds(370, 10, 300, 128);
		txtUnitRepairOrder.setBackground(SWTResourceManager.getColor(255, 102, 102));		// RED
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
		tableJobsRepairOrder.setFocus();

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
		btnAddJob.setBounds(676, 212, 140, 74);
		btnAddJob.setText("Add Job");

		btnDeleteJob = new Button(shlRepairOrder, SWT.NONE);
		btnDeleteJob.setBounds(733, 292, 83, 52);
		btnDeleteJob.setText("Delete Job");
		// END Jobs table

		// RO Controls
		btnCashierRo = new Button(shlRepairOrder, SWT.NONE);
		btnCashierRo.setBounds(676, 10, 103, 64);
		btnCashierRo.setText("Cashier");

		btnSaveRo = new Button(shlRepairOrder, SWT.NONE);
		btnSaveRo.setBounds(822, 212, 142, 132);
		btnSaveRo.setText("Save");

		btnCancel = new Button(shlRepairOrder, SWT.NONE);
		btnCancel.setBounds(676, 292, 53, 52);
		btnCancel.setText("Cancel");
		
		btnClose = new Button(shlRepairOrder, SWT.NONE);
		btnClose.setText("Close");
		btnClose.setBounds(785, 10, 70, 64);
		// END RO controls
	}
	
	private void jobTabs() {
		// Job Tabs Buttons
		// These buttons need to be before the TabFolder to be painted over top of it
		btnDeleteLineItem = new Button(shlRepairOrder, SWT.NONE);
		btnDeleteLineItem.setBounds(386, 350, 121, 56);
		btnDeleteLineItem.setText("Delete Line Item");
		btnDeleteLineItem.setVisible(false);
		btnDeleteLineItem.setEnabled(false);

		btnAddLaborLine = new Button(shlRepairOrder, SWT.NONE);
		btnAddLaborLine.setBounds(255, 350, 121, 56);
		btnAddLaborLine.setText("Add Labor");
		btnAddLaborLine.setVisible(false);
		btnAddLaborLine.setEnabled(false);

		btnDeleteLaborLine = new Button(shlRepairOrder, SWT.NONE);
		btnDeleteLaborLine.setBounds(386, 350, 121, 56);
		btnDeleteLaborLine.setText("Delete Labor");
		btnDeleteLaborLine.setVisible(false);
		btnDeleteLaborLine.setEnabled(false);
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
		// END Labor tab
		// END Job tabs
	}
	
	private void setupListeners() {
		// LISTENERS
		// RO Controls
		shlRepairOrder.addListener(SWT.Traverse, event -> {		// disable pressing [Esc] to close the dialog
			if (event.detail == SWT.TRAVERSE_ESCAPE) {
				event.doit = false;
			}
		});
		
		shlRepairOrder.addListener(SWT.Close, new RepairOrderCloseListener(this));

		btnCancel.addMouseListener(new RepairOrderCancelListener(this));
		
		btnSaveRo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (roId == -1) {
					saveNewRo();
				} else {
					saveRo(currentRepairOrder);
				}
			}
		});
		
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shlRepairOrder.close();
			}
		});
		
		btnCashierRo.addMouseListener(new MouseAdapter() {
			// TODO refactor into it's own class
			@Override
			public void mouseDown(MouseEvent e) {
				// spawn amount due dialog box
				if (txtCustomerRepairOrder.getData() == null) {
					MessageBox customerRequiredBox = new MessageBox(shlRepairOrder, SWT.ICON_INFORMATION);
					customerRequiredBox.setText("Notice");
					customerRequiredBox.setMessage("Please select a Customer");
					customerRequiredBox.open();
					return;
				}
				AmountDueDialog amountDueDialog = new AmountDueDialog(shlRepairOrder, getStyle());
				boolean cashieredNow = amountDueDialog.open(textFinalTotalRepairOrder.getText());

				if (cashieredNow) {
					Main.getLogger().log(Level.INFO, "RO Cashiered");
					currentRepairOrder.setClosedDate(Timestamp.from(Instant.now()));
					cashiered = true;
					saveRo(currentRepairOrder);
					shlRepairOrder.close();
				}
			}
		});
		
		customerSearchListener = new CustomerSearchListener(txtCustomerRepairOrder);
		txtCustomerRepairOrder.addMouseListener(customerSearchListener);
		txtCustomerRepairOrder.addModifyListener(new RequiredTextBoxModifyListener(txtCustomerRepairOrder));

		unitSearchListener = new UnitSearchListener(txtUnitRepairOrder);
		txtUnitRepairOrder.addMouseListener(unitSearchListener);
		txtUnitRepairOrder.addModifyListener(new RequiredTextBoxModifyListener(txtUnitRepairOrder));
		// END RO Controls
		
		// Job Controls
		btnAddJob.addMouseListener(new RepairOrderJobAddListener(this));

		btnDeleteJob.addMouseListener(new RepairOrderJobDeleteListener(this));

		tableJobsRepairOrder.addSelectionListener(new RepairOrderJobSelectedListener(this));

		btnDeleteLineItem.addMouseListener(new RepairOrderPartDeleteLineItemListener(jobPartsTable, tableJobsRepairOrder, this));

		btnAddLaborLine.addMouseListener(new RepairOrderLaborAddListener(this));

		btnDeleteLaborLine.addMouseListener(new RepairOrderLaborDeleteListener(this));

		tabFolderJobsRepairOrder.addSelectionListener(new SelectionAdapter() {
			// TODO refactor into it's own class
			@Override		// set visibility of Tab function buttons
			public void widgetSelected(SelectionEvent e) {
				if (tabFolderJobsRepairOrder.getSelectionIndex() == 0) {		// Job Details tab
					btnDeleteLineItem.setVisible(false);
					btnAddLaborLine.setVisible(false);
					btnDeleteLaborLine.setVisible(false);
				} else if (tabFolderJobsRepairOrder.getSelectionIndex() == 1) {		// Parts tab
					Main.getLogger().log(Level.INFO, "Repair Order Parts tab selected");
					btnDeleteLineItem.setVisible(true);
					btnAddLaborLine.setVisible(false);
					btnDeleteLaborLine.setVisible(false);
				} else if (tabFolderJobsRepairOrder.getSelectionIndex() == 2) {		// Labor tab
					Main.getLogger().log(Level.INFO, "Repair Order Labor tab selected");
					btnDeleteLineItem.setVisible(false);
					btnAddLaborLine.setVisible(true);
					btnDeleteLaborLine.setVisible(true);
				}
			}
		});

		jobNameModifiedListener = new JobNameModifiedListener(txtJobName, tableJobsRepairOrder);
		txtJobName.addModifyListener(jobNameModifiedListener);
		jobNameFocusListener = new TextBoxFocusListener(txtJobName);
		txtJobName.addFocusListener(jobNameFocusListener);

		complaintsFocusListener = new TextBoxFocusListener(txtComplaints);
		txtComplaints.addFocusListener(complaintsFocusListener);

		resolutionFocusListener = new TextBoxFocusListener(txtResolution);
		txtResolution.addFocusListener(resolutionFocusListener);
		
		reccomendationsFocusListener = new TextBoxFocusListener(txtReccomendations);
		txtReccomendations.addFocusListener(reccomendationsFocusListener);

		repairOrderPartTableListener = new InvoicePartTableListener(jobPartsTable, tableJobsRepairOrder, this, shlRepairOrder);
		jobPartsTable.addListener(SWT.MouseDown, repairOrderPartTableListener);

		repairOrderLaborTableListener = new RepairOrderLaborTableListener(jobLaborTable, tableJobsRepairOrder, this);
		jobLaborTable.addListener(SWT.MouseDown, repairOrderLaborTableListener);

		jobDetailsModifiedListener = new JobDetailsModifiedListener(this);
		txtJobName.addModifyListener(jobDetailsModifiedListener);
		txtComplaints.addModifyListener(jobDetailsModifiedListener);
		txtResolution.addModifyListener(jobDetailsModifiedListener);
		txtReccomendations.addModifyListener(jobDetailsModifiedListener);
		
		// END LISTENERS
	}
	
	public void reloadRo() {
		loadRoFromDb(currentRepairOrder);
	}
	
	private void loadRoFromDb(RepairOrder repairOrder) {
		// set Dialog boxes and stuff from repairOrder fields
		if (repairOrder != null) {
			currentRepairOrder = repairOrder;
			roId = repairOrder.getRepairOrderId();
			textRoNum.setText(Long.toString(roId));
			
			textCreatedDate.setText(repairOrder.getCreatedDate().toString());
			textCashieredDate.setText(repairOrder.getClosedDate() == null ? "" : repairOrder.getClosedDate().toString());
			
			if (!textCashieredDate.getText().equals("")) {
				disableRoControls();
			}
			
			if (repairOrder.getCustomerId() != 0) {
				txtCustomerRepairOrder.setData(DbServices.searchForObjectByPk(new Customer(repairOrder.getCustomerId())));
				txtCustomerRepairOrder.setText(repairOrder.getCustomerName() + "\n" + repairOrder.getCustomerData());
			}

			if (repairOrder.getUnitId() != 0) {
				Unit roUnit = (Unit) DbServices.searchForObjectByPk(new Unit(repairOrder.getUnitId()));
				txtUnitRepairOrder.setData(roUnit);
				txtUnitRepairOrder.setText(roUnit.getYear() + " " + roUnit.getMake() + "\n" +
						roUnit.getModel() + "\nVin: " + roUnit.getVin() + "\nColor: " +
						roUnit.getColor() + "\n" + "Mileage: " + roUnit.getMileage());
//				txtUnitRepairOrder.setText(repairOrder.getUnitYear() + " " + repairOrder.getUnitMake() + "\n" +
//						repairOrder.getUnitModel() + "\n" + repairOrder.getUnitVin());
//				txtUnitRepairOrder.setData(DbServices.searchForObjectByPk(new Unit(repairOrder.getUnitId())));
			}

			List<Job> roJobs = loadRoJobsFromDb();
			repairOrder.setJobs(roJobs);
			tableJobsRepairOrder.setSelection(0);
			tableJobsRepairOrder.notifyListeners(SWT.Selection, new Event());
			this.calcRoTotal();
		}
	}

	private void disableRoControls() {
		// disable editing of text boxes, disable buttons
			// remove listeners from Customer and Unit text box and from Parts and Labor tables
		cashiered = true;
		txtCustomerRepairOrder.removeMouseListener(customerSearchListener);
		txtUnitRepairOrder.removeMouseListener(unitSearchListener);
		btnCashierRo.setEnabled(false);
		btnAddJob.setEnabled(false);
		btnDeleteJob.setEnabled(false);
		btnCancel.setEnabled(false);
		btnSaveRo.setEnabled(false);
		btnDeleteLineItem.setEnabled(false);
		btnAddLaborLine.setEnabled(false);
		btnDeleteLaborLine.setEnabled(false);
		txtJobName.setEditable(false);
		txtJobName.removeFocusListener(jobNameFocusListener);
		txtComplaints.setEditable(false);
		txtComplaints.removeFocusListener(complaintsFocusListener);
		txtResolution.setEditable(false);
		txtResolution.removeFocusListener(resolutionFocusListener);
		txtReccomendations.setEditable(false);
		txtReccomendations.removeFocusListener(reccomendationsFocusListener);
		jobPartsTable.removeListener(SWT.MouseDown, repairOrderPartTableListener);
		jobLaborTable.removeListener(SWT.MouseDown, repairOrderLaborTableListener);		
	}
	
	private List<Job> loadRoJobsFromDb() {
		List<Job> roJobs = new ArrayList<>();
		for (Job job : (Job[]) DbServices.searchForObjectsByPk(new Job(roId))) {
			if (job == null) {
				break;
			}
			RepairOrderJobTableItem jobTableItem = new RepairOrderJobTableItem(tableJobsRepairOrder, getStyle());

			// find and build labor
			List<JobLabor> jobLabor = new ArrayList<>(Arrays.asList((JobLabor[]) DbServices.searchForObjectsByPk(new JobLabor(job.getJobId()))));
			jobLabor.removeAll(Collections.singleton(null));
			job.setLabor(jobLabor);

			// find and build Parts
			List<JobPart> listOfJobParts = new ArrayList<>(Arrays.asList((JobPart[]) DbServices.searchForObjectsByPk(new JobPart(job.getJobId()))));
			listOfJobParts.removeAll(Collections.singleton(null));
			if (!cashiered) {
				checkPartsOnHand(job, listOfJobParts);
			}
			
			job.setJobParts(listOfJobParts);
			roJobs.add(job);
			jobTableItem.setData(job);
		}
		return roJobs;
	}

	private void checkPartsOnHand(Job job, List<JobPart> jobParts) {
		// check each jobPart and see if current jobPart.quantity > jobPart.getPart.getOnHand, if so inform user
			// with warning dialog and change quantity to onHand
		boolean partsOnHandChanged = false;
		Map<String, List<JobPart>> changedParts = new HashMap<>();

		for (JobPart jobPart : jobParts) {
			if (jobPart.getQuantity() > jobPart.getPart().getOnHand()) {
				partsOnHandChanged = true;
				changedParts.computeIfAbsent(job.getJobName(), jp -> new ArrayList<>()).add(jobPart);
				jobPart.setQuantity(jobPart.getPart().getOnHand());
			}
		}
		if (partsOnHandChanged) {
			PartQuantityWarning partQuantityWarning = new PartQuantityWarning(shlRepairOrder, getStyle());
			partQuantityWarning.setParts(changedParts);
			partQuantityWarning.open();
		}		
	}

	public void disableJobTabs() {
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
		btnDeleteLineItem.setEnabled(false);
		btnAddLaborLine.setEnabled(false);
		btnDeleteLaborLine.setEnabled(false);
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
			JobPart currentTIPart = (JobPart) currentPartTableItem.getData();
			List<JobPart> currentParts = currentJob.getJobParts();
			if (currentTIPart != null && !currentParts.contains(currentTIPart)) {
				currentJob.addJobPart((JobPart) currentPartTableItem.getData());
			}
		}

		for (TableItem currentLaborTableItem : jobLaborTable.getItems()) {
			if (!currentJob.getLabor().contains(currentLaborTableItem.getData()))
				currentJob.addLabor((JobLabor) currentLaborTableItem.getData());
		}
	}
	
	private void saveNewRo() {
		RepairOrder repairOrder = new RepairOrder();
		repairOrder.setCreatedDate(Timestamp.from(Instant.now()));
		textCreatedDate.setText(repairOrder.getCreatedDate().toString());
		saveRo(repairOrder);
	}
	
	private void saveRo(RepairOrder repairOrder) {
		if (txtCustomerRepairOrder.getData() != null && ((Customer) txtCustomerRepairOrder.getData()).getCustomerId() != -1) {
			repairOrder.setCustomerId(((Customer) txtCustomerRepairOrder.getData()).getCustomerId());
			repairOrder.setCustomerName(txtCustomerRepairOrder.getText(0, txtCustomerRepairOrder.getText().indexOf('\n')).split(","));
			repairOrder.setCustomerData(txtCustomerRepairOrder.getText().substring(txtCustomerRepairOrder.getText().indexOf('\n')));
		} else {
			// if we haven't selected a Customer, complain - an RO Customer is required
			MessageBox customerRequiredBox = new MessageBox(shlRepairOrder, SWT.ICON_INFORMATION);
			customerRequiredBox.setText("Notice");
			customerRequiredBox.setMessage("Please select a Customer");
			customerRequiredBox.open();
			return;
		}
		
		if (txtUnitRepairOrder.getData() != null && ((Unit) txtUnitRepairOrder.getData()).getUnitId() != -1) {
			Unit thisUnit = (Unit) txtUnitRepairOrder.getData();
			repairOrder.setUnitId(thisUnit.getUnitId());
			repairOrder.setUnitYear(Integer.toString(thisUnit.getYear()));
			repairOrder.setUnitMake(thisUnit.getMake());
			repairOrder.setUnitModel(thisUnit.getModel());
			repairOrder.setUnitVin(thisUnit.getVin());
		} else {
			// if we haven't selected a Unit, complain - an RO Unit is required
			MessageBox customerRequiredBox = new MessageBox(shlRepairOrder, SWT.ICON_INFORMATION);
			customerRequiredBox.setText("Notice");
			customerRequiredBox.setMessage("Please select a Unit");
			customerRequiredBox.open();
			return;
		}
		
		// TODO do something with the returned updateCount
		DbServices.deleteDetailsFromRo(detailsToDelete);
		
		if (tableJobsRepairOrder.getItemCount() > 0) {
			for (TableItem jobTableItem : tableJobsRepairOrder.getItems()) {
				if (!repairOrder.getJobs().contains(jobTableItem.getData())) {		// maybe use HashSet here
					repairOrder.addJob((Job) jobTableItem.getData());
				}
			}
		}
		
		repairOrder.setTax(new BigDecimal(textTaxRepairOrder.getText().replaceAll(ONLY_DECIMALS, "")));
		repairOrder.setTotal(new BigDecimal(textFinalTotalRepairOrder.getText().replaceAll(ONLY_DECIMALS, "")));
		
		// send RepairOrder object to DbServices
		DbServices.saveObject(repairOrder);
		roId = repairOrder.getRepairOrderId();
		textRoNum.setText(Long.toString(roId));
		detailsToDelete = new HashMap<>();
		currentRepairOrder = repairOrder;
		result = repairOrder;
	}
}
