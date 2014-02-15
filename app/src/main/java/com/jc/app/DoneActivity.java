package com.jc.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alison on 12/17/13.
 */
public class DoneActivity extends Activity {
    Task var;
    String name;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_done);

        Bundle extras = getIntent().getExtras();
        String value = null;
        int pos;

        final String ide;
        if (extras != null) {
            value = extras.getString("position");
            pos = Integer.parseInt(value);
            ide = extras.getString("id");
            name = extras.getString("name");
        } else {
            ide = "0";
        }
        
        // Initializing SQLiteHelper Database
        final DBHandler db = new DBHandler(this);
        db.open();
        var = db.getTaskByName(name);
        ArrayList<Task> tasks = db.getTasks();

        // Sets Task Name
        String taskName = tasks.get(Integer.parseInt(value)).toString();
        TextView taskSubject = (TextView) findViewById(R.id.DoneTaskTitle);
        taskSubject.setText(taskName);

        //Doing math for displaying average time
        int totalTime = var.allTime;
        int totalTries = var.total;
        int average = totalTime/totalTries
        
        //Displaying all the info by calling TextViews and populating them with times
        TextView averageView = (TextView) findViewById(R.id.average);
        averageView.setText(intToTime(average));
        
        TextView bestView = (TextView) findViewById(R.id.best);
        bestView.setText(intToTime(var.best));
        
        TextView todayView = (TextView) findViewById(R.id.today);
        todayView.setText(intToTime(Integer.parseInt(var.length)));

        //Initializing TextViews, these ones have string reference
        //in the XML
        TextView todayBelow = (TextView) findViewById(R.id.displayTodayBelow);
        TextView averageBelow = (TextView) findViewById(R.id.displayAverageBelow);
        TextView bestBelow = (TextView) findViewById(R.id.displayBestBelow);

        //Fonts need to be applied to the TextViews programatically
        Typeface tf = Typeface.createFromAsset(getAssets(), "missiongl.otf");
        bestView.setTypeface(tf);
        todayView.setTypeface(tf);
        averageView.setTypeface(tf);
        todayBelow.setTypeface(tf);
        averageBelow.setTypeface(tf);
        bestBelow.setTypeface(tf);
        taskSubject.setTypeface(tf);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Time is stored as integers in our Database so it's important 
    //to turn them back into more readable time when being displayed
    public static String intToTime(int time) {
        int seconds = time % 60;
        String secondsString = String.valueOf(seconds);
        if (secondsString.length() == 1) {
            secondsString = "0" + secondsString;
        }
        int minutes = time / 60;
        String minutesString = String.valueOf(minutes);
        if (minutesString.length() == 1) {
            minutesString = "0" + minutesString;
        }
        if (minutes >= 60) {
            minutes = time % 60;
            minutesString = String.valueOf(minutes);
            if (minutesString.length() == 1) {
                minutesString = "0" + minutesString;
            }
            int hours = time / 60;
            String hoursString = String.valueOf(hours);
            String timeString = hoursString + ":" + minutesString + ":" + secondsString;
        }
        String timeString = minutesString + ":" + secondsString;

        return timeString;
    }
}
