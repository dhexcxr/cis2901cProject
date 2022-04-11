package cis2901c.main;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

public class Settings {
	
//	private String result = "";
	InputStream inputStream;
	
	private String propFileName = "resources/settings.properties";
	private String database = "";
	private boolean skipCancelConfirm = false;
	private boolean skipCloseConfirm = false;
	

	public Settings() {
		try {
			Properties prop = new Properties();
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("settings file '" + propFileName + "' not found");
			}
			
			database = prop.getProperty("database");
			skipCancelConfirm = Boolean.parseBoolean(prop.getProperty("skipCancelConfirm"));
			skipCloseConfirm = Boolean.parseBoolean(prop.getProperty("skipCloseConfirm"));		
		} catch (Exception e) {
			Main.getLogger().log(Level.SEVERE, "Exception {0}", e.toString());
		}
	}
	
	public String getDatabase() {
		return database;
	}
	
	public boolean skipCancelConfirm() {
		return skipCancelConfirm;
	}
	
	public boolean skipCloseConfirm() {
		return skipCloseConfirm;
	}

}
