package cis2901c.listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cis2901c.main.Main;
import cis2901c.main.PartSearchDialog;
import cis2901c.objects.InvoicePart;
import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.InvoicePartTableItem;
import cis2901c.objects.MyText;
import cis2901c.objects.Part;

public class InvoicePartEditorListener implements Listener {

	private InvoicePartTable partInvoiceTable;
	private TableItem selectedTableItem;
	private int selectedTableItemIndex;
	private int selectedColumnIndex;
	private Text editorTxtBox;
	
	private Text txtPartsTotalInvoice;
	private Text txtTaxInvoice;
	private Text txtFinalTotal;
	private Text textCategoryInvoice;
	private Text textSupplierInvoice;
	private Text textNotesInvoice;

	// this allows us to ignore the FocusOut listener so we don't call PartSearchDialog twice
	// if there is text in Part Number column when we "double" click to open Search box
	protected boolean ignoreFocusOut = false;

	private static final String ONLY_DECIMALS = "[^-0-9.]"; // find a better name

	public InvoicePartEditorListener(InvoicePartTable partInvoiceTable, int selectedTableItemIndex, int selectedColumnIndex,
										Text editorTxtBox, List<MyText> invoiceDetailText) {
		this.partInvoiceTable = partInvoiceTable;
		this.selectedTableItem = partInvoiceTable.getItem(selectedTableItemIndex);
		this.selectedTableItemIndex = selectedTableItemIndex;
		this.selectedColumnIndex = selectedColumnIndex;
		this.editorTxtBox = editorTxtBox;

		this.txtPartsTotalInvoice = invoiceDetailText.get(0);
		this.txtTaxInvoice = invoiceDetailText.get(1);
		this.txtFinalTotal = invoiceDetailText.get(2);
		this.textCategoryInvoice = invoiceDetailText.get(3);
		this.textSupplierInvoice = invoiceDetailText.get(4);
		this.textNotesInvoice = invoiceDetailText.get(5);
	}

	@Override
	public void handleEvent(Event event) {
		// TODO streamline all these ignoreFocusOuts
		if (ignoreFocusOut) {
			return;
		}
		if (event.type == SWT.MouseDown ) {
			if (selectedColumnIndex == InvoicePartTable.PART_NUMBER_COLUMN) {
				ignoreFocusOut = true;
				PartSearchDialog partSearchDialog = new PartSearchDialog(Display.getDefault().getActiveShell(),SWT.NONE);
				InvoicePart selectedPart = new InvoicePart((Part) partSearchDialog.open(editorTxtBox.getText()));
				if (selectedPart.getPart() != null) {
					editorTxtBox.setText(selectedPart.getPartNumber());
					paintInvoiceLines(selectedPart);  
				}
				ignoreFocusOut = false;
			}
		} else if (event.type == SWT.FocusOut) {
			setColumnData();
		} else if (event.type == SWT.Traverse && event.detail == SWT.TRAVERSE_ESCAPE) {
			editorTxtBox.dispose();
		} else if (event.type == SWT.Traverse) {
			setColumnData();
		}
	}
	
	private void setColumnData() {
		if (selectedColumnIndex == InvoicePartTable.PART_NUMBER_COLUMN && !ignoreFocusOut) {
			// we entered a part number
			findPartNumber();
			calculateInvoiceTotal();
		} else if (selectedColumnIndex == InvoicePartTable.QUANTITY_COLUMN &&
				(selectedTableItem.getData() != null && !editorTxtBox.getText().equals(""))) {
			// we entered a part quantity
				setPartQuantity(selectedTableItem);
				calculateInvoiceTotal();
		} else if (selectedColumnIndex == InvoicePartTable.PART_PRICE_COLUMN &&
				(selectedTableItem.getData() != null && !editorTxtBox.getText().equals(""))) {
			// we entered a part price
			setPartPrice(selectedTableItem);
			calculateInvoiceTotal();
		}
		editorTxtBox.dispose();
	}

	private void findPartNumber() {
		ignoreFocusOut = true;
		Main.log(Level.INFO, "findPartNumber called");
		// search for part, populate selected TableItem fields, add new TableItem, calculate total
		if (editorTxtBox.getText().length() > 0) {
			Part[] partResults = (Part[]) DbServices.searchForObject(new Part(editorTxtBox.getText()));
			InvoicePart editedLineItem = (InvoicePart) selectedTableItem.getData();
			if (editedLineItem == null) {
				editedLineItem = new InvoicePart();
			}
			if (partResults[1] == null && partResults[0] != null) {		// if there's only 1 result
					editedLineItem.setPart(partResults[0]);
			} else {
				// if there's more than 1 result, or no results returned, call Item Search Box and show result
				// TODO fix bug, if there is data entered into part number box, and then the app looses focus (ie, click task bar)
					// there is then no Active Shell associated with application (Display.getDefault().getActiveShell() returns null)
					// find a better way to get shell
				PartSearchDialog partSearchDialog = new PartSearchDialog(Display.getDefault().getActiveShell(),SWT.NONE);
				Part part = (Part) partSearchDialog.open(editorTxtBox.getText());
				if (part == null) {
					ignoreFocusOut = false;
					return;
				}
//				editedLineItem = new InvoicePart((Part) partSearchDialog.open(editorTxtBox.getText()));
				editedLineItem.setPart(part);
			}
			
			// TODO this is kinda ugly, I might be able to move this into paintInvoiceLines(Part editedLineItem)
			TableItem editedTableItem = null;
			for (TableItem tableItem : partInvoiceTable.getItems()) {
				if (editedLineItem.getPartNumber().equals(tableItem.getText(InvoicePartTableItem.PART_NUMBER_COLUMN))
						&& !tableItem.equals(selectedTableItem)) {
					selectedTableItem = tableItem;
					editedTableItem = tableItem;
					partInvoiceTable.setSelection(editedTableItem);
					int quantity = Integer.parseInt(tableItem.getText(InvoicePartTableItem.QUANTITY_COLUMN));
					tableItem.setText(InvoicePartTableItem.QUANTITY_COLUMN, Integer.toString(quantity + 1));
					setPartQuantity(editedTableItem, quantity + 1);
					ignoreFocusOut = false;
					return;
				}
			}
			paintInvoiceLines(editedLineItem);
		}
		ignoreFocusOut = false;
	}

	private void paintInvoiceLines(InvoicePart editedLineItem) {
		Main.log(Level.INFO, "paintInvoiceLines called");
		// TODO if entered part number matches a part number already on invoice, get TableItem, get quantity column, increase by 1
		// TODO i think if we change this to a List<TableItem> we can just do a currentTableItems.contains(editedLineItem)
																// to find part and increase Quantity by 1
		TableItem[] currentTableItems = partInvoiceTable.getItems();
		// TODO if editedLineItem is already in currentTableItems, find it's index and increase Quantity by 1

		if (partInvoiceTable.getItem(selectedTableItemIndex).getData() == null) {
			// if we're not editing an already populated TableItem line item
			partInvoiceTable.paint(editedLineItem, selectedTableItemIndex);
		} else {
//			Part[] currentParts = new Part[currentTableItems.length];
			InvoicePart[] currentParts = new InvoicePart[currentTableItems.length];
			for (int i = 0; i < currentTableItems.length; i++) {
				currentParts[i] = (InvoicePart) currentTableItems[i].getData();
			}			
			currentParts[selectedTableItemIndex] = editedLineItem;
			partInvoiceTable.removeAll();
			partInvoiceTable.paint(currentParts);
		}
		textCategoryInvoice.setText(editedLineItem.getPart().getCategory());
		textSupplierInvoice.setText(editedLineItem.getPart().getSupplier());
		textNotesInvoice.setText(editedLineItem.getPart().getNotes());
		@SuppressWarnings("unused")									// this adds another new, empty TableItem at the end of the Invoice Line Items
		TableItem tableItem = new InvoicePartTableItem(partInvoiceTable, SWT.NONE, partInvoiceTable.getItemCount());	// so we can continue selecting and adding parts
	}

	
	// TODO refactor into private void setPartQuantity(TableItem item, int quantity)
		// and this one v calls that one ^ with setPartQuantity(item, editorTxtBox.getText().replaceAll(ONLY_DECIMALS, ""))
		// then the Dupe Part Quantity Code in findPartNumber() can call setPartQuantity(editedTableItem, quantity)
	private void setPartQuantity(TableItem item) {
//		setPartQuantity(item, Integer.parseInt(item.getText(InvoicePartTable.QUANTITY_COLUMN)));
		setPartQuantity(item, Integer.parseInt(editorTxtBox.getText().replaceAll(ONLY_DECIMALS, "")));	// <--------
	}																										//		|
																											// 		|
	private void setPartQuantity(TableItem item, int quantity) {		// TODO change Quantity to a String so this | does not throw a NumberFormatExcept
		ignoreFocusOut = true;												// we can parse to an int in this method where necessary, and set to [something]
		InvoicePart selectedInvoicePart = (InvoicePart) selectedTableItem.getData();		// if it turns to "" after removing all but decimals
		Part selectedPart = selectedInvoicePart.getPart();
//		String currentQuantity = item.getText(InvoicePartTable.QUANTITY_COLUMN);
		String currentQuantity = Integer.toString(quantity);
//		String newQuantity = editorTxtBox.getText().replaceAll(ONLY_DECIMALS, "");
		String newQuantity = currentQuantity;
		if (newQuantity.equals("")) {
			newQuantity = currentQuantity;
		}
		if (Integer.parseInt(newQuantity) > selectedPart.getOnHand()) {
			// if quantity entered is more than OnHand, pop up dialog telling user as much and set to OnHand
			MessageBox onHandWarningDialogBox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_INFORMATION);
			onHandWarningDialogBox.setText("Notice");
			onHandWarningDialogBox.setMessage("Quantity entered is more than On Hand Quantity\n\nQuantity set to On Hand");
			onHandWarningDialogBox.open();
			newQuantity = Integer.toString(selectedPart.getOnHand());
		}
		item.setText(InvoicePartTable.QUANTITY_COLUMN, newQuantity);
		selectedInvoicePart.setQuantity(quantity);
		BigDecimal extendedPrice = new BigDecimal(item.getText(InvoicePartTable.QUANTITY_COLUMN)).multiply(new BigDecimal(item.getText(InvoicePartTable.PART_PRICE_COLUMN)));
		item.setText(InvoicePartTable.EXTENDED_PRICE_COLUMN, (extendedPrice.toString()));
//		selectedInvoicePart.setSoldPrice(extendedPrice);
		ignoreFocusOut = false;
	}

	private void setPartPrice(TableItem item) {							   // TODO fix this,   v   , probably needs to be ONLY_DECIMALS
		ignoreFocusOut = true;
		InvoicePart selectedInvoicePart = (InvoicePart) selectedTableItem.getData();
		String partPrice = editorTxtBox.getText().replaceAll("[^.0-9]", "").equals("") ?
								item.getText(InvoicePartTable.PART_PRICE_COLUMN) : editorTxtBox.getText().replaceAll(ONLY_DECIMALS, "");
		item.setText(InvoicePartTable.PART_PRICE_COLUMN, partPrice);
		selectedInvoicePart.setSoldPrice(new BigDecimal(partPrice));
		BigDecimal extendedPrice = new BigDecimal(item.getText(InvoicePartTable.QUANTITY_COLUMN))
										.multiply(new BigDecimal(item.getText(InvoicePartTable.PART_PRICE_COLUMN)));
		item.setText(InvoicePartTable.EXTENDED_PRICE_COLUMN, extendedPrice.toString());

		ignoreFocusOut = false;
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
