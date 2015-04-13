package com.example.jarthur.bookexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class HomeScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Make logo show up in action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    // code from: http://blogs.technicise.com/
    // how-to-open-a-new-android-activity-from-another-activity-on-clicking-a-button/
    public void openCreateExchangeActivity(View view) {
        // Go to the Loan page
        Intent intent = new Intent(this, CreateExchangeActivity.class);
        startActivity(intent);
    }

    public void openViewExchangeActivity(View view) {
        // TODO make sure ViewExchangeActivity gets the Exchange
        // Go to the ViewExchangeActivity page that displays the state of a particular exchange
        // TODO send Exchange details with Intent
        Intent intent = new Intent(this, ViewExchangeActivity.class);
        startActivity(intent);
    }

    public void openBookDetailsActivity(View view) {
        // Go to the Book Details page
        // TODO send Book details with Intent
        Intent intent = new Intent(this, BookDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
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
