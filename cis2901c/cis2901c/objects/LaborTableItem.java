package cis2901c.objects;

import java.math.BigDecimal;
import java.util.Map;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class LaborTableItem extends TableItem implements DbObjectSavable {
	
//	private static final int TECHNICIAN_COLUMN = 0;
//	private static final int DESCRIPTION_COLUMN = 1;
	private static final int HOURS_COLUMN = 2;
	private static final int RATE_COLUMN = 3;
//	private static final int TOTAL_COLUMN = 4;

	public LaborTableItem(Table parent, int style) {
		super(parent, style);
		this.setText(HOURS_COLUMN, "0");		// TODO choose a good default
		// TODO set default labor rate in settings
		this.setText(RATE_COLUMN, "100");
	}

	public LaborTableItem(Table parent, int style, int index) {
		super(parent, style, index);
		// TODO Auto-generated constructor stub
	}

	@Override
	public long getDbPk() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getPkName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getDataMap() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setData(String technician, String description, BigDecimal hours, BigDecimal rate) {
		if (this.getData() == null) {
			super.setData(new Labor(technician, description, hours, rate));
		} else {
			Labor tableItemLabor = (Labor) this.getData();
			tableItemLabor.setTechnician(technician);
			tableItemLabor.setDescription(description);
			tableItemLabor.setHours(hours);
			tableItemLabor.setLaborRate(rate);
		}

	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
