package client;

import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Client {

    final static String HOST = "knuth.cs.hmc.edu";
    final static int PORT = 6789;

    // TODO set dynamically
    public static int userId = -1;  // id of the current user, set in LoginActivity

    // TODO don't include this stuff in production version
    /* Set this to true to debug without actually connecting to the server. */
    private static boolean debug = true;

    private static Request send(Request r) throws IOException {

        // We need to set a specific thread policy so we can open sockets on the main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Socket s = new Socket(HOST, PORT);

        OutputStream os = s.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);

        InputStream is = s.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);

        oos.writeObject(r);
        oos.flush();

        Request response = null;
        try {
            response = (Request) ois.readObject();
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

    public static String getUsernameFromUserID(int user_id) throws IOException {
        Request r = new Request(Request.Type.GET_USERNAME_FROM_USERID);
        r.params.put("user_id", user_id);
        r = send(r);
        return (String) r.reply;
    }

    public static int createLogin(String username, String password) throws IOException {
        Request r = new Request(Request.Type.CREATE_LOGIN);
        r.params.put("username", username);
        r.params.put("password", password);
        r = send(r);
        return (Integer) r.reply;
    }

    public static List<Book> searchBook(String query) throws Exception {

        if (debug) {
            Book b = new Book();
            b.book_title = "The Hobbit";
            b.author = "J. R. R. Tolkien";
            b.image_url = "https://d.gr-assets.com/books/1372847500m/5907.jpg";
            List<Book> newList = new ArrayList<>();
            newList.add(b);
            return newList;
        }

        Request r = new Request(Request.Type.SEARCH_BOOK);
        r.params.put("query", query);
        r = send(r);
        return (ArrayList<Book>) r.reply;
    }

    public static void createBook(Book book) throws IOException {
        Request r = new Request(Request.Type.CREATE_BOOK);
        r.params.put("book", book);
        r = send(r);
        return;
    }

    public static Book getBook(int book_id) throws Exception {

        if (debug) {
            Book b = new Book();
            b.book_title = "The Hobbit";
            b.author = "J. R. R. Tolkien";
            b.image_url = "https://d.gr-assets.com/books/1372847500m/5907.jpg";
            return b;
        }

        Request r = new Request(Request.Type.GET_BOOK);
        r.params.put("book_id", book_id);
        r = send(r);
        return (Book) r.reply;
    }

    public static List<Exchange> getPublicExchanges() throws Exception {

        if (debug) {
            Exchange e = new Exchange();
            e.book_title = "The Hobbit";
            Exchange ee = new Exchange();
            ee.book_title = "Hop on Pop";
            List<Exchange> list = new ArrayList<Exchange>();

            e.loaner_id = userId;
            ee.borrower_id = userId;

            Date d = new Date();
            e.create_date = d;

            ee.create_date = d;


            list.add(e);
            list.add(ee);
            return list;
        }

        Request r = new Request(Request.Type.GET_PUBLIC_EXCHANGES);
        r = send(r);
        return (ArrayList<Exchange>) r.reply;
    }

    public static List<Exchange> getPrivateExchanges(int user_id) throws Exception {

       if (debug) {
           List<Exchange> myExchanges = new ArrayList<Exchange>();
           Exchange e = new Exchange();
           e.book_title = "Harry Potter";
           e.exchange_type = Exchange.Type.BORROW;
           e.status = Exchange.Status.ACCEPTED;
           e.loaner_id = userId;

           myExchanges.add(e);

           return myExchanges;
       }

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

        if (debug) {
            return;
        }

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