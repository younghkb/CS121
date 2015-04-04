import java.io.File;
import java.util.Scanner;

public class QueryBuilder {
	static String key = "";
	static String urlPrefix = "https://www.goodreads.com/search/index.xml?key=";

	public static String makeURL(String query) throws Exception {
		
		// get key from ignored file
		Scanner scan = new Scanner(new File("data.private"));
		key = scan.nextLine();
		scan.close();
		
		return urlPrefix + key + "&q=" + query;
	}
	
}