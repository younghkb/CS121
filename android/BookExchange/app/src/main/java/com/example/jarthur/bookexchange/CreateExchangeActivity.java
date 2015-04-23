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

public class CreateExchangeActivity extends ActionBarActivity {

    // Gives the servers the parameters for a new exchange, then queries to get it

    private Exchange newExchange = new Exchange();
    private Book newBook = new Book();
    private ArrayList<Book> bookList = new ArrayList<>();

    private static Log logger;

    private SearchView bookQuery;

    private static DialogInterface.OnClickListener createListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exchange);

        // Make logo show up in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        createBookQueryBox();

        createExchangeTypeSpinner();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_loan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement  //?? -HR
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        DialogFragment confirmDialog = new AlertDialogFragment();
        confirmDialog.show(getFragmentManager(), "confirm_dialog");
    }

    private void createBookQueryBox() {
        // TODO MAKE THIS WORK

        bookQuery = (SearchView) findViewById(R.id.bookQueryBox);
        bookQuery.setQueryHint("Search for a book by title, author, or ISBN");
        bookQuery.setVisibility(View.VISIBLE);

        // Okay to set to final? (required for use in inner class)
        final AdapterView.OnItemClickListener bookSelectedListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newBook = bookList.get(position);
                newExchange.book_title = newBook.book_title;
                newExchange.book_id = newBook.book_id;
                newExchange.status = Exchange.Status.INITIAL;

                try {
                    bookQuery.setQuery(newBook.book_title, false);
                    logger.i("CreateExchangeActivity", "Updating Query Box and book list");

                    // A little hacky because we're manually setting the visibility of the
                    // list view this adapter controls
                    ListView booksFound = (ListView) findViewById(R.id.booksFound);
                    booksFound.setVisibility(View.GONE);
                }
                catch (Exception e) {
                    logger.e("CreateExchangeActivity","exception on book selected", e);
                }
            }
        };

        bookQuery.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                logger.i("CreateExchangeActivity", "selecting book");
                // Get the user input text with leading or ending whitespace removed
                bookQuery = (SearchView) findViewById(R.id.bookQueryBox);       // TODO needed?
                String queryText = bookQuery.getQuery().toString().trim();
                if (queryText.isEmpty()) {
                    // TODO give visual feedback
                    return false;
                }
                else {
                    try {
                        bookList = (ArrayList<Book>) Client.searchBook(queryText);

                        // TODO if book list is empty show 'no books found'
                        ListView booksFound = (ListView) findViewById(R.id.booksFound);
                        booksFound.setAdapter(new BookListAdapter(getApplicationContext(), bookList));
                        booksFound.setVisibility(View.VISIBLE);
                        logger.i("OnQueryTextSubmit", "setting visibility of book list");
                        booksFound.setOnItemClickListener(bookSelectedListener);
                    }
                    catch (Exception e) {
                        logger.e("CreateExchangeActivity", "exception in selectBook()", e);
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ListView booksFound = (ListView) findViewById(R.id.booksFound);
                booksFound.setVisibility(View.GONE);
                return true;
            }
        });

    }

    private void createExchangeTypeSpinner() {
        // Drop down menu for user to pick 'offer' or 'request'
        Spinner pickExchangeType = (Spinner) findViewById(R.id.SpinnerOfferLoan);
        pickExchangeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                logger.i("CreateExchangeActivity", "picking offer type");

                // OFFER 0
                // REQUEST 1
                if (position == 0) {            // "Offer" selected
                    newExchange.exchange_type = Exchange.Type.LOAN;
                    newExchange.loaner_id = Client.userId;
                    newExchange.borrower_id = 0;
                } else if (position == 1) {     // "Request" selected
                    newExchange.exchange_type = Exchange.Type.BORROW;
                    newExchange.borrower_id = Client.userId;
                    newExchange.loaner_id = 0;
                }
            }
        });
    }

}


