package cis2901c.main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import cis2901c.listeners.DbServices;
import cis2901c.listeners.InfoTextBoxModifyListener;
import cis2901c.listeners.RequiredTextBoxModifyListener;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.objects.Customer;
import cis2901c.objects.MyText;
import cis2901c.objects.Unit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

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
	// TODO i think this is a hack way to do this, unitId is used in Mouse Down listener for Save button
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
//		setText("SWT Dialog");
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
	
	public Object open(Unit unit) {
		createContents();

		// set txtBoxes when opening a current Unit for editing
		if (unit.getOwner() != null)
			txtOwner.setText(unit.getOwner());
		if (unit.getMake() != null)
			txtMake.setText(unit.getMake());
		if (unit.getModelName() != null)
			txtModelName.setText(unit.getModelName());
		if (unit.getMileage() != 0)
			txtMileage.setText(Integer.toString(unit.getMileage()));
		if (unit.getVin() != null)
			txtVinNumber.setText(unit.getVin());
		if (unit.getModel() != null)
			txtModel.setText(unit.getModel());
		if (unit.getModelYear() != 0)
			txtYear.setText(Integer.toString(unit.getModelYear()));
		if (unit.getColor() != null)
			txtColor.setText(unit.getColor());
		if (unit.getNotes() != null)
			txtNotes.setText(unit.getNotes());
		if (unit.getUnitId() != 0)
			unitId = unit.getUnitId();
		if (unit.getCustomerId() != -1)
			customerId = unit.getCustomerId(); 
		this.unit = unit;
		
		// open Edit Unit dialog, customized for editing a current Unit
		shlNewUnit.setText("Edit Unit Details");
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
		shlNewUnit.setText("Add Unit");
		shlNewUnit.setSize(592, 348);
		
		// Unit dialog controls
		Button btnSaveUnit = new Button(shlNewUnit, SWT.NONE);
		btnSaveUnit.setText("Save Unit");
		btnSaveUnit.setBounds(10, 268, 413, 26);
		btnSaveUnit.addMouseListener(new MouseAdapter() {		// in-line listener
			@Override
			public void mouseDown(MouseEvent e) {
				if (unitId == -1) {
					// save new Unit with current text boxes
					addNewUnit();
				} else {
					System.out.println("Save existing unit");
					// save modifications to existing customer
					saveUnit(unit);
				}
			}
		});
		
		Button btnCancel = new Button(shlNewUnit, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(429, 268, 135, 26);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shlNewUnit.dispose();
			}
		});
		
		// Unit text boxes
		txtOwner = new MyText(shlNewUnit, SWT.BORDER);
		txtOwner.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtOwner.setText("Owner...");
		txtOwner.setBounds(10, 10, 554, 26);
		txtOwner.setEditable(false);
		txtOwner.addModifyListener(new RequiredTextBoxModifyListener(txtOwner));
		txtOwner.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// open Customer Search dialog with Owner text box double click
				CustomerSearchDialog customerSearchDialog = new CustomerSearchDialog(shlNewUnit, getStyle());
				Customer selectedCustomer = new Customer();
				if (!txtOwner.getText().equals("Owner...")) {
					// if we're editing a current unit, pass Owner name to Customer Search Dialog
					selectedCustomer = (Customer) customerSearchDialog.open(txtOwner.getText());
				} else {
					// open normal, empty Customer Search Dialog
					selectedCustomer = (Customer) customerSearchDialog.open();
				}
				if (selectedCustomer instanceof Customer) {
					txtOwner.setText(selectedCustomer.getLastName() + ", " + selectedCustomer.getFirstName());
					customerId = selectedCustomer.getCustomerId();
				}
			}
		});
		
		txtMake = new MyText(shlNewUnit, SWT.BORDER);
		txtMake.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtMake.setText("Make...");
		txtMake.setBounds(10, 42, 270, 26);
		txtMake.addFocusListener(new TextBoxFocusListener(txtMake));
		txtMake.addModifyListener(new RequiredTextBoxModifyListener(txtMake));
		
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
		
		txtVinNumber = new MyText(shlNewUnit, SWT.BORDER);
		txtVinNumber.setText("Vin Number...");
		txtVinNumber.setBounds(10, 138, 554, 26);
		txtVinNumber.addFocusListener(new TextBoxFocusListener(txtVinNumber));
		txtVinNumber.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				// VIN txt box, make red if VIN isn't 17 digits long
					// we still allow user to make shorter VINs to accommodate VINs from the '60s etc
				System.out.println(txtVinNumber.isModified());
				if (!txtVinNumber.getText().equals("Vin Number...") && txtVinNumber.getText().length() != 17) {
					txtVinNumber.setModified(false);
					txtVinNumber.setBackground(SWTResourceManager.getColor(255, 102, 102));		// RED
				} else {
					txtVinNumber.setModified(true);
					txtVinNumber.setBackground(SWTResourceManager.getColor(255, 255, 255));		// WHITE
				}
			}
		});
		
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
		
		shlNewUnit.setTabList(new Control[]{txtOwner, txtMake, txtModel, txtModelName, txtYear, txtMileage, txtColor, txtVinNumber, txtNotes, btnSaveUnit, btnCancel});

	}

	protected void addNewUnit() {
		saveUnit(new Unit());	
	}
	
	protected void saveUnit(Unit unit) {
		if (customerId == -1) {
			// if we haven't selected an Owner, complain - a Unit Owner is required
			MessageBox ownerRequirementBox = new MessageBox(shlNewUnit, SWT.ICON_INFORMATION);
			ownerRequirementBox.setText("Notice");
			ownerRequirementBox.setMessage("Please select an Owner");
			ownerRequirementBox.open();
			return;
		} else {
			unit.setCustomerId(customerId);
			unit.setOwner(txtOwner.getText());			
		}
		
		if (txtMake.isModified()) {
			unit.setMake(txtMake.getText());
		} else {
			// dialog box stating make is required
			MessageBox makeRequirementBox = new MessageBox(shlNewUnit, SWT.ICON_INFORMATION);
			makeRequirementBox.setText("Notice");
			makeRequirementBox.setMessage("Please enter a Make/Manufacturer");
			makeRequirementBox.open();
			return;
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
				
		DbServices.saveObject(unit);
		shlNewUnit.dispose();
		}
	}
}