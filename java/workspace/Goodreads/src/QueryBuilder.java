import java.io.File;
import java.util.Scanner;

public class QueryBuilder {
	static String key = "";

	public static void main(String[] args) throws Exception {
		
		// get key from ignored file
		Scanner scan = new Scanner(new File("data.private"));
		key += scan.nextLine();
		scan.close();
		
		System.out.println(makeURLfromISBN("123"));
	}

	public static String makeURLfromISBN(String isbn) throws Exception {
		
		// get key from ignored file
		Scanner scan = new Scanner(new File("data.private"));
		key += scan.nextLine();
		scan.close();
		return "https://www.goodreads.com/search/index.xml?key=" + key + "&q=" + isbn;
	}
}