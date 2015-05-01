package com.example.jarthur.bookexchange;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import client.Book;
import client.Exchange;

/* Adapts a list of Books to display in the CreateExchangeActivity */
//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
public class BookListAdapter extends ArrayAdapter<Book> {

    Log logger;
    private String TAG = "BookListAdapter";

    public BookListAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    /** Returns the convertView to display the data we want. */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Book book = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_view, parent, false);
        }

        TextView myView = (TextView) convertView;
        myView.setText(book.book_title);


//        myView.setTextColor(Color.BLACK);
//        myView.setTextSize(16);
//        myView.setBackgroundColor(Color.WHITE);


        return myView;
    }

}