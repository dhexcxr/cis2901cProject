package cis2901c.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Job extends DbObjectSearchable implements DbObjectSavable {
	
	private	String jobName;
	private String complaints;
	private String resolution;
	private String reccomendations;
	
	List<Part> parts;
	List<Labor> labor;

	public Job() {
		// TODO Auto-generated constructor stub
	}

	public Job(String jobName, String complaints, String resolution, String reccomendations, List<Part> parts,
			List<Labor> labor) {
		super();
		this.jobName = jobName;
		this.complaints = complaints;
		this.resolution = resolution;
		this.reccomendations = reccomendations;
		this.parts = parts;
		this.labor = labor;
	}

	public String getJobName() {
		return jobName == null ? "" : jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getComplaints() {
		return complaints == null ? "" : complaints;
	}

	public void setComplaints(String complaints) {
		this.complaints = complaints;
	}

	public String getResolution() {
		return resolution == null ? "" : resolution;
	}
	
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getReccomendations() {
		return reccomendations == null ? "" : reccomendations;
	}

	public void setReccomendations(String reccomendations) {
		this.reccomendations = reccomendations;
	}

	public List<Part> getParts() {
		if (parts == null) {
			parts = new ArrayList<>();
		}
		return parts;
	}

	public void setParts(List<Part> parts) {
		this.parts = parts;
	}
	
	public void addPart(Part part) {
		this.parts.add(part);
	}
	
	public void deletePart(Part part) {
		this.parts.remove(part);
	}

	public List<Labor> getLabor() {
		if (labor == null) {
			labor = new ArrayList<>();
		}
		return labor;
	}

	public void setLabor(List<Labor> labor) {
		this.labor = labor;
	}
	
	public void addLabor(Labor labor) {
		this.labor.add(labor);
	}
	
	public void deleteLabor(Labor labor) {
		this.labor.remove(labor);
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

}
