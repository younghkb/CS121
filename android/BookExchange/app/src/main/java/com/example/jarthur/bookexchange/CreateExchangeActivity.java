package com.example.jarthur.bookexchange;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import client.Book;
import client.Client;
import client.Exchange;

// Currently using java.util.Date, NOT java.sql.Date
// TODO is above comment necessary?

/**
 * This class allows the user to create an exchange (offer or request). It takes in the user's
 * query (book name, author, etc), contacts Goodreads, and returns 5 possible options for what the
 * book could be. If there is no good result, then it notifies the user that no matches were found.
 * The user marks the exchange as a request or an offer and then submits. This request is recorded
 * in the database as part of the user's data.
 */
public class CreateExchangeActivity extends ActionBarActivity {

    // Gives the servers the parameters for a new exchange, then queries to get it
    private Exchange newExchange = new Exchange();
    private Book newBook = new Book();
    private ArrayList<Book> bookList = new ArrayList<>();

    // Used for logging statements about what actions the class is executing
    private static Log logger;

    // Holds the string from the user's search
    private EditText bookQuery;

    // Listener for the confirmation dialog regarding the exchange creation
    private static DialogInterface.OnClickListener createListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exchange);

        // Makes the logo show up in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        createBookQueryBox();

        // Set default values (exchange type defaults to offer)
        newExchange.exchange_type = Exchange.Type.LOAN;
        newExchange.loaner_id = Client.userId;
        newExchange.borrower_id = 0;

        // When the dialog confirming the exchange appears, (if the user clicks yes) this tries
        // to send the exchange data to the database.
        createListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                logger.i("CreateExchangeActivity", "Create Exchange button has been clicked");

                try {
                    Client.createBook(newBook);
                    Client.createExchange(newExchange);
                    returnToHomeScreen();

                } catch (Exception e) {
                    logger.e("Confirm Dialog", "exception", e);

                }
            }
        };
    }

    // Moves user from current activity to home screen activity
    private void returnToHomeScreen() {
        Intent i = new Intent(this, HomeScreen.class);
        startActivity(i);
    }

    // Defines the attributes of the dialog fragment that asks the user to confirm their
    // decision to create this exchange
    public static class AlertDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.confirm_dialog)
                    .setPositiveButton(R.string.yes, createListener)
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            // No action necessary
                        }
                    });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    // Creates the confirmation dialog and makes sure that the user has picked a book.
    public void confirmAlert(View view) {
        if (newBook.book_title == null || newBook.book_title == "") {
            bookQuery = (EditText) findViewById(R.id.bookQueryBox);
            bookQuery.setError("You must select a book");
            return;
        }
        DialogFragment confirmDialog = new AlertDialogFragment();
        confirmDialog.show(getFragmentManager(), "confirm_dialog");
    }

    // Creates the search bar so that the user can look for a book either by title, author, or ISBN
    private void createBookQueryBox() {

        // Defines the bookQuery view to record what the user has searched, and displays a hint
        // regarding how the seach bar should be used
        bookQuery = (EditText) findViewById(R.id.bookQueryBox);
        bookQuery.setHint("Search for a book by title, author, or ISBN");

        // Required to be final for use in inner class
        final AdapterView.OnItemClickListener bookSelectedListener =
                new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newBook = bookList.get(position);
                newExchange.book_title = newBook.book_title;
                newExchange.book_id = newBook.book_id;
                newExchange.status = Exchange.Status.INITIAL;

                try {
                    bookQuery.setText(newBook.book_title);
                    logger.i("CreateExchangeActivity", "Updating Query Box and book list");


                    // Manually sets the visibility of the list view this adaptor controls
                    ListView booksFound = (ListView) findViewById(R.id.booksFound);
                    booksFound.setVisibility(View.GONE);
                } catch (Exception e) {
                    logger.e("CreateExchangeActivity", "exception on book selected", e);
                }
            }
        };

        // Sets a listener that checks for when the user has searched for a book
        bookQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // When the user searches, makes sure they have actually input a search item.
                // If that item returns nothing, this notifies the user. Otherwise, displays
                // a list of 5 books that are possible matches.
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String queryText = bookQuery.getText().toString().trim();
                    if (queryText.isEmpty()) {
                        bookQuery.setError("Search was empty!");
                    }
                    try {
                        bookList = (ArrayList<Book>) Client.searchBook(queryText);

                        if (bookList.isEmpty()) {
                            bookQuery.setError("No books found!");
                        }
                        ListView booksFound = (ListView) findViewById(R.id.booksFound);
                        booksFound.setAdapter(new BookListAdapter(getApplicationContext(), bookList));
                        booksFound.setVisibility(View.VISIBLE);
                        logger.i("OnQueryTextSubmit", "setting visibility of book list");
                        booksFound.setOnItemClickListener(bookSelectedListener);
                    } catch (Exception e) {
                        logger.e("CreateExchangeActivity", "exception in selectBook()", e);
                    }
                    return true;
                }
                return false;
            }
        });

    }

    // Sets the type of exchange as either a loan or a request (borrow). Note that we default
    // exchanges to be offers, which was set in onCreate.
    public void onRadioButtonClicked(View view) {
        if (view.getId() == R.id.offer) {
            newExchange.exchange_type = Exchange.Type.LOAN;
            newExchange.loaner_id = Client.userId;
            newExchange.borrower_id = 0;
        }
        else if (view.getId() == R.id.request) {
            newExchange.exchange_type = Exchange.Type.BORROW;
            newExchange.borrower_id = Client.userId;
            newExchange.loaner_id = 0;
        }
    }
}


