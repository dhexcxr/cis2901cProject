package cis2901c.listeners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cis2901c.main.RepairOrderDialog;
import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.MyText;
import cis2901c.objects.RepairOrder;
import cis2901c.objects.RepairOrderJobTable;

// TODO i don't think I need this

public class RepairOrderPartTableListener implements Listener{
	
	private InvoicePartTable partInvoiceTable;
	private TableEditor editor;
	private List<MyText> invoiceDetailText;
	private RepairOrderJobTable tableJobsRepairOrder;
	private RepairOrderDialog repairOrderDialog;

	public RepairOrderPartTableListener(InvoicePartTable invoicePartTable, RepairOrderJobTable tableJobsRepairOrder, RepairOrderDialog repairOrderDialog) {
		this(invoicePartTable, new ArrayList<>());
		this.invoiceDetailText.add(new MyText(new Shell(), 0));		// I think this is a total hack way to do this
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.tableJobsRepairOrder = tableJobsRepairOrder;
		this.repairOrderDialog = repairOrderDialog;
	}

	public RepairOrderPartTableListener(InvoicePartTable invoicePartTable, List<MyText> invoiceDetailText) {
		this.partInvoiceTable = invoicePartTable;
		this.invoiceDetailText = invoiceDetailText;
		
		this.editor = new TableEditor(partInvoiceTable);
		editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	}
	
	@Override
	public void handleEvent(Event event) {
        int currentPartTableItemIndex = partInvoiceTable.getSelectionIndex();
        Rectangle tableWidgetArea = partInvoiceTable.getClientArea();
        Point clickPoint = new Point(event.x, event.y);
        while (currentPartTableItemIndex < partInvoiceTable.getItemCount() && currentPartTableItemIndex >= 0) {		// scan through Invoice Part's TableItems
        	boolean visible = false;
        	final TableItem selectedTableItem = partInvoiceTable.getItem(currentPartTableItemIndex);
        	for (int i = 0; i < partInvoiceTable.getColumnCount(); i++) {		// find selected column
        		Rectangle selectedTableItemColumnBounds = selectedTableItem.getBounds(i);
        		if (selectedTableItemColumnBounds.contains(clickPoint) &&
        				(i == InvoicePartTable.PART_NUMBER_COLUMN || i == InvoicePartTable.QUANTITY_COLUMN || i == InvoicePartTable.PART_PRICE_COLUMN)) {
        			final int selectedColumnIndex = i;
        			Text editorTxtBox = new Text(partInvoiceTable, SWT.NONE);
        			int currentJobTableItemIndex = tableJobsRepairOrder.getSelectionIndex();
        			Listener textListener = new RepairOrderPartEditorListener(partInvoiceTable, currentPartTableItemIndex, selectedColumnIndex,
        																		editorTxtBox, invoiceDetailText,
        																		tableJobsRepairOrder, currentJobTableItemIndex, repairOrderDialog);
        			editor.setEditor(editorTxtBox, selectedTableItem, i);
        			editorTxtBox.addListener(SWT.FocusOut, textListener);
        			editorTxtBox.addListener(SWT.Traverse, textListener);
        			editorTxtBox.addListener(SWT.MouseDown, textListener);
        			editorTxtBox.setText(selectedTableItem.getText(i));
        			editorTxtBox.selectAll();
        			editorTxtBox.setFocus();
        			return;
        		}
        		if (!visible && selectedTableItemColumnBounds.intersects(tableWidgetArea)) {
        			visible = true;
        		}
        	}
        	if (!visible)
        		return;
        	currentPartTableItemIndex++;
        }
//        tableJobsRepairOrder.notifyListeners(SWT.BUTTON4, new Event());		// save Parts and Labor
	}
}
