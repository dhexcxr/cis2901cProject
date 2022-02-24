package cis2901c.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TableItem;
import cis2901c.main.Main;
import cis2901c.objects.Job;
import cis2901c.objects.Labor;
import cis2901c.objects.MyTable;
import cis2901c.objects.MyText;
import cis2901c.objects.Part;

public class JobModifiedListener implements Listener{
	
	TabFolder tabFolderJobsRepairOrder;
	List<MyText> jobDetailWidgets;
	List<MyTable> jobTables;

	public JobModifiedListener(TabFolder tabFolderJobsRepairOrder, List<MyText> jobDetailWidgets,
			List<MyTable> jobTables) {
		this.tabFolderJobsRepairOrder = tabFolderJobsRepairOrder;
		this.jobDetailWidgets = jobDetailWidgets;
		this.jobTables = jobTables;
	}

//	public JobModifiedListener(TabFolder tabFolderJobsRepairOrder2, List<MyText> jobDetailWidgets,
//			List<MyTable> jobTables) {
//		// TODO Auto-generated constructor stub
//	}

//	@Override
//	public void focusGained(FocusEvent e) {
//		super.focusGained(e);
//		saveJob();
//	}
//
//	@Override
//	public void focusLost(FocusEvent e) {
//		super.focusLost(e);
//		saveJob();
//	}
	
	private void saveJob() {
		Job currentJob = (Job) tabFolderJobsRepairOrder.getData();
//		currentJob.setJobName(jobDetailWidgets.get(0).getText());
//		currentJob.setComplaints(jobDetailWidgets.get(1).getText());
//		currentJob.setResolution(jobDetailWidgets.get(2).getText());
//		currentJob.setReccomendations(jobDetailWidgets.get(3).getText());
		currentJob.setParts(new ArrayList<>());
		for (TableItem currentPartTableItem : jobTables.get(0).getItems()) {
			currentJob.addPart((Part) currentPartTableItem.getData());
		}
		currentJob.setLabor(new ArrayList<>());
		for (TableItem currentLaborTableItem : jobTables.get(1).getItems()) {
			currentJob.addLabor((Labor) currentLaborTableItem.getData());
		}
		
	}

	@Override
	public void handleEvent(Event event) {
		Main.log(Level.INFO, "JobModifiedListener called by:\n" + event.widget);
		if (event.type == SWT.BUTTON4) {
			saveJob();
		}
	}

}