package cis2901c.objects;

import org.eclipse.swt.widgets.Composite;

public class RepairOrderJobTable extends MyTable {
	
	public static final int JOB_NAME_COLUMN = 0;
	public static final int PART_TOTAL_COLUMN = 1;
	public static final int LABOR_TOTAL_COLUMN = 2;
	public static final int JOB_TOTAL_COLUMN = 3;

	public RepairOrderJobTable(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void paint(Object object) {
		// empty
	}

	@Override
	public void paint(Object object, int selectedItemIndex) {
		// empty
	}

	@Override
	public void paint(Object[] objects) {
		// empty

	}

	@Override
	public void sort(int i) {
		// empty
	}

}
