package cis2901c;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.SWTResourceManager;

public class NewCustomerDialog extends Dialog {

	protected Object result;
	protected Shell shell;
//	protected Shell parent;
	private MyText txtFirstName;
	private MyText txtAddress;
	private MyText txtCity;
	private MyText txtState;
	private MyText txtZipCode;
	private MyText txtLastName;
	private MyText txtHomePhone;
	private MyText txtWorkPhone;
	private MyText txtCellPhone;
	private MyText txtEmail;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewCustomerDialog(Shell parent, int style) {
		super(parent, style);
//		this.parent = parent;
//		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		// TODO add function to highlight Last Name box with Red background if no text in box - DONE
		
		shell = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
//		shell.setLocation(0, 0);		// TODO get the position nicer
		shell.setSize(580, 255);
		shell.setText(getText());
		
		txtFirstName = new MyText(shell, SWT.BORDER);
		txtFirstName.setText("First Name...");
		txtFirstName.setBounds(10, 10, 272, 26);
		TextBoxFocusListener textBoxFocusListener = new TextBoxFocusListener(txtFirstName);
		txtFirstName.addFocusListener(textBoxFocusListener);
		InfoTextBoxModifyListener infoTextBoxModifyListener = new InfoTextBoxModifyListener(txtFirstName);
		txtFirstName.addModifyListener(infoTextBoxModifyListener);
		
		txtAddress = new MyText(shell, SWT.BORDER);
		txtAddress.setText("Address...");
		txtAddress.setBounds(10, 42, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtAddress);
		txtAddress.addFocusListener(textBoxFocusListener);
		
		txtCity = new MyText(shell, SWT.BORDER);
		txtCity.setText("City...");
		txtCity.setBounds(10, 74, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtCity);
		txtCity.addFocusListener(textBoxFocusListener);
		
		txtState = new MyText(shell, SWT.BORDER);
		txtState.setText("State,..");
		txtState.setBounds(10, 106, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtState);
		txtState.addFocusListener(textBoxFocusListener);
		
		txtZipCode = new MyText(shell, SWT.BORDER);
		txtZipCode.setText("Zip Code...");
		txtZipCode.setBounds(10, 138, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtZipCode);
		txtZipCode.addFocusListener(textBoxFocusListener);
		
		txtLastName = new MyText(shell, SWT.BORDER);
		txtLastName.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtLastName.setText("Last Name/Company Name...");
		txtLastName.setBounds(292, 10, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtLastName);
		txtLastName.addFocusListener(textBoxFocusListener);
		LastNameModifyListener lastNameModifyListener = new LastNameModifyListener(txtLastName);
		txtLastName.addModifyListener(lastNameModifyListener);
		
		txtHomePhone = new MyText(shell, SWT.BORDER);
		txtHomePhone.setText("Home Phone...");
		txtHomePhone.setBounds(292, 42, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtHomePhone);
		txtHomePhone.addFocusListener(textBoxFocusListener);
		
		txtWorkPhone = new MyText(shell, SWT.BORDER);
		txtWorkPhone.setText("Work Phone...");
		txtWorkPhone.setBounds(292, 74, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtWorkPhone);
		txtWorkPhone.addFocusListener(textBoxFocusListener);
		
		txtCellPhone = new MyText(shell, SWT.BORDER);
		txtCellPhone.setText("Cell Phone...");
		txtCellPhone.setBounds(292, 106, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtCellPhone);
		txtCellPhone.addFocusListener(textBoxFocusListener);
		
		txtEmail = new MyText(shell, SWT.BORDER);
		txtEmail.setText("E-Mail...");
		txtEmail.setBounds(292, 138, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtEmail);
		txtEmail.addFocusListener(textBoxFocusListener);
		
		Button btnSaveCustomerButton = new Button(shell, SWT.NONE);
		btnSaveCustomerButton.addMouseListener(new MouseAdapter() {		// in-line listener
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					addNewCustomer();
				} catch (SQLException e1) {
					System.out.println("Error saving new customer data to database");
					e1.printStackTrace();
				}
			}
		});
		btnSaveCustomerButton.setBounds(50, 170, 181, 30);
		btnSaveCustomerButton.setText("Save Customer");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {		// in-line listener
			@Override
			public void mouseDown(MouseEvent e) {
				shell.dispose();
			}
		});
		btnCancel.setBounds(332, 170, 181, 30);
		btnCancel.setText("Cancel");
		shell.setTabList(new Control[]{txtFirstName, txtLastName, txtAddress, txtHomePhone, txtCity, txtWorkPhone, txtState, txtCellPhone, txtZipCode, txtEmail, btnSaveCustomerButton, btnCancel});

	}
	
	public void addNewCustomer() throws SQLException {
		
		boolean isAnythingModified = false;
		
		if (!txtLastName.isModified()) {
			// dialog box stating last name is required
			return;
		}
		
		// TODO finish
		
		System.out.println("Save New Cusrtoemr button pressed");
		// on "Save" button press in New Customer dialog
		// get data entered into dialog
		// sanitize, regex phones to 10 digit, check email format, require at least last name
		
		
		StringBuilder queryString = new StringBuilder("INSERT INTO cis2901c.customer ( ) VALUES ( );");
		// use regex to build query
		//	if (txtXXX.isModified() {
		//		take queryString, find first set of parens, insert Column Name
		//		find second set of parens, insert txtXXX.getText()
		
		if (txtFirstName.isModified()) {
			isAnythingModified = true;
			StringBuilder firstNameColumn  = new StringBuilder();
			int columnInsertionPoint = queryString.indexOf(")");
			if (queryString.charAt(columnInsertionPoint - 2) != '(') {
				firstNameColumn.append(',');
			}
			firstNameColumn.append("firstName");
			queryString.insert(columnInsertionPoint - 1, firstNameColumn);
			
			StringBuilder firstNameField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 2) != '(') {
				firstNameField.append(',');
			}
			firstNameField.append("'" + txtFirstName.getText() + "'");
			queryString.insert(fieldInsertionPoint, firstNameField);
		}
		
		if (txtLastName.isModified()) {
			isAnythingModified = true;
			StringBuilder lastNameColumn  = new StringBuilder();
			int columnInsertionPoint = queryString.indexOf(")");
			if (queryString.charAt(columnInsertionPoint - 2) != '(') {
				lastNameColumn.append(',');
			}
			lastNameColumn.append("lastName");
			queryString.insert(columnInsertionPoint - 1, lastNameColumn);
			
			StringBuilder lastNameField = new StringBuilder();
			int fieldInsertionPoint = queryString.lastIndexOf(")");
			if (queryString.charAt(fieldInsertionPoint - 2) != '(') {
				lastNameField.append(',');
			}
			lastNameField.append("'" + txtLastName.getText() + "'");
			queryString.insert(fieldInsertionPoint, lastNameField);
		}
		
		if (isAnythingModified) {
			Connection dbConnection = Main.getDbConnection();
//			
			PreparedStatement statement = dbConnection.prepareStatement(queryString.toString());
//			statement.setString(1, txtFirstName.getText());
//			statement.setString(2, "%" + query + "%");
			statement.execute();
			dbConnection.close();
		}
		
		// at the end, PreparedStatement statement = Main.getDbConnection().prepareStatement(queryString);
		// statement.execute();
		
		shell.dispose();
	}
}

class LastNameModifyListener implements ModifyListener {
	
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
		//			background = red		
	}
	
}
