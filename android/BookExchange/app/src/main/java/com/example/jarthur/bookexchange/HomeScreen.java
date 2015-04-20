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
        getAvailableBooks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the list of available books      TODO is this needed?
        getAvailableBooks();
    }

    // Go createExchangeActivity page where the user can make a new loan or offer
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
            // TODO debug this
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /** Collects a list of the exchanges the user is participating in. */
    private void getUserExchanges() {
        try {
            userExchanges = (ArrayList<Exchange>) Client.getPrivateExchanges(Client.userId);
            // TODO make sure it works if list is empty
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
                openViewExchangeActivity(view, position);
            }
        };

        userExchangeList.setOnItemClickListener(exchangeListener);
    }

    private void getAvailableBooks() {

        try {
            availableExchanges = (ArrayList<Exchange>) Client.getPublicExchanges();
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
}
