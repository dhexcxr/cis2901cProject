package cis2901c.objects;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Labor extends DbObjectSearchable implements DbObjectSavable {
	
	private long laborId = -1;
	private long JobId;
	private String technician;
	private String description;
	private BigDecimal hours = BigDecimal.valueOf(0);
	private BigDecimal laborRate = BigDecimal.valueOf(0);
	
	private Map<String, String> dataMap = new HashMap<>();
	
	public Labor() {
		this("", "", BigDecimal.valueOf(0), BigDecimal.valueOf(100));
	}

	public Labor(String technician, String description, BigDecimal hours, BigDecimal laborRate) {
		super();
		this.technician = technician;
		this.description = description;
		this.hours = hours;
		this.laborRate = laborRate;
	}

	public long getLaborId() {
		return laborId;
	}

	public void setLaborId(long laborId) {
		this.laborId = laborId;
	}

	public long getJobId() {
		return JobId;
	}

	public void setJobId(long jobId) {
		JobId = jobId;
	}

	public String getTechnician() {
		return technician;
	}

	public void setTechnician(String technician) {
		this.technician = technician;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getHours() {
		return hours;
	}

	public void setHours(BigDecimal hours) {
		this.hours = hours;
	}

	public BigDecimal getLaborRate() {
		return laborRate;
	}

	public void setLaborRate(BigDecimal laborRate) {
		this.laborRate = laborRate;
	}

	@Override
	public long getDbPk() {
		return getLaborId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		laborId = dbPk;
	}

	@Override
	public String getPkName() {
		return "laborid";
	}

	@Override
	public String getTableName() {
		return "joblabor";
	}

	@Override
	public Map<String, String> getDataMap() {
		if (dataMap.isEmpty()) {
			if (laborId != -1) {
				dataMap.put("laborid", Long.toString(laborId));
			}
			dataMap.put("description", description);
			dataMap.put("hours", hours.toString());
			dataMap.put("hourrate", laborRate.toString());
			dataMap.put("technician", technician);
		}
		return dataMap;
	}

}
