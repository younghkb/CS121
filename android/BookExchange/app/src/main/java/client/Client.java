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

/**
 * This class handles communication between the app and the server.
 */
public class Client {

    final static String HOST = "knuth.cs.hmc.edu";
    final static int PORT = 6789;

    // Id of the current user, set in LoginActivity.
    // All users should have strictly positive id numbers.
    public static int userId = -1;

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

    // Logs the user in. The logic checking to make sure that the username and password are
    // reasonable is contained in LoginActivity.
    public static int login(String username, String password) throws IOException {
        Request r = new Request(Request.Type.LOGIN);
        r.params.put("username", username);
        r.params.put("password", password);
        r = send(r);
        return (Integer) r.reply;
    }

    public static String getUsernameFromUserID(int user_id) throws IOException {
        if (user_id == 0) {     // Slightly hacky fix so the server doesn't have to deal with this.
            return "Unknown";
        }
        Request r = new Request(Request.Type.GET_USERNAME_FROM_USERID);
        r.params.put("user_id", user_id);
        r = send(r);
        return (String) r.reply;
    }

    // If login exists, returns -1. Otherwise, returns new user ID.
    public static int createLogin(String username, String password) throws IOException {
        Request r = new Request(Request.Type.CREATE_LOGIN);
        r.params.put("username", username);
        r.params.put("password", password);
        r = send(r);
        return (Integer) r.reply;
    }

    public static List<Book> searchBook(String query) throws Exception {
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
        Request r = new Request(Request.Type.GET_BOOK);
        r.params.put("book_id", book_id);
        r = send(r);
        return (Book) r.reply;
    }

    public static List<Exchange> getPublicExchanges() throws Exception {
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