package com.example.jarthur.bookexchange;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.logging.Logger;

/* Adapts a list of Exchanges to display on the Home Screen */
//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
public class ExchangeListAdapter extends ArrayAdapter<Exchange> {

    Log logger;
    private String TAG = "ExchangeListAdapter";

    public ExchangeListAdapter(Context context, ArrayList<Exchange> exchanges) {
        super(context, 0, exchanges);
        logger.i(TAG, "Creating ExchangeListAdapter");
    }

    /** Returns the convertView to display the data we want (I think) ????. */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        logger.i(TAG, "GetView called");
        Exchange exchange = getItem(position);
        logger.i(TAG, exchange.toString());



        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.exchange_view, parent, false);
            logger.i(TAG, "Inflating ExchangeView" + exchange.book_title);

        }

        Button myView = (Button) convertView;

        myView.setText(exchange.book_title);

        return myView;
    }

}
