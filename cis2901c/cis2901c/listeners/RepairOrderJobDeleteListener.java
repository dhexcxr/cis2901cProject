package cis2901c.listeners;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TabFolder;

import cis2901c.main.RepairOrderDialog;
import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.Job;
import cis2901c.objects.JobLaborTable;
import cis2901c.objects.MyText;
import cis2901c.objects.RepairOrderJobTable;

public class RepairOrderJobDeleteListener extends MouseAdapter {
	
	private RepairOrderDialog roDialog;
	private RepairOrderJobTable tableJobsRepairOrder;
	private TabFolder tabFolderJobsRepairOrder;
	private List<MyText> jobDetailsText;
	private InvoicePartTable jobPartsTable;
	private JobLaborTable jobLaborTable;

	public RepairOrderJobDeleteListener(RepairOrderDialog roDialog) {
		this.roDialog = roDialog;
		this.tableJobsRepairOrder = roDialog.getTableJobsRepairOrder();
		this.tabFolderJobsRepairOrder = roDialog.getTabFolderJobsRepairOrder();
		this.jobDetailsText = roDialog.getJobDetailsText();
		this.jobPartsTable = roDialog.getJobPartsTable();
		this.jobLaborTable = roDialog.getJobLaborTable();
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		// create new Job on Job Table
		int selectedIndex = -1;
		if (tableJobsRepairOrder.getSelectionIndex() >= 0 && tableJobsRepairOrder.getSelectionIndex() < tableJobsRepairOrder.getItemCount()
				&& tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).getData() != null) {

			Job selectedJob = (Job) tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).getData();
			// keep track of things to delete
			roDialog.addDetailsToDelete(selectedJob.getTableName(), selectedJob.getJobId());
			
			selectedIndex = tableJobsRepairOrder.getSelectionIndex();
			tableJobsRepairOrder.remove(selectedIndex);
			// remove Job we just deleted from tabFolder
			if (selectedJob.equals(tabFolderJobsRepairOrder.getData())) {
				tabFolderJobsRepairOrder.setData(null);
			}
			selectedIndex = selectedIndex == 0 ? 0 : selectedIndex - 1;
			tableJobsRepairOrder.setSelection(selectedIndex);
			tableJobsRepairOrder.notifyListeners(SWT.Selection, new Event());

			roDialog.calcRoTotal();
		}
		
		// disable Job Tabs if no jobs on Job Table
		if (tableJobsRepairOrder.getItemCount() == 0) {
			jobDetailsText.get(0).setEnabled(false);
			jobDetailsText.get(0).setText("Job Name...");
			jobDetailsText.get(1).setEnabled(false);
			jobDetailsText.get(1).setText("Complaints...");
			jobDetailsText.get(2).setEnabled(false);
			jobDetailsText.get(2).setText("Resolution...");
			jobDetailsText.get(3).setEnabled(false);
			jobDetailsText.get(3).setText("Reccomendations...");
			jobPartsTable.setEnabled(false);
			jobPartsTable.removeAll();
			jobLaborTable.setEnabled(false);
			jobLaborTable.removeAll();
		} 
	}

}
