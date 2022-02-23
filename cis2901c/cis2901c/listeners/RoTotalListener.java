package cis2901c.listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;

import cis2901c.objects.MyText;
import cis2901c.objects.RepairOrderJobTable;

public class RoTotalListener implements Listener{
	
	private RepairOrderJobTable roJobTable;
	private MyText roSubTotal;
	private MyText roTax;
	private MyText roFinalTotal;
	
	private static final String ONLY_DECIMALS = "[^0-9.]";		// find a better name

	public RoTotalListener(RepairOrderJobTable roJobTable, MyText roSubTotal, MyText roTax, MyText roFinalTotal) {
		super();
		this.roJobTable = roJobTable;
		this.roSubTotal = roSubTotal;
		this.roTax = roTax;
		this.roFinalTotal = roFinalTotal;
	}

	@Override
	public void handleEvent(Event event) {
		BigDecimal total = BigDecimal.valueOf(0);
		for (TableItem currentItem: roJobTable.getItems()) {
			String totalString = currentItem.getText(RepairOrderJobTable.JOB_TOTAL_COLUMN).replaceAll(ONLY_DECIMALS, "");
			total = total.add(new BigDecimal(totalString));
		}
		roSubTotal.setText("$" + total.setScale(2, RoundingMode.CEILING).toString());
		BigDecimal tax = total.multiply(BigDecimal.valueOf(0.065));
		roTax.setText("$" + tax.setScale(2, RoundingMode.CEILING).toString());
		roFinalTotal.setText("$" + total.add(tax).setScale(2, RoundingMode.CEILING).toString());
	}

}
