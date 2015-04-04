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
	public static ArrayList<Book> parseQuery(String queryURL) throws Exception {
		
		URL url = new URL(queryURL);
		InputStream inputStream;

		inputStream = url.openStream(); // throws an IOException
		
		// Create the DOM builder and document from the input
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(inputStream);
				
		NodeList nodeList = document.getDocumentElement().getChildNodes();
				
		ArrayList<Book> bookList = new ArrayList<>();
		Node work;
		Book book = new Book();
		
		// Find the nodes we want in the xml
		try {
			
			Node search = findNode("search", nodeList);
			Node result = findNode("results", search.getChildNodes());
			NodeList works = result.getChildNodes();
			
			// TODO might want to limit to first n results??
			for (int i = 0; i < works.getLength(); i++) {
				
				work = works.item(i);
				
				if (work instanceof Text) {
					continue;
				}
				
				Node bookNode = findNode("best_book", work.getChildNodes());
				book = parseBookNode(bookNode);
			}
						
			// todo edit book object with info from book.show
				
		}
		catch (NullPointerException e) {
			System.out.println("Node not found!");
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
	
	/* Returns a book created from parsing a best_book node. */
	private static Book parseBookNode(Node bookNode) {
		
		Book book = new Book();
		Node child;
		
		NodeList childNodes = bookNode.getChildNodes();
		
		// Iterate through the children
		for (int i = 0; i < childNodes.getLength(); i++) {
			
			child = childNodes.item(i);
			if (child instanceof Text) {	// ignore whitespace
				continue;
			}

			String name = child.getNodeName();
			
			// For debugging
			System.out.println(i + ": " + name);
			
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
		
		return book;
	}
	

	// TODO add parsing for book.show
}
