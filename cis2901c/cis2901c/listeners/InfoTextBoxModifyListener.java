package cis2901c.listeners;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import cis2901c.MyText;

public class InfoTextBoxModifyListener implements ModifyListener {			// SWT implementation
	
	private MyText text;
	private String textBoxText;
	
	public InfoTextBoxModifyListener(MyText text) {
		// TODO Auto-generated constructor stub
		this.text = text;
		this.textBoxText = text.getText();
	}

	@Override
	public void modifyText(ModifyEvent e) {
		if (text.getText().length() > 0 && !text.getText().equals(textBoxText)) {
			text.setModified(true);
		} else {
			text.setModified(false);
		}	
	}
}
