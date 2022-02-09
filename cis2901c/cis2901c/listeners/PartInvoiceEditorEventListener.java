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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cis2901c.main.PartSearchDialog;
import cis2901c.objects.MyInvoiceTableItem;
import cis2901c.objects.MyPartInvoiceTable;
import cis2901c.objects.Part;

public class PartInvoiceEditorEventListener implements Listener{
	
//	final int PART_NUMBER_COLUMN = 0;
//	final int QUANTITY_COLUMN = 2;
//	final int PART_PRICE_COLUMN = 5;
//	final int EXTENDED_PRICE_COLUMN = 6;
	
	// this is now used here and in PhoneNumberTextBox
	private static final String NOT_NUMBERS = "[^0-9]";		// find a better name
	
	// this allows us to ignore the FocusOut listener so we don't call PartSearchDialog twice
			// if there is text in Part Number column when we "double" click to open Search box
	private boolean ignoreFocusOut = false;
	
	private MyPartInvoiceTable partInvoiceTable;
	private TableEditor editor;
	private Text txtPartsTotalInvoice;
	private Text txtTaxInvoice;
	private Text txtFinalTotal;
	private Text textCategoryInvoice;
	private Text textSupplierInvoice;
	private Text textNotesInvoice;
	
	
	
	Text editorTxtBox = null;
	int currentIndex;
	
	public PartInvoiceEditorEventListener(MyPartInvoiceTable partInvoiceTable, TableEditor editor, Text txtPartsTotalInvoice,
		Text txtTaxInvoice, Text txtFinalTotal, Text textCategoryInvoice, Text textSupplierInvoice, Text textNotesInvoice) {
		this.partInvoiceTable = partInvoiceTable;
		this.editor = editor;
		this.txtPartsTotalInvoice = txtPartsTotalInvoice;
		this.txtTaxInvoice = txtTaxInvoice;
		this.txtFinalTotal = txtFinalTotal;
		this.textCategoryInvoice = textCategoryInvoice;
		this.textSupplierInvoice = textSupplierInvoice;
		this.textNotesInvoice = textNotesInvoice;
	}

	@Override
	public void handleEvent(Event event) {
		// based on "edit a cell in a table (in place, fancy)"
			// https://git.eclipse.org/c/platform/eclipse.platform.swt.git/tree/examples/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet124.java
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
        		if (rect.contains(pt) && (i == MyPartInvoiceTable.PART_NUMBER_COLUMN ||
        											i == MyPartInvoiceTable.QUANTITY_COLUMN ||
        												i == MyPartInvoiceTable.PART_PRICE_COLUMN)) {
        			final int column = i;
        			editorTxtBox = new Text(partInvoiceTable, SWT.NONE);
        			Listener textListener = new Listener() {
        				public void handleEvent(final Event e) {
        					switch (e.type) {
        					case SWT.MouseDown:
        						if (column == MyPartInvoiceTable.PART_NUMBER_COLUMN) {
        							ignoreFocusOut = true;
        							PartSearchDialog partSearchDialog = new PartSearchDialog(Display.getDefault().getActiveShell(),SWT.NONE);
        							Part selectedPart = (Part) partSearchDialog.open(editorTxtBox.getText());
        							if (selectedPart != null) {
        								paintInvoiceLines(selectedPart);  
        							}
        							ignoreFocusOut = false;
        						}
        						break;
        					case SWT.FocusOut:
        						if (column == MyPartInvoiceTable.PART_NUMBER_COLUMN && !ignoreFocusOut) {
        							// we entered a part number
        							findPartNumber();
        							calculateInvoiceTotal();
        						} else if (column == MyPartInvoiceTable.QUANTITY_COLUMN) {
        							// we entered a part quantity
        							if (partInvoiceTable.getSelection()[0].getData() != null && !editorTxtBox.getText().equals("")) {
        								setPartQuantity(item);
        								calculateInvoiceTotal();
        							}
        						} else if (column == MyPartInvoiceTable.PART_PRICE_COLUMN &&
        								(partInvoiceTable.getSelection()[0].getData() != null && !editorTxtBox.getText().equals(""))) {
        							// we entered a part price
        							setPartPrice(item);
        							calculateInvoiceTotal();
        						}
        						editorTxtBox.dispose();
        						break;
        					case SWT.Traverse:
        						switch (e.detail) {
        						case SWT.TRAVERSE_RETURN:
        							if (column == MyPartInvoiceTable.PART_NUMBER_COLUMN) {
        								findPartNumber();
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
	}
	
	private void findPartNumber() {
		System.out.println("findPartNumber called");
		// search for part, populate selected TableItem fields, add new TableItem, calculate total
		if (editorTxtBox.getText().length() > 0) {
			// if entered part number matches a part number already on invoice, get TableItem, get quantity column, increase by 1
			Part[] partResults = (Part[]) DbServices.searchForObject(partInvoiceTable, editorTxtBox.getText());
			Part editedLineItem = null;
			if (partResults[1] == null && partResults[0] != null) {		// if there's only 1 result
				editedLineItem = partResults[0];
			} else {
				// if there's more than 1 result, or no results returned, call Item Search Box and show result
				PartSearchDialog partSearchDialog = new PartSearchDialog(Display.getDefault().getActiveShell(),SWT.NONE);
				editedLineItem = (Part) partSearchDialog.open(editorTxtBox.getText());
			}
			paintInvoiceLines(editedLineItem);
		}
	}
	
	private void paintInvoiceLines(Part editedLineItem) {
		System.out.println("paintInvoiceLines called");
		
		TableItem[] currentTableItems = partInvoiceTable.getItems();
		// TODO if editedLineItem is already in currentTableItems, find it's index and increase Quantity by 1
		
		if (partInvoiceTable.getItem(currentIndex).getData() == null) {
			// if we're not editing an already populated TableItem line item
			partInvoiceTable.paint(editedLineItem);
		} else {
			Part[] currentParts = new Part[currentTableItems.length];
			for (int i = 0; i < currentTableItems.length; i++) {
				currentParts[i] = (Part) currentTableItems[i].getData();
			}			
			currentParts[currentIndex] = editedLineItem;
			partInvoiceTable.removeAll();
			partInvoiceTable.paint(currentParts);
		}
		textCategoryInvoice.setText(editedLineItem.getCategory());
		textSupplierInvoice.setText(editedLineItem.getSupplier());
		textNotesInvoice.setText(editedLineItem.getNotes());
		@SuppressWarnings("unused")									// this adds another new, empty TableItem at the end of the Invoice Line Items
		TableItem tableItem = new MyInvoiceTableItem(partInvoiceTable, SWT.NONE, partInvoiceTable.getItemCount());	// so we can continue selecting and adding parts
	}
	
	private void setPartQuantity(TableItem item) {
		Part selectedPart = (Part) partInvoiceTable.getSelection()[0].getData();
		if (Integer.parseInt(editorTxtBox.getText().replaceAll(NOT_NUMBERS, "")) > selectedPart.getOnHand()) {
    		// if quantity entered is more than OnHand, pop up dialog telling user as much and set to OnHand
			MessageBox onHandWarningDialogBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_INFORMATION);
			onHandWarningDialogBox.setText("Notice");
			onHandWarningDialogBox.setMessage("Quantity entered is more than On Hand Quantity\n\nQuantity set to On Hand");
			onHandWarningDialogBox.open();
    		editorTxtBox.setText(Integer.toString(selectedPart.getOnHand()));
    	}
		item.setText(MyPartInvoiceTable.QUANTITY_COLUMN, editorTxtBox.getText().replaceAll(NOT_NUMBERS, "").equals("") ? "0" :
															editorTxtBox.getText().replaceAll(NOT_NUMBERS, ""));
		item.setText(MyPartInvoiceTable.EXTENDED_PRICE_COLUMN, (new BigDecimal(item.getText(MyPartInvoiceTable.QUANTITY_COLUMN)).multiply(
				new BigDecimal(item.getText(MyPartInvoiceTable.PART_PRICE_COLUMN))).toString()));
	}
	
	private void setPartPrice(TableItem item) {
		item.setText(MyPartInvoiceTable.PART_PRICE_COLUMN, editorTxtBox.getText().replaceAll("[^.0-9]", "").equals("") ? "0" :
															editorTxtBox.getText().replaceAll(NOT_NUMBERS, ""));
		item.setText(MyPartInvoiceTable.EXTENDED_PRICE_COLUMN, (new BigDecimal(item.getText(MyPartInvoiceTable.QUANTITY_COLUMN)).multiply(
				new BigDecimal(item.getText(MyPartInvoiceTable.PART_PRICE_COLUMN))).toString()));
	}
	
	private void calculateInvoiceTotal() {
		BigDecimal taxRate = BigDecimal.valueOf(0.065);		// TODO set tax rate in application settings
		BigDecimal total = BigDecimal.valueOf(0);
		TableItem[] items = partInvoiceTable.getItems();
		for (TableItem item : items) {
			if (item.getText(MyPartInvoiceTable.EXTENDED_PRICE_COLUMN).equals("")) {
				// ignore new TableItem at end of list with no part data set 
				break;
			}
			System.out.println("Total before: " + total.toString());
			total = total.add(new BigDecimal(item.getText(MyPartInvoiceTable.EXTENDED_PRICE_COLUMN)));
			System.out.println("Item price to BD: " + new BigDecimal(item.getText(MyPartInvoiceTable.EXTENDED_PRICE_COLUMN)).toString());
			System.out.println("Total after: " + total.toString());
		}
		txtPartsTotalInvoice.setText("$" + total.setScale(2, RoundingMode.CEILING).toString());
		txtTaxInvoice.setText("$" + taxRate.multiply(total).setScale(2, RoundingMode.CEILING).toString());
		txtFinalTotal.setText("$" + taxRate.multiply(total).add(total).setScale(2, RoundingMode.CEILING).toString());
	}
}
