package cis2901c.listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cis2901c.objects.MyTable;

public class DeleteLineItemListener extends MouseAdapter{
	
	private MyTable partTable_Invoice;
	private Text txtPartsTotal_Invoice;
	private Text txtTax_Invoice;
	private Text txtFinalTotal;
	
	public DeleteLineItemListener(MyTable partTable_Invoice2, Text txtPartsTotal_Invoice, Text txtTax_Invoice,
			Text txtFinalTotal) {
		super();
		this.partTable_Invoice = partTable_Invoice2;
		this.txtPartsTotal_Invoice = txtPartsTotal_Invoice;
		this.txtTax_Invoice = txtTax_Invoice;
		this.txtFinalTotal = txtFinalTotal;
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		if (partTable_Invoice.getSelectionIndex() >= 0 && partTable_Invoice.getSelectionIndex() < partTable_Invoice.getItemCount() &&
													partTable_Invoice.getItem(partTable_Invoice.getSelectionIndex()).getData() != null) {
			partTable_Invoice.remove(partTable_Invoice.getSelectionIndex());
			
			// the following copied from PartInvoiceEditorEventListener, need to find a better way to calc invoice total
			int EXTENDED_PRICE_COLUMN = 6;
			BigDecimal taxRate = new BigDecimal(0.065);		// TODO set tax rate in application settings
			BigDecimal total = new BigDecimal(0);
			TableItem[] items = partTable_Invoice.getItems();
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
			// END copied section
		}
	}
}
