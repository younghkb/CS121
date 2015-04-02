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
		
		System.out.println(nodeList.getLength() + " child nodes");
		
		List<Book> bookList = new ArrayList<>();
		NodeList childNodes = null;
		Node child;
		Book book;
		
		// Find the nodes we want in the xml
		try {
			
			Node search = findNode("search", nodeList);
			Node result = findNode("results", search.getChildNodes());
			Node work = findNode("work", result.getChildNodes());
			Node bookNode = findNode("best_book", work.getChildNodes());
				
			childNodes = bookNode.getChildNodes();
		}
		catch (NullPointerException e) {
			System.out.println("Node not found!");
		}
		
		// Create a new book object
		book = new Book();
		
		// Iterate through the children
		for (int j = 0; j < childNodes.getLength(); j++) {
			
			child = childNodes.item(j);
			if (child instanceof Text) {	// ignore whitespace
				continue;
			}

			String name = child.getNodeName();
			
			System.out.println(j + ": " + name);
			
			if (name == "id") {
				book.id = child.getTextContent();
				System.out.println(child.getTextContent());

			}
			else if (name == "title") {
				book.title = child.getTextContent();
			}
			else if (name == "author") {
				Node authorName = findNode("name", child.getChildNodes());
				book.author = authorName.getTextContent();
			}
			else if (name == "image_url") {
				book.imageUrl = child.getTextContent();
			}
			else if (name == "small_image_url") {
				book.smallImageUrl = child.getTextContent();
			}
		}
		
		bookList.add(book);

		
		// For logging and debugging purposes		
		for (Book b : bookList) {
			System.out.println(b.title + " by " + b.author + ", id = " + b.id);
		}
		return bookList;
	}
	
	private static Node findNode(String nodeName, NodeList nodeList) {
		
		Node node;
		for (int i = 0; i < nodeList.getLength(); i++) {
			node = nodeList.item(i);
			
				// Element as opposed to Text
			if ((node instanceof Element) && node.getNodeName() == nodeName) {
				return node;
			}
		}
		return null;
	}

	// TODO add parsing for book.show
}
