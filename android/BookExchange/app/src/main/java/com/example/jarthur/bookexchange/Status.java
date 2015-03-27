package com.example.jarthur.bookexchange;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Status extends ActionBarActivity {

    // controls whether we see request, borrow, or loan
    int type = 0;
    final int REQUEST = 0;
    final int BORROW = 1;
    final int LOAN = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // Make logo show up in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // TODO Call database and set type

        // Sets visibilities correctly based on type
        // Pages can show book, otherPerson, contactInfo, dueDate, finishButton
        switch(type){
            // Does not show dueDate
            case REQUEST:
                View b = findViewById(R.id.dueDate);
                b.setVisibility(View.GONE);
                break;

            // Does not show finishButton
            case BORROW:
                View c = findViewById(R.id.finishButton);
                c.setVisibility(View.GONE);
                break;

            // Shows everything
            case LOAN:
            default:
                break;
        }


    }

    //TODO figure out how to really write this show warning function
    public void showWarning() {
        DialogFragment dialog = new YesNoDialog();
        Bundle args = new Bundle();
        args.putString("title", "T");
        args.putString("message", "M");
        dialog.setArguments(args);
        //dialog.setTargetFragment(this, YES_NO_CALL);
        dialog.show(getFragmentManager(), "tag");
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
