package cis2901c.main;

import cis2901c.listeners.CustomerSearchListener;
import cis2901c.listeners.DbServices;
import cis2901c.listeners.InfoTextBoxModifyListener;
import cis2901c.listeners.RequiredTextBoxModifyListener;
import cis2901c.listeners.TextBoxFocusListener;
import cis2901c.objects.Customer;
import cis2901c.objects.MyText;
import cis2901c.objects.Unit;

import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

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
		if (unit.getYear() != 0)
			txtYear.setText(Integer.toString(unit.getYear()));
		if (unit.getColor() != null)
			txtColor.setText(unit.getColor());
		if (unit.getNotes() != null)
			txtNotes.setText(unit.getNotes());
		if (unit.getUnitId() != 0)
			unitId = unit.getUnitId();
		if (unit.getCustomerId() != 0)
			customerId = unit.getCustomerId();
		this.unit = unit;
		
		txtOwner.setData(DbServices.searchForCustomer(customerId));
		
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
		shlNewUnit.setSize(592, 516);
		
		Gui.setDialogAtCenter(shlNewUnit);
				
		// Unit text boxes
		Label lblOwner = new Label(shlNewUnit, SWT.NONE);
		lblOwner.setBounds(10, 10, 70, 20);
		lblOwner.setText("Owner:");
		txtOwner = new MyText(shlNewUnit, SWT.BORDER);
		txtOwner.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtOwner.setText("");
		txtOwner.setBounds(10, 36, 554, 26);
		txtOwner.setEditable(false);
		txtOwner.addModifyListener(new RequiredTextBoxModifyListener(txtOwner));
		txtOwner.addMouseListener(new CustomerSearchListener(txtOwner));

		Label lblMake = new Label(shlNewUnit, SWT.NONE);
		lblMake.setBounds(10, 68, 70, 20);
		lblMake.setText("Make:");
		txtMake = new MyText(shlNewUnit, SWT.BORDER);
		txtMake.setBackground(SWTResourceManager.getColor(255, 102, 102));
//		txtMake.setText("Make...");
		txtMake.setBounds(10, 94, 270, 26);
		txtMake.addFocusListener(new TextBoxFocusListener(txtMake));
		txtMake.addModifyListener(new RequiredTextBoxModifyListener(txtMake));

		Label lblModel = new Label(shlNewUnit, SWT.NONE);
		lblModel.setBounds(294, 68, 70, 20);
		lblModel.setText("Model:");
		txtModel = new MyText(shlNewUnit, SWT.BORDER);
//		txtModel.setText("Model...");
		txtModel.setBounds(286, 94, 270, 26);
		txtModel.addFocusListener(new TextBoxFocusListener(txtModel));
		txtModel.addModifyListener(new InfoTextBoxModifyListener(txtModel));

		Label lblModelName = new Label(shlNewUnit, SWT.NONE);
		lblModelName.setBounds(10, 126, 90, 20);
		lblModelName.setText("Model Name:");
		txtModelName = new MyText(shlNewUnit, SWT.BORDER);
//		txtModelName.setText("Model Name...");
		txtModelName.setBounds(10, 152, 270, 26);
		txtModelName.addFocusListener(new TextBoxFocusListener(txtModelName));
		txtModelName.addModifyListener(new InfoTextBoxModifyListener(txtModelName));

		Label lblYear = new Label(shlNewUnit, SWT.NONE);
		lblYear.setBounds(286, 126, 70, 20);
		lblYear.setText("Year:");
		txtYear = new MyText(shlNewUnit, SWT.BORDER);
//		txtYear.setText("Year");
		txtYear.setBounds(286, 152, 270, 26);
		txtYear.addFocusListener(new TextBoxFocusListener(txtYear));
		txtYear.addModifyListener(new InfoTextBoxModifyListener(txtYear));

		Label lblMileage = new Label(shlNewUnit, SWT.NONE);
		lblMileage.setBounds(10, 184, 70, 20);
		lblMileage.setText("Mileage:");
		txtMileage = new MyText(shlNewUnit, SWT.BORDER);
//		txtMileage.setText("Mileage...");
		txtMileage.setBounds(10, 210, 270, 26);
		txtMileage.addFocusListener(new TextBoxFocusListener(txtMileage));
		txtMileage.addModifyListener(new InfoTextBoxModifyListener(txtMileage));

		Label lblColor = new Label(shlNewUnit, SWT.NONE);
		lblColor.setBounds(286, 184, 70, 20);
		lblColor.setText("Color:");
		txtColor = new MyText(shlNewUnit, SWT.BORDER);
//		txtColor.setText("Color...");
		txtColor.setBounds(286, 210, 270, 26);
		txtColor.addFocusListener(new TextBoxFocusListener(txtColor));
		txtColor.addModifyListener(new InfoTextBoxModifyListener(txtColor));

		Label lblVinNumber = new Label(shlNewUnit, SWT.NONE);
		lblVinNumber.setBounds(10, 242, 90, 20);
		lblVinNumber.setText("Vin Number:");
		txtVinNumber = new MyText(shlNewUnit, SWT.BORDER);
//		txtVinNumber.setText("Vin Number...");
		txtVinNumber.setBounds(10, 268, 554, 26);
		txtVinNumber.addFocusListener(new TextBoxFocusListener(txtVinNumber));
		txtVinNumber.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				// VIN txt box, make red if VIN isn't 17 digits long
					// we still allow user to make shorter VINs to accommodate VINs from the '60s etc
				Main.log(Level.INFO, "Editing unit: " + txtVinNumber.isModified());
				if (!txtVinNumber.getText().equals("Vin Number...") && txtVinNumber.getText().length() != 17) {
					txtVinNumber.setModified(false);
					txtVinNumber.setBackground(SWTResourceManager.getColor(255, 102, 102));		// RED
				} else {
					txtVinNumber.setModified(true);
					txtVinNumber.setBackground(SWTResourceManager.getColor(255, 255, 255));		// WHITE
				}
			}
		});
		
		Label lblNotes = new Label(shlNewUnit, SWT.NONE);
		lblNotes.setBounds(10, 300, 70, 20);
		lblNotes.setText("Notes:");
		txtNotes = new MyText(shlNewUnit, SWT.BORDER | SWT.WRAP);
//		txtNotes.setText("Notes...");
		txtNotes.setBounds(10, 326, 554, 92);
		txtNotes.addFocusListener(new TextBoxFocusListener(txtNotes));
		txtNotes.addModifyListener(new InfoTextBoxModifyListener(txtNotes));

		// Unit dialog controls
		Label label = new Label(shlNewUnit, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 424, 546, 5);
		
		Button btnSaveUnit = new Button(shlNewUnit, SWT.NONE);
		btnSaveUnit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnSaveUnit.setText("Save Unit");
		btnSaveUnit.setBounds(10, 435, 413, 26);
		btnSaveUnit.addMouseListener(new MouseAdapter() {		// in-line listener
			@Override
			public void mouseDown(MouseEvent e) {
				if (unitId == -1) {
					// save new Unit with current text boxes
					addNewUnit();
				} else {
					Main.log(Level.INFO, "Save existing unit");
					// save modifications to existing customer
					saveUnit(unit);
				}
			}
		});
		
		Button btnCancel = new Button(shlNewUnit, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(429, 435, 135, 26);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shlNewUnit.close();
			}
		});
		
		shlNewUnit.setTabList(new Control[]{txtOwner, txtMake, txtModel, txtModelName, txtYear,
								txtMileage, txtColor, txtVinNumber, txtNotes, btnSaveUnit, btnCancel});

	}

	protected void addNewUnit() {
		saveUnit(new Unit());	
	}
	
	protected void saveUnit(Unit unit) {
		if (txtOwner.getData() != null && ((Customer) txtOwner.getData()).getCustomerId() != -1) {
			unit.setCustomerId(((Customer) txtOwner.getData()).getCustomerId());
			unit.setOwner(txtOwner.getText());
		} else {
			// if we haven't selected an Owner, complain - a Unit Owner is required
			MessageBox ownerRequirementBox = new MessageBox(shlNewUnit, SWT.ICON_INFORMATION);
			ownerRequirementBox.setText("Notice");
			ownerRequirementBox.setMessage("Please select an Owner");
			ownerRequirementBox.open();
			return;
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
				Main.getLogger().log(Level.FINER, e.getMessage(), e);
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
				unit.setYear(Integer.parseInt(txtYear.getText()));
			} catch (Exception e) {
				Main.getLogger().log(Level.FINER, e.getMessage(), e);
			}
		}
		
		if (txtColor.isModified()) {
			unit.setColor(txtColor.getText());
		}
		
		if (txtNotes.isModified()) {
			unit.setNotes(txtNotes.getText());
		}
		
		DbServices.saveObject(unit);
		result = unit;
		shlNewUnit.close();
	}
}