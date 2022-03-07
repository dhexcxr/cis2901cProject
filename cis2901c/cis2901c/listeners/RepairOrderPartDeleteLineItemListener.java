package cis2901c.listeners;

import java.math.BigDecimal;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Text;

import cis2901c.main.RepairOrderDialog;
import cis2901c.objects.MyTable;
import cis2901c.objects.RepairOrderJobTable;
import cis2901c.objects.RepairOrderJobTableItem;

public class RepairOrderPartDeleteLineItemListener extends InvoicePartDeleteLineItemListener {
	
	private RepairOrderJobTable tableJobsRepairOrder;
	private RepairOrderDialog repairOrderDialog;

	public RepairOrderPartDeleteLineItemListener(MyTable repairOrderPartTableInvoice, RepairOrderJobTable tableJobsRepairOrder,
				RepairOrderDialog repairOrderDialog) {
		super(repairOrderPartTableInvoice);
		this.tableJobsRepairOrder = tableJobsRepairOrder;
		this.repairOrderDialog = repairOrderDialog;
		// TODO Auto-generated constructor stub
	}

	public RepairOrderPartDeleteLineItemListener(MyTable repairOrderPartTableInvoice, Text txtPartsTotalInvoice,
			Text txtTaxInvoice, Text txtFinalTotal) {
		super(repairOrderPartTableInvoice, txtPartsTotalInvoice, txtTaxInvoice, txtFinalTotal);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void mouseDown(MouseEvent event) {
		super.mouseDown(event);
		totalParts();
	}
	
	private void totalParts() {
		RepairOrderJobTableItem selectedJobTableItem = (RepairOrderJobTableItem) tableJobsRepairOrder.getSelection()[0];
		String textToParse = super.txtPartsTotalInvoice.getText();
		textToParse = textToParse.replaceAll("[^0-9.]", "");
		BigDecimal partTotal = new BigDecimal(textToParse.equals("") ? "0" : textToParse);
		selectedJobTableItem.setPartTotal(partTotal);
		selectedJobTableItem.setText(RepairOrderJobTable.PART_TOTAL_COLUMN, "$" + partTotal.toString());
		selectedJobTableItem.setText(RepairOrderJobTable.JOB_TOTAL_COLUMN, "$" + (partTotal.add(selectedJobTableItem.getLaborTotal()).toString()));
		repairOrderDialog.calcRoTotal();
	}

}
