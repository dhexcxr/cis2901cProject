package cis2901c.listeners;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.MyText;
import cis2901c.objects.RepairOrderJobTable;
import cis2901c.objects.RepairOrderJobTableItem;

public class RepairOrderPartEditorListener extends InvoicePartEditorListener {
	
	private List<MyText> invoiceDetailText;
	private RepairOrderJobTable tableJobsRepairOrder;
	private boolean ignoreFocusOut = false;

	public RepairOrderPartEditorListener(InvoicePartTable partInvoiceTable, int selectedTableItemIndex,
			int selectedColumnIndex, Text editorTxtBox, List<MyText> invoiceDetailText, RepairOrderJobTable tableJobsRepairOrder) {
		super(partInvoiceTable, selectedTableItemIndex, selectedColumnIndex, editorTxtBox, invoiceDetailText);
		// TODO connect Job Part total
		this.invoiceDetailText = invoiceDetailText;
		this.tableJobsRepairOrder = tableJobsRepairOrder;
	}
	
	@Override
	public void handleEvent(Event event) {
		if (!ignoreFocusOut) {
			ignoreFocusOut = true;
			super.handleEvent(event);
			totalParts();
			ignoreFocusOut = false;
		}
	}
	
	private void totalParts() {
//		String textToParse = invoiceDetailText.get(0).getText();
//		tableJobsRepairOrder.getSelection()[0].setText(RepairOrderJobTable.PART_TOTAL_COLUMN, textToParse);
		
		RepairOrderJobTableItem selectedJobTableItem = (RepairOrderJobTableItem) tableJobsRepairOrder.getSelection()[0];
		String textToParse = invoiceDetailText.get(0).getText();
		textToParse = textToParse.replaceAll("[^0-9.]", "");
		BigDecimal partTotal = new BigDecimal(textToParse);
		selectedJobTableItem.setPartTotal(partTotal);
		selectedJobTableItem.setText(RepairOrderJobTable.PART_TOTAL_COLUMN, "$" + partTotal.toString());
		selectedJobTableItem.setText(RepairOrderJobTable.JOB_TOTAL_COLUMN, "$" + (partTotal.add(selectedJobTableItem.getLaborTotal()).toString()));
		tableJobsRepairOrder.notifyListeners(SWT.BUTTON5, new Event());
	}

	
}
