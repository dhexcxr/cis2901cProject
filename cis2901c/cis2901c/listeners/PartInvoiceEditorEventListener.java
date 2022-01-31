package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cis2901c.main.PartSearchDialog;
import cis2901c.objects.MyPartInvoiceTable;
import cis2901c.objects.MyTable;
import cis2901c.objects.Part;

public class PartInvoiceEditorEventListener implements Listener{
	
	private MyTable partInvoiceTable;
	private TableEditor editor;
	Text editorTxtBox = null;
	int currentIndex;
	
	public PartInvoiceEditorEventListener(MyTable partInvoiceTable, TableEditor editor) {
		this.partInvoiceTable = partInvoiceTable;
		this.editor = editor;
	}

	@Override
	public void handleEvent(Event event) {
		Rectangle clientArea = partInvoiceTable.getClientArea();
        Point pt = new Point(event.x, event.y);
        int index = partInvoiceTable.getSelectionIndex();
        currentIndex = partInvoiceTable.getSelectionIndex();
        while (index < partInvoiceTable.getItemCount()) {
          boolean visible = false;
          if (index < 0 || index > partInvoiceTable.getItemCount()) {		// TODO add less than 0 check to above while loop
        	  return;
          }
          final TableItem item = partInvoiceTable.getItem(index);
          for (int i = 0; i < partInvoiceTable.getColumnCount(); i++) {
            Rectangle rect = item.getBounds(i);
            if (rect.contains(pt) && (i == 0 || i == 2 || i == 5)) {
              final int column = i;
              editorTxtBox = new Text(partInvoiceTable, SWT.NONE);
              Listener textListener = new Listener() {
                public void handleEvent(final Event e) {
                  switch (e.type) {
                  case SWT.FocusOut:
                    if (column == 0) {
                    	setPartInvoiceLine();
                    }
                    
                    // if quantity column, set quantity, calc total
                    
                    // if extended price column, set extended price, calc total
                    editorTxtBox.dispose();
                    break;
                  case SWT.Traverse:
                    switch (e.detail) {
                    case SWT.TRAVERSE_RETURN:
                    	 if (column == 0) {
                         	setPartInvoiceLine();
                         }
                    // FALL THROUGH
                    case SWT.TRAVERSE_ESCAPE:
                      editorTxtBox.dispose();
                      e.doit = false;
                    }
                    break;
                  }
                }
              };
              editorTxtBox.addListener(SWT.FocusOut, textListener);
              editorTxtBox.addListener(SWT.Traverse, textListener);
              editor.setEditor(editorTxtBox, item, i);
              editorTxtBox.setText(item.getText(i));
              editorTxtBox.selectAll();
              editorTxtBox.setFocus();
              return;
            }
            if (!visible && rect.intersects(clientArea)) {
              visible = true;
            }
          }
          if (!visible)
            return;
          index++;
        }
//        TableItem tableItem = new TableItem(partInvoiceTable, SWT.NONE, partInvoiceTable.getItemCount());
	}
	
	private void setPartInvoiceLine() {
		// search for part, populate selected TableItem fields, add new TableItem, calculate total
		if (editorTxtBox.getText().length() > 0) {
			
			Part[] partResults = (Part[]) DbServices.searchForObject(partInvoiceTable, editorTxtBox.getText());
			Part editedLineItem = null;
			if (partResults[1] == null) {		// if there's only 1 results returned
				editedLineItem = partResults[0];
			} else {
				// if DbServices.searchForObject returns more than 1 result, call Item Search Box and show result
				PartSearchDialog partSearchDialog = new PartSearchDialog(Display.getDefault().getActiveShell(),SWT.NONE);
				editedLineItem = (Part) partSearchDialog.open(editorTxtBox.getText());
			}
			
			if (partInvoiceTable.getItem(currentIndex).getData() == null) {
				// if we're not editing an already populated TableItem line item
				partInvoiceTable.paint(editedLineItem);
			} else {
				TableItem[] currentTableItems = partInvoiceTable.getItems();
				Part[] currentParts = new Part[currentTableItems.length];
				for (int i = 0; i < currentTableItems.length; i++) {
					currentParts[i] = (Part) currentTableItems[i].getData();
				}			
				
				currentParts[currentIndex] = editedLineItem;
				partInvoiceTable.removeAll();
				partInvoiceTable.paint(currentParts);
			}
			TableItem tableItem = new TableItem(partInvoiceTable, SWT.NONE, partInvoiceTable.getItemCount());
		}
	}
}
