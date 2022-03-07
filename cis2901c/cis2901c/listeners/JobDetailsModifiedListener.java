package cis2901c.listeners;

import java.util.List;
import java.util.logging.Level;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.TabFolder;
import cis2901c.main.Main;
import cis2901c.objects.Job;
import cis2901c.objects.MyTable;
import cis2901c.objects.MyText;

public class JobDetailsModifiedListener implements ModifyListener {

	TabFolder tabFolderJobsRepairOrder;
	List<MyText> jobDetailWidgets;
	List<MyTable> jobTables;

	public JobDetailsModifiedListener(TabFolder tabFolderJobsRepairOrder, List<MyText> jobDetailWidgets,
			List<MyTable> jobTables) {
		this.tabFolderJobsRepairOrder = tabFolderJobsRepairOrder;
		this.jobDetailWidgets = jobDetailWidgets;
		this.jobTables = jobTables;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		Main.log(Level.INFO, "JobModifyListener called by:\n" + e.widget);
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
