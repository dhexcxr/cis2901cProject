package cis2901c.listeners;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;

import cis2901c.objects.MyText;

public class TextBoxFocusListener implements FocusListener {				//SWT imple
	private MyText txtBox;
	private String textBoxText;
	
	public TextBoxFocusListener(MyText textBox) {
		this.txtBox = textBox;
		this.textBoxText = textBox.getText();
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		System.out.println(textBoxText + " box focused gained");
		if (txtBox.getText().equals(textBoxText)) {
			txtBox.setText("");
		}		
	}

	@Override
	public void focusLost(FocusEvent e) {
		System.out.println(textBoxText + " box focused lost");
		if (txtBox.getText().equals("")) {
			txtBox.setText(textBoxText);
//			txtBox.setModified(false);
//		} else {
//			txtBox.setModified(true);
		}
	}
}
