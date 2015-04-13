package com.example.jarthur.bookexchange;

import android.app.Application;

import java.util.List;

/**
 * This class holds the global information and state of the application. None of the information
 * is saved across sessions.
 */
public class BookExchange extends Application {

    private List<Book> bookList;

    private Book currentBook;

    private List<Exchange> exchangeList;

    private Exchange currentExchange;

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public Book getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(Book currentBook) {
        this.currentBook = currentBook;
    }

    public List<Exchange> getExchangeList() {
        return exchangeList;
    }

    public void setExchangeList(List<Exchange> exchangeList) {
        this.exchangeList = exchangeList;
    }

    public Exchange getCurrentExchange() {
        return currentExchange;
    }

    public void setCurrentExchange(Exchange currentExchange) {
        this.currentExchange = currentExchange;
    }
}
