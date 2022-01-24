package cis2901c.objects;

public class Unit {
	
	// might not need this
	private long unitId = -1;
	private long customerId;
	private String make;
	private String model;
	private String modelName;
	private int modelYear;
	private int mileage;
	private String color;
	private String vin;
	private String notes;
	
	private String owner;
	
	public Unit() {
	}
	
	public Unit(long unitId, long customerId, String make, String model, String modelName, int modelYear, int mileage, String color,
			String vin, String notes, String owner) {
		super();
		this.unitId = unitId;
		this.customerId = customerId;
		this.make = make;
		this.model = model;
		this.modelName = modelName;
		this.modelYear = modelYear;
		this.mileage = mileage;
		this.color = color;
		this.vin = vin;
		this.notes = notes;
		this.owner = owner;
	}

	public long getUnitId() {
		return unitId;
	}

	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public String getModelName() {
		return modelName;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getModelYear() {
		return modelYear;
	}

	public void setModelYear(int modelYear) {
		this.modelYear = modelYear;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
