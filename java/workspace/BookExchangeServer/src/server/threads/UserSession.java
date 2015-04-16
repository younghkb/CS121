package server.threads;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;

import logging.Log;
import server.grfetch.GRFetch;
import client.Exchange;
import client.Request;
import database.SQLB;
import database.SQLE;

/**
 * Thread for accepted user connections
 */
public class UserSession extends Thread {
	Socket s;
	
	// TODO hangup gracefully if data is not expected type
	
	public void run() {
		try {
			Log.log("UserSession", "New Connection", "");
			
			InputStream is = s.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);

			OutputStream os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			
			Log.log("UserSession", "Waiting for Request", "");
			
			Request r = null;
			try {
				r = (Request) ois.readObject();
			} catch (ClassNotFoundException e) {
				System.err.println(e);
			}
			
			Log.log("UserSession", "Request Recieved", r.toString());
			
			process(r);
			oos.writeObject(r);
			oos.flush();
			
			Log.log("UserSession", "Response Sent", r.toString());
			
			oos.close();
			os.close();
			s.close();
		} catch (IOException|SQLException|ParseException e) {
			System.err.println(e);
		}
	}
	
	public UserSession(Socket s) {
		this.s = s;
	}
	
	private void process(Request r) throws SQLException, ParseException {
		switch (r.type) {
		case LOGIN:
			r.reply = SQLE.login((String) r.params.get("username"), (String) r.params.get("password"));
			break;
		case CREATE_LOGIN:
			r.reply = SQLE.createLogin((String) r.params.get("username"), (String) r.params.get("password"));
			break;
		case SEARCH_BOOK:
			r.reply = GRFetch.queryBooks((String) r.params.get("query")); // TODO make it pass number of responses, put into database?
			break;
		case GET_BOOK:
			r.reply = SQLE.getBook((Integer) r.params.get("book_id"));
			break;
		case GET_PUBLIC_EXCHANGES:
			r.reply = SQLE.getPublicExchanges();
			break;
		case GET_PRIVATE_EXCHANGES:
			r.reply = SQLE.getBook((Integer) r.params.get("user_id"));
			break;
		case CREATE_EXCHANGE:
			DBWrite.queue(SQLB.createExchange((Exchange) r.params.get("exchange")));
			r.reply = r; // TODO better thing to do?
			break;
		case UPDATE_EXCHANGE_BORROWER:
			DBWrite.queue(SQLB.updateBorrower((Integer) r.params.get("exchange_id"), (Integer) r.params.get("borrower_id")));
			r.reply = r; // TODO better thing to do?
			break;
		case UPDATE_EXCHANGE_LOANER:
			DBWrite.queue(SQLB.updateBorrower((Integer) r.params.get("exchange_id"), (Integer) r.params.get("loaner_id")));
			r.reply = r; // TODO better thing to do?
			break;
		case UPDATE_EXCHANGE_STATUS:
			DBWrite.queue(SQLB.updateExchangeStatus((Integer) r.params.get("exchange_id"), (Exchange.Status) r.params.get("status")));
			r.reply = r; // TODO better thing to do?
			break;
		}
	}
	
} // end of UserThread


/*
 * SEARCH_BOOK,
 * GET_BOOK,
 * GET_PUBLIC_EXCHANGES,
 * GET_PRIVATE_EXCHANGES,
 * CREATE_EXCHANGE,
 * UPDATE_EXCHANGE;
 */