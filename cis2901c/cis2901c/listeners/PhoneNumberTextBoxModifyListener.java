package cis2901c.listeners;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import cis2901c.objects.MyText;

public class PhoneNumberTextBoxModifyListener implements ModifyListener{
	private MyText text;
	private String textBoxText;
	
	// allow us to ignore modify events inside listener
	boolean ignore = false;
	
	public PhoneNumberTextBoxModifyListener(MyText text) {
		this.text = text;
		this.textBoxText = text.getText();
	}

	@Override
	public void modifyText(ModifyEvent e) {
		// help track if text box has been modified
		if (ignore) {
			return;
		}
		
		if (text.getText().length() > 0 && !text.getText().equals(textBoxText)) {
			// format text in text box, account for different length county codes
			text.setModified(true);
			ignore = true;
			text.setText(text.getText().replaceAll("[^0-9]", ""));
			if (text.getText().replaceAll("[^0-9]", "").length() >= 7 && text.getText().replaceAll("[^0-9]", "").length() < 10) {
				text.setText(text.getText().replaceFirst("(\\d{3})(\\d+)", "$1-$2"));
			} else if (text.getText().replaceAll("[^0-9]", "").length() < 11) {
				text.setText(text.getText().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3"));
			} else if (text.getText().replaceAll("[^0-9]", "").length() == 11) {
				text.setText(text.getText().replaceFirst("(\\d{1})(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3-$4"));
			} else if (text.getText().replaceAll("[^0-9]", "").length() == 12) {
				text.setText(text.getText().replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3-$4"));
			} else if (text.getText().replaceAll("[^0-9]", "").length() == 13) {
				text.setText(text.getText().replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3-$4"));
			}
			ignore = false;
			text.setSelection(text.getText().length());
		} else {
			text.setModified(false);
		}
	}
}