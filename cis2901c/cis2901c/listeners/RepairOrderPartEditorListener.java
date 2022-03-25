package cis2901c.listeners;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cis2901c.main.RepairOrderDialog;
import cis2901c.objects.InvoicePart;
import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.JobPart;
import cis2901c.objects.MyText;
import cis2901c.objects.RepairOrderJobTable;
import cis2901c.objects.RepairOrderJobTableItem;

public class RepairOrderPartEditorListener extends InvoicePartEditorListener {
	
	private InvoicePartTable partInvoiceTable;
	private int selectedTableItemIndex;
	private Text editorTxtBox;
	private List<MyText> invoiceDetailText;
	private Shell parent;
	private RepairOrderJobTable tableJobsRepairOrder;
	private int currentJobTableItemIndex;
	private RepairOrderDialog repairOrderDialog;

	public RepairOrderPartEditorListener(InvoicePartTable partInvoiceTable, int selectedTableItemIndex,
			int selectedColumnIndex, Text editorTxtBox, List<MyText> invoiceDetailText, Shell parent,
				RepairOrderJobTable tableJobsRepairOrder, int currentJobTableItemIndex,
					RepairOrderDialog repairOrderDialog) {
		super(partInvoiceTable, selectedTableItemIndex, selectedColumnIndex, editorTxtBox, invoiceDetailText, parent);
		// TODO connect Job Part total
		this.partInvoiceTable = partInvoiceTable;
		this.selectedTableItemIndex = selectedTableItemIndex;
		this.editorTxtBox = editorTxtBox;
		this.invoiceDetailText = invoiceDetailText;
		this.parent = parent;
		this.tableJobsRepairOrder = tableJobsRepairOrder;
		this.currentJobTableItemIndex = currentJobTableItemIndex;
		this.repairOrderDialog = repairOrderDialog;
	}
	
	@Override
	public void handleEvent(Event event) {
		if (!ignoreFocusOut && editorTxtBox.getText().length() > 0) {
			if (partInvoiceTable.getItem(selectedTableItemIndex).getData() == null) {
				partInvoiceTable.getItem(selectedTableItemIndex).setData(new JobPart());
			}
			super.handleEvent(event);
			InvoicePart newInvoicePart = (InvoicePart) partInvoiceTable.getItem(selectedTableItemIndex).getData(); 
			if (newInvoicePart != null && newInvoicePart.getPart() != null) {
				repairOrderDialog.saveJob();
				totalParts();
			} else {
				partInvoiceTable.getItem(selectedTableItemIndex).setData(null);
			}
		}
		editorTxtBox.dispose();
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
