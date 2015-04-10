package com.example.jarthur.bookexchange;

import java.util.Date;

public class Exchange {

    public Integer exchange_id;
    public Integer loaner_id;
    public Integer borrower_id;

    public String book_id;     // FIXME this is a String in the Book class
    public String book_title;

    public Date create_date;
    public Date start_date;
    public Date end_date;

    public ExchangeStatus status;

    public Exchange() {}

    public String toString() {
        return String.format("%d\n%d\n%d\n%d\n%s\n%s\n%s\n%s\n%s\n",
                exchange_id, loaner_id, borrower_id, book_id, book_title, create_date, start_date, end_date, status);
    }
}
