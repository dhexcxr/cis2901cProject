package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cis2901c.objects.LaborTable;
import cis2901c.objects.RepairOrderJobTable;

public class RepairOrderLaborTableListener implements Listener {
	
	private LaborTable jobLaborTable;
	private TableEditor editor;
	private RepairOrderJobTable tableJobsRepairOrder;

	public RepairOrderLaborTableListener(LaborTable jobLaborTable, RepairOrderJobTable tableJobsRepairOrder) {
		this.jobLaborTable = jobLaborTable;
		this.tableJobsRepairOrder = tableJobsRepairOrder;
		this.editor = new TableEditor(jobLaborTable);
		editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	}

	@Override
	public void handleEvent(Event event) {
		int currentTableItemIndex = jobLaborTable.getSelectionIndex();
        Rectangle tableWidgetArea = jobLaborTable.getClientArea();
        Point clickPoint = new Point(event.x, event.y);
        while (currentTableItemIndex < jobLaborTable.getItemCount() && currentTableItemIndex >= 0) {		// scan through Invoice Part's TableItems
        	boolean visible = false;
        	final TableItem selectedTableItem = jobLaborTable.getItem(currentTableItemIndex);
        	for (int i = 0; i < jobLaborTable.getColumnCount(); i++) {		// find selected column
        		Rectangle selectedTableItemColumnBounds = selectedTableItem.getBounds(i);
        		if (selectedTableItemColumnBounds.contains(clickPoint) &&
        				(i == LaborTable.TECHNICIAN_COLUMN || i == LaborTable.DESCRIPTION_COLUMN ||
        								i == LaborTable.HOURS_COLUMN || i == LaborTable.RATE_COLUMN)) {
        			final int selectedColumnIndex = i;
        			Text editorTxtBox = new Text(jobLaborTable, SWT.NONE);
        			Listener textListener = new RepairOrderLaborEditorListener(jobLaborTable, currentTableItemIndex, selectedColumnIndex,
        																		editorTxtBox, tableJobsRepairOrder);
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
        	currentTableItemIndex++;
        }
//        tableJobsRepairOrder.notifyListeners(SWT.BUTTON4, new Event());		// save Parts and Labor
	}

}
