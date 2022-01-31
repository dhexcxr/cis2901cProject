package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cis2901c.objects.MyTable;

public class PartInvoiceEditorEventListener implements Listener{
	
	private MyTable partInvoiceTable;
	private TableEditor editor;
	Text editorTxtBox = null;
	
	public PartInvoiceEditorEventListener(MyTable partInvoiceTable, TableEditor editor) {
		this.partInvoiceTable = partInvoiceTable;
		this.editor = editor;
	}

	@Override
	public void handleEvent(Event event) {
		Rectangle clientArea = partInvoiceTable.getClientArea();
        Point pt = new Point(event.x, event.y);
        int index = partInvoiceTable.getTopIndex();
        while (index < partInvoiceTable.getItemCount()) {
          boolean visible = false;
          final TableItem item = partInvoiceTable.getItem(index);
          for (int i = 0; i < partInvoiceTable.getColumnCount(); i++) {
            Rectangle rect = item.getBounds(i);
            if (rect.contains(pt) && (i == 0 || i == 2 || i == 5)) {
              final int column = i;
//              final Text editorTxtBox = new Text(partInvoiceTable, SWT.NONE);
              editorTxtBox = new Text(partInvoiceTable, SWT.NONE);
              Listener textListener = new Listener() {
                public void handleEvent(final Event e) {
                  switch (e.type) {
                  case SWT.FocusOut:
                    item.setText(column, editorTxtBox.getText());
                    // if partnumber column, SetPartNumber
                    if (column == 0) {
                    	SetPartNumber();
                    }
                    
                    // if quantity column, set quantity, calc total
                    
                    // if extended price column, set extended price, calc total
                    editorTxtBox.dispose();
                    break;
                  case SWT.Traverse:
                    switch (e.detail) {
                    case SWT.TRAVERSE_RETURN:
                      item
                          .setText(column, editorTxtBox
                              .getText());
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
	}
	
	private void SetPartNumber() {
		// search for part, populate selected TableItem fields, add new TableItem, calculate total
		partInvoiceTable.paint(DbServices.searchForObject(partInvoiceTable, editorTxtBox.getText())[0]);
		TableItem tableItem = new TableItem(partInvoiceTable, SWT.NONE);
	}
}
