package com.example.jarthur.bookexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class HomeScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Make logo show up in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // List of exchanges user is participating in
        List<Exchange> userExchanges = new ArrayList<Exchange>();
        int userId = 1111;      // FIXME get global static userId
        try {
            userExchanges = Client.getPrivateExchanges(userId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ExchangeListAdapter exchangeAdapter = new ExchangeListAdapter(getApplicationContext(), userExchanges);
        ListView userExchangeList = (ListView) findViewById(R.id.userExchangeList);
        userExchangeList.setAdapter(exchangeAdapter);

        // List of Available Books
        List<Exchange> availableBooks = new ArrayList<Exchange>();

        try {
            availableBooks = Client.getPublicExchanges();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ExchangeListAdapter bookAdapter = new ExchangeListAdapter(getApplicationContext(), availableBooks);

        ListView bookList = (ListView) findViewById(R.id.bookList);
        bookList.setAdapter(bookAdapter);

        // TODO set what happens on click of item (ExchangeView) in list
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("ExchangeView clicked!");
                openBookDetailsActivity(view);
            }
        });
    }

    // Go createExchangeActivity page where the user can make a new loan or offer
    public void openCreateExchangeActivity(View view) {
        Intent intent = new Intent(this, CreateExchangeActivity.class);
        startActivity(intent);
    }

    // Go to the ViewExchangeActivity page that displays the state of a particular exchange
    public void openViewExchangeActivity(View view) {
        //Exchange myExchange = view.getExchange(); TODO
        Exchange myExchange = new Exchange();
        Bundle bundle = new Bundle();
        bundle.putSerializable("exchange", myExchange);
        Intent intent = new Intent(this, ViewExchangeActivity.class);
        startActivity(intent.putExtras(bundle));
    }

    // Go to the Book Details page
    public void openBookDetailsActivity(View view) {
        Book myBook = new Book();
        try {
            ExchangeView myExchange = (ExchangeView) view;

            myBook = Client.getBook(myExchange.getExchange().book_id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("book", myBook);
        Intent intent = new Intent(this, BookDetailsActivity.class);
        startActivity(intent.putExtras(bundle));
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
}
