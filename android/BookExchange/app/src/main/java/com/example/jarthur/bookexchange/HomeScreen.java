package com.example.jarthur.bookexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

import client.Book;
import client.Client;
import client.Exchange;

/**
 * This is the homepage for our app. From here, the user can log out (which takes them to a cleared
 * LoginActivity), or go to CreateExchangeActivity, BookDetailsActivity, or ViewExchangeActivity.
 * This screen also organizes exchanges by those that the user is a member of and those that are
 * open and created by other users.
 */
public class HomeScreen extends ActionBarActivity {

    private Log logger;
    private String TAG = "HomeScreen";

    private AdapterView.OnItemClickListener exchangeListener;
    private AdapterView.OnItemClickListener bookListener;

    private ArrayList<Exchange> availableExchanges = new ArrayList<Exchange>();
    private ArrayList<Exchange> userExchanges = new ArrayList<Exchange>();


    // This function initializes the layout and data present when the HomeScreen is started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Make logo show up in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // Grabs the data regarding current exchanges the user is in
        getUserExchanges();

        // TODO differentiate between offer and request!!!! IMPORTANT
        getPublicExchanges();
    }

    // Updates exchange data whenever the user returns to the home screen.
    @Override
    protected void onResume() {
        super.onResume();
        logger.i(TAG, "Updating!");
        // Refresh the list of available books
        getUserExchanges();
        getPublicExchanges();
    }

    // Go to createExchangeActivity page where the user can make a new loan or offer
    public void openCreateExchangeActivity(View view) {
        Intent intent = new Intent(this, CreateExchangeActivity.class);
        startActivity(intent);
    }

    // Go to the ViewExchangeActivity page that displays the state of a particular exchange
    public void openViewExchangeActivity(View view, int position) {

        try {
            Exchange myExchange = userExchanges.get(position);

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



    // Collects a list of the exchanges the user is participating in.
    private void getUserExchanges() {
        // Grabs and displays the exchanges the user is in, or displays nothing if the user has
        // no exchanges.
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

        // TODO explain this in a comment
        ExchangeListAdapter exchangeAdapter = new ExchangeListAdapter(getApplicationContext(), userExchanges);
        ListView userExchangeList = (ListView) findViewById(R.id.userExchangeList);
        userExchangeList.setAdapter(exchangeAdapter);

        // Sets a listener for when an exchange is clicked on, which takes the user to the details
        // for the associated book.
        exchangeListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                logger.i(TAG, "ExchangeView clicked!");
                openViewExchangeActivity(view, position);
            }
        };

        userExchangeList.setOnItemClickListener(exchangeListener);
    }

    // Collects a list of public exchanges, either unaccepted offers or unfulfilled requests
    private void getPublicExchanges() {

        try {
            availableExchanges = (ArrayList<Exchange>) Client.getPublicExchanges();
            if (availableExchanges.isEmpty()) {
                View noExchanges = findViewById(R.id.ifNoOpenExchanges);
                noExchanges.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ExchangeListAdapter bookAdapter = new ExchangeListAdapter(getApplicationContext(), availableExchanges);

        ListView bookList = (ListView) findViewById(R.id.bookList);
        bookList.setAdapter(bookAdapter);

        bookListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                logger.i(TAG, "ExchangeView clicked!");
                openBookDetailsActivity(view, position);
            }
        };

        bookList.setOnItemClickListener(bookListener);
    }

    // Returns the user to the login activity when clicking logout. The login activity handles
    // clearing stored user information.
    public void onLogout(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
