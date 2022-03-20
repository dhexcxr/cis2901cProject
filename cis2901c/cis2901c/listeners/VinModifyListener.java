package cis2901c.listeners;

import java.util.logging.Level;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.wb.swt.SWTResourceManager;

import cis2901c.main.Main;
import cis2901c.objects.MyText;

public class VinModifyListener implements ModifyListener {

	private MyText txtBox;
	private String txtBoxStartingText;
	
	public VinModifyListener(MyText txtBox) {
		this.txtBox = txtBox;
		this.txtBoxStartingText = txtBox.getText();
	}

	@Override
	public void modifyText(ModifyEvent e) {
		// VIN txt box, make red if VIN isn't 17 digits long
		// we still allow user to make shorter VINs to accommodate VINs from the '60s etc
		Main.log(Level.INFO, "Editing unit: " + txtBox.isModified());
		if (txtBox.getText().length() != 17) {
			txtBox.setBackground(SWTResourceManager.getColor(255, 102, 102));		// RED
		} else {
			txtBox.setBackground(SWTResourceManager.getColor(255, 255, 255));		// WHITE
		}

		if (!txtBox.getText().equals(txtBoxStartingText)) {
			txtBox.setModified(true);
		} else {
			txtBox.setModified(false);
		}
		
	}

}
