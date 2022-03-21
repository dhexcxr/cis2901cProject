package cis2901c.objects;

import org.eclipse.swt.widgets.Table;

public class JobPartTableItem extends InvoicePartTableItem {
	
	private JobPart jobPart;
	
	public JobPartTableItem(Table parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	public JobPartTableItem(MyTable parent, int style, int itemCount) {
		super(parent, style, itemCount);
		// TODO Auto-generated constructor stub
	}
	
	public void setData(JobPart jobPart) {
		this.jobPart = jobPart;
		super.setData(jobPart);
		this.setText(PART_NUMBER_COLUMN, jobPart.getPartNumber());
		this.setText(DESCRIPTION_COLUMN, jobPart.getDescription());
		this.setText(QUANTITY_COLUMN, Integer.toString(jobPart.getQuantity()));
		this.setText(SOLDPRICE_COLUMN, jobPart.getSoldPrice().toString());
	}
	
	@Override
	public long getDbPk() {
		// when this is called in DbServices we'll always be making a new TableItem
		return -1;					// ie you can't edit a TableItem
	}
	
	@Override
	public String getPkName() {
		// TODO Auto-generated method stub
		return "jobpartid";
	}
	
	@Override
	public String getFkName() {
		// TODO Auto-generated method stub
		return "jobid";
	}

}
