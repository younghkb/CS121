package com.example.jarthur.bookexchange;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.InputStream;
import java.net.URL;

// TODO get image from url

public class BookDetailsActivity extends ActionBarActivity {

    // TODO get book id from parent activities

    // FIXME This may be static or a singleton??
    RequestManager requestManager;

    // Goodreads id of the book (stored in the Exchange data)
    String bookId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Book myBook = requestManager.findBook(bookId);

        // Initial, Request, etc.
        ExchangeStatus exchangeStatus = myBook.status;

        // Sets visibilities correctly based on type
        // hides the request button from the details of a book involved in an exchange
        if (exchangeStatus == ExchangeStatus.ACCEPTED || exchangeStatus == ExchangeStatus.COMPLETED) {

            // Does not show request button, owner or possible loan period
            View b = findViewById(R.id.requestButton);
            b.setVisibility(View.GONE);

            View c = findViewById(R.id.loanPeriod);
            c.setVisibility(View.GONE);

            View d = findViewById(R.id.owner);
            d.setVisibility(View.GONE);
        }
        // Otherwise show everything
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_details, menu);
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

    // from: http://stackoverflow.com/questions/6407324/how-to-get-image-from-url-in-android
    // theoretically, this will help in getting the image from the goodreads data
    // i have my doubts
    //TODO make this work gosh darn it
    public static Drawable imageFromUrl(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            //is.
            Drawable d = Drawable.createFromStream(is, "Goodreads Image URL");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
