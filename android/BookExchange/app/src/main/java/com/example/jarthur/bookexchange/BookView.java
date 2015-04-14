package com.example.jarthur.bookexchange;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

// TODO may not need this class

public class BookView extends Button {

    private Book myBook;

    public BookView(Context context, AttributeSet attributes, Book book) {
        super(context, attributes);
        myBook = book;
    }

    public Book getBook() {
        return myBook;
    }

}
