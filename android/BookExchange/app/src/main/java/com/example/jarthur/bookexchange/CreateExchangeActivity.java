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
    private List<Book> bookList = new ArrayList<>();

    private static Log logger;

    private String[] columnName = {"_id"};      // necessary for CursorAdapter to work
    private final int DEFAULT_NUM_BOOKS = 5;

    private SearchView bookQuery;
    private CursorAdapter suggestionsAdapter;
    private Cursor suggestionsCursor = new MatrixCursor(columnName, DEFAULT_NUM_BOOKS);

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
                    Client.createExchange(newExchange);     // FIXME app is hanging
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

    // TODO deprecate
    private void selectBook() {

        // Get the user input text with leading or ending whitespace removed
        bookQuery = (SearchView) findViewById(R.id.bookQueryBox);
        String queryText = bookQuery.getQuery().toString().trim();
        if (queryText.isEmpty()) {
            // TODO give visual feedback
        }
        else {
            try {
                bookList = Client.searchBook(queryText);
                // TODO put in stuff
                newBook = bookList.get(0);       // TODO make user choose

                newExchange.book_title = newBook.book_title;
                newExchange.book_id = newBook.book_id;

            }
            catch (Exception e) {
                logger.e("CreateExchangeActivity", "exception in selectBook()", e);
            }
        }
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
        // http://stackoverflow.com/questions/5099814/knowing-when-edit-text-is-done-being-edited
        bookQuery = (SearchView) findViewById(R.id.bookQueryBox);
        bookQuery.setQueryHint("Search for a book by title, author, or ISBN");


        bookQuery.setOnSearchClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView booksFound = new ListView(getApplicationContext());
                suggestionsAdapter.getDropDownView(0, booksFound, bookQuery);
                logger.i("CreateExchangeActivity", "selecting book");
            }
        });

        // Create a new Cursor Adapter that constantly listens for changes
        suggestionsAdapter = new CursorAdapter(getApplicationContext(), suggestionsCursor,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                TextView bookView = new TextView(context);
                int pos = cursor.getPosition();
                bookView.setText(bookList.get(pos).toString());
                return bookView;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                    // TODO????
            }
        };

        bookQuery.setSuggestionsAdapter(suggestionsAdapter);
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
                //Button setDate = (Button) findViewById(R.id.setDate);

                if (position == 0) {
                    newExchange.exchange_type = Exchange.Type.LOAN;
                    newExchange.loaner_id = Client.userId;
                    newExchange.borrower_id = 0;
                    //setDate.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    newExchange.exchange_type = Exchange.Type.BORROW;
                    newExchange.borrower_id = Client.userId;
                    newExchange.loaner_id = 0;
                    //setDate.setVisibility(View.GONE);
                }
            }
        });
    }

    // Deprecated for now
    // Loaner can pick date that loan will end.
/*    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        final Calendar c = Calendar.getInstance();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            Date date = c.getTime();        // hopefully returns the correct date (TEST ME!)
            newExchange.end_date = date;

           // assert that date is after 'now'
        }
    }*/

    /*public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }*/
}


