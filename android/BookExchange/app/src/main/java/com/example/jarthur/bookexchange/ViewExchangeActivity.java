package com.example.jarthur.bookexchange;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import client.Book;
import client.Client;
import client.Exchange;

// Originally 'Status'
public class ViewExchangeActivity extends ActionBarActivity {

    private static Log logger;

    // TODO okay to make these static?
    // Might need to clear them in onFinish()
    private static Exchange myExchange;
    private static Book myBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exchange);

        Intent i = getIntent();
        myExchange = (Exchange) i.getSerializableExtra("exchange");
        myBook = (Book) i.getSerializableExtra("book");

        // Make logo show up in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        Exchange.Status exchangeStatus = myExchange.status;

        // true if the current user is the owner of this particular book
        boolean isOwner = (Client.userId == myExchange.loaner_id);

        // Sets visibilities correctly based on type and status
        // Pages can show book (always), otherPerson, contactInfo, dueDate, finishButton

        View otherPerson = findViewById(R.id.otherPerson);
        View contactInfo = findViewById(R.id.contactInfo);
        View dueDate = findViewById(R.id.dueDate);
        View finishButton = findViewById(R.id.finishButton);
        //View cancelButton = findViewById(R.id.cancelButton);

        switch (exchangeStatus) {

            case INITIAL:
                otherPerson.setVisibility(View.GONE);
                contactInfo.setVisibility(View.GONE);
                if (!isOwner) {     // Borrow request
                    dueDate.setVisibility(View.GONE);
                }
                finishButton.setVisibility(View.GONE);
                break;

            case RESPONSE:   // Same as Initial
                otherPerson.setVisibility(View.GONE);
                contactInfo.setVisibility(View.GONE);
                if (!isOwner) {     // Borrow request
                    dueDate.setVisibility(View.GONE);
                }
                finishButton.setVisibility(View.GONE);
                break;

            case ACCEPTED:
                if (!isOwner) {     // only person owning book can end exchange
                    finishButton.setVisibility(View.GONE);
                }
                break;

            case COMPLETED:
                finishButton.setVisibility(View.GONE);  // already finished
                break;

            default:
                break;
        }

    }

    /* Confirmation for completion or cancellation of an exchange. */
    public static class AlertDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.alert_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES! Or, you know, confirm that yes, the user does
                            // want to do this
                            try {
                                Client.updateExchangeStatus(myExchange.exchange_id, Exchange.Status.COMPLETED);

                            } catch (Exception e) {
                                logger.e("ViewExchangeActivity", "exception", e);
                            }
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            // TODO return to previous activity?
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    public void confirmAlert(View view) {
        DialogFragment newFragment = new AlertDialogFragment();
        newFragment.show(getFragmentManager(), "alert");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
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