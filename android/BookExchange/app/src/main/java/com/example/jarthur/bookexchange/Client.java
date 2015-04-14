package com.example.jarthur.bookexchange;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {

    final static String HOST = "cs.knuth.hmc.edu";
    final static int PORT = 6789;

    // Example!
/*    public static void main(String[] arg) throws Exception {
        try {
            //Log.log("Client", "Client Start", "");
//			Request r = new Request(Request.Type.SEARCH_BOOK);
//			r.params.put("query", "The Hobbit");
//			Request reply = send(r);
//			System.out.println(reply);
            System.out.println(searchBook("Bakemonogatari"));
        } catch (Exception e) {
            System.err.println(e);
        }

    }*/

    private static Request send(Request r) throws Exception {
        Socket s = new Socket(HOST, PORT);

        OutputStream os = s.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);

        InputStream is = s.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);

        //Log.log("Client", "Request Pending", r.toString());

        oos.writeObject(r);
        oos.flush();

        //Log.log("Client", "Resquest Sent", r.toString());
        //Log.log("Client", "Response Pending", "");

        Request response = (Request) ois.readObject();

        //Log.log("Client", "Response Recieved", response.toString());

        oos.close();
        os.close();
        s.close();

        return response;
    }

    public static List<Book> searchBook(String query) throws Exception {
        Request r = new Request(Request.Type.SEARCH_BOOK);
        r.params.put("query", query);
        r = send(r);
        return (ArrayList<Book>) r.reply;
    }

    public static Book getBook(int book_id) throws Exception {
        Request r = new Request(Request.Type.GET_BOOK);
        r.params.put("book_id", book_id);
        r = send(r);
        return (Book) r.reply;
    }

    // FIXME test
    public static List<Exchange> getPublicExchanges() throws Exception {
/*        Exchange e = new Exchange();
        e.book_title = "The Hobbit";
        Exchange ee = new Exchange();
        ee.book_title = "Hop on Pop";
        List<Exchange> list = new ArrayList<Exchange>();

        list.add(e);
        list.add(ee);
        return list;*/

        Request r = new Request(Request.Type.GET_PUBLIC_EXCHANGES);
        r = send(r);
        return (ArrayList<Exchange>) r.reply;
    }

    public static List<Exchange> getPrivateExchanges(int user_id) throws Exception {
        Request r = new Request(Request.Type.GET_PRIVATE_EXCHANGES);
        r.params.put("user_id", user_id);
        r = send(r);
        return (ArrayList<Exchange>) r.reply;
    }

    public static void createExchange(Exchange exchange) throws Exception {
        Request r = new Request(Request.Type.CREATE_EXCHANGE);
        r.params.put("exchange", exchange);
        r = send(r);
        return;
    }

    public static void updateExchangeBorrower(int exchange_id, int borrower_id) throws Exception {
        Request r = new Request(Request.Type.UPDATE_EXCHANGE_BORROWER);
        r.params.put("exchange_id", exchange_id);
        r.params.put("borrower_id", borrower_id);
        r = send(r);
        return;
    }

    public static void updateExchangeLoaner(int exchange_id, int loaner_id) throws Exception {
        Request r = new Request(Request.Type.UPDATE_EXCHANGE_LOANER);
        r.params.put("exchange_id", exchange_id);
        r.params.put("loaner_id", loaner_id);
        r = send(r);
        return;
    }

    public static void updateExchangeStatus(int exchange_id, Exchange.Status status) throws Exception {
        Request r = new Request(Request.Type.UPDATE_EXCHANGE_STATUS);
        r.params.put("exchange_id", exchange_id);
        r.params.put("status", status);
        r = send(r);
        return;
    }

}