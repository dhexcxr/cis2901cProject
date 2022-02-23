package cis2901c.listeners;

import java.util.List;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.MyText;
import cis2901c.objects.RepairOrderJobTable;

public class RepairOrderPartEditorListener extends InvoicePartEditorListener {
	
	private List<MyText> invoiceDetailText;
	private RepairOrderJobTable tableJobsRepairOrder;

	public RepairOrderPartEditorListener(InvoicePartTable partInvoiceTable, int selectedTableItemIndex,
			int selectedColumnIndex, Text editorTxtBox, List<MyText> invoiceDetailText, RepairOrderJobTable tableJobsRepairOrder) {
		super(partInvoiceTable, selectedTableItemIndex, selectedColumnIndex, editorTxtBox, invoiceDetailText);
		// TODO connect Job Part total
		this.invoiceDetailText = invoiceDetailText;
		this.tableJobsRepairOrder = tableJobsRepairOrder;
	}
	
	@Override
	public void handleEvent(Event event) {
		super.handleEvent(event);
		totalParts();
	}
	
	private void totalParts() {
		tableJobsRepairOrder.getSelection()[0].setText(RepairOrderJobTable.PART_TOTAL_COLUMN, invoiceDetailText.get(0).getText());
	}

	
}
