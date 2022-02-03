package cis2901c.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.graphics.DeviceData;		// Sleak

import cis2901c.listeners.DbServices;

public class Main {
	private static Shell shell = null;

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
	    
	    
	    Display display = new Display();
		shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));
		shell.setText("Service Salamander");
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
		} else {
			System.out.println("DB Connection error dialog box");
			MessageBox dbConnectionError = new MessageBox(shell, SWT.ERROR);
			dbConnectionError.setText("DB Error");
			dbConnectionError.setMessage("Unable to connect to database");
			dbConnectionError.open();
		}
		
		// disconnect from SQL DB before close
		DbServices.disconnectFromDb();
		display.close();
	}
}
