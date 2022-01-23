package cis2901c.main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
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
		// set properties (text fields) via Customer object, used when opening a current customer to edit
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
		this.customerId = customer.getCustomerId();
		this.customer = customer;
		
		// open new New Customer dialog, customize for editing current customer
		shlNewCustomer.setText("Edit Customer Details");
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
		shlNewCustomer.setSize(592, 255);
		shlNewCustomer.setText("New Customer");
		
		// New Customer dialog controls
		Button btnSaveCustomerButton = new Button(shlNewCustomer, SWT.NONE);
		btnSaveCustomerButton.setText("Save Customer");
		btnSaveCustomerButton.setBounds(50, 170, 181, 30);
		btnSaveCustomerButton.addMouseListener(new MouseAdapter() {		// in-line listener
			@Override
			public void mouseDown(MouseEvent e) {
				if (customerId == -1) {
					// create a new Customer
					addNewCustomer();
				} else {
					System.out.println("Save existing customer");
					// save modifications to existing customer
					saveCustomer(customer);
				}
			}
		});
		
		Button btnCancel = new Button(shlNewCustomer, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(332, 170, 181, 30);
		btnCancel.addMouseListener(new MouseAdapter() {		// in-line listener
			@Override
			public void mouseDown(MouseEvent e) {
				shlNewCustomer.dispose();
			}
		});
		
		// customer detail text boxes
		txtFirstName = new MyText(shlNewCustomer, SWT.BORDER);
		txtFirstName.setText("First Name...");
		txtFirstName.setBounds(10, 10, 272, 26);
		txtFirstName.addFocusListener(new TextBoxFocusListener(txtFirstName));
		txtFirstName.addModifyListener(new InfoTextBoxModifyListener(txtFirstName));
		
		txtLastName = new MyText(shlNewCustomer, SWT.BORDER);
		txtLastName.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtLastName.setText("Last Name/Company Name...");
		txtLastName.setBounds(292, 10, 272, 26);
		txtLastName.addFocusListener(new TextBoxFocusListener(txtLastName));
		txtLastName.addModifyListener(new RequiredTextBoxModifyListener(txtLastName));
		
		txtAddress = new MyText(shlNewCustomer, SWT.BORDER);
		txtAddress.setText("Address...");
		txtAddress.setBounds(10, 42, 272, 26);
		txtAddress.addFocusListener(new TextBoxFocusListener(txtAddress));
		txtAddress.addModifyListener(new InfoTextBoxModifyListener(txtAddress));
		
		txtCity = new MyText(shlNewCustomer, SWT.BORDER);
		txtCity.setText("City...");
		txtCity.setBounds(10, 74, 272, 26);
		txtCity.addFocusListener(new TextBoxFocusListener(txtCity));
		txtCity.addModifyListener(new InfoTextBoxModifyListener(txtCity));
		
		txtState = new MyText(shlNewCustomer, SWT.BORDER);
		txtState.setText("State...");
		txtState.setBounds(10, 106, 272, 26);
		txtState.addFocusListener(new TextBoxFocusListener(txtState));
		txtState.addModifyListener(new InfoTextBoxModifyListener(txtState));
		
		txtZipCode = new MyText(shlNewCustomer, SWT.BORDER);
		txtZipCode.setText("Zip Code...");
		txtZipCode.setBounds(10, 138, 272, 26);
		txtZipCode.addFocusListener(new TextBoxFocusListener(txtZipCode));
		txtZipCode.addModifyListener(new InfoTextBoxModifyListener(txtZipCode));
		
		txtHomePhone = new MyText(shlNewCustomer, SWT.BORDER);
		txtHomePhone.setText("Home Phone...");
		txtHomePhone.setBounds(292, 42, 272, 26);
		txtHomePhone.addFocusListener(new TextBoxFocusListener(txtHomePhone));
		txtHomePhone.addModifyListener(new InfoTextBoxModifyListener(txtHomePhone));
		
		txtWorkPhone = new MyText(shlNewCustomer, SWT.BORDER);
		txtWorkPhone.setText("Work Phone...");
		txtWorkPhone.setBounds(292, 74, 272, 26);
		txtWorkPhone.addFocusListener(new TextBoxFocusListener(txtWorkPhone));
		txtWorkPhone.addModifyListener(new InfoTextBoxModifyListener(txtWorkPhone));
		
		txtCellPhone = new MyText(shlNewCustomer, SWT.BORDER);
		txtCellPhone.setText("Cell Phone...");
		txtCellPhone.setBounds(292, 106, 272, 26);
		txtCellPhone.addFocusListener(new TextBoxFocusListener(txtCellPhone));
		txtCellPhone.addModifyListener(new InfoTextBoxModifyListener(txtCellPhone));
		
		txtEmail = new MyText(shlNewCustomer, SWT.BORDER);
		txtEmail.setText("E-Mail...");
		txtEmail.setBounds(292, 138, 272, 26);
		txtEmail.addFocusListener(new TextBoxFocusListener(txtEmail));
		txtEmail.addModifyListener(new InfoTextBoxModifyListener(txtEmail));
		
		shlNewCustomer.setTabList(new Control[]{txtFirstName, txtLastName, txtAddress, txtHomePhone, txtCity,
				txtWorkPhone, txtState, txtCellPhone, txtZipCode, txtEmail, btnSaveCustomerButton, btnCancel});
	}
	
	public void addNewCustomer() {
		saveCustomer(new Customer());
	}
	
	public void saveCustomer(Customer customer) {
		
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
				customer.setZipCode(0);
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
