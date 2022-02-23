package cis2901c.listeners;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cis2901c.main.Main;
import cis2901c.objects.LaborTable;
import cis2901c.objects.MyText;
import cis2901c.objects.RepairOrderJobTable;

public class RepairOrderLaborEditorListener implements Listener {

	private LaborTable jobLaborTable;
	private TableItem selectedTableItem;
	private int selectedTableItemIndex;
	private int selectedColumnIndex;
	private Text editorTxtBox;
	
//	private List<MyText> invoiceDetailText;
	private RepairOrderJobTable tableJobsRepairOrder;
	
//	private Text txtLaborTotal;

	private static final String ONLY_DECIMALS = "[^0-9.]";		// find a better name

	public RepairOrderLaborEditorListener(LaborTable jobLaborTable, int selectedTableItemIndex, int selectedColumnIndex,
										Text editorTxtBox, List<MyText> invoiceDetailText, RepairOrderJobTable tableJobsRepairOrder) {
		this.jobLaborTable = jobLaborTable;
		this.selectedTableItem = jobLaborTable.getItem(selectedTableItemIndex);
		this.selectedTableItemIndex = selectedTableItemIndex;
		this.selectedColumnIndex = selectedColumnIndex;
		this.editorTxtBox = editorTxtBox;

//		this.txtLaborTotal = invoiceDetailText.get(0);
		
//		this.invoiceDetailText = invoiceDetailText;
		this.tableJobsRepairOrder = tableJobsRepairOrder;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.type == SWT.FocusOut) {
			setColumnData();
		} else if (event.type == SWT.Traverse && event.detail == SWT.TRAVERSE_RETURN) {
			setColumnData();
		} else if (event.type == SWT.Traverse && event.detail == SWT.TRAVERSE_ESCAPE) {
			editorTxtBox.dispose();
		}
		
		setTotalLabor();
	}
	
	private void setColumnData() {
		if (selectedColumnIndex == LaborTable.TECHNICIAN_COLUMN || selectedColumnIndex == LaborTable.DESCRIPTION_COLUMN) {
			selectedTableItem.setText(selectedTableItemIndex, editorTxtBox.getText());
		} else if (selectedColumnIndex == LaborTable.HOURS_COLUMN || selectedColumnIndex == LaborTable.RATE_COLUMN) {
																			 // TODO fix this,   v   , probably needs to be ONLY_DECIMALS
			selectedTableItem.setText(selectedColumnIndex, editorTxtBox.getText().replaceAll("[^.0-9]", "").equals("") ? "0" :
				editorTxtBox.getText().replaceAll(ONLY_DECIMALS, ""));
			calculateJobTotal();
		}
		editorTxtBox.dispose();
	}
	
	private void calculateJobTotal() {
		BigDecimal hours = new BigDecimal(selectedTableItem.getText(LaborTable.HOURS_COLUMN));
		BigDecimal rate = new BigDecimal(selectedTableItem.getText(LaborTable.RATE_COLUMN));
		selectedTableItem.setText(LaborTable.TOTAL_COLUMN, (hours.multiply(rate).toString()));
	}
	
	private void setTotalLabor() {
		tableJobsRepairOrder.getSelection()[0].setText(RepairOrderJobTable.LABOR_TOTAL_COLUMN, calculateLaborTotal().toString());
	}
	
	private BigDecimal calculateLaborTotal() {
//		BigDecimal taxRate = BigDecimal.valueOf(0.065);		// TODO set tax rate in application settings
		BigDecimal total = BigDecimal.valueOf(0);
		TableItem[] items = jobLaborTable.getItems();
		for (TableItem item : items) {
			if (item.getText(LaborTable.TOTAL_COLUMN).equals("")) {
				// ignore new TableItem at end of list with no data set 
				break;
			}
			Main.log(Level.INFO, "labor Total before: " + total.toString());
			total = total.add(new BigDecimal(item.getText(LaborTable.TOTAL_COLUMN)));
			Main.log(Level.INFO, "labor price to BD: " + new BigDecimal(item.getText(LaborTable.TOTAL_COLUMN)).toString());
			Main.log(Level.INFO, "labor Total after: " + total.toString());
		}
		return total;
	}

}
