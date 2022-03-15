package cis2901c.objects;

import java.math.BigDecimal;
import java.util.Map;

public class Labor extends DbObjectSearchable implements DbObjectSavable {
	
	private String technician;
	private String description;
	private BigDecimal hours = BigDecimal.valueOf(0);
	private BigDecimal laborRate = BigDecimal.valueOf(0);
	
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

}
