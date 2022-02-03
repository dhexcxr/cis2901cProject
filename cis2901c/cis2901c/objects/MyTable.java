package cis2901c.objects;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class MyTable extends Table {

	public MyTable(Composite parent, int style) {
		super(parent, style);
	}
	
	public void paint(Object object) {
		System.out.println("This shouldn't ever be called");
		System.out.println(Thread.currentThread().getStackTrace());
	}
	
	public void paint(Object[] objects) {
		System.out.println("This shouldn't ever be called");
		System.out.println(Thread.currentThread().getStackTrace());
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
