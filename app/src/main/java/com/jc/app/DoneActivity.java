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

        final String ide;
        if (extras != null) {
            value = extras.getString("position");
            ide = extras.getString("id");
            name = extras.getString("name");
            Log.d("extrasss", name);
        } else {
            ide = "0";
        }

        final DBHandler db = new DBHandler(this);
        db.open();
        var = db.getTaskByName(name);
        ArrayList<Task> tasks = db.getTasks();


        String taskName = tasks.get(Integer.parseInt(value)).toString();
        //Log.d("Please work you can do it", booya);
        TextView timerSubject = (TextView) findViewById(R.id.textView);

        timerSubject.setText(taskName);

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
