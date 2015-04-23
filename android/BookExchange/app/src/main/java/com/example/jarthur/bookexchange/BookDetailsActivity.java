package com.example.jarthur.bookexchange;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import client.Book;
import client.Client;
import client.Exchange;

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
            View requestButton = findViewById(R.id.requestButton);
            requestButton.setVisibility(View.GONE);

            // Loan period is being deprecated for now.
            //View c = findViewById(R.id.loanPeriod);
            //c.setVisibility(View.GONE);

            // Commented out because I don't think this should be hidden - HR 4/20/15
            //View d = findViewById(R.id.owner);
            //d.setVisibility(View.GONE);
        }
        // Otherwise show everything

        // TODO factor out the book details that are also used in ViewExchange
        TextView title = (TextView) findViewById(R.id.bookTitle);
        title.setText(myBook.book_title);

        TextView author = (TextView) findViewById(R.id.author);
        author.setText("Author: " + myBook.author);

        ImageView cover = (ImageView) findViewById(R.id.cover);
        try {
            GetImageTask task = new GetImageTask();
            task.execute(new URL(myBook.image_url));
            Drawable coverImage = task.get(5, TimeUnit.SECONDS); // Wait no more than 5 seconds
            cover.setImageDrawable(coverImage);
        }
        catch (Exception e) {
            logger.e("BookDetails", "exception", e);
        }

        TextView isbn = (TextView) findViewById(R.id.ISBN);
        if (myBook.isbn != null) {
            isbn.setText("ISBN: " + myBook.isbn);
        }
        else {
            isbn.setText("ISBN: not available");
        }

        TextView pubYear = (TextView) findViewById(R.id.pubYear);
        if (myBook.pub_year != null) {
            pubYear.setText("Publication Year: " + myBook.pub_year);
        }
        else {
            pubYear.setText("Publication Year: not available");
        }

        TextView owner = (TextView) findViewById(R.id.owner);
        try {
            String ownerName = Client.getUsernameFromUserID(myExchange.loaner_id);
            if (ownerName != null) {
                owner.setText("Owner: " + ownerName);
            }
            else {
                owner.setVisibility(View.GONE);
            }
        }
        catch (Exception e) {
            logger.e("BookDetails", "exception", e);
        }

        // Loan period is being deprecated for now
        /*TextView loanPeriod = (TextView) findViewById(R.id.loanPeriod);
        loanPeriod.setText("Loan Period: " + myExchange.start_date.toString()
                + " to " + myExchange.end_date.toString()); */

        final Button requestButton = (Button) findViewById(R.id.requestButton);
        if (myExchange.exchange_id == Client.userId) {      // Users can't request their own book
            requestButton.setVisibility(View.GONE);
        }
        else {
            requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Client.updateExchangeBorrower(myExchange.exchange_id, Client.userId);
                        // TODO update exchange status
                        requestButton.setText("Requested!");
                    }
                    catch (Exception e) {
                        logger.e("BookDetails", "exception", e);
                    }
                }
            });
        }
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
}
