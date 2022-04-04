package cis2901c.listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TableItem;

import cis2901c.main.RepairOrderDialog;
import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.InvoicePartTableItem;
import cis2901c.objects.Job;
import cis2901c.objects.JobLabor;
import cis2901c.objects.JobLaborTable;
import cis2901c.objects.JobLaborTableItem;
import cis2901c.objects.JobPart;
import cis2901c.objects.MyText;
import cis2901c.objects.RepairOrderJobTable;

public class RepairOrderJobSelectedListener extends SelectionAdapter {
	
	private RepairOrderDialog roDialog;
	private TabFolder tabFolderJobsRepairOrder;
	private RepairOrderJobTable tableJobsRepairOrder;
	private List<MyText> jobDetailsText;
	private InvoicePartTable jobPartsTable;
	private JobLaborTable jobLaborTable;
	private List<Button> jobDetailsButtons;

	public RepairOrderJobSelectedListener(RepairOrderDialog roDialog) {
		this.roDialog = roDialog;
		this.tabFolderJobsRepairOrder = roDialog.getTabFolderJobsRepairOrder();
		this.tableJobsRepairOrder = roDialog.getTableJobsRepairOrder();
		this.jobDetailsText = roDialog.getJobDetailsText();
		this.jobPartsTable = roDialog.getJobPartsTable();
		this.jobLaborTable = roDialog.getJobLaborTable();
		this.jobDetailsButtons = roDialog.getJobDetailsButtons();
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (tableJobsRepairOrder.getSelectionIndex() < 0 || tableJobsRepairOrder.getSelectionIndex() >= tableJobsRepairOrder.getItemCount()) {
			return;
		}
		
		jobDetailsText.get(0).removeModifyListener(roDialog.getJobNameModifiedListener());
		jobDetailsText.get(0).removeModifyListener(roDialog.getJobDetailsModifiedListener());
		jobDetailsText.get(1).removeModifyListener(roDialog.getJobDetailsModifiedListener());
		jobDetailsText.get(2).removeModifyListener(roDialog.getJobDetailsModifiedListener());
		jobDetailsText.get(3).removeModifyListener(roDialog.getJobDetailsModifiedListener());
		
		// job selected
		jobDetailsText.get(0).setEnabled(true);
		jobDetailsText.get(1).setEnabled(true);
		jobDetailsText.get(2).setEnabled(true);
		jobDetailsText.get(3).setEnabled(true);
		jobPartsTable.setEnabled(true);
		jobLaborTable.setEnabled(true);
		
		jobDetailsButtons.get(0).setEnabled(true);
		jobDetailsButtons.get(1).setEnabled(true);
		jobDetailsButtons.get(2).setEnabled(true);
		
		Job selectedJob = (Job) tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).getData();
		// copy selected Job to Job Tabs
		if (selectedJob != null && !selectedJob.equals(tabFolderJobsRepairOrder.getData())) {
			tabFolderJobsRepairOrder.setData(selectedJob);
			// set Job data into Job Tabs
			jobDetailsText.get(0).setText(selectedJob.getJobName().equals("") ? "Job Name..." : selectedJob.getJobName());
			jobDetailsText.get(1).setText(selectedJob.getComplaints().equals("") ? "Complaints..." : selectedJob.getComplaints());
			jobDetailsText.get(2).setText(selectedJob.getResolution().equals("") ? "Resolution..." : selectedJob.getResolution());
			jobDetailsText.get(3).setText(selectedJob.getReccomendations().equals("") ? "Reccomendations..." : selectedJob.getReccomendations());
			
			// TODO add other methods to set up Job Detail tabs, Part tab, and Labor tab
			jobPartsTable.removeAll();
//			for (Part part : selectedJob.getParts()) {
			for (JobPart jobPart : selectedJob.getJobParts()) {
				// TODO see how I stored quantity in Invoice, or figure out how to store quantity
				if (jobPart == null) {
					break;
				}
				addPartToPartTableItem(jobPart);
			}
			@SuppressWarnings("unused")				// this adds a new, empty TableItem at the end of the Invoice Line Items
			TableItem tableItem = new InvoicePartTableItem(jobPartsTable, SWT.NONE);	// so we can add parts
			
			jobLaborTable.removeAll();
			for (JobLabor labor : selectedJob.getLabor()) {
				if (labor == null) {
					break;
				}
				addLaborToLaborTableItem(labor);
			}
		}

		
		jobDetailsText.get(0).addModifyListener(roDialog.getJobNameModifiedListener());
		jobDetailsText.get(0).addModifyListener(roDialog.getJobDetailsModifiedListener());
		jobDetailsText.get(1).addModifyListener(roDialog.getJobDetailsModifiedListener());
		jobDetailsText.get(2).addModifyListener(roDialog.getJobDetailsModifiedListener());
		jobDetailsText.get(3).addModifyListener(roDialog.getJobDetailsModifiedListener());
	}
	
//	private void addPartToPartTableItem(Part part) {
	private void addPartToPartTableItem(JobPart jobPart) {
		// used in tableJobsRepairOrder.addSelectionListener
		InvoicePartTableItem jobPartTableItem = new InvoicePartTableItem(jobPartsTable, roDialog.getStyle());
		jobPartTableItem.setText(new String[] {jobPart.getPartNumber(), jobPart.getDescription(), Integer.toString(jobPart.getQuantity()),
				Integer.toString(jobPart.getPart().getOnHand()), jobPart.getPart().getCost().toString(),
				jobPart.getSoldPrice().toString(), jobPart.getSoldPrice().multiply(BigDecimal.valueOf(jobPart.getQuantity())).toString()});
//		jobPartTableItem.setData(jobPart.getPart());
		jobPartTableItem.setData(jobPart);
	}
	
	private void addLaborToLaborTableItem(JobLabor jobLabor) {
		// used in tableJobsRepairOrder.addSelectionListener
		JobLaborTableItem jobLaborTableItem = new JobLaborTableItem(jobLaborTable, roDialog.getStyle());
		jobLaborTableItem.setText(new String[] {jobLabor.getTechnician(), jobLabor.getDescription(), jobLabor.getHours().toString(), jobLabor.getLaborRate().toString(),
							jobLabor.getHours().multiply(jobLabor.getLaborRate()).setScale(2, RoundingMode.CEILING).toString()});
		jobLaborTableItem.setData(jobLabor);
	}

}
