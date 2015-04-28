package com.example.jarthur.bookexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

import client.Book;
import client.Client;
import client.Exchange;


public class HomeScreen extends ActionBarActivity {

    private Log logger;
    private String TAG = "HomeScreen";

    private AdapterView.OnItemClickListener exchangeListener;
    private AdapterView.OnItemClickListener bookListener;

    private ArrayList<Exchange> availableExchanges = new ArrayList<Exchange>();
    private ArrayList<Exchange> userExchanges = new ArrayList<Exchange>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Make logo show up in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        getUserExchanges();

        getPublicExchanges();
    }

    // Update when we return to the home screen.
    @Override
    protected void onResume() {
        super.onResume();
        logger.i(TAG, "Updating on resume.");
        // Refresh the lists of books
        getUserExchanges();
        getPublicExchanges();
    }

    @Override
    protected void onStart() {
        super.onStart();
        logger.i(TAG, "Updating on start.");
        // Refresh the lists of books
        getUserExchanges();
        getPublicExchanges();
    }

    // Go createExchangeActivity page where the user can make a new loan or offer
    public void openCreateExchangeActivity(View view) {
        Intent intent = new Intent(this, CreateExchangeActivity.class);
        startActivity(intent);
    }

    // Get the correct exchange to display. Separate from openViewExchangeActivity so that
    // openViewExchangeActivity can be called directly with an Exchange.
    public void onExchangeClicked(View view, int position) {
        logger.i(TAG, "Exchanged clicked at position " + position);

        try {
            Exchange myExchange = userExchanges.get(position);

            openViewExchangeActivity(myExchange);
        }
        catch (Exception e) {
            logger.e(TAG, "exception", e);
        }
    }

    // Go to the ViewExchangeActivity page that displays the state of a particular exchange
    public void openViewExchangeActivity(Exchange currentExchange) {

        try {
            Exchange myExchange = currentExchange;

            Book myBook = Client.getBook(myExchange.book_id);

            Bundle bundle = new Bundle();
            bundle.putSerializable("book", myBook);
            bundle.putSerializable("exchange", myExchange);

            Intent intent = new Intent(this, ViewExchangeActivity.class);
            startActivity(intent.putExtras(bundle));
        }
        catch (Exception e) {
            logger.e(TAG, "exception", e);
        }
    }

    // Go to the Book Details page
    public void openBookDetailsActivity(View view, int position) {

        try {
            Exchange myExchange = availableExchanges.get(position);
            Book myBook = Client.getBook(myExchange.book_id);

            Bundle bundle = new Bundle();
            bundle.putSerializable("book", myBook);
            bundle.putSerializable("exchange", myExchange);

            Intent intent = new Intent(this, BookDetailsActivity.class);
            startActivity(intent.putExtras(bundle));
        }
        catch (Exception e) {
            logger.e(TAG, "exception", e);
        }

    }



    /** Collects a list of the exchanges the user is participating in. */
    private void getUserExchanges() {
        try {
            userExchanges = (ArrayList<Exchange>) Client.getPrivateExchanges(Client.userId);
            logger.i(TAG, "Calling get Private Exchanges, found " + userExchanges.size());
            if (userExchanges.isEmpty()) {
                View noExchanges = findViewById(R.id.ifNoUserExchanges);
                noExchanges.setVisibility(View.VISIBLE);
            }
            if (userExchanges == null) {
                logger.i(TAG, "User exchanges list was null");
                return;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ExchangeListAdapter exchangeAdapter = new ExchangeListAdapter(getApplicationContext(), userExchanges);
        ListView userExchangeList = (ListView) findViewById(R.id.userExchangeList);
        userExchangeList.setAdapter(exchangeAdapter);

        exchangeListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                logger.i(TAG, "ExchangeView clicked!");
                onExchangeClicked(view, position);
            }
        };

        userExchangeList.setOnItemClickListener(exchangeListener);
    }

    private void getPublicExchanges() {

        try {
            availableExchanges = (ArrayList<Exchange>) Client.getPublicExchanges();
            logger.i(TAG, "Calling get Public Exchanges, found " + availableExchanges.size());
            if (availableExchanges.isEmpty()) {
                View noExchanges = findViewById(R.id.ifNoOpenExchanges);
                noExchanges.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // FIXME might have to recreate the adapter every time the list changes?
        ExchangeListAdapter bookAdapter = new ExchangeListAdapter(getApplicationContext(), availableExchanges);

        ListView bookList = (ListView) findViewById(R.id.bookList);
        bookList.setAdapter(bookAdapter);

        bookListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                logger.i(TAG, "ExchangeView clicked!");
                Exchange myExchange = availableExchanges.get(position);
                if (myExchange.borrower_id == Client.userId || myExchange.loaner_id == Client.userId) {
                    onExchangeClicked(view, position);
                }
                else {
                    openBookDetailsActivity(view, position);
                }
            }
        };

        bookList.setOnItemClickListener(bookListener);
    }

    public void onLogout(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
