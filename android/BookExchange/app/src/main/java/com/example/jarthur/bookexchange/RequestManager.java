package com.example.jarthur.bookexchange;


// handles requests to and from the server

import java.util.List;

// TODO implement methods

// Ask Andrew—should this be static or a singleton?
public class RequestManager {


     /* Returns a list of books from Goodreads. */
    public List<Book> findBooks(String query) {

        return null;

    }

    /* Returns a specific book based on the Goodreads id (primary key). */
    public Book findBook(String bookId) {

        return null;

    }

    /* Returns the list of the exchanges a user is participating or has participated in. */
    // TODO do we want this to be sorted by date? We can do this with sql.
    public List<Exchange> getUserExchanges(Integer userId) {

        return null;

    }


    /* Add a new exchange in the database. Returns true if successful. */
    public boolean createExchange(Exchange newExchange) {

        return false;

    }

    /* Update exchange status. */
    public boolean updateExchange() {

        return false;

    }

}