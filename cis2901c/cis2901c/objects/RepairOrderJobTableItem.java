package cis2901c.objects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class RepairOrderJobTableItem extends TableItem implements DbObjectSavable {
	
	// TODO I don't think this needs to implement DbObjectSavable
	
	public static final int JOB_NAME_COLUMN = 0;
	public static final int PART_TOTAL_COLUMN = 1;
	public static final int LABOR_TOTAL_COLUMN = 2;
	public static final int JOB_TOTAL_COLUMN = 3;
	
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
	public void setDbPk(long dbPk) {
//		laborId = dbPk;
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
			partTotal = partTotal.add(part.getRetail()).setScale(2, RoundingMode.CEILING);
		}
		this.setText(PART_TOTAL_COLUMN, "$" + partTotal.toString());
		
		laborTotal = BigDecimal.valueOf(0);
		for (JobLabor labor : thisJob.getLabor()) {
			laborTotal = laborTotal.add(labor.getHours().multiply(labor.getLaborRate())).setScale(2, RoundingMode.CEILING);
		}
		this.setText(LABOR_TOTAL_COLUMN, "$" + laborTotal.toString());
		
		this.setText(JOB_TOTAL_COLUMN, "$" + (partTotal.add(laborTotal).toString()));
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
