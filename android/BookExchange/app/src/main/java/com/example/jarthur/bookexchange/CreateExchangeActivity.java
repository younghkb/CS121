package com.example.jarthur.bookexchange;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import client.Book;
import client.Client;
import client.Exchange;

// Currently using java.util.Date, NOT java.sql.Date

public class CreateExchangeActivity extends ActionBarActivity {

    // Gives the servers the parameters for a new exchange, then queries to get it

    // hopefully okay to make this static
    private static Exchange newExchange = new Exchange();

    private static Book newBook = new Book();

    private static Log logger;

    private static DialogInterface.OnClickListener createListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exchange);

        // Make logo show up in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // TODO MAKE THIS WORK
        // http://stackoverflow.com/questions/5099814/knowing-when-edit-text-is-done-being-edited
        EditText bookQuery = (EditText) findViewById(R.id.bookQueryBox);

        bookQuery.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    selectBook();
                    logger.i("CreateExchangeActivity", "selecting book");
                }
            }
        });


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
                    //setDate.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    newExchange.exchange_type = Exchange.Type.BORROW;
                    //setDate.setVisibility(View.GONE);
                }
            }
        });

        createListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                logger.i("CreateExchangeActivity", "Create Exchange button has been clicked");

                returnToHomeScreen();
                /*Toast t = Toast.makeText(getActivity().getApplicationContext(),
                        "", Toast.LENGTH_SHORT);

                try {
                    Client.createBook(newBook);
                    Client.createExchange(newExchange);
                    t.setText("Exchange created!");
                    t.show();

                } catch (Exception e) {
                    logger.e("Confirm Dialog", "exception", e);
                    t.setText("Error! :(");
                    t.show();
                }*/
            }
        };
    }

    private void returnToHomeScreen() {
        Intent i = new Intent(this, HomeScreen.class);
        startActivity(i);
    }

    private void selectBook() {

        // Get the user input text with leading or ending whitespace removed
        EditText bookQuery = (EditText) findViewById(R.id.bookQueryBox);
        String queryText = bookQuery.getText().toString().trim();
        if (queryText.isEmpty()) {
            // TODO give visual feedback
        }
        else {
            try {
                List<Book> booksFound = Client.searchBook(queryText);
                // TODO put in stuff
                newBook = booksFound.get(0);       // TODO make user choose

                newExchange.book_title = newBook.book_title;
                newExchange.book_id = newBook.book_id;

            }
            catch (Exception e) {
                logger.e("CreateExchangeActivity", "exception in selectBook()", e);
            }
        }

        // We need to require that the user choose an exchange type
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

           // TODO assert that date is after 'now'
        }
    }*/

    /*public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }*/
}


