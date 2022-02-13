package cis2901c.listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Level;

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

import cis2901c.main.Main;
import cis2901c.main.PartSearchDialog;
import cis2901c.objects.InvoicePartTableItem;
import cis2901c.objects.MyText;
import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.Part;

public class InvoicePartTableListener implements Listener{
	
//	final int PART_NUMBER_COLUMN = 0;
//	final int QUANTITY_COLUMN = 2;
//	final int PART_PRICE_COLUMN = 5;
//	final int EXTENDED_PRICE_COLUMN = 6;
	
	// this is now used here and in PhoneNumberTextBox
	private static final String NOT_NUMBERS = "[^0-9]";		// find a better name
	
	// this allows us to ignore the FocusOut listener so we don't call PartSearchDialog twice
			// if there is text in Part Number column when we "double" click to open Search box
	private boolean ignoreFocusOut = false;
	
	private InvoicePartTable partInvoiceTable;
	private TableEditor editor;
	private Text txtPartsTotalInvoice;
	private Text txtTaxInvoice;
	private Text txtFinalTotal;
	private Text textCategoryInvoice;
	private Text textSupplierInvoice;
	private Text textNotesInvoice;
	
	private List<MyText> invoiceDetailText;
	
	
	
	Text editorTxtBox = null;
//	int selectedTableItemIndex;
	
//	public InvoicePartTableListener(InvoicePartTable invoicePartTable, Text txtPartsTotalInvoice, Text txtTaxInvoice,
//		Text txtFinalTotal, Text textCategoryInvoice, Text textSupplierInvoice, Text textNotesInvoice) {
	public InvoicePartTableListener(InvoicePartTable invoicePartTable, List<MyText> invoiceDetailText) {
		this.partInvoiceTable = invoicePartTable;
//		this.editor = editor;
		this.txtPartsTotalInvoice = invoiceDetailText.get(0);
		this.txtTaxInvoice = invoiceDetailText.get(1);
		this.txtFinalTotal = invoiceDetailText.get(2);
		this.textCategoryInvoice = invoiceDetailText.get(3);
		this.textSupplierInvoice = invoiceDetailText.get(4);
		this.textNotesInvoice = invoiceDetailText.get(5);
		this.invoiceDetailText = invoiceDetailText;
		
		this.editor = new TableEditor(partInvoiceTable);
		editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	}

	@Override
	public void handleEvent(Event event) {
		// based on "edit a cell in a table (in place, fancy)"
			// https://git.eclipse.org/c/platform/eclipse.platform.swt.git/tree/examples/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet124.java
//		int selectedTableItemIndex = partInvoiceTable.getSelectionIndex();
        
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
        			editorTxtBox = new Text(partInvoiceTable, SWT.NONE);
        			Listener textListener = new InvoicePartEditorListener(partInvoiceTable, selectedTableItem, selectedColumnIndex, editorTxtBox, invoiceDetailText);
        			editorTxtBox.addListener(SWT.FocusOut, textListener);
        			editorTxtBox.addListener(SWT.Traverse, textListener);
        			editorTxtBox.addListener(SWT.MouseDown, textListener);
        			editor.setEditor(editorTxtBox, selectedTableItem, i);
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
	
	private void findPartNumber() {
		Main.log(Level.INFO, "findPartNumber called");
		// search for part, populate selected TableItem fields, add new TableItem, calculate total
		if (editorTxtBox.getText().length() > 0) {
			// if entered part number matches a part number already on invoice, get TableItem, get quantity column, increase by 1
			Part[] partResults = (Part[]) DbServices.searchForObject(new Part(editorTxtBox.getText()));
			Part editedLineItem = null;
			if (partResults[1] == null && partResults[0] != null) {		// if there's only 1 result
				editedLineItem = partResults[0];
			} else {
				// if there's more than 1 result, or no results returned, call Item Search Box and show result
				PartSearchDialog partSearchDialog = new PartSearchDialog(Display.getDefault().getActiveShell(),SWT.NONE);
				editedLineItem = (Part) partSearchDialog.open(editorTxtBox.getText());
			}
			if (editedLineItem != null) {
				paintInvoiceLines(editedLineItem);	
			}
		}
	}
	
	private void paintInvoiceLines(Part editedLineItem) {
		Main.log(Level.INFO, "paintInvoiceLines called");
		int selectedTableItemIndex = partInvoiceTable.getSelectionIndex();
		
		TableItem[] currentTableItems = partInvoiceTable.getItems();
		// TODO if editedLineItem is already in currentTableItems, find it's index and increase Quantity by 1
		
		if (partInvoiceTable.getItem(selectedTableItemIndex).getData() == null) {
			// if we're not editing an already populated TableItem line item
			partInvoiceTable.paint(editedLineItem);
		} else {
			Part[] currentParts = new Part[currentTableItems.length];
			for (int i = 0; i < currentTableItems.length; i++) {
				currentParts[i] = (Part) currentTableItems[i].getData();
			}			
			currentParts[selectedTableItemIndex] = editedLineItem;
			partInvoiceTable.removeAll();
			partInvoiceTable.paint(currentParts);
		}
		textCategoryInvoice.setText(editedLineItem.getCategory());
		textSupplierInvoice.setText(editedLineItem.getSupplier());
		textNotesInvoice.setText(editedLineItem.getNotes());
		@SuppressWarnings("unused")									// this adds another new, empty TableItem at the end of the Invoice Line Items
		TableItem tableItem = new InvoicePartTableItem(partInvoiceTable, SWT.NONE, partInvoiceTable.getItemCount());	// so we can continue selecting and adding parts
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
		item.setText(InvoicePartTable.QUANTITY_COLUMN, editorTxtBox.getText().replaceAll(NOT_NUMBERS, "").equals("") ? "0" :
															editorTxtBox.getText().replaceAll(NOT_NUMBERS, ""));
		item.setText(InvoicePartTable.EXTENDED_PRICE_COLUMN, (new BigDecimal(item.getText(InvoicePartTable.QUANTITY_COLUMN)).multiply(
				new BigDecimal(item.getText(InvoicePartTable.PART_PRICE_COLUMN))).toString()));
	}
	
	private void setPartPrice(TableItem item) {
		item.setText(InvoicePartTable.PART_PRICE_COLUMN, editorTxtBox.getText().replaceAll("[^.0-9]", "").equals("") ? "0" :
															editorTxtBox.getText().replaceAll(NOT_NUMBERS, ""));
		item.setText(InvoicePartTable.EXTENDED_PRICE_COLUMN, (new BigDecimal(item.getText(InvoicePartTable.QUANTITY_COLUMN)).multiply(
				new BigDecimal(item.getText(InvoicePartTable.PART_PRICE_COLUMN))).toString()));
	}
	
	private void calculateInvoiceTotal() {
		BigDecimal taxRate = BigDecimal.valueOf(0.065);		// TODO set tax rate in application settings
		BigDecimal total = BigDecimal.valueOf(0);
		TableItem[] items = partInvoiceTable.getItems();
		for (TableItem item : items) {
			if (item.getText(InvoicePartTable.EXTENDED_PRICE_COLUMN).equals("")) {
				// ignore new TableItem at end of list with no part data set 
				break;
			}
			Main.log(Level.INFO, "Total before: " + total.toString());
			total = total.add(new BigDecimal(item.getText(InvoicePartTable.EXTENDED_PRICE_COLUMN)));
			Main.log(Level.INFO, "Item price to BD: " + new BigDecimal(item.getText(InvoicePartTable.EXTENDED_PRICE_COLUMN)).toString());
			Main.log(Level.INFO, "Total after: " + total.toString());
		}
		txtPartsTotalInvoice.setText("$" + total.setScale(2, RoundingMode.CEILING).toString());
		txtTaxInvoice.setText("$" + taxRate.multiply(total).setScale(2, RoundingMode.CEILING).toString());
		txtFinalTotal.setText("$" + taxRate.multiply(total).add(total).setScale(2, RoundingMode.CEILING).toString());
	}
}
