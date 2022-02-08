package cis2901c.objects;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public abstract class MyTable extends Table {

	protected MyTable(Composite parent, int style) {
		super(parent, style);
	}
	
	public abstract void paint(Object object);
	
	public abstract void paint(Object[] objects);
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public abstract void sort(int i);
}
