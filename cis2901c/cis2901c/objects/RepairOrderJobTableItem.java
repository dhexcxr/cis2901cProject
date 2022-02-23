package cis2901c.objects;

import java.math.BigDecimal;
import java.util.Map;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class RepairOrderJobTableItem extends TableItem implements DbObjectSavable {
	
	private static final int JOB_NAME_COLUMN = 0;
	private static final int PART_TOTAL_COLUMN = 1;
	private static final int LABOR_TOTAL_COLUMN = 2;
	private static final int JOB_TOTAL_COLUMN = 3;
	
	private BigDecimal partTotal;
	private BigDecimal laborTotal;

	public RepairOrderJobTableItem(Table parent, int style) {
		super(parent, style);
		
		// TODO Auto-generated constructor stub
	}

	public BigDecimal getPartTotal() {
		return partTotal == null ? BigDecimal.valueOf(0) : partTotal;
	}

	public void setPartTotal(BigDecimal partTotal) {
		this.partTotal = partTotal;
	}

	public BigDecimal getLaborTotal() {
		return laborTotal == null ? BigDecimal.valueOf(0) : laborTotal;
	}

	public void setLaborTotal(BigDecimal laborTotal) {
		this.laborTotal = laborTotal;
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
	
	@Override
	public void setData(Object data) {
		super.setData(data);
		// TODO set TableItem columns from data attributes
		Job thisJob = (Job) data;
		this.setText(JOB_NAME_COLUMN, thisJob.getJobName());
		
		partTotal = BigDecimal.valueOf(0);
		for (Part part : thisJob.getParts()) {
			partTotal = partTotal.add(part.getRetail());
		}
		this.setText(PART_TOTAL_COLUMN, "$" + partTotal.toString());
		
		laborTotal = BigDecimal.valueOf(0);
		for (Part part : thisJob.getParts()) {
			laborTotal = partTotal.add(part.getRetail());
		}
		this.setText(LABOR_TOTAL_COLUMN, "$" + laborTotal.toString());
		
		this.setText(JOB_TOTAL_COLUMN, "$" + (partTotal.add(laborTotal).toString()));
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
