package com.example.jarthur.bookexchange;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

import client.Exchange;

/* Adapts a list of Exchanges to display on the Home Screen */
//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
public class ExchangeListAdapter extends ArrayAdapter<Exchange> {

    Log logger;
    private String TAG = "ExchangeListAdapter";

    public ExchangeListAdapter(Context context, ArrayList<Exchange> exchanges) {
        super(context, 0, exchanges);
    }

    /** Returns the convertView to display the data we want (I think) ????. */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Exchange exchange = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.exchange_view, parent, false);
        }

        Button myView = (Button) convertView;
        myView.setText(exchange.book_title);

        return myView;
    }

}
