package xmlparse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import logging.Duration;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import database.entry.Book;


/* DOM parser for the Goodreads XML book data. 
 * Based on http://www.javacodegeeks.com/2013/05/parsing-xml-using-dom-sax-and-stax-parser-in-java.html
 */
public class GRParser {
	
	/* Parses the XML returned from the Goodreads query and returns the list of books found. */
	public static List<Book> parse(String xml) throws Exception {
		
		InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
		
		// Create the DOM builder and document from the input
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(is);
		//Document document = builder.parse(inputStream);
				
		NodeList nodeList = document.getDocumentElement().getChildNodes();
				
		List<Book> bookList = new ArrayList<Book>();
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
				
				//book = getBookDetails(data, book);
				
				bookList.add(book);
				//System.out.println(book.title + " by " + book.author + ", id = " + book.id);
			}
				
		}
		catch (NullPointerException e) {
			System.out.println("Node not found!");
		}
		
		return bookList;
	}
	
	public static void parseDetails(Book book, String xml) throws Exception {	
		InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
		
		// Create the DOM builder and document from the input
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(is);
				
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
				book.pub_year = child.getTextContent();
			}
			else if (name == "work") {
				child = findNode("original_publication_year", child.getChildNodes());
				book.orig_pub_year = child.getTextContent();
			}
		}
		// book -> isbn, isbn13,
		// -> work ->, orig pub year
		//return book;
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
				book.book_id = Integer.parseInt(child.getTextContent());
			}
			else if (name == "title") {
				book.book_title = child.getTextContent();
			}
			else if (name == "author") {
				Node authorName = findNode("name", child.getChildNodes());
				book.author = authorName.getTextContent();
			}
			else if (name == "image_url") {
				book.image_url = child.getTextContent();
			}
			else if (name == "small_image_url") {
				book.small_image_url = child.getTextContent();
			}
		}
		
		return book;
	}
}
