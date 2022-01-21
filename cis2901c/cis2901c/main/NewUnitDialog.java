package cis2901c.main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import cis2901c.listeners.DbServices;
import cis2901c.listeners.InfoTextBoxModifyListener;
import cis2901c.listeners.RequiredTextBoxModifyListener;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.objects.Customer;
import cis2901c.objects.MyText;
import cis2901c.objects.Unit;

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;

public class NewUnitDialog extends Dialog {

	protected Object result;
	protected Shell shlNewUnit;
	private MyText txtOwner;
	private MyText txtMake;
	private MyText txtModelName;
	private MyText txtMileage;
	private MyText txtVinNumber;
	private MyText txtModel;
	private MyText txtYear;
	private MyText txtColor;
	private MyText txtNotes;
	
	// TODO i think this is a hack way to do this, customerId is used in Mouse Down listener for Save button
		// to determine if we need to call addNew... or saveExisting....
	private long unitId = -1;
	private long customerId = -1;
	private Unit unit;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewUnitDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlNewUnit.open();
		shlNewUnit.layout();
		Display display = getParent().getDisplay();
		while (!shlNewUnit.isDisposed()) {
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
		shlNewUnit = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlNewUnit.setSize(592, 348);
		shlNewUnit.setText("Add Unit");
		
		// TODO require Owner entry/make red if empty
		// TODO double click to search owners
		txtOwner = new MyText(shlNewUnit, SWT.BORDER);
		txtOwner.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				CustomerSearchDialog customerSearchDialog = new CustomerSearchDialog(shlNewUnit, getStyle());
				Customer selectedCustomer = (Customer) customerSearchDialog.open();
				if (selectedCustomer instanceof Customer) {
				txtOwner.setText(selectedCustomer.getLastName() + ", " + selectedCustomer.getFirstName());
				customerId = selectedCustomer.getCustomerId();
				}
				// this shoudl return a Customer object
				// need to set FirstName and LastName and customerId from selected customer
			}
		});
		txtOwner.setEditable(false);
		txtOwner.setText("Owner...");
		txtOwner.setBounds(10, 10, 554, 26);
		
		// TODO require Make entry/make red if empty
		txtMake = new MyText(shlNewUnit, SWT.BORDER);
		txtMake.setText("Make...");
		txtMake.setBounds(10, 42, 270, 26);
		txtMake.addFocusListener(new TextBoxFocusListener(txtMake));
		txtMake.addModifyListener(new InfoTextBoxModifyListener(txtMake));
		
		txtModelName = new MyText(shlNewUnit, SWT.BORDER);
		txtModelName.setText("Model Name...");
		txtModelName.setBounds(10, 74, 270, 26);
		txtModelName.addFocusListener(new TextBoxFocusListener(txtModelName));
		txtModelName.addModifyListener(new InfoTextBoxModifyListener(txtModelName));
		
		txtMileage = new MyText(shlNewUnit, SWT.BORDER);
		txtMileage.setText("Mileage...");
		txtMileage.setBounds(10, 106, 270, 26);
		txtMileage.addFocusListener(new TextBoxFocusListener(txtMileage));
		txtMileage.addModifyListener(new InfoTextBoxModifyListener(txtMileage));
		
		// TODO require 17 chars
		txtVinNumber = new MyText(shlNewUnit, SWT.BORDER);
		txtVinNumber.setText("Vin Number...");
		txtVinNumber.setBounds(10, 138, 554, 26);
		txtVinNumber.addFocusListener(new TextBoxFocusListener(txtVinNumber));
		txtVinNumber.addModifyListener(new RequiredTextBoxModifyListener(txtVinNumber));
		
		txtModel = new MyText(shlNewUnit, SWT.BORDER);
		txtModel.setText("Model...");
		txtModel.setBounds(294, 42, 270, 26);
		txtModel.addFocusListener(new TextBoxFocusListener(txtModel));
		txtModel.addModifyListener(new InfoTextBoxModifyListener(txtModel));
		
		txtYear = new MyText(shlNewUnit, SWT.BORDER);
		txtYear.setText("Year");
		txtYear.setBounds(294, 74, 270, 26);
		txtYear.addFocusListener(new TextBoxFocusListener(txtYear));
		txtYear.addModifyListener(new InfoTextBoxModifyListener(txtYear));
		
		txtColor = new MyText(shlNewUnit, SWT.BORDER);
		txtColor.setText("Color...");
		txtColor.setBounds(294, 106, 270, 26);
		txtColor.addFocusListener(new TextBoxFocusListener(txtColor));
		txtColor.addModifyListener(new InfoTextBoxModifyListener(txtColor));
		
		txtNotes = new MyText(shlNewUnit, SWT.BORDER);
		txtNotes.setText("Notes...");
		txtNotes.setBounds(10, 170, 554, 92);
		txtNotes.addFocusListener(new TextBoxFocusListener(txtNotes));
		txtNotes.addModifyListener(new InfoTextBoxModifyListener(txtNotes));
		
		Button btnSaveUnit = new Button(shlNewUnit, SWT.NONE);
		btnSaveUnit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// build Unit object, save to DB
			}
		});
		
		btnSaveUnit.addMouseListener(new MouseAdapter() {		// in-line listener
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					if (unitId == -1) {
						addNewUnit();
					} else {
						System.out.println("Save existing unit");
						// save modifications to existing customer
						saveUnit(unit);
					}
				} catch (SQLException e1) {
					System.out.println("Error saving new unit data to database");
					e1.printStackTrace();
				}
			}
		});
		
		btnSaveUnit.setBounds(10, 268, 413, 26);
		btnSaveUnit.setText("Save Unit");
		
		Button btnCancel = new Button(shlNewUnit, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shlNewUnit.dispose();
			}
		});
		btnCancel.setBounds(429, 268, 135, 26);
		btnCancel.setText("Cancel");
		shlNewUnit.setTabList(new Control[]{txtOwner, txtMake, txtModel, txtModelName, txtYear, txtMileage, txtColor, txtVinNumber, txtNotes, btnSaveUnit, btnCancel});

	}

	protected void addNewUnit() throws SQLException {
		saveUnit(new Unit());	
	}
	
	protected void saveUnit(Unit unit) throws SQLException {
		// if txt(dialog text boxes) are modified
			// set Unit.fields
			// (HINT, see NewCustomerDialog
		if (!txtMake.isModified()) {
			// dialog box stating last name is required
			MessageBox lastNameRequirementBox = new MessageBox(shlNewUnit, SWT.ICON_INFORMATION);
			lastNameRequirementBox.setText("Notice");
			lastNameRequirementBox.setMessage("Please enter a Make/Manufacturer");
			lastNameRequirementBox.open();
			return;
		} else {
			unit.setMake(txtMake.getText());
		}
		
		if (txtModelName.isModified()) {
			unit.setModelName(txtModelName.getText());
		}
		
		if (txtMileage.isModified()) {
			try {
				unit.setMileage(Integer.parseInt(txtMileage.getText()));
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
//				unit.setMileage(0);		// oh yeah, Object int fields default to 0
			}
		}
		
		if (txtVinNumber.isModified()) {
			unit.setVin(txtVinNumber.getText());
		}
		
		if (txtModel.isModified()) {
			unit.setModel(txtModel.getText());
		}
		
		if (txtYear.isModified()) {
			try {
				unit.setModelYear(Integer.parseInt(txtYear.getText()));
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
		
		if (txtColor.isModified()) {
			unit.setColor(txtColor.getText());
		}
		
		if (txtNotes.isModified()) {
			unit.setNotes(txtNotes.getText());
		}

		
		
//		DbServices.saveObject(unit);		// COMMENTED FOR TESTING
		
		shlNewUnit.dispose();
	}
}
