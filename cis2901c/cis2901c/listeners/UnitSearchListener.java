package cis2901c.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;

import cis2901c.main.UnitSearchDialog;
import cis2901c.objects.MyText;
import cis2901c.objects.Unit;

public class UnitSearchListener extends MouseAdapter{

	MyText txtBox;
	String txtBoxStartingText;

	public UnitSearchListener(MyText txtBox) {
		this.txtBox = txtBox;
		this.txtBoxStartingText = txtBox.getText();
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		UnitSearchDialog unitSearchDialog = new UnitSearchDialog(Display.getDefault().getActiveShell(), SWT.NONE);
		Unit selectedUnit;
		if (!txtBox.getText().equals(txtBoxStartingText)) {
			// if we're editing a current unit, pass Search to Unit Search Dialog
			selectedUnit = (Unit) unitSearchDialog.open(txtBox.getText());
		} else {
			// open normal, empty Unit Search Dialog
			selectedUnit = (Unit) unitSearchDialog.open();
		}
		if (selectedUnit instanceof Unit) {
			StringBuilder unitData = new StringBuilder(selectedUnit.getOwner());
			txtBox.setText(unitData.toString());
			txtBox.setData(selectedUnit);
		}
	}
}
