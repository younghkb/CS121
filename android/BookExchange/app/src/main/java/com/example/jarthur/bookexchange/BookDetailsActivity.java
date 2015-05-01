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

/**
 * This activity displays the details of a book (title, author, cover, etc). This is accessed
 * when the user is looking at an open exchange that they are not participating in. Through this
 * activity, the user can request or offer in response to the open exchange, thus making it
 * exchange in which they are a member (so the book will be viewed through ViewExchangeActivity).
 */
public class BookDetailsActivity extends ActionBarActivity {

   // Used for logging messages to LogCat
   private static Log logger;

   // Initializes the book and exchange status
   Book myBook;
   Exchange myExchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // TODO explain this
        Intent i = getIntent();
        myExchange = (Exchange) i.getSerializableExtra("exchange");
        myBook = (Book) i.getSerializableExtra("book");

        // Sets the exchange status to appropriate type, like Initial, Request, etc.
        Exchange.Status exchangeStatus = myExchange.status;

        // TODO factor out the book details that are also used in ViewExchange
        TextView title = (TextView) findViewById(R.id.bookTitle);
        title.setText(myBook.book_title);

        TextView author = (TextView) findViewById(R.id.author);
        author.setText("Author: " + myBook.author);

        // Grabs the cover of the book from Goodreads
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

        // Grabs the ISBN of the book from Goodreads
        TextView isbn = (TextView) findViewById(R.id.ISBN);
        if (myBook.isbn != null) {
            isbn.setText("ISBN: " + myBook.isbn);
        }
        else {
            isbn.setText("ISBN: not available");
        }

        // Grabs the publishing year from Goodreads
        TextView pubYear = (TextView) findViewById(R.id.pubYear);
        if (myBook.pub_year != null) {
            pubYear.setText("Publication Year: " + myBook.pub_year);
        }
        else {
            pubYear.setText("Publication Year: not available");
        }

        // Grabs the owner of the open exchange from the database
        TextView owner = (TextView) findViewById(R.id.owner);
        try {
            String ownerName = Client.getUsernameFromUserID(myExchange.loaner_id);
            if (ownerName != "Unknown") {
                owner.setText("Owner: " + ownerName);
            }
            else {
                owner.setVisibility(View.GONE);
            }
        }
        catch (Exception e) {
            logger.e("BookDetails", "exception", e);
        }

        // Ensures that users can't request their own book
        if (myExchange.loaner_id == Client.userId || myExchange.borrower_id == Client.userId) {
            // TODO add cancel button
        }
        // If the open exchange is an request, add the appropriate buttons so that the user
        // can offer the book
        else if (myExchange.exchange_type == Exchange.Type.BORROW) {
            final Button offerButton = (Button) findViewById(R.id.offerButton);
            offerButton.setVisibility(View.VISIBLE);
            offerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Client.updateExchangeLoaner(myExchange.loaner_id, Client.userId);
                        Client.updateExchangeStatus(myExchange.exchange_id, Exchange.Status.RESPONSE);
                        offerButton.setText("Offered!");
                    }
                    catch (Exception e) {
                        logger.e("OfferButton", "exception", e);
                    }
                }
            });

        }

        // If the open exchange is an offer, sets up the appropriate buttons so that the user
        // can request the book.
        else {
            final Button requestButton = (Button) findViewById(R.id.requestButton);
            requestButton.setVisibility(View.VISIBLE);
            requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Client.updateExchangeBorrower(myExchange.exchange_id, Client.userId);
                        Client.updateExchangeStatus(myExchange.exchange_id, Exchange.Status.RESPONSE);
                        requestButton.setText("Requested!");
                    }
                    catch (Exception e) {
                        logger.e("RequestButton", "exception", e);
                    }
                }
            });

        }

    }



}
