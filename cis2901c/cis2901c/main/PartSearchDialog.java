package cis2901c.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

import cis2901c.listeners.SearchTextBoxListeners;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.objects.MyText;
import cis2901c.objects.MyPartInventoryTable;

public class PartSearchDialog extends Dialog{

	// TODO make a SearchDialog class, extend PartSearch and CustomerSearch from that class

	protected Object result;		// TODO probably change this to Part, wouldn't need cast for results
	protected Shell shlPartSearch;
	private MyPartInventoryTable partTable_SearchDialog;
	private MyText partSearchTextBox;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public PartSearchDialog(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlPartSearch.open();
		shlPartSearch.layout();
		Display display = getParent().getDisplay();
		while (!shlPartSearch.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public Object open(String queryString) {
		createContents();
		
		// populate Search Text Box with selected Owner
		partSearchTextBox.setText(queryString);
		shlPartSearch.open();
		shlPartSearch.layout();
		Display display = getParent().getDisplay();
		while (!shlPartSearch.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlPartSearch = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlPartSearch.setText("Part Search");
		shlPartSearch.setSize(946, 410);
		
		Gui.setDialogAtCenter(shlPartSearch);

		// search results table						
		partTable_SearchDialog = new MyPartInventoryTable(shlPartSearch, SWT.BORDER | SWT.FULL_SELECTION);
		partTable_SearchDialog.setLinesVisible(true);
		partTable_SearchDialog.setHeaderVisible(true);
		partTable_SearchDialog.setBounds(10, 42, 908, 282);
		partTable_SearchDialog.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (partTable_SearchDialog.getSelection().length > 0) {
					// return Customer selected from Customer Search Dialog table on double click
					result =  partTable_SearchDialog.getSelection()[0].getData();
					shlPartSearch.close();
				}
			}
		});

		TableColumn tblclmnPartNumber_Inventory = new TableColumn(partTable_SearchDialog, SWT.NONE);
		tblclmnPartNumber_Inventory.setWidth(126);
		tblclmnPartNumber_Inventory.setText("Part Number");

		TableColumn tblclmnDescription_Inventory = new TableColumn(partTable_SearchDialog, SWT.NONE);
		tblclmnDescription_Inventory.setWidth(245);
		tblclmnDescription_Inventory.setText("Description");

		TableColumn tblclmnOnHand_Inventory = new TableColumn(partTable_SearchDialog, SWT.NONE);
		tblclmnOnHand_Inventory.setWidth(100);
		tblclmnOnHand_Inventory.setText("On Hand");

		TableColumn tblclmnSupplier_Inventory = new TableColumn(partTable_SearchDialog, SWT.NONE);
		tblclmnSupplier_Inventory.setWidth(109);
		tblclmnSupplier_Inventory.setText("Supplier");

		TableColumn tblclmnCategory_Inventory = new TableColumn(partTable_SearchDialog, SWT.NONE);
		tblclmnCategory_Inventory.setWidth(115);
		tblclmnCategory_Inventory.setText("Category");

		TableColumn tblclmnCost_Inventory = new TableColumn(partTable_SearchDialog, SWT.NONE);
		tblclmnCost_Inventory.setWidth(100);
		tblclmnCost_Inventory.setText("Cost");

		TableColumn tblclmnRetailPrice_Inventory = new TableColumn(partTable_SearchDialog, SWT.NONE);
		tblclmnRetailPrice_Inventory.setWidth(110);
		tblclmnRetailPrice_Inventory.setText("Retail Price");

		// dialog  controls
		Button btnSelectPart = new Button(shlPartSearch, SWT.NONE);
		btnSelectPart.setText("Select Part");
		btnSelectPart.setBounds(10, 330, 256, 26);
		btnSelectPart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (partTable_SearchDialog.getSelection().length > 0) {
					result =  partTable_SearchDialog.getSelection()[0].getData();
					shlPartSearch.close();
				}
			}
		});

		Button btnCancel = new Button(shlPartSearch, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(662, 330, 256, 26);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// close dialog with no selection
				result = null;
				shlPartSearch.close();
			}
		});

		partSearchTextBox = new MyText(shlPartSearch, SWT.BORDER);
		partSearchTextBox.setText("Search...");
		partSearchTextBox.setBounds(10, 10, 909, 26);
		partSearchTextBox.setFocus();
		partSearchTextBox.addModifyListener(new SearchTextBoxListeners(partSearchTextBox, partTable_SearchDialog));
		partSearchTextBox.addFocusListener(new TextBoxFocusListener(partSearchTextBox));
	}
}
