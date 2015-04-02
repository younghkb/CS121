import java.io.InputStream;
import java.net.URL;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;


/* DOM parser for the Goodreads XML book data. 
 * Based on http://www.javacodegeeks.com/2013/05/parsing-xml-using-dom-sax-and-stax-parser-in-java.html
 */
public class BookParser {
	
	/* Parses the XML returned from the Goodreads query and returns the list of books found. */
	public static List<Book> parseQuery(String queryURL) throws Exception {
		
		// "https://www.goodreads.com/search.xml?key=YOUR_KEY&q=Ender%27s+Game"
		//url = new URL("https://www.goodreads.com/search/index.xml?key=" + key + "&q=Ender%27s+Game");
		URL url = new URL(queryURL);
		InputStream inputStream;

		inputStream = url.openStream(); // throws an IOException
		
		// Create the DOM builder and document from the input
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(inputStream);
				
		// When searching by ISBN, there should only be one node, but we'll put the code in place
		// to parse multiple results.
		NodeList nodeList = document.getDocumentElement().getChildNodes();
		
		List<Book> bookList = new ArrayList<>();
		Node node;
		Node child;
		Book book;
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			node = nodeList.item(i);	
			
			// If the node is a best_book node.
			if (node instanceof Element && (((Element) node).getTagName() == "best_book")) {
				
				book = new Book();
								
				NodeList childNodes = node.getChildNodes();
				
				// If we're really sure that the same child nodes will always appear in the same order, 
				// we can iterate through the children without using if/else if statements.
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
				bookList.add(book);
			}
		}
		
		// For logging and debugging purposes		
		for (Book b : bookList) {
			System.out.println(b.title + " by " + b.author + ", id = " + b.id);
		}
		return bookList;
	}

}
