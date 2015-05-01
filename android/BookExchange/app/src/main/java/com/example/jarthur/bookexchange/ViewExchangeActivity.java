package com.example.jarthur.bookexchange;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import client.Book;
import client.Client;
import client.Exchange;

/** This class is very similar to BookDetailsActivity, which has more detailed commenting 
 * regarding its functionality
 */
public class ViewExchangeActivity extends ActionBarActivity {

    private static Log logger;

    private Exchange myExchange;
    private static int exchangeId;  // Needs to be static for use in inner declared class
    private Book myBook;

    private boolean isOwner;

    private String borrowerName = "Unknown";
    private String ownerName = "Unknown";

    private static AlertDialog.Builder confirmBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exchange);

        Intent i = getIntent();
        myExchange = (Exchange) i.getSerializableExtra("exchange");
        myBook = (Book) i.getSerializableExtra("book");

        logger.i("ViewExchange. Book: ", myBook.toString());
        exchangeId = myExchange.exchange_id;

        // Make logo show up in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // true if the current user is the owner of this particular book
        isOwner = (Client.userId == myExchange.loaner_id);

        setAlertDialog();

        setBookInfo();
    }

    private void setAlertDialog() {
        // Initialized here so it can call the non-static method setStatusText()
        confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setMessage(R.string.alert_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES! Or, you know, confirm that yes, the user does
                        // want to do this
                        try {
                            logger.i("ViewExchangeActivity", "updating exchange status");
                            myExchange.status = Exchange.Status.COMPLETED;
                            Client.updateExchangeStatus(exchangeId, Exchange.Status.COMPLETED);
                            setStatusText();    // Also updates finishButton

                        } catch (Exception e) {
                            logger.e("ViewExchangeActivity", "exception", e);
                        }
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
    }

    private void setBookInfo() {
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
            logger.e("ViewExchangeActivity", "exception", e);
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

        // Note: we will set loaner/borrower id before offer/request has been accepted
        try {
            borrowerName = Client.getUsernameFromUserID(myExchange.borrower_id);
            ownerName = Client.getUsernameFromUserID(myExchange.loaner_id);
            logger.i("ViewExchange", "Owner Name: " + ownerName);
        }
        catch (Exception e) {
            logger.e("ViewExchange", "exception", e);
        }

        // Exchanges in this activity should always have an owner
        TextView owner = (TextView) findViewById(R.id.owner);
        owner.setText("Owner: " + ownerName);

        setStatusText();
    }

    private void setStatusText() {

        logger.i("ViewExchangeActivity", "setting status text");

        TextView status = (TextView) findViewById(R.id.exchangeStatus);

        RadioGroup action = (RadioGroup) findViewById(R.id.handleRequest);   // Default GONE
        action.setVisibility(View.GONE);

        switch (myExchange.status) {
            case INITIAL:
                if (isOwner) {
                    status.setText("You have posted an offer to loan this book.");
                    break;
                }
                status.setText("You have posted a request to borrow this book.");
                break;

            case RESPONSE:
                if (isOwner) {
                    if (myExchange.exchange_type == Exchange.Type.LOAN) {
                        status.setText(borrowerName + " has requested this book. Would you like to accept the request?");
                        action.setVisibility(View.VISIBLE);

                    }
                    else { // Borrow
                        status.setText("You have offered to loan this book to " + borrowerName + ".");
                    }
                    break;
                }
                else {  // not owner
                    if (myExchange.exchange_type == Exchange.Type.LOAN) {
                        status.setText("You have sent a request to borrow this book from " + ownerName + ".");
                        break;
                    }
                    else { // Borrow
                        status.setText(ownerName + " has offered to loan you this book. Would you like to accept the offer?");
                        action.setVisibility(View.VISIBLE);
                    }
                    break;
                }

            case ACCEPTED:
                if (isOwner) {
                    status.setText("You are lending this book to " + borrowerName);
                    break;
                }
                else if (myExchange.exchange_type == Exchange.Type.BORROW) {
                    status.setText("You may now borrow this book from " + ownerName + ".");
                }
                else if (myExchange.exchange_type == Exchange.Type.LOAN) {
                    status.setText(ownerName + " has accepted your request. You may now borrow this book.");
                }
                break;

            case COMPLETED:
                logger.i("SetStatusText", "setting status to completed");
                status.setText("This exchange has been completed.");
                break;

            default:
                break;
        }

        View finishButton = findViewById(R.id.finishButton);
        if (myExchange.status == Exchange.Status.ACCEPTED && isOwner) {
            finishButton.setVisibility(View.VISIBLE);
        }
        else {
            logger.i("SetStatusText", "hiding finish button");
            finishButton.setVisibility(View.GONE);
        }
    }

    public void onRadioButtonClicked(View view) {
        if (view.getId() == R.id.accept) {
            myExchange.status = Exchange.Status.ACCEPTED;
            try {
                Client.updateExchangeStatus(myExchange.exchange_id, Exchange.Status.ACCEPTED);
            }
            catch (Exception e) {
                logger.e("ViewExchangeActivity", "exception", e);
            }
        }
        else if (view.getId() == R.id.reject) {
            myExchange.status = Exchange.Status.INITIAL;
            try {
                Client.updateExchangeStatus(myExchange.exchange_id, Exchange.Status.INITIAL);
            }
            catch (Exception e) {
                logger.e("ViewExchangeActivity", "exception", e);
            }
        }

        // Reset to reflect the new status
        setStatusText();
    }


    /* Confirmation for completion or cancellation of an exchange. */
    public static class AlertDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = confirmBuilder;
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    public void confirmAlert(View view) {
        DialogFragment newFragment = new AlertDialogFragment();
        newFragment.show(getFragmentManager(), "alert");
    }

}
