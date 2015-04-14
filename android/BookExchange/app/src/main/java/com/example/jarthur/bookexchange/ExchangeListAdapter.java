package com.example.jarthur.bookexchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
public class ExchangeListAdapter extends ArrayAdapter<Exchange> {

    public ExchangeListAdapter(Context context, List<Exchange> exchanges) {
        super(context, 0, exchanges);
    }

    /** Returns the convertView to display the data we want (I think) ????. */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Exchange exchange = getItem(position);
        ExchangeView myView = (ExchangeView) convertView;

        // Check if an existing view is being reused, otherwise inflate the view
        if (myView == null) {
            myView = (ExchangeView) LayoutInflater.from(getContext()).inflate(R.layout.exchange_view, parent, false);
        }

        myView.setText(exchange.book_title);

        return (View) myView;
    }

}
