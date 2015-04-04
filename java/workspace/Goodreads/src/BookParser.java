import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


/* DOM parser for the Goodreads XML book data. 
 * Based on http://www.javacodegeeks.com/2013/05/parsing-xml-using-dom-sax-and-stax-parser-in-java.html
 */
public class BookParser {
	
	/* Parses the XML returned from the Goodreads query and returns the list of books found. */
	public static ArrayList<Book> parseQuery(URL queryURL) throws Exception {
		
		InputStream inputStream;

		inputStream = queryURL.openStream(); // throws an IOException
		
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
				
				book = getBookDetails(book);
				
				bookList.add(book);
				System.out.println(book.title + " by " + book.author + ", id = " + book.id);
			}
				
		}
		catch (NullPointerException e) {
			System.out.println("Node not found!");
		}

		return bookList;
	}
	
	/* Helper method. */
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
			
			if (name == "id") {
				book.id = child.getTextContent();
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
	

	private static Book getBookDetails(Book book) throws Exception {
		
		URL url = QueryBuilder.makeBookDetailsURL(book.id);
		
		InputStream inputStream;	// different input stream than the one for the search query 
		inputStream = url.openStream(); // throws an IOException
		
		// Create the DOM builder and document from the input
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(inputStream);
				
		NodeList nodeList = document.getDocumentElement().getChildNodes();
		Node child;
		
		Node bookNode = findNode("book", nodeList);
		
		NodeList childNodes = bookNode.getChildNodes();
		
		// Iterate through the children to find the information we want
		for (int i = 0; i < childNodes.getLength(); i++) {
			
			child = childNodes.item(i);
			if (child instanceof Text) {	// ignore whitespace
				continue;
			}

			String name = child.getNodeName();
			
			if (name == "isbn") {		// Can also get isbn13 if we want
				book.isbn = child.getTextContent();
			}
			else if (name == "publication_year") {
				book.pubYear = child.getTextContent();
			}
			else if (name == "work") {
				child = findNode("original_publication_year", child.getChildNodes());
				book.originalPubYear = child.getTextContent();
			}
		}
		
		
		// book -> isbn, isbn13,
		// -> work ->, orig pub year
		return book;
	}
}
