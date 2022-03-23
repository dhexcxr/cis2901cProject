package cis2901c.listeners;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import cis2901c.main.RepairOrderDialog;
import cis2901c.objects.InvoicePart;
import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.JobPart;
import cis2901c.objects.MyText;
import cis2901c.objects.Part;
import cis2901c.objects.RepairOrderJobTable;
import cis2901c.objects.RepairOrderJobTableItem;

public class RepairOrderPartEditorListener extends InvoicePartEditorListener {
	
	private InvoicePartTable partInvoiceTable;
	private int selectedTableItemIndex;
	private List<MyText> invoiceDetailText;
	private RepairOrderJobTable tableJobsRepairOrder;
	private int currentJobTableItemIndex;
	private RepairOrderDialog repairOrderDialog;

	public RepairOrderPartEditorListener(InvoicePartTable partInvoiceTable, int selectedTableItemIndex,
			int selectedColumnIndex, Text editorTxtBox, List<MyText> invoiceDetailText, RepairOrderJobTable tableJobsRepairOrder, int currentJobTableItemIndex, RepairOrderDialog repairOrderDialog) {
		super(partInvoiceTable, selectedTableItemIndex, selectedColumnIndex, editorTxtBox, invoiceDetailText);
		// TODO connect Job Part total
		this.partInvoiceTable = partInvoiceTable;
		this.selectedTableItemIndex = selectedTableItemIndex;
		this.invoiceDetailText = invoiceDetailText;
		this.tableJobsRepairOrder = tableJobsRepairOrder;
		this.currentJobTableItemIndex = currentJobTableItemIndex;
		this.repairOrderDialog = repairOrderDialog;
	}
	
	@Override
	public void handleEvent(Event event) {
		if (!ignoreFocusOut) {
			if (partInvoiceTable.getItem(selectedTableItemIndex).getData() == null) {
				partInvoiceTable.getItem(selectedTableItemIndex).setData(new JobPart());
			}
			super.handleEvent(event);
//			tableJobsRepairOrder.notifyListeners(SWT.BUTTON4, new Event());		// save Parts and Labor
			if (partInvoiceTable.getItem(selectedTableItemIndex).getData() != null) {
//				Part part = ((InvoicePart) partInvoiceTable.getItem(selectedTableItemIndex).getData()).getPart();
//				partInvoiceTable.getItem(selectedTableItemIndex).setData(new JobPart(part));
//				InvoicePart invoicePart = (InvoicePart) partInvoiceTable.getItem(selectedTableItemIndex).getData();
				JobPart invoicePart = (JobPart) partInvoiceTable.getItem(selectedTableItemIndex).getData();
//				partInvoiceTable.getItem(selectedTableItemIndex).setData(new JobPart(invoicePart));
				partInvoiceTable.getItem(selectedTableItemIndex).setData(invoicePart);
				repairOrderDialog.saveJob();
				totalParts();
			}
		}
	}
	
	private void totalParts() {
		RepairOrderJobTableItem selectedJobTableItem = (RepairOrderJobTableItem) tableJobsRepairOrder.getItem(currentJobTableItemIndex);
		String textToParse = invoiceDetailText.get(0).getText();
		textToParse = textToParse.replaceAll("[^0-9.]", "");
		BigDecimal partTotal = new BigDecimal(textToParse);
		selectedJobTableItem.setPartTotal(partTotal);
		selectedJobTableItem.setText(RepairOrderJobTable.PART_TOTAL_COLUMN, "$" + partTotal.toString());
		selectedJobTableItem.setText(RepairOrderJobTable.JOB_TOTAL_COLUMN, "$" + (partTotal.add(selectedJobTableItem.getLaborTotal()).toString()));
		repairOrderDialog.calcRoTotal();
	}

	
}
