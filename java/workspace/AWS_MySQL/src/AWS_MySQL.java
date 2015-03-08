import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

// modified from code found at
// https://docs.oracle.com/cd/E17952_01/connector-j-en/connector-j-usagenotes-connect-drivermanager.html

// Notice, do not import com.mysql.jdbc.*
// or you will have problems!

public class AWS_MySQL {
	
	static Connection conn = null;
	
    public static void main(String[] args) throws Exception {
    	
    	// load private data
    	Scanner scan = new Scanner(new File("data.private"));
		String address = "";
		address += scan.nextLine();
		
		String user = "";
		user += scan.nextLine();
		
		String pw = "";
		pw += scan.nextLine();
		
		scan.close();
    	
    	try {
    	    conn =
    	       DriverManager.getConnection("jdbc:mysql://" + address + "?" +
    	                                   "user=" + user + "&password=" + pw);

    	    // Do something with the Connection
    	    System.out.println("DONE");
    	} catch (SQLException ex) {
    	    // handle any errors
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	}
    }
}