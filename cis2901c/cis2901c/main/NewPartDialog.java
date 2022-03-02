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
import cis2901c.objects.MyText;
import cis2901c.objects.Part;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class NewPartDialog extends Dialog {

	protected Object result;
	protected Shell shlAddPart;
	private MyText txtPartNumber;
	private MyText txtSupplier;
	private MyText txtDescription;
	private MyText txtOnHand;
	private MyText txtNotes;
	private MyText txtCategory;
	private MyText txtCost;
	private MyText txtRetail;
	
	private int partId = -1;
	private Part part;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public NewPartDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlAddPart.open();
		shlAddPart.layout();
		Display display = getParent().getDisplay();
		while (!shlAddPart.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public Object open(Part part) {
		createContents();

		this.partId = part.getPartId();
		this.txtPartNumber.setText(part.getPartNumber());
		this.txtSupplier.setText(part.getSupplier());
		this.txtDescription.setText(part.getDescription());
		if (part.getOnHand() != -1) {
			this.txtOnHand.setText(Integer.toString(part.getOnHand()));
		}
		this.txtNotes.setText(part.getNotes());
		this.txtCategory.setText(part.getCategory());
		this.txtCost.setText(part.getCost().toString());
		this.txtRetail.setText(part.getRetail().toString());

		this.part = part;
		
		// open new New Part dialog, customize for editing current customer
		shlAddPart.setText("Edit Part Details");
		shlAddPart.open();
		shlAddPart.layout();
		Display display = getParent().getDisplay();
		while (!shlAddPart.isDisposed()) {
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
		shlAddPart = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlAddPart.setSize(592, 408);
		shlAddPart.setText("Add Part");
		
		Gui.setDialogAtCenter(shlAddPart);

		Label lblPartNumber = new Label(shlAddPart, SWT.NONE);
		lblPartNumber.setBounds(10, 10, 86, 20);
		lblPartNumber.setText("Part Number:");
		txtPartNumber = new MyText(shlAddPart, SWT.BORDER);
		txtPartNumber.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtPartNumber.setBounds(10, 36, 270, 26);
		txtPartNumber.addModifyListener(new RequiredTextBoxModifyListener(txtPartNumber));
		txtPartNumber.addFocusListener(new TextBoxFocusListener(txtPartNumber));

		Label lblSupplier = new Label(shlAddPart, SWT.NONE);
		lblSupplier.setBounds(286, 10, 70, 20);
		lblSupplier.setText("Supplier:");
		txtSupplier = new MyText(shlAddPart, SWT.BORDER);
		txtSupplier.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtSupplier.setBounds(286, 36, 142, 26);
		txtSupplier.addModifyListener(new RequiredTextBoxModifyListener(txtSupplier));
		txtSupplier.addFocusListener(new TextBoxFocusListener(txtSupplier));

		Label lblCategory = new Label(shlAddPart, SWT.NONE);
		lblCategory.setBounds(434, 10, 70, 20);
		lblCategory.setText("Category:");
		txtCategory = new MyText(shlAddPart, SWT.BORDER);
		txtCategory.setBounds(434, 36, 130, 26);
		txtCategory.addModifyListener(new InfoTextBoxModifyListener(txtCategory));
		txtCategory.addFocusListener(new TextBoxFocusListener(txtCategory));

		Label lblPartDescription = new Label(shlAddPart, SWT.NONE);
		lblPartDescription.setBounds(10, 68, 108, 20);
		lblPartDescription.setText("Part Description:");
		txtDescription = new MyText(shlAddPart, SWT.BORDER);
		txtDescription.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtDescription.setBounds(10, 94, 554, 26);
		txtDescription.addModifyListener(new RequiredTextBoxModifyListener(txtDescription));
		txtDescription.addFocusListener(new TextBoxFocusListener(txtDescription));

		Label lblQuantityOnHand = new Label(shlAddPart, SWT.NONE);
		lblQuantityOnHand.setBounds(10, 126, 122, 20);
		lblQuantityOnHand.setText("Quantity On Hand:");
		txtOnHand = new MyText(shlAddPart, SWT.BORDER);
		txtOnHand.setBounds(10, 152, 170, 26);
		txtOnHand.addModifyListener(new InfoTextBoxModifyListener(txtOnHand));
		txtOnHand.addFocusListener(new TextBoxFocusListener(txtOnHand));

		Label lblCost = new Label(shlAddPart, SWT.NONE);
		lblCost.setBounds(202, 126, 70, 20);
		lblCost.setText("Cost:");	
		txtCost = new MyText(shlAddPart, SWT.BORDER);
		txtCost.setBounds(202, 152, 170, 26);
		txtCost.addModifyListener(new InfoTextBoxModifyListener(txtCost));
		txtCost.addFocusListener(new TextBoxFocusListener(txtCost));

		Label lblRetailPrice = new Label(shlAddPart, SWT.NONE);
		lblRetailPrice.setBounds(394, 126, 77, 20);
		lblRetailPrice.setText("Retail Price:");
		txtRetail = new MyText(shlAddPart, SWT.BORDER);
		txtRetail.setBounds(394, 152, 170, 26);
		txtRetail.addModifyListener(new InfoTextBoxModifyListener(txtRetail));
		txtRetail.addFocusListener(new TextBoxFocusListener(txtRetail));

		Label lblNotes = new Label(shlAddPart, SWT.NONE);
		lblNotes.setBounds(10, 184, 70, 20);
		lblNotes.setText("Notes:");
		txtNotes = new MyText(shlAddPart, SWT.BORDER | SWT.WRAP);
		txtNotes.setBounds(10, 210, 554, 104);
		txtNotes.addModifyListener(new InfoTextBoxModifyListener(txtNotes));
		txtNotes.addFocusListener(new TextBoxFocusListener(txtNotes));
		
		// New Part dialog controls
		Button btnSavePart = new Button(shlAddPart, SWT.NONE);
		btnSavePart.setText("Save Part");
		btnSavePart.setBounds(10, 328, 413, 26);
		btnSavePart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (partId == -1) {
					addNewPart();
				} else {
					savePart(part);
				}
			}
		});
		
		Button btnCancel = new Button(shlAddPart, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shlAddPart.close();
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setBounds(429, 328, 135, 26);
		
		Label label = new Label(shlAddPart, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 320, 554, 2);
		
		shlAddPart.setTabList(new Control[]{txtPartNumber, txtSupplier, txtCategory, txtDescription,
										txtOnHand, txtCost, txtRetail, txtNotes, btnSavePart, btnCancel});
	}

	protected void addNewPart() {
		savePart(new Part());
	}

	private void savePart(Part part) {
		if (txtPartNumber.isModified()) {
			part.setPartNumber(txtPartNumber.getText());
		} else {
			// dialog box stating part number is required
			MessageBox lastNameRequirementBox = new MessageBox(shlAddPart, SWT.ICON_INFORMATION);
			lastNameRequirementBox.setText("Notice");
			lastNameRequirementBox.setMessage("Please enter a Part Number");
			lastNameRequirementBox.open();
			return;
		}
		
		if (txtSupplier.isModified()) {
			part.setSupplier(txtSupplier.getText());
		} else {
			// dialog box stating supplier is required
			MessageBox lastNameRequirementBox = new MessageBox(shlAddPart, SWT.ICON_INFORMATION);
			lastNameRequirementBox.setText("Notice");
			lastNameRequirementBox.setMessage("Please enter a Supplier");
			lastNameRequirementBox.open();
			return;
		}
		
		if (txtCategory.isModified()) {
			part.setCategory(txtCategory.getText());
		}
		
		if (txtDescription.isModified()) {
			part.setDescription(txtDescription.getText());
		} else {
			// dialog box stating description is required
			MessageBox lastNameRequirementBox = new MessageBox(shlAddPart, SWT.ICON_INFORMATION);
			lastNameRequirementBox.setText("Notice");
			lastNameRequirementBox.setMessage("Please enter a Part Description");
			lastNameRequirementBox.open();
			return;
		}
		
		if (txtNotes.isModified()) {
			part.setNotes(txtNotes.getText());
		}
		
		if (txtRetail.isModified()) {
			try {
				part.setRetail(new BigDecimal(txtRetail.getText()));
			} catch (NumberFormatException e) {
				Main.getLogger().log(Level.FINER, e.getMessage(), e);
			}
		}
		
		if (txtCost.isModified()) {
			try {
				part.setCost(new BigDecimal(txtCost.getText()));
			} catch (NumberFormatException e) {
				Main.getLogger().log(Level.FINER, e.getMessage(), e);
			}
		}
		
		if (txtOnHand.isModified()) {
			try {
				part.setOnHand(Integer.parseInt(txtOnHand.getText()));
			} catch (NumberFormatException e) {
				Main.getLogger().log(Level.FINER, e.getMessage(), e);
			}
		}
		
		DbServices.saveObject(part);
		result = part;
		shlAddPart.close();
	}
}
