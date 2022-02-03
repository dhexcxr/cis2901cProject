package cis2901c.listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
import cis2901c.objects.MyTable;
import cis2901c.objects.Part;

public class PartInvoiceEditorEventListener implements Listener{
	
	final int PART_NUMBER_COLUMN = 0;
	final int QUANTITY_COLUMN = 2;
	final int PART_PRICE_COLUMN = 5;
	final int EXTENDED_PRICE_COLUMN = 6;
	
	private MyTable partInvoiceTable;
	private TableEditor editor;
	private Text txtPartsTotal_Invoice;
	private Text txtTax_Invoice;
	private Text txtFinalTotal;
	
	Text editorTxtBox = null;
	int currentIndex;
	
	public PartInvoiceEditorEventListener(MyTable partInvoiceTable, TableEditor editor, Text txtPartsTotal_Invoice, Text txtTax_Invoice, Text txtFinalTotal) {
		this.partInvoiceTable = partInvoiceTable;
		this.editor = editor;
		this.txtPartsTotal_Invoice = txtPartsTotal_Invoice;
		this.txtTax_Invoice = txtTax_Invoice;
		this.txtFinalTotal = txtFinalTotal;
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
            if (rect.contains(pt) && (i == PART_NUMBER_COLUMN || i == QUANTITY_COLUMN || i == PART_PRICE_COLUMN)) {
              final int column = i;
              editorTxtBox = new Text(partInvoiceTable, SWT.NONE);
              Listener textListener = new Listener() {
                public void handleEvent(final Event e) {
                  switch (e.type) {
                  case SWT.MouseDown:
                	  if (column == PART_NUMBER_COLUMN) {
                		  PartSearchDialog partSearchDialog = new PartSearchDialog(Display.getDefault().getActiveShell(),SWT.NONE);
                		  Part selectedPart = (Part) partSearchDialog.open(editorTxtBox.getText());
                		  if (selectedPart != null) {
                			  paintInvoiceLines(selectedPart);  
                		  }
                	  }
                	  break;
                  case SWT.FocusOut:
                    if (column == PART_NUMBER_COLUMN) {
                    	// we entered a part number
                    	setPartInvoiceLine();
                    	calculateInvoiceTotal();
                    } else if (column == QUANTITY_COLUMN) {
                    	// we entered a part quantity
                    	if (partInvoiceTable.getSelection()[0].getData() != null) {
                    		setPartQuantity(item);
//                    		Part selectedPart = (Part) partInvoiceTable.getSelection()[0].getData();
//                    		if (Integer.parseInt(editorTxtBox.getText().replaceAll("[^0-9]", "")) > selectedPart.getOnHand()) {
//                        		// if quantity entered is more than OnHand, pop up dialog telling user as much and set to OnHand
//                        		editorTxtBox.setText(Integer.toString(selectedPart.getOnHand()));
//                        	}
//                    		item.setText(QUANTITY_COLUMN, editorTxtBox.getText().replaceAll("[^0-9]", "").equals("") ? "0" :  editorTxtBox.getText().replaceAll("[^0-9]", ""));
//                    		item.setText(EXTENDED_PRICE_COLUMN, Integer.toString(Integer.parseInt(item.getText(QUANTITY_COLUMN)) * Integer.parseInt(item.getText(PART_PRICE_COLUMN))));
                    		calculateInvoiceTotal();
                    	}
                    } else if (column == PART_PRICE_COLUMN) {
                    	if (partInvoiceTable.getSelection()[0].getData() != null) {
                    	setPartPrice(item);
                    	calculateInvoiceTotal();
                    	}
                    }
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
              editorTxtBox.addListener(SWT.MouseDown, textListener);
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
			if (partResults[1] == null && partResults[0] != null) {		// if there's only 1 result, or no results returned
				editedLineItem = partResults[0];
			} else {
				// if DbServices.searchForObject returns more than 1 result, call Item Search Box and show result
				PartSearchDialog partSearchDialog = new PartSearchDialog(Display.getDefault().getActiveShell(),SWT.NONE);
				editedLineItem = (Part) partSearchDialog.open(editorTxtBox.getText());
			}
			
			paintInvoiceLines(editedLineItem);
		}
	}
	
	private void paintInvoiceLines(Part editedLineItem) {
		if (partInvoiceTable.getItem(currentIndex).getData() == null) {
			// if we're not editing an already populated TableItem line item
//			Part[] currentParts = new Part[] {editedLineItem};
			partInvoiceTable.paint(editedLineItem);
//			partInvoiceTable.paint(currentParts);
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
	
	private void setPartQuantity(TableItem item) {
		Part selectedPart = (Part) partInvoiceTable.getSelection()[0].getData();
		if (Integer.parseInt(editorTxtBox.getText().replaceAll("[^0-9]", "")) > selectedPart.getOnHand()) {
    		// if quantity entered is more than OnHand, pop up dialog telling user as much and set to OnHand
    		editorTxtBox.setText(Integer.toString(selectedPart.getOnHand()));
    	}
		item.setText(QUANTITY_COLUMN, editorTxtBox.getText().replaceAll("[^0-9]", "").equals("") ? "0" :  editorTxtBox.getText().replaceAll("[^0-9]", ""));
		item.setText(EXTENDED_PRICE_COLUMN, (new BigDecimal(item.getText(QUANTITY_COLUMN)).multiply(new BigDecimal(item.getText(PART_PRICE_COLUMN))).toString()));
	}
	
	private void setPartPrice(TableItem item) {
		item.setText(PART_PRICE_COLUMN, editorTxtBox.getText().replaceAll("[^0-9]", "").equals("") ? "0" :  editorTxtBox.getText().replaceAll("[^0-9]", ""));
		item.setText(EXTENDED_PRICE_COLUMN, (new BigDecimal(item.getText(QUANTITY_COLUMN)).multiply(new BigDecimal(item.getText(PART_PRICE_COLUMN))).toString()));
	}
	
	private void calculateInvoiceTotal() {
		BigDecimal taxRate = new BigDecimal(0.065);		// TODO set tax rate in application settings
		BigDecimal total = new BigDecimal(0);
		TableItem[] items = partInvoiceTable.getItems();
		for (TableItem item : items) {
			if (item.getText(EXTENDED_PRICE_COLUMN).equals("")) {
				// ignore new TableItem at end of list with no part data set 
				break;
			}
			System.out.println("Total before: " + total.toString());
			total = total.add(new BigDecimal(item.getText(EXTENDED_PRICE_COLUMN)));
			System.out.println("Item price to BD: " + new BigDecimal(item.getText(EXTENDED_PRICE_COLUMN)).toString());
			System.out.println("Total after: " + total.toString());
		}
		txtPartsTotal_Invoice.setText("$" + total.setScale(2, RoundingMode.CEILING).toString());
		txtTax_Invoice.setText("$" + taxRate.multiply(total).setScale(2, RoundingMode.CEILING).toString());
		txtFinalTotal.setText("$" + taxRate.multiply(total).add(total).setScale(2, RoundingMode.CEILING).toString());
	}
}
