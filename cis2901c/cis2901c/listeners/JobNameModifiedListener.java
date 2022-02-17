package cis2901c.listeners;

import java.util.logging.Level;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.TableItem;

import cis2901c.main.Main;
import cis2901c.objects.MyText;
import cis2901c.objects.RepairOrderJobTable;

public class JobNameModifiedListener extends InfoTextBoxModifyListener {
	
	private RepairOrderJobTable tableJobsRepairOrder;

	public JobNameModifiedListener(MyText text, RepairOrderJobTable tableJobsRepairOrder) {
		super(text);
		this.tableJobsRepairOrder = tableJobsRepairOrder;
	}
	
	@Override
	public void modifyText(ModifyEvent e) {
		// help track if text box has been modified
		super.modifyText(e);
		if (super.txtBox.isModified()) {
			Main.log(Level.INFO, "Job Name modified subclass is working");
			TableItem selectedJob = tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex());
			selectedJob.setText(0, super.txtBox.getText());
		} else {
			tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).setText(0, "");
		}
	}

}
