package com.example.jarthur.bookexchange;

import android.content.Context;
import android.graphics.Color;
import android.hardware.camera2.params.BlackLevelPattern;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

import client.Client;
import client.Exchange;

/* Adapts a list of Exchanges to display on the Home Screen */
//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
public class ExchangeListAdapter extends ArrayAdapter<Exchange> {

    Log logger;
    private String TAG = "ExchangeListAdapter";

    public ExchangeListAdapter(Context context, ArrayList<Exchange> exchanges) {
        super(context, 0, exchanges);
    }

    /** Returns the convertView to display the data we want. */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Exchange exchange = getItem(position);
        String text = "";

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.exchange_view, parent, false);
        }

        Button myView = (Button) convertView;

        if (exchange.status == Exchange.Status.INITIAL) {
            text += exchange.exchange_type + ": ";
        }

        text += exchange.book_title;

        if (isActionRequired(exchange)) {
            text += " (response requested)";
            myView.setTextColor(Color.parseColor("6CB8AA"));
        }

        myView.setText(text);

        return myView;
    }


    /** Returns true if the current user should take action on this exchange. */
    private boolean isActionRequired(Exchange myExchange) {

        boolean isOwner = (Client.userId == myExchange.loaner_id);


        if (myExchange.status == Exchange.Status.RESPONSE) {
            if (isOwner && (myExchange.exchange_type == Exchange.Type.LOAN)) {
                return true;
            }
            else if (myExchange.exchange_type == Exchange.Type.BORROW) {
                return true;
            }
        }

        return false;
    }

}
