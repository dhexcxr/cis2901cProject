package cis2901c.objects;

import java.math.BigDecimal;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class JobLaborTableItem extends TableItem {
	
	private static final int HOURS_COLUMN = 2;
	private static final int RATE_COLUMN = 3;

	public JobLaborTableItem(Table parent, int style) {
		super(parent, style);
		this.setText(HOURS_COLUMN, "0");
		this.setText(RATE_COLUMN, "100");
	}

	public JobLaborTableItem(Table parent, int style, int index) {
		super(parent, style, index);
	}
	
	public void setData(String technician, String description, BigDecimal hours, BigDecimal rate) {
		if (this.getData() == null) {
			super.setData(new JobLabor(technician, description, hours, rate));
		} else {
			JobLabor tableItemLabor = (JobLabor) this.getData();
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
