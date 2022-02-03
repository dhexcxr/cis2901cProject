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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;

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
	private Button btnSavePart;
	private Button btnCancel;
	
	private int partId = -1;
	private Part part;
//	private boolean newPart;

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
		
		if (part.getPartId() != -1)
			this.partId = part.getPartId();
		if (part.getPartNumber() != null && part.getPartNumber().trim().length() > 0)
			this.txtPartNumber.setText(part.getPartNumber());
		if (part.getSupplier() != null && part.getSupplier().trim().length() > 0)
			this.txtSupplier.setText(part.getSupplier());
		if (part.getDescription() != null && part.getDescription().trim().length() > 0)
			this.txtDescription.setText(part.getDescription());
		if (part.getOnHand() != -1)
			this.txtOnHand.setText(Integer.toString(part.getOnHand()));
		if (part.getNotes() != null && part.getNotes().trim().length() > 0)
			this.txtNotes.setText(part.getNotes());
		if (part.getCategory() != null && part.getCategory().trim().length() > 0)
			this.txtCategory.setText(part.getCategory());
		if (part.getCost() != null && !part.getCost().equals(new BigDecimal(0)))
			this.txtCost.setText(part.getCost().toString());
		if (part.getRetail() != null && !part.getRetail().equals(new BigDecimal(0)))
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
		shlAddPart.setSize(592, 300);
		shlAddPart.setText("Add Part");
		
		Gui.setDialogAtCenter(shlAddPart);
		
		txtPartNumber = new MyText(shlAddPart, SWT.BORDER);
		txtPartNumber.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtPartNumber.setText("Part Number...");
		txtPartNumber.setBounds(10, 10, 270, 26);
		txtPartNumber.addModifyListener(new RequiredTextBoxModifyListener(txtPartNumber));
		txtPartNumber.addFocusListener(new TextBoxFocusListener(txtPartNumber));
		
		txtSupplier = new MyText(shlAddPart, SWT.BORDER);
		txtSupplier.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtSupplier.setText("Supplier...");
		txtSupplier.setBounds(286, 10, 142, 26);
		txtSupplier.addModifyListener(new RequiredTextBoxModifyListener(txtSupplier));
		txtSupplier.addFocusListener(new TextBoxFocusListener(txtSupplier));
		
		txtDescription = new MyText(shlAddPart, SWT.BORDER);
		txtDescription.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtDescription.setText("Description...");
		txtDescription.setBounds(10, 42, 554, 26);
		txtDescription.addModifyListener(new RequiredTextBoxModifyListener(txtDescription));
		txtDescription.addFocusListener(new TextBoxFocusListener(txtDescription));
		
		txtOnHand = new MyText(shlAddPart, SWT.BORDER);
		txtOnHand.setText("On Hand...");
		txtOnHand.setBounds(10, 74, 170, 26);
		txtOnHand.addModifyListener(new InfoTextBoxModifyListener(txtOnHand));
		txtOnHand.addFocusListener(new TextBoxFocusListener(txtOnHand));
		
		txtNotes = new MyText(shlAddPart, SWT.BORDER);
		txtNotes.setText("Notes...");
		txtNotes.setBounds(10, 106, 554, 104);
		txtNotes.addModifyListener(new InfoTextBoxModifyListener(txtNotes));
		txtNotes.addFocusListener(new TextBoxFocusListener(txtNotes));
		
		txtCategory = new MyText(shlAddPart, SWT.BORDER);
		txtCategory.setText("Category...");
		txtCategory.setBounds(434, 10, 130, 26);
		txtCategory.addModifyListener(new InfoTextBoxModifyListener(txtCategory));
		txtCategory.addFocusListener(new TextBoxFocusListener(txtCategory));
		
		txtCost = new MyText(shlAddPart, SWT.BORDER);
		txtCost.setText("Cost...");
		txtCost.setBounds(202, 74, 170, 26);
		txtCost.addModifyListener(new InfoTextBoxModifyListener(txtCost));
		txtCost.addFocusListener(new TextBoxFocusListener(txtCost));
		
		txtRetail = new MyText(shlAddPart, SWT.BORDER);
		txtRetail.setText("Retail...");
		txtRetail.setBounds(394, 74, 170, 26);
		txtRetail.addModifyListener(new InfoTextBoxModifyListener(txtRetail));
		txtRetail.addFocusListener(new TextBoxFocusListener(txtRetail));
		
		// New Part dialog controls
		btnSavePart = new Button(shlAddPart, SWT.NONE);
		btnSavePart.setText("Save Part");
		btnSavePart.setBounds(10, 216, 413, 26);
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
		
		btnCancel = new Button(shlAddPart, SWT.NONE);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shlAddPart.close();
			}
		});
		btnCancel.setText("Cancel");
		btnCancel.setBounds(429, 216, 135, 26);
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
				// TODO: handle exception
				System.out.println(e);
				e.printStackTrace();
			}
		}
		
		if (txtCost.isModified()) {
			try {
				part.setCost(new BigDecimal(txtCost.getText()));
			} catch (NumberFormatException e) {
				// TODO: handle exception
				System.out.println(e);
				e.printStackTrace();
			}
		}
		
		if (txtOnHand.isModified()) {
			part.setOnHand(Integer.parseInt(txtOnHand.getText()));
		}
		
		DbServices.saveObject(part);
		result = part;
		shlAddPart.close();
	}
}
