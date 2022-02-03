package cis2901c.objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

public class MyPartInventoryTable extends MyTable{

	public MyPartInventoryTable(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
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
			tableItem.setText(new String[] {part.getPartNumber(), part.getDescription(),
					Integer.toString(part.getOnHand() == -1 ? 0 : part.getOnHand()), part.getSupplier(),
								part.getCategory(), part.getCost().toString(), part.getRetail().toString()});
			tableItem.setData(part);
		}
	}

}
