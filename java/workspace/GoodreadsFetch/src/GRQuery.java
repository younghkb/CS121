import java.io.File;
import java.util.Scanner;


public class GRQuery {
	static String key = "";

	public static void main(String[] args) throws Exception {
		// get key from ignored file
		Scanner scan = new Scanner(new File("data.private"));
		key += scan.nextLine();
		scan.close();
		
		System.out.println(mkURLString("123"));
	}

	public static String mkURLString(String isbn) {
		return "https://www.goodreads.com/search/index.xml?key=" + key + "&q=" + isbn;
	}
}