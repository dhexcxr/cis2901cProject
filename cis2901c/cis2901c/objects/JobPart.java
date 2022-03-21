package cis2901c.objects;

import java.math.BigDecimal;
import java.util.Map;

public class JobPart extends DbObjectSearchable implements DbObjectSavable {
	
	// TODO i'm not sure I need this, I think I can just use JobPartTableItem to save this stuff to the DB
	
	// jobpart Database Table
	
	private long jobPartId = -1;
	private long partId;
	private long jobId;
	private String partNumber;
	private String description;
	private int quantity;
	private BigDecimal soldPrice = BigDecimal.valueOf(0);

	public JobPart() {
		this("", "", 0, BigDecimal.valueOf(0));
	}
	
	public JobPart(String partNumber, String description, int quantity,
			BigDecimal soldPrice) {
		this(-1, 0, 0, partNumber, description, quantity, soldPrice);
	}

	public JobPart(long jobPartId, long partId, long jobId, String partNumber, String description, int quantity,
			BigDecimal soldPrice) {
		super();
		this.jobPartId = jobPartId;
		this.partId = partId;
		this.jobId = jobId;
		this.partNumber = partNumber;
		this.description = description;
		this.quantity = quantity;
		this.soldPrice = soldPrice;
	}

	public long getJobPartId() {
		return jobPartId;
	}

	public void setJobPartId(long jobPartId) {
		this.jobPartId = jobPartId;
	}

	public long getPartId() {
		return partId;
	}

	public void setPartId(long partId) {
		this.partId = partId;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getSoldPrice() {
		return soldPrice;
	}

	public void setSoldPrice(BigDecimal soldPrice) {
		this.soldPrice = soldPrice;
	}

	@Override
	public long getDbPk() {
		return getJobPartId();
	}

	@Override
	public void setDbPk(long dbPk) {
		jobPartId = dbPk;
	}

	@Override
	public String getPkName() {
		return "jobpartid";
	}

	@Override
	public String getTableName() {
		return "jobpart";
	}

	@Override
	public Map<String, String> getDataMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
