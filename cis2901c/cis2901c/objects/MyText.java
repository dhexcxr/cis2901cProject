package cis2901c.objects;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class MyText extends Text {
	
	private boolean modified = false;

	public MyText(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}	
}
