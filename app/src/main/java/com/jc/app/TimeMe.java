package com.jc.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amclaughlin on 12/17/13.
 */
public class TimeMe extends Activity {
    Task var;
    String name;
    private Chronometer chronometer;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.timer);

        Bundle extras = getIntent().getExtras();
        String value = null;

        final String ide;
        if (extras != null) {
            value = extras.getString("position");
            ide = extras.getString("id");
            name = extras.getString("name");
        } else {
            ide = "0";
        }

        final DBHandler db = new DBHandler(this);
        db.open();
        var = db.getTaskByName(name);
        ArrayList<Task> tasks = db.getTasks();


        String nameOfTask = tasks.get(Integer.parseInt(value)).toString();
        TextView timerSubject = (TextView) findViewById(R.id.timerSubject);

        Typeface tf = Typeface.createFromAsset(getAssets(), "missiongl.otf");
        timerSubject.setText(nameOfTask);
        timerSubject.setTypeface(tf);
        timerSubject.setTextColor(Color.parseColor("#14b8db"));

        final Map<String, Integer> map = new HashMap<String, Integer>();
        final String[] timing = {"false"};


        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setTextColor(Color.parseColor("#14b8db"));
        chronometer.setTypeface(tf);

        chronometer.setBase(SystemClock.elapsedRealtime());
        final ImageButton start_button = (ImageButton) findViewById(R.id.start_button);
        start_button.setImageResource(R.drawable.play_wide);
//        ImageButton stop_button = (ImageButton) findViewById(R.id.stop_button);
        ImageButton done_button = (ImageButton) findViewById(R.id.done_button);

        start_button.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                if (timing[0].equals("false")) {
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
                start_button.setImageResource(R.drawable.pause_wide);
                timing[0] = "true";

                } else {
                    chronometer.stop();
                    start_button.setImageResource(R.drawable.play_wide);
                    timing[0] = "false";
                }

            }
        });
//
//        stop_button.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View view) {
//                chronometer.stop();
//            }
//        });

        done_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                chronometer.stop();

                String doneText = chronometer.getText().toString();

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

                db.deleteTaskByName(name);
                var.length = doneText;
                var.time = seconds;
                var.complete = "true";
                var.allTime = var.allTime + seconds;
                var.total += 1;

                if (seconds < var.best) {
                    var.best = seconds;
                }

                db.addTask(var);

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
