package cis2901c.main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.SWTResourceManager;

import cis2901c.listeners.DbServices;
import cis2901c.listeners.InfoTextBoxModifyListener;
import cis2901c.listeners.RequiredTextBoxModifyListener;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.objects.Customer;
import cis2901c.objects.MyText;

public class NewCustomerDialog extends Dialog {

	protected Object result;
	protected Shell shlNewCustomer;
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
	// TODO i think this is a hack way to do this, customerId is used in Mouse Down listener for Save button
		// to determine if we need to call addNew... or saveExisting....
	private long customerId = -1;
	private Customer customer;

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
		shlNewCustomer.open();
		shlNewCustomer.layout();
		Display display = getParent().getDisplay();
		while (!shlNewCustomer.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public Object open(Customer customer) {
		// set properties (text fields) via Customer object, for opening a current customer to edit
		createContents();
		
		if (customer.getFirstName() != null)
			txtFirstName.setText(customer.getFirstName());
		if (customer.getAddress() != null)
			txtAddress.setText(customer.getAddress());
		if (customer.getCity() != null)
			txtCity.setText(customer.getCity());
		if (customer.getState() != null)
			txtState.setText(customer.getState());
		if (customer.getZipCode() != 0)
			txtZipCode.setText(Integer.toString(customer.getZipCode()));
		if (customer.getLastName() != null)
			txtLastName.setText(customer.getLastName());
		if (customer.getHomePhone() != 0)
			txtHomePhone.setText(Integer.toString(customer.getHomePhone()));
		if (customer.getWorkPhone() != 0)
			txtWorkPhone.setText(Integer.toString(customer.getWorkPhone()));
		if (customer.getCellPhone() != 0)
			txtCellPhone.setText(Integer.toString(customer.getCellPhone()));
		if (customer.getEmail() != null)
			txtEmail.setText(customer.getEmail());
		customerId = customer.getCustomerId();
		
		this.customer = customer;
		
		shlNewCustomer.setText("Modify Customer");
				
		shlNewCustomer.open();
		shlNewCustomer.layout();
		Display display = getParent().getDisplay();
		while (!shlNewCustomer.isDisposed()) {
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
		
		shlNewCustomer = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
//		shell.setLocation(0, 0);		// TODO get the position nicer
		shlNewCustomer.setSize(592, 255);
		shlNewCustomer.setText("New Customer");
		
		txtFirstName = new MyText(shlNewCustomer, SWT.BORDER);
		txtFirstName.setText("First Name...");
		txtFirstName.setBounds(10, 10, 272, 26);
		TextBoxFocusListener textBoxFocusListener = new TextBoxFocusListener(txtFirstName);
		txtFirstName.addFocusListener(textBoxFocusListener);
		InfoTextBoxModifyListener infoTextBoxModifyListener = new InfoTextBoxModifyListener(txtFirstName);
		txtFirstName.addModifyListener(infoTextBoxModifyListener);
		
		txtLastName = new MyText(shlNewCustomer, SWT.BORDER);
		txtLastName.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtLastName.setText("Last Name/Company Name...");
		txtLastName.setBounds(292, 10, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtLastName);
		txtLastName.addFocusListener(textBoxFocusListener);
//		RequiredTextBoxModifyListener lastNameModifyListener = new RequiredTextBoxModifyListener(txtLastName);
		txtLastName.addModifyListener(new RequiredTextBoxModifyListener(txtLastName));
		
		txtAddress = new MyText(shlNewCustomer, SWT.BORDER);
		txtAddress.setText("Address...");
		txtAddress.setBounds(10, 42, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtAddress);
		txtAddress.addFocusListener(textBoxFocusListener);
		txtAddress.addModifyListener(new InfoTextBoxModifyListener(txtAddress));
		
		txtCity = new MyText(shlNewCustomer, SWT.BORDER);
		txtCity.setText("City...");
		txtCity.setBounds(10, 74, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtCity);
		txtCity.addFocusListener(textBoxFocusListener);
		txtCity.addModifyListener(new InfoTextBoxModifyListener(txtCity));
		
		txtState = new MyText(shlNewCustomer, SWT.BORDER);
		txtState.setText("State...");
		txtState.setBounds(10, 106, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtState);
		txtState.addFocusListener(textBoxFocusListener);
		txtState.addModifyListener(new InfoTextBoxModifyListener(txtState));
		
		txtZipCode = new MyText(shlNewCustomer, SWT.BORDER);
		txtZipCode.setText("Zip Code...");
		txtZipCode.setBounds(10, 138, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtZipCode);
		txtZipCode.addFocusListener(textBoxFocusListener);
		txtZipCode.addModifyListener(new InfoTextBoxModifyListener(txtZipCode));
		
		txtHomePhone = new MyText(shlNewCustomer, SWT.BORDER);
		txtHomePhone.setText("Home Phone...");
		txtHomePhone.setBounds(292, 42, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtHomePhone);
		txtHomePhone.addFocusListener(textBoxFocusListener);
		txtHomePhone.addModifyListener(new InfoTextBoxModifyListener(txtHomePhone));
		
		txtWorkPhone = new MyText(shlNewCustomer, SWT.BORDER);
		txtWorkPhone.setText("Work Phone...");
		txtWorkPhone.setBounds(292, 74, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtWorkPhone);
		txtWorkPhone.addFocusListener(textBoxFocusListener);
		txtWorkPhone.addModifyListener(new InfoTextBoxModifyListener(txtWorkPhone));
		
		txtCellPhone = new MyText(shlNewCustomer, SWT.BORDER);
		txtCellPhone.setText("Cell Phone...");
		txtCellPhone.setBounds(292, 106, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtCellPhone);
		txtCellPhone.addFocusListener(textBoxFocusListener);
		txtCellPhone.addModifyListener(new InfoTextBoxModifyListener(txtCellPhone));
		
		txtEmail = new MyText(shlNewCustomer, SWT.BORDER);
		txtEmail.setText("E-Mail...");
		txtEmail.setBounds(292, 138, 272, 26);
		textBoxFocusListener = new TextBoxFocusListener(txtEmail);
		txtEmail.addFocusListener(textBoxFocusListener);
		txtEmail.addModifyListener(new InfoTextBoxModifyListener(txtEmail));
		
		Button btnSaveCustomerButton = new Button(shlNewCustomer, SWT.NONE);
		btnSaveCustomerButton.addMouseListener(new MouseAdapter() {		// in-line listener
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					if (customerId == -1) {
						addNewCustomer();
					} else {
						System.out.println("Save existing customer");
						// save modifications to existing customer
						saveCustomer(customer);
					}
				} catch (SQLException e1) {
					System.out.println("Error saving new customer data to database");
					e1.printStackTrace();
				}
			}
		});
		btnSaveCustomerButton.setBounds(50, 170, 181, 30);
		btnSaveCustomerButton.setText("Save Customer");
		
		Button btnCancel = new Button(shlNewCustomer, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {		// in-line listener
			@Override
			public void mouseDown(MouseEvent e) {
				shlNewCustomer.dispose();
			}
		});
		btnCancel.setBounds(332, 170, 181, 30);
		btnCancel.setText("Cancel");
		shlNewCustomer.setTabList(new Control[]{txtFirstName, txtLastName, txtAddress, txtHomePhone, txtCity, txtWorkPhone, txtState, txtCellPhone, txtZipCode, txtEmail, btnSaveCustomerButton, btnCancel});

	}
	
	public void addNewCustomer() throws SQLException {
		saveCustomer(new Customer());
	}
	
	public void saveCustomer(Customer customer) throws SQLException {
		
//		if (txtLastName.getText().equals("Last Name/Company Name...")) {
		if (!txtLastName.isModified()) {
			// dialog box stating last name is required
			MessageBox lastNameRequirementBox = new MessageBox(shlNewCustomer, SWT.ICON_INFORMATION);
			lastNameRequirementBox.setText("Notice");
			lastNameRequirementBox.setMessage("Please enter a Last Name or Company Name");
			lastNameRequirementBox.open();
			return;
		} else {
			customer.setLastName(txtLastName.getText());
		}
		
		if(txtFirstName.isModified()) {
			customer.setFirstName(txtFirstName.getText());
		}
		
		if (txtAddress.isModified()) {
			customer.setAddress(txtAddress.getText());
		}

		if (txtCity.isModified()) {
			customer.setCity(txtCity.getText());
		}
		
		if (txtState.isModified()) {
			customer.setState(txtState.getText());
		}
		
		if (txtZipCode.isModified()) {
			try {
				customer.setZipCode(Integer.parseInt(txtZipCode.getText()));		// maybe set zip to string in DB
			} catch (Exception e) {				// maybe put an error dialog box here
				System.out.println(e);
				e.printStackTrace();
//				customer.setZipCode(0);		// oh yeah, Object int fields default to 0
			}
		}
		
		
		// for phone numbers maybe do regex that removes all non-numeric characters 
		if (txtHomePhone.isModified()) {		// maybe set phone nums to string in DB
			try {
				customer.setHomePhone(Integer.parseInt(txtHomePhone.getText().replaceAll("[()\\s-]+", "")));
			} catch (Exception e) {				// maybe put an error dialog box here
				System.out.println(e);
				e.printStackTrace();
				customer.setHomePhone(0);
			}
		}
		
		if (txtWorkPhone.isModified()) {
			try {
				customer.setWorkPhone(Integer.parseInt(txtWorkPhone.getText().replaceAll("[()\\s-]+", "")));
			} catch (Exception e) {				// maybe put an error dialog box here
				System.out.println(e);
				e.printStackTrace();
				customer.setWorkPhone(0);
			}
		}
		
		if (txtCellPhone.isModified()) {
			try {
				customer.setCellPhone(Integer.parseInt(txtCellPhone.getText().replaceAll("[()\\s-]+", "")));
			} catch (Exception e) {				// maybe put an error dialog box here
				System.out.println(e);
				e.printStackTrace();
				customer.setCellPhone(0);
			}
		}
		
		if (txtEmail.isModified()) {		// make dialog box, if txtEmail.isModified, check its format
												// vs regex to ensure (name)@(domain).(tld)
			customer.setEmail(txtEmail.getText());
		}

		DbServices.saveObject(customer);
		
		shlNewCustomer.dispose();
	}
}

//class LastNameModifyListener implements ModifyListener {
//	
//	private MyText txtBox;
//	private String textBoxText;
//	
//	public LastNameModifyListener(MyText textBox) {
//		this.txtBox = textBox;
//		this.textBoxText = textBox.getText();
//	}
//
//	@Override
//	public void modifyText(ModifyEvent e) {
//		System.out.println(txtBox.isModified());
//		if (txtBox.getText().length() > 0 && !txtBox.getText().equals(textBoxText)) {
//			txtBox.setModified(true);
//			txtBox.setBackground(SWTResourceManager.getColor(255, 255, 255));		// WHITE
//		} else {
//			txtBox.setModified(false);
//			txtBox.setBackground(SWTResourceManager.getColor(255, 102, 102));		// RED
//		}	
//	}
//}
