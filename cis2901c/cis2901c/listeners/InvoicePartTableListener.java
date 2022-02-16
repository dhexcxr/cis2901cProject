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

import cis2901c.objects.MyText;
import cis2901c.objects.InvoicePartTable;

public class InvoicePartTableListener implements Listener {
		
	private InvoicePartTable partInvoiceTable;
	private TableEditor editor;
	private List<MyText> invoiceDetailText;
	
	public InvoicePartTableListener(InvoicePartTable invoicePartTable) {
		this(invoicePartTable, new ArrayList<>());
		this.invoiceDetailText.add(new MyText(new Shell(), 0));		// I think this is a total hack way to do this
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
	}
	
	public InvoicePartTableListener(InvoicePartTable invoicePartTable, List<MyText> invoiceDetailText) {
		this.partInvoiceTable = invoicePartTable;
		this.invoiceDetailText = invoiceDetailText;
		
		this.editor = new TableEditor(partInvoiceTable);
		editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	}

	@Override
	public void handleEvent(Event event) {   
        int currentTableItemIndex = partInvoiceTable.getSelectionIndex();
        Rectangle tableWidgetArea = partInvoiceTable.getClientArea();
        Point clickPoint = new Point(event.x, event.y);
        while (currentTableItemIndex < partInvoiceTable.getItemCount() && currentTableItemIndex >= 0) {		// scan through Invoice Part's TableItems
        	boolean visible = false;
        	final TableItem selectedTableItem = partInvoiceTable.getItem(currentTableItemIndex);
        	for (int i = 0; i < partInvoiceTable.getColumnCount(); i++) {		// find selected column
        		Rectangle selectedTableItemColumnBounds = selectedTableItem.getBounds(i);
        		if (selectedTableItemColumnBounds.contains(clickPoint) &&
        				(i == InvoicePartTable.PART_NUMBER_COLUMN || i == InvoicePartTable.QUANTITY_COLUMN || i == InvoicePartTable.PART_PRICE_COLUMN)) {
        			final int selectedColumnIndex = i;
        			Text editorTxtBox = new Text(partInvoiceTable, SWT.NONE);
        			Listener textListener = new InvoicePartEditorListener(partInvoiceTable, currentTableItemIndex, selectedColumnIndex,
        																		editorTxtBox, invoiceDetailText);
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
	}
}
