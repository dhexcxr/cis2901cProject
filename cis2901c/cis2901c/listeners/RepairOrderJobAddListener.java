package cis2901c.listeners;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TableItem;

import cis2901c.main.RepairOrderDialog;
import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.InvoicePartTableItem;
import cis2901c.objects.Job;
import cis2901c.objects.JobLaborTable;
import cis2901c.objects.MyText;
import cis2901c.objects.RepairOrderJobTable;
import cis2901c.objects.RepairOrderJobTableItem;

public class RepairOrderJobAddListener extends MouseAdapter {
	
	private RepairOrderDialog roDialog;
	private TabFolder tabFolderJobsRepairOrder;
	private RepairOrderJobTable tableJobsRepairOrder;
	private List<MyText> jobDetailsText;
	private InvoicePartTable jobPartsTable;
	private JobLaborTable jobLaborTable;

	public RepairOrderJobAddListener(RepairOrderDialog roDialog) {
		this.roDialog = roDialog;
		this.tabFolderJobsRepairOrder = roDialog.getTabFolderJobsRepairOrder();
		this.tableJobsRepairOrder = roDialog.getTableJobsRepairOrder();
		this.jobDetailsText = roDialog.getJobDetailsText();
		this.jobPartsTable = roDialog.getJobPartsTable();
		this.jobLaborTable = roDialog.getJobLaborTable();
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		// TODO judging by the comment on 2 lines below I think this might should've been for tableJobsRepairOrder
			// but it seems to be working fine so I don't know that its actually needed
		tabFolderJobsRepairOrder.notifyListeners(SWT.Selection, new Event());		// trigger saving Job details
		// create new Job on Job Table
		RepairOrderJobTableItem job = new RepairOrderJobTableItem(tableJobsRepairOrder, roDialog.getStyle());
		job.setData(new Job());
		tableJobsRepairOrder.setSelection(tableJobsRepairOrder.getItemCount() - 1);
		tableJobsRepairOrder.notifyListeners(SWT.Selection, new Event());
		
		
		jobDetailsText.get(0).setText("Job Name...");
		jobDetailsText.get(1).setText("Complaints...");
		jobDetailsText.get(2).setText("Resolution...");
		jobDetailsText.get(3).setText("Reccomendations...");
		jobPartsTable.removeAll();
		@SuppressWarnings("unused")				// this adds a new, empty TableItem at the end of the Invoice Line Items
		TableItem tableItem = new InvoicePartTableItem(jobPartsTable, SWT.NONE);	// so we can add parts
		jobLaborTable.removeAll();
	}

}
