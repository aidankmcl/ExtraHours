package com.jc.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by amclaughlin on 12/16/13.
 */
public class MainActivity extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView asio = (TextView) findViewById(R.id.hintText);

        Typeface tf = Typeface.createFromAsset(getAssets(), "missiongl.otf");
        asio.setTypeface(tf);

        final ListView mainTasks = (ListView) findViewById(R.id.mainTasks);

        final DBHandler db = new DBHandler(this);
        db.open();

        final ArrayList<Task> tasks = db.getTasks();

        final TaskAdapter taskListAdapter = new TaskAdapter(this.getApplicationContext(), tasks);

        TextView message = (TextView) findViewById(R.id.hintText);
        TextView totalTime = (TextView) findViewById(R.id.totalTimeText);
        if (taskListAdapter.isEmpty()){
            message.setVisibility(View.VISIBLE);
            totalTime.setVisibility(View.GONE);
        }
        else {
            message.setVisibility(View.GONE);
            totalTime.setVisibility(View.VISIBLE);
        }

        mainTasks.setAdapter(taskListAdapter);

        String estimatedTime;
        Integer timeForAllTasks = 0;
        db.open();
        ArrayList<Task> getTimeArrayList = db.getTasks();
        for (int x=0;x<getTimeArrayList.size();x++) {
            Task counting = tasks.get(x);
            if (counting.total > 0) {
            int avgIndividual = counting.allTime/counting.total;
            timeForAllTasks += avgIndividual;
            }
        }

        estimatedTime = DoneActivity.intToTime(timeForAllTasks);

        totalTime.setText("Total   "+ estimatedTime);
        totalTime.setTypeface(tf);
        totalTime.setTextColor(Color.parseColor("#ffffff"));
        totalTime.setBackgroundColor(Color.parseColor("#14b8db"));


        mainTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int pos, long ide) {

                String completed = ((Task) arg0.getItemAtPosition(pos)).complete;

                if (completed.equals("true")) {
                    Intent i = new Intent(getApplicationContext(), DoneActivity.class); // creates a new intent i, which is how Android passes information between activities, and defines this intent as a way to navigate to the SecondActivity
                    i.putExtra("position", String.valueOf(pos));
                    i.putExtra("id", ide);
                    i.putExtra("name", ((Task) arg0.getItemAtPosition(pos)).name);
                    startActivityForResult(i, 1); // tells Android to make the intent active
                } else {
                    Intent i = new Intent(getApplicationContext(), TimeMe.class); // creates a new intent i, which is how Android passes information between activities, and defines this intent as a way to navigate to the SecondActivity
                    i.putExtra("position", String.valueOf(pos));
                    i.putExtra("id", ide);
                    i.putExtra("name", ((Task) arg0.getItemAtPosition(pos)).name);
                    startActivityForResult(i, 1); // tells Android to make the intent active
                }

            }

            ;
        });

        mainTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(final AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Erase this task?");
                
                // Set up the buttons
                builder.setPositiveButton("OK", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = ((Task) arg0.getItemAtPosition(pos)).name;
                                db.deleteTaskByName(title);
                                taskListAdapter.notifyDataSetChanged();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivityForResult(i, 1);
                            }
                        });
                builder.setNegativeButton("NO!", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.toNewTask:
                Intent i = new Intent(getApplicationContext(), MakeTask.class);
                startActivityForResult(i, 1);
                return true;
            case R.id.resetAll:
                DBHandler db = new DBHandler(this);
                db.open();
                ArrayList<Task> tasks = db.getTasks();
                for (int x=0;x<tasks.size();x++) {
                    Task setCompFalse = tasks.get(x);
                    setCompFalse.complete = "false";
                    db.deleteTaskByName(tasks.get(x).name);
                    db.addTask(setCompFalse);
                    Intent resetActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(resetActivity);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
