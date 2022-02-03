package cis2901c.objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

public class MyUnitTable extends MyTable{

	public MyUnitTable(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void paint(Object[] unitResults) {
		// build each TableItem to fill Unit Table
		for (Unit unit : (Unit[]) unitResults) {
			if (unit == null) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
			tableItem.setText(new String[] {unit.getOwner(), unit.getMake(), unit.getModel(),
				unit.getModelName(), unit.getYear() == 0 ? "" : Integer.toString(unit.getYear()),
						Integer.toString(unit.getMileage()), unit.getColor(), unit.getVin(), unit.getNotes()});
			tableItem.setData(unit);
		}
	}

}
