package cis2901c.listeners;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;

import cis2901c.objects.MyText;

public class TextBoxFocusListener implements FocusListener {
	private MyText txtBox;
	private String textBoxText;
	
	public TextBoxFocusListener(MyText textBox) {
		this.txtBox = textBox;
		this.textBoxText = textBox.getText();
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		System.out.println(textBoxText + " box focused gained");
		// enable "flashy" text box effects
		if (txtBox.getText().equals(textBoxText)) {
			txtBox.setText("");
		}		
	}

	@Override
	public void focusLost(FocusEvent e) {
		System.out.println(textBoxText + " box focused lost");
		// enable "flashy" text box effects
		if (txtBox.getText().equals("")) {
			txtBox.setText(textBoxText);
		}
	}
}
