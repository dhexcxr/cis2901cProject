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
import cis2901c.objects.InvoicePart;
import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.MyText;
import cis2901c.objects.RepairOrderJobTable;

public class RepairOrderPartTableListener implements Listener{
	
	private InvoicePartTable partInvoiceTable;
	private TableEditor editor;
	private List<MyText> invoiceDetailText;
	private RepairOrderJobTable tableJobsRepairOrder;
	private RepairOrderDialog repairOrderDialog;
	private Shell parent;
	
	private boolean visible = false;

	public RepairOrderPartTableListener(InvoicePartTable invoicePartTable, RepairOrderJobTable tableJobsRepairOrder, RepairOrderDialog repairOrderDialog, Shell parent) {
		this(invoicePartTable, new ArrayList<>());
		this.invoiceDetailText.add(new MyText(new Shell(), 0));		// I think this is a total hack way to do this
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.tableJobsRepairOrder = tableJobsRepairOrder;
		this.repairOrderDialog = repairOrderDialog;
		this.parent = parent;
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
        Point clickPoint = new Point(event.x, event.y);
        while (currentPartTableItemIndex < partInvoiceTable.getItemCount() && currentPartTableItemIndex >= 0) {		// scan through Invoice Part's TableItems
        	if (findColumn(currentPartTableItemIndex, clickPoint)) {
        		return;
        	}
        	if (!visible)
        		return;
        	currentPartTableItemIndex++;
        }
	}
	
	private boolean findColumn(int currentPartTableItemIndex, Point clickPoint) {
		final TableItem selectedTableItem = partInvoiceTable.getItem(currentPartTableItemIndex);
		for (int i = 0; i < partInvoiceTable.getColumnCount(); i++) {		// find selected column
    		Rectangle selectedTableItemColumnBounds = selectedTableItem.getBounds(i);
    		if (selectedTableItemColumnBounds.contains(clickPoint)
    				&& (i == InvoicePartTable.PART_NUMBER_COLUMN || i == InvoicePartTable.QUANTITY_COLUMN || i == InvoicePartTable.PART_PRICE_COLUMN)) {
    			if ((i == InvoicePartTable.QUANTITY_COLUMN || i == InvoicePartTable.PART_PRICE_COLUMN)
    					&& (selectedTableItem.getData() == null || ((InvoicePart) selectedTableItem.getData()).getPart() == null)) {
    				return true;		// this IF is what finally did it, the whole not editing QTY or Price without an associated Part
    			}
    			final int selectedColumnIndex = i;
    			Text editorTxtBox = new Text(partInvoiceTable, SWT.NONE);
    			int currentJobTableItemIndex = tableJobsRepairOrder.getSelectionIndex();
    			Listener textListener = new RepairOrderPartEditorListener(partInvoiceTable, currentPartTableItemIndex, selectedColumnIndex,
    																		editorTxtBox, invoiceDetailText, parent,
    																		tableJobsRepairOrder, currentJobTableItemIndex, repairOrderDialog);
    			editor.setEditor(editorTxtBox, selectedTableItem, i);
    			editorTxtBox.addListener(SWT.FocusOut, textListener);
    			editorTxtBox.addListener(SWT.Traverse, textListener);
    			editorTxtBox.addListener(SWT.MouseDown, textListener);
    			editorTxtBox.setText(selectedTableItem.getText(i));
    			editorTxtBox.selectAll();
    			editorTxtBox.setFocus();
    			return true;
    		}
    		if (!visible && selectedTableItemColumnBounds.intersects(partInvoiceTable.getClientArea())) {
    			visible = true;
    		}
    	}
		return false;
	}
}
