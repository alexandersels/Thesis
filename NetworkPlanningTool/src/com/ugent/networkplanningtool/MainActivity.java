package com.ugent.networkplanningtool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

/**
 * Class containing the start screen of the application
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    /**
     * Open the planner view
     * @param view the pressed button
     */
    public void openPlanner(View view) {
        Intent planningIntent = new Intent(this, PlannerActivity.class);
        startActivity(planningIntent);
    }

    /**
     * Open the localisation view
     * @param view the pressed button
     */
    public void openLocalisation(View view) {
        Intent localisationIntent = new Intent(this, LocalisationActivity.class);
        startActivity(localisationIntent);
    }
}
