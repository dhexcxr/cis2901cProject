// program icon from - https://www.flaticon.com/free-icon/salamander_6483659
// splash screen photo from Creative Commons

package cis2901c.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.graphics.DeviceData;		// Sleak

import cis2901c.listeners.DbServices;

public class Main {
	
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static Display display;

	public static void main(String[] args) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Start GUI
		
//		DeviceData data = new DeviceData();		// Sleak
//	    data.tracking = true;					// Sleak
//		Display display = new Display(data);	// "data" parameter is Sleak
//		Sleak sleak = new Sleak();				// Sleak
//	    sleak.open();							// Sleak
	    
	    
	    display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));
		shell.setText("Service Salamander");
		shell.setImage(new Image(Main.display(), "C:\\Users\\People\\git\\cis2901c\\cis2901c\\resources\\icon.png"));
		@SuppressWarnings("unused")
		Gui gui = new Gui(shell, SWT.NONE);
		shell.pack();
		shell.open();
		
		// Connect to SQL Database
		DbServices.connectToDb();
		if (DbServices.isConnected()) {
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
//		} else {
//			getLogger().log(Level.SEVERE, "DB Connection error dialog box");
//			MessageBox dbConnectionError = new MessageBox(shell, SWT.ERROR);
//			dbConnectionError.setText("DB Error");
//			dbConnectionError.setMessage("Unable to connect to database");
//			dbConnectionError.open();
		}
		
		// disconnect from SQL DB before close
		DbServices.disconnectFromDb();
		display.close();
	}

	public static Logger getLogger() {
		return LOGGER;
	}
	
	public static void log(Level level, String msg) {
		LOGGER.log(level, msg);
	}
	
	public static final Display display() {
		return display;
	}
}
