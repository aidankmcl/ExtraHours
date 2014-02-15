package com.jc.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by amclaughlin on 12/16/13.
 */

public class MakeTask extends Activity {
    DBHandler db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize buttons
        final ImageButton addToSQL = (ImageButton) findViewById(R.id.addTask);
        final SeekBar whenIsIt = (SeekBar) findViewById(R.id.whenIsIt);
        final ImageView priority = (ImageView) findViewById(R.id.imageView);
        TextView TitleTaskMake = (TextView) findViewById(R.id.newTaskTitle);

        //Setting font
        Typeface tf = Typeface.createFromAsset(getAssets(), "missiongl.otf");
        TitleTaskMake.setTypeface(tf);

        //Priority image button had issues initially, kept this
        //piece of debugging to be safe.
        priority.setVisibility(View.VISIBLE);

        //Initializing Database
        db = new DBHandler(this);
        db.open();

        //Setting up 'CREATE' button at bottom of screen
        addToSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputTaskName = (EditText) findViewById(R.id.inputTaskName);
                ArrayList<Task> tasks = db.getTasks();
                
                //Preparing data for DB
                String skip = "false"; //We set skip to 'true' on tasks that we don't want in DB
                String name = inputTaskName.getText().toString();
                Integer when = whenIsIt.getProgress();

                //Checks to see if you're about to use a name that's already taken
                for (int x = 0; x<tasks.size(); x++) {
                    if (name.equals(tasks.get(x).name)) {
                        Toast.makeText(getApplicationContext(), "Please use unique names", Toast.LENGTH_SHORT).show();
                        skip = "true";  //We don't want repeat names
                        break;
                    }
                }

                //
                if (name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Choose a name!", Toast.LENGTH_SHORT).show();
                    skip = "true";  // Doesn't enter empty named task
                }

                //Adding to DB
                if (skip.equals("false")) {
                    // The 100000 number is so that 'Best Time' starts out huge and goes down
                    Task task = new Task(name,"false","none",0,when,100000,0,0); 
                    task.setId("");
                    db.addTask(task);

                    //Goes back to main screen via MainActivity
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }

                if (skip.equals("true")) {
                    //Empties input text field if no task is added
                    inputTaskName.setText("");
                }
            }
        });
    }

    //Setting upwards navigation on actionbar
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
