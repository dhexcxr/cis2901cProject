package cis2901c.main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import cis2901c.listeners.CustomerSearchListener;
import cis2901c.listeners.RequiredTextBoxModifyListener;
import cis2901c.listeners.UnitSearchListener;
import cis2901c.objects.Job;
import cis2901c.objects.MyText;
import cis2901c.objects.RepairOrderJobTable;
import cis2901c.objects.RepairOrderJobTableItem;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import java.util.function.Consumer;

public class RepairOrderDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private MyText txtCustomerRepairOrder;
	private MyText txtUnitRepairOrder;
	private RepairOrderJobTable tableJobsRepairOrder;
	private Text txtSubTotalRepairOrder;
	private Text textTaxRepairOrder;
	private Text textFinalTotalRepairOrder;

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
				if (tableJobsRepairOrder.getSelectionIndex() >= 0 && tableJobsRepairOrder.getSelectionIndex() < tableJobsRepairOrder.getItemCount()
						&& tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).getData() != null) {
					tableJobsRepairOrder.remove(tableJobsRepairOrder.getSelectionIndex());
				}
			}
		});
		
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
		
		txtSubTotalRepairOrder = new Text(grpTotals, SWT.BORDER);
		txtSubTotalRepairOrder.setBounds(10, 26, 128, 26);
		txtSubTotalRepairOrder.setEditable(false);
		txtSubTotalRepairOrder.setText("SubTotal");
		
		Label lblTax = new Label(grpTotals, SWT.NONE);
		lblTax.setBounds(148, 0, 21, 20);
		lblTax.setText("Tax");
		
		textTaxRepairOrder = new Text(grpTotals, SWT.BORDER);
		textTaxRepairOrder.setBounds(148, 26, 128, 26);
		textTaxRepairOrder.setText("Tax");
		textTaxRepairOrder.setEditable(false);
		
		Label lblFinalTotal = new Label(grpTotals, SWT.NONE);
		lblFinalTotal.setBounds(286, 0, 70, 20);
		lblFinalTotal.setText("Final Total");
		
		textFinalTotalRepairOrder = new Text(grpTotals, SWT.BORDER);
		textFinalTotalRepairOrder.setBounds(286, 26, 128, 26);
		textFinalTotalRepairOrder.setText("Final Total");
		textFinalTotalRepairOrder.setEditable(false);
		
		TabFolder tabFolderJobsRepairOrder = new TabFolder(shell, SWT.NONE);
		tabFolderJobsRepairOrder.setBounds(10, 383, 954, 296);
		
		TabItem tbtmJobDetails = new TabItem(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmJobDetails.setText("Job Details");
		
		TabItem tbtmParts = new TabItem(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmParts.setText("Parts");
		
		TabItem tbtmLabor = new TabItem(tabFolderJobsRepairOrder, SWT.NONE);
		tbtmLabor.setText("Labor");

	}
}
