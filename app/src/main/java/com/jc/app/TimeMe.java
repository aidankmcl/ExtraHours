package com.jc.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by amclaughlin on 12/17/13.
 */
public class TimeMe extends Activity {
    private Chronometer chronometer;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.timer);

        DBHandler db = new DBHandler(this);
        db.open();
        ArrayList<Task> tasks = db.getTasks();

        Bundle extras = getIntent().getExtras();
        String value = null;
        if (extras != null) {
            value = extras.getString("new_variable_name");
            Log.d("extrasss", value);
        }

        String booya = tasks.get(Integer.parseInt(value)).toString();

        Log.d("Please work you can do it", booya);
        TextView timerSubject = (TextView) findViewById(R.id.timerSubject);
        timerSubject.setText(booya);

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        ImageButton start_button = (ImageButton) findViewById(R.id.start_button);
        ImageButton stop_button = (ImageButton) findViewById(R.id.stop_button);
        ImageButton done_button = (ImageButton) findViewById(R.id.done_button);

        start_button.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                int stoppedMilliseconds = 0;

                String chronoText = chronometer.getText().toString();
                String array[] = chronoText.split(":");
                if (array.length == 2) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                            + Integer.parseInt(array[1]) * 1000;
                } else if (array.length == 3) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                            + Integer.parseInt(array[1]) * 60 * 1000
                            + Integer.parseInt(array[2]) * 1000;
                }

                chronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                chronometer.start();
            }
        });

        stop_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                chronometer.stop();
            }
        });

        done_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                chronometer.stop();

                String doneText = chronometer.getText().toString();
                Log.d("donetime", doneText); //change this to add to SQL

                int seconds = 0;
                String array[] = doneText.split(":");
                if (array.length == 2) {
                    seconds = Integer.parseInt(array[0]) * 60
                            + Integer.parseInt(array[1]);
                } else if (array.length == 3) {
                    seconds = Integer.parseInt(array[0]) * 60 * 60
                            + Integer.parseInt(array[1]) * 60
                            + Integer.parseInt(array[2]);
                }
                Log.d("seconds", Integer.toString(seconds)); //change this to add SQL

                //when activity is done go back to main screen
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

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
}
