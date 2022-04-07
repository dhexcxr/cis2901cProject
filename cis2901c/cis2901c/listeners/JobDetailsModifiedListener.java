package cis2901c.listeners;

import java.util.List;
import java.util.logging.Level;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.TabFolder;
import cis2901c.main.Main;
import cis2901c.main.RepairOrderDialog;
import cis2901c.objects.Job;
import cis2901c.objects.MyText;

public class JobDetailsModifiedListener implements ModifyListener {

	private TabFolder tabFolderJobsRepairOrder;
	private List<MyText> jobDetailWidgets;
	
	public JobDetailsModifiedListener(RepairOrderDialog roDialog) {
		this.tabFolderJobsRepairOrder = roDialog.getTabFolderJobsRepairOrder();
		this.jobDetailWidgets = roDialog.getJobDetailsText();
	}

	@Override
	public void modifyText(ModifyEvent e) {
		Main.getLogger().log(Level.INFO, "JobDetailModifiedListener called by: {0}", e.widget);
		saveJobDetails();
	}

	
	private void saveJobDetails() {
		Job currentJob = (Job) tabFolderJobsRepairOrder.getData();
		if (currentJob == null) {
			return;
		}
		currentJob.setJobName(jobDetailWidgets.get(0).getText());
		currentJob.setComplaints(jobDetailWidgets.get(1).getText());
		currentJob.setResolution(jobDetailWidgets.get(2).getText());
		currentJob.setReccomendations(jobDetailWidgets.get(3).getText());		
	}
}
