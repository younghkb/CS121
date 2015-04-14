package hmc.cs121.jarthur.bookexchange;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
    public void openCreateLoan(View view) {
        // Go to the Loan page
        Intent intent = new Intent(this, CreateLoan.class);
        startActivity(intent);
    }

    public void openStatus(View view) {
        // Go to the Status page
        Intent intent = new Intent(this, Status.class);
        startActivity(intent);
    }

    public void openBookDetails(View view) {
        // Go to the Status page
        Intent intent = new Intent(this, BookDetails.class);
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
