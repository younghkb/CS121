package com.example.jarthur.bookexchange;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.InputStream;
import java.net.URL;


public class BookDetails extends ActionBarActivity {
    // controls whether we see a book currently in an exchange or that is just available
    int type = 1;
    final int AVAILABLE = 0;
    final int CURRENT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // TODO Call database and set type

        // Sets visibilities correctly based on type
        // hides the request button from the details of a book currently
        // involved in an exchange
        switch (type) {
            // Does not show request button, owner or possible loan period
            case CURRENT:
                View b = findViewById(R.id.requestButton);
                b.setVisibility(View.GONE);

                View c = findViewById(R.id.loanPeriod);
                c.setVisibility(View.GONE);

                View d = findViewById(R.id.owner);
                d.setVisibility(View.GONE);
                break;

            // Shows everything
            case AVAILABLE:
            default:
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_details, menu);
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

    // from: http://stackoverflow.com/questions/6407324/how-to-get-image-from-url-in-android
    // theoretically, this will help in getting the image from the goodreads data
    // i have my doubts
    //TODO make this work gosh darn it
    public static Drawable imageFromUrl(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            //is.
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
