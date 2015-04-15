package com.example.jarthur.bookexchange;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

public class BookDetailsActivity extends ActionBarActivity {

   private static Log logger;

   Book myBook;
   Exchange myExchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Intent i = getIntent();
        myExchange = (Exchange) i.getSerializableExtra("exchange");
        myBook = (Book) i.getSerializableExtra("book");

        // Initial, Request, etc.
        Exchange.Status exchangeStatus = myExchange.status;

        // Sets visibilities correctly based on type
        // hides the request button from the details of a book involved in an exchange
        if (exchangeStatus == Exchange.Status.ACCEPTED || exchangeStatus == Exchange.Status.COMPLETED) {

            // Does not show request button, owner or possible loan period
            View b = findViewById(R.id.requestButton);
            b.setVisibility(View.GONE);

            View c = findViewById(R.id.loanPeriod);
            c.setVisibility(View.GONE);

            View d = findViewById(R.id.owner);
            d.setVisibility(View.GONE);
        }
        // Otherwise show everything

        TextView title = (TextView) findViewById(R.id.bookTitle);
        title.setText(myBook.book_title);

        TextView author = (TextView) findViewById(R.id.author);
        author.setText("Author: " + myBook.author);

        ImageView cover = (ImageView) findViewById(R.id.cover);
        cover.setImageDrawable(imageFromUrl(myBook.image_url));

        TextView isbn = (TextView) findViewById(R.id.ISBN);
        isbn.setText("ISBN: " + myBook.isbn);

        TextView pubYear = (TextView) findViewById(R.id.pubYear);
        pubYear.setText("Publication Year: " + myBook.pub_year);

        // TODO We want to show the name of the user, not their id
        //TextView owner = (TextView) findViewById(R.id.owner);
        //owner.setText("Owner: " + myExchange.loaner_id);

        TextView loanPeriod = (TextView) findViewById(R.id.loanPeriod);
        //loanPeriod.setText("Loan Period: " + myExchange.start_date.toString() + " to " + myExchange.end_date.toString());
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
    // Getting an android.os.NetworkOnMainThreadException because the main thread is trying to access the internet
    public static Drawable imageFromUrl(String url) {

        try {
            logger.i("BookDetails", "Called imageFromUrl");
            InputStream is = (InputStream) new URL("https://d.gr-assets.com/books/1372847500m/5907.jpg").getContent();
            //is.
            Drawable d = Drawable.createFromStream(is, "Goodreads Image URL");
            return d;
        } catch (Exception e) {
            logger.e("BookDetails", "exception", e);
            return null;
        }
    }
}
