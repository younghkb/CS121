package com.example.jarthur.bookexchange;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

/* Private task to get the cover image from the Goodreads url. */
public class GetImageTask extends AsyncTask<URL, Integer, Drawable> {

    Drawable bookImage;
    Log logger;

    protected Drawable doInBackground(URL... url) {
        try {
            logger.i("GetImageTask", "Getting Image");
            URL myURL = url[0];
            InputStream is = (InputStream) myURL.getContent();
            bookImage = Drawable.createFromStream(is, "Goodreads Image URL");
            return bookImage;
        } catch (Exception e) {
            logger.e("GetImageTask", "exception", e);
        }
        return null;    // TODO better error handling
    }
}