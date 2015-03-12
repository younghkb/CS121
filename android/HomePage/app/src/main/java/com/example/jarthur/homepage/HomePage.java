package com.example.jarthur.homepage;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnClickListener;

public class HomePage extends ActionBarActivity {

    Button button;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);



      //  ImageView logo = (ImageView) findViewById(R.id.logo);
      //  logo.setImageResource(R.drawable.logo);
      //  addListenerOnButton();
    }

    // public View onCreateView(LayoutInflater inflater, ViewGroup container)

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
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
//
//    public void addListenerOnButton() {
//
//        image = (ImageView) findViewById(R.id.imageView1);
//
//        button = (Button) findViewById(R.id.btnChangeImage);
//        button.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                image.setImageResource(R.drawable.logo);
//            }
//
//        });
//
//    }

  //  public void onActivityCreated(Bundle savedInstanceState) {
     //   super.onActivityCreated(savedInstanceState);
       // final int resId = ImageDetailActivity.imageResIds[mImageNum];
   //    imageLogo = (ImageView)
   //    imageLogo.setImageResources(resId);
  //  }

}
