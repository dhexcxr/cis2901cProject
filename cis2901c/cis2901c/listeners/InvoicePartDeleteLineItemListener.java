package cis2901c.listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cis2901c.main.Main;
import cis2901c.objects.InvoicePartTable;
import cis2901c.objects.MyTable;

public class InvoicePartDeleteLineItemListener extends MouseAdapter{
	
	private MyTable partTableInvoice;
	protected Text txtPartsTotalInvoice;
	private Text txtTaxInvoice;
	private Text txtFinalTotal;
	
	public InvoicePartDeleteLineItemListener(MyTable partTableInvoice) {
		this(partTableInvoice, new Text(new Shell(), 0), new Text(new Shell(), 0), new Text(new Shell(), 0));
	}
	
	public InvoicePartDeleteLineItemListener(MyTable partTableInvoice, Text txtPartsTotalInvoice, Text txtTaxInvoice,
			Text txtFinalTotal) {
		super();
		this.partTableInvoice = partTableInvoice;
		this.txtPartsTotalInvoice = txtPartsTotalInvoice;
		this.txtTaxInvoice = txtTaxInvoice;
		this.txtFinalTotal = txtFinalTotal;
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		if (partTableInvoice.getSelectionIndex() >= 0 && partTableInvoice.getSelectionIndex() < partTableInvoice.getItemCount() &&
													partTableInvoice.getItem(partTableInvoice.getSelectionIndex()).getData() != null) {
			int selectedIndex = partTableInvoice.getSelectionIndex();
			partTableInvoice.remove(selectedIndex);
			
			selectedIndex = selectedIndex == 0 ? 0 : selectedIndex - 1;
			partTableInvoice.setSelection(selectedIndex);
			partTableInvoice.notifyListeners(SWT.Selection, new Event());
			
			BigDecimal taxRate = BigDecimal.valueOf(0.065);
			BigDecimal total = BigDecimal.valueOf(0);
			TableItem[] items = partTableInvoice.getItems();
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
}
