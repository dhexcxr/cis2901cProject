package cis2901c.objects;

import org.eclipse.swt.widgets.Composite;

public class RoTable extends MyTable {

	public RoTable(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paint(Object object) {
		// build each TableItem to fill Unit Table
			paint(object,this.getSelectionIndex());
	}

	@Override
	public void paint(Object object, int selectedTableItemIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Object[] objects) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sort(int i) {
		// TODO Auto-generated method stub

	}

}
