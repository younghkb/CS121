package com.example.jarthur.bookexchange;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class ExchangeView extends Button {

    private Exchange myExchange;

    public ExchangeView(Context context) {
        super(context);
    }

    // This constructor is the one used when automatically inflating the view.
    public ExchangeView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public ExchangeView(Context context, AttributeSet attributes, Exchange exchange) {
        super(context, attributes);
        myExchange = exchange;
    }

    public Exchange getExchange() {
        return myExchange;
    }

    public String getBookTitle() {
        return myExchange.book_title;
    }
}
