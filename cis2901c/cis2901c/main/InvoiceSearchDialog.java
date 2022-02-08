package cis2901c.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cis2901c.listeners.SearchTextBoxListeners;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.objects.MyInvoiceTable;
import cis2901c.objects.MyText;
import org.eclipse.swt.widgets.TableColumn;

public class InvoiceSearchDialog extends Dialog {
	
	Shell shlInvoiceSearch;
	Object result;

	public InvoiceSearchDialog(Shell parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	public Object open() {
		createContents();
		shlInvoiceSearch.open();
		shlInvoiceSearch.layout();
		Display display = getParent().getDisplay();
		while (!shlInvoiceSearch.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	private void createContents() {
		shlInvoiceSearch = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlInvoiceSearch.setText("Invoice Search");
		shlInvoiceSearch.setSize(653, 410);
		
		Gui.setDialogAtCenter(shlInvoiceSearch);
		
		MyInvoiceTable invoiceTable = new MyInvoiceTable(shlInvoiceSearch, SWT.BORDER | SWT.FULL_SELECTION);
		invoiceTable.setBounds(10, 42, 614, 282);
		invoiceTable.setLinesVisible(true);
		invoiceTable.setHeaderVisible(true);
		invoiceTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (invoiceTable.getSelection().length > 0) {
					// return Customer selected from Customer Search Dialog table on double click
					result =  invoiceTable.getSelection()[0].getData();
					shlInvoiceSearch.close();
				}
			}
		});
				
		MyText invoiceSearchTextBox = new MyText(shlInvoiceSearch, SWT.BORDER);
		invoiceSearchTextBox.setText("Search...");
		invoiceSearchTextBox.setBounds(9, 10, 614, 26);
		invoiceSearchTextBox.setFocus();
		invoiceSearchTextBox.addModifyListener(new SearchTextBoxListeners(invoiceSearchTextBox, invoiceTable));
		
		TableColumn tblclmnInvoiceNumber = new TableColumn(invoiceTable, SWT.NONE);
		tblclmnInvoiceNumber.setWidth(83);
		tblclmnInvoiceNumber.setText("Invoice #");
		
		TableColumn tblclmnCustomer = new TableColumn(invoiceTable, SWT.NONE);
		tblclmnCustomer.setWidth(180);
		tblclmnCustomer.setText("Customer");
		
		TableColumn tblclmnCashierDatetime = new TableColumn(invoiceTable, SWT.NONE);
		tblclmnCashierDatetime.setWidth(152);
		tblclmnCashierDatetime.setText("Cashier Date/Time");
		
		TableColumn tblclmnLineItems = new TableColumn(invoiceTable, SWT.NONE);
		tblclmnLineItems.setWidth(82);
		tblclmnLineItems.setText("Line Items");
		
		TableColumn tblclmnTotal = new TableColumn(invoiceTable, SWT.NONE);
		tblclmnTotal.setWidth(100);
		tblclmnTotal.setText("Total");
		invoiceSearchTextBox.addFocusListener(new TextBoxFocusListener(invoiceSearchTextBox));

		// dialog  controls
		Button btnOpenInvoice = new Button(shlInvoiceSearch, SWT.NONE);
		btnOpenInvoice.setText("Open Invoice");
		btnOpenInvoice.setBounds(10, 330, 256, 26);
		btnOpenInvoice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (invoiceTable.getSelection().length > 0) {
					result =  invoiceTable.getSelection()[0].getData();
					shlInvoiceSearch.close();
				}
			}
		});

		Button btnCancel = new Button(shlInvoiceSearch, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(368, 330, 256, 26);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// close dialog
				shlInvoiceSearch.close();
			}
		});
	}

}
