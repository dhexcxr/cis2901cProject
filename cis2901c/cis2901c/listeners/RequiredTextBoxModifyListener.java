package cis2901c.listeners;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.wb.swt.SWTResourceManager;

import cis2901c.MyText;

public class LastNameModifyListener implements ModifyListener {
	
	private MyText txtBox;
	private String textBoxText;
	
	public LastNameModifyListener(MyText textBox) {
		this.txtBox = textBox;
		this.textBoxText = textBox.getText();
	}

	@Override
	public void modifyText(ModifyEvent e) {
		System.out.println(txtBox.isModified());
		if (txtBox.getText().length() > 0 && !txtBox.getText().equals(textBoxText)) {
			txtBox.setModified(true);
			txtBox.setBackground(SWTResourceManager.getColor(255, 255, 255));		// WHITE
		} else {
			txtBox.setModified(false);
			txtBox.setBackground(SWTResourceManager.getColor(255, 102, 102));		// RED
		}	
	}
}
