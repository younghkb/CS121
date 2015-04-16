package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import logging.Log;

public abstract class Client {
	
	final static String HOST = "localhost";
	//final static String HOST = "knuth.cs.hmc.edu";
	final static int PORT = 6789;
	
	public static void main(String[] arg) {
		try {
			Log.log("Client", "Client Start", "");
//			Request r = new Request(Request.Type.SEARCH_BOOK);
//			r.params.put("query", "The Hobbit");
//			Request reply = send(r);
//			System.out.println(reply);
			//System.out.println(getBook(206962));
			
			//System.out.println(getPublicExchanges());
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	// TODO add book (selectBook) which takes ISBN and adds to DB
	// TODO searchBook with size
	// TODO rename reply as response
	
	private static Request send(Request r) throws IOException {
		Socket s = new Socket(HOST, PORT);
		
		OutputStream os = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		
		InputStream is = s.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		
		Log.log("Client", "Request Pending", r.toString());
		
		oos.writeObject(r);
		oos.flush();
		
		Log.log("Client", "Resquest Sent", r.toString());
		Log.log("Client", "Response Pending", "");
		
		Request response = null;
		try {
			response = (Request) ois.readObject();
			Log.log("Client", "Response Recieved", response.toString());
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		}
		
		oos.close();
		os.close();
		s.close();
		
		return response;
	}
	
	public static int login(String username, String password) throws IOException {
		Request r = new Request(Request.Type.LOGIN);
		r.params.put("username", username);
		r.params.put("password", password);
		r = send(r);
		return (Integer) r.reply;
	}
	
	public static int createLogin(String username, String password) throws IOException {
		Request r = new Request(Request.Type.CREATE_LOGIN);
		r.params.put("username", username);
		r.params.put("password", password);
		r = send(r);
		return (Integer) r.reply;
	}
	
	public static List<Book> searchBook(String query) throws IOException {
		Request r = new Request(Request.Type.SEARCH_BOOK);
		r.params.put("query", query);
		r = send(r);
		return (List<Book>) r.reply;
	}

	public static Book getBook(int book_id) throws IOException {
		Request r = new Request(Request.Type.GET_BOOK);
		r.params.put("book_id", book_id);
		r = send(r);
		return (Book) r.reply;
	}

	public static List<Exchange> getPublicExchanges() throws IOException {
		Request r = new Request(Request.Type.GET_PUBLIC_EXCHANGES);
		r = send(r);
		return (List<Exchange>) r.reply;
	}
	
	public static List<Exchange> getPrivateExchanges(int user_id) throws IOException {
		Request r = new Request(Request.Type.GET_PRIVATE_EXCHANGES);
		r.params.put("user_id", user_id);
		r = send(r);
		return (List<Exchange>) r.reply;
	}

	public static void createExchange(Exchange exchange) throws IOException {
		Request r = new Request(Request.Type.CREATE_EXCHANGE);
		r.params.put("exchange", exchange);
		r = send(r);
		return;
	}

	public static void updateExchangeBorrower(int exchange_id, int borrower_id) throws IOException {
		Request r = new Request(Request.Type.UPDATE_EXCHANGE_BORROWER);
		r.params.put("exchange_id", exchange_id);
		r.params.put("borrower_id", borrower_id);
		r = send(r);
		return;
	}
	
	public static void updateExchangeLoaner(int exchange_id, int loaner_id) throws IOException {
		Request r = new Request(Request.Type.UPDATE_EXCHANGE_LOANER);
		r.params.put("exchange_id", exchange_id);
		r.params.put("loaner_id", loaner_id);
		r = send(r);
		return;
	}
	
	public static void updateExchangeStatus(int exchange_id, Exchange.Status status) throws IOException {
		Request r = new Request(Request.Type.UPDATE_EXCHANGE_STATUS);
		r.params.put("exchange_id", exchange_id);
		r.params.put("status", status);
		r = send(r);
		return;
	}

}

/*
 * SEARCH_BOOK,
 * GET_BOOK,
 * GET_PUBLIC_EXCHANGES,
 * GET_PRIVATE_EXCHANGES,
 * CREATE_EXCHANGE,
 * UPDATE_EXCHANGE;
 */
