package cis2901c.objects;

import java.util.Map;

public interface DbObjectSavable {
	
	public long getDbPk();
	
	public String getPkName();
	
	public String getTableName();
	
	public Map<String, String> getDataMap();
}
