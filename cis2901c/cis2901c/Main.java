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
//	private static Gui gui = null;
	private static Shell shell = null;
//	private static Connection mainDbConnection;

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		// TODO set up connection to MariaDB
//		Class.forName("com.mysql.cj.jdbc.Driver");
		
//		mainDbConnection = DriverManager.getConnection("jdbc:mysql://TestUser:test@localhost:3306/cis2901c");
		
//		Statement statement = mainDbConnection.createStatement();
		
		Display display = new Display();
//		Shell shell = new Shell(display);
		shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));
		shell.setText("Service Salamander");
		Gui gui = new Gui(shell, SWT.NONE);
//		gui = new Gui(shell, SWT.NONE);
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
	protected static Shell getShell() {
//		gui = null;
		if (shell.equals(null)) {
			System.out.println("Error loading Gui: shell cannot be null");
			System.out.println(new Throwable().getStackTrace());
			System.exit(-1);
		}
		return shell;
	}
	
//	protected static Gui getGui() {
//		gui = null;
//		if (gui.equals(null)) {
//			System.out.println("Error loading Gui: Gui cannot be null");
//			System.out.println(new Throwable().getStackTrace());
//			System.exit(-1);
//		}
//		return gui;
//	}
		
	// TODO research if creating a new connection everytime we need one hurts performance
	// see if I want to setAutoCommit to false before returning Connection, every call would have to connection.commit, or .rollback if an exception is caught
	protected static Connection getDbConnection() {
//		return mainDbConnection;
		String url = "jdbc:mysql://localhost:3306/cis2901c";
		String user = "TestUser";
		String pass = "test";
		Connection dbConnection = null;
		try {
			dbConnection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			System.out.println("Error connecting to database");
			e.printStackTrace();
		}
		return dbConnection;
	}
	
//	public static Statement getDbStatement() {
//		return mainDbConnection.createStatement();
//	}

}
