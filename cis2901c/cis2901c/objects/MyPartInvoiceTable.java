package cis2901c.objects;

import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

import cis2901c.main.Main;

public class MyPartInvoiceTable extends MyTable{
	
	// TODO find out where these are being used and see if I want to move them somewhere else, these are table indecies for Invoice Table columns
	public static final int PART_NUMBER_COLUMN = 0;
	public static final int QUANTITY_COLUMN = 2;
	public static final int PART_PRICE_COLUMN = 5;
	public static final int EXTENDED_PRICE_COLUMN = 6;

	public MyPartInvoiceTable(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paint(Object object) {
		// build each TableItem to fill Unit Table
			if (object == null) {
				return;
			}
			Part part = (Part) object;
			TableItem tableItem = this.getItem(this.getSelectionIndex());
				// TODO break out all tables into classes so we can correctly paint all without conditional paint	
			tableItem.setText(new String[] {part.getPartNumber(), part.getDescription(), Integer.toString(1),
					Integer.toString(part.getOnHand()),	part.getCost().toString(), part.getRetail().toString(),
							part.getRetail().toString()});
			tableItem.setData(part);
	}
	
	@Override
	public void paint(Object[] partResults) {
		// build each TableItem to fill Unit Table
		for (Part part : (Part[]) partResults) {
			if (part == null) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
				// TODO break out all tables into classes so we can correctly paint all without conditional paint
			tableItem.setText(new String[] {part.getPartNumber(), part.getDescription(), Integer.toString(1),
													Integer.toString(part.getOnHand()), part.getCost().toString(),
														part.getRetail().toString(), part.getRetail().toString()});
			tableItem.setData(part);
		}
	}
	
	@Override
	public void sort(int sortColumn) {
		// invoice table doesn't sort, only display parts in order they were entered
		Main.log(Level.WARNING, "Paint method should not be called on Part Invoice Table");
	}
}