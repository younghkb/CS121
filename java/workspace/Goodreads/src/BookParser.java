import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/* DOM parser for the Goodreads XML book data. 
 * Based on http://www.javacodegeeks.com/2013/05/parsing-xml-using-dom-sax-and-stax-parser-in-java.html
 */
public class BookParser {
	
	public static void parseQuery(String queryURL) throws Exception {
		
		// "https://www.goodreads.com/search.xml?key=YOUR_KEY&q=Ender%27s+Game"
		//url = new URL("https://www.goodreads.com/search/index.xml?key=" + key + "&q=Ender%27s+Game");
		URL url = new URL(queryURL);
		InputStream inputStream;

		// get key from ignored file
		Scanner scan = new Scanner(new File("data.private"));
		String key = "";
		key += scan.nextLine();
		scan.close();

		inputStream = url.openStream(); // throws an IOException
		
		// Create the DOM builder
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		Document document = builder.parse(inputStream);
		
		List<Book> bookList = new ArrayList<>();
		
		// When searching by ISBN, there should only be one node, but we'll put the code in place
		// to parse multiple results.
		NodeList nodeList = document.getDocumentElement().getChildNodes();
		Node node;
		Node child;
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			node = nodeList.item(i);	
			
			// If the node is a best_book node.
			if (node instanceof Element && (((Element) node).getTagName() == "best_book")) {
				
				Book book = new Book();
								
				NodeList childNodes = node.getChildNodes();
				
				for (int j = 0; j < childNodes.getLength(); j++) {
					child = childNodes.item(j);
					String name = child.getNodeName();
					
					if (name == "id") {
						book.id = child.getNodeValue();
					}
					else if (name == "title") {
						book.title = child.getNodeValue();
					}
					else if (name == "author") {
						book.author = child.getLastChild().getNodeValue();
					}
					else if (name == "image_url") {
						book.imageUrl = child.getNodeValue();
					}
					else if (name == "small_image_url") {
						book.smallImageUrl = child.getNodeValue();
					}
				}
			}
		}
	}

}
