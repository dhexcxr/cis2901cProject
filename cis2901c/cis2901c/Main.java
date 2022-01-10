package cis2901c;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import java.sql.Statement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {
	private static Gui gui = null;
	private static Connection mainDbConnection;

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		// TODO set up connection to MariaDB
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		mainDbConnection = DriverManager.getConnection("jdbc:mysql://TestUser:test@localhost:3306/cis2901c");
		
//		Statement statement = mainDbConnection.createStatement();
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));
		shell.setText("Service Salamander");
		gui = new Gui(shell, SWT.NONE);
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
	public static Gui getGui() {
		return gui;
	}
	
	public static Connection getDbConnection() {
		return mainDbConnection;		
	}
	
//	public static Statement getDbStatement() {
//		return mainDbConnection.createStatement();
//	}

}
