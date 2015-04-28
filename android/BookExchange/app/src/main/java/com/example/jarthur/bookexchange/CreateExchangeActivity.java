package com.example.jarthur.bookexchange;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import client.Book;
import client.Client;
import client.Exchange;

// Currently using java.util.Date, NOT java.sql.Date

public class CreateExchangeActivity extends ActionBarActivity {

    // Gives the servers the parameters for a new exchange, then queries to get it

    private Exchange newExchange = new Exchange();
    private Book newBook = new Book();
    private ArrayList<Book> bookList = new ArrayList<>();

    private static Log logger;

    private EditText bookQuery;

    private static DialogInterface.OnClickListener createListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exchange);

        // Make logo show up in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        createBookQueryBox();

        // Set defaults (exchange type defaults to offer)
        newExchange.exchange_type = Exchange.Type.LOAN;
        newExchange.loaner_id = Client.userId;
        newExchange.borrower_id = 0;

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

    private void returnToHomeScreen() {
        Intent i = new Intent(this, HomeScreen.class);
        startActivity(i);
    }


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
                            // Do we want to do anything here?
                        }
                    });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


    public void confirmAlert(View view) {
        if (isQueryValid()) {
            DialogFragment confirmDialog = new AlertDialogFragment();
            confirmDialog.show(getFragmentManager(), "confirm_dialog");
        }
    }

    private boolean isQueryValid() {
        if (newBook.book_title == null || newBook.book_title == "") {
            bookQuery = (EditText) findViewById(R.id.bookQueryBox);
            bookQuery.setError("You must select a book!");
            return false;
        }
        return true;
    }

    private void createBookQueryBox() {

        bookQuery = (EditText) findViewById(R.id.bookQueryBox);
        bookQuery.setHint("Search for a book by title, author, or ISBN");

        // setting to final required for use in inner class
        final AdapterView.OnItemClickListener bookSelectedListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newBook = bookList.get(position);
                newExchange.book_title = newBook.book_title;
                newExchange.book_id = newBook.book_id;
                newExchange.status = Exchange.Status.INITIAL;

                try {
                    bookQuery.setText(newBook.book_title);
                    isQueryValid();     // Reset the error notification
                    logger.i("CreateExchangeActivity", "Updating Query Box and book list");

                    // A little hacky because we're manually setting the visibility of the
                    // list view this adapter controls
                    ListView booksFound = (ListView) findViewById(R.id.booksFound);
                    booksFound.setVisibility(View.GONE);
                } catch (Exception e) {
                    logger.e("CreateExchangeActivity", "exception on book selected", e);
                }
            }
        };


        bookQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

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


