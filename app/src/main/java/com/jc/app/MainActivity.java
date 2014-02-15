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

        //Hint text that appears when you have no tasks created
        TextView hint = (TextView) findViewById(R.id.hintText);
        Typeface tf = Typeface.createFromAsset(getAssets(), "missiongl.otf");
        hint.setTypeface(tf);

        //Creating main ListView
        final ListView mainTasks = (ListView) findViewById(R.id.mainTasks);

        //Initializing SQLite Datahase Hanndler
        final DBHandler db = new DBHandler(this);
        db.open();
        //grabbing our Tasks from memory
        final ArrayList<Task> tasks = db.getTasks();
        //Creating taskListAdapter to populate mainTasks with 
        //tasks from SQL database
        final TaskAdapter taskListAdapter = new TaskAdapter(this.getApplicationContext(), tasks);

        TextView totalTime = (TextView) findViewById(R.id.totalTimeText);
        if (taskListAdapter.isEmpty()){
            hint.setVisibility(View.VISIBLE);
            totalTime.setVisibility(View.GONE);
        }
        else {
            hint.setVisibility(View.GONE);
            totalTime.setVisibility(View.VISIBLE);
        }

        //Setting adapter, populating ListView
        mainTasks.setAdapter(taskListAdapter);

        String estimatedTime;
        Integer timeForAllTasks = 0;
        db.open();
        
        //Getting total for Overall Average that sits at
        //the bottom of the Activity Main
        for (int x=0;x<tasks.size();x++) {
            Task counting = tasks.get(x);
            if (counting.total > 0) {
                int avgIndividual = counting.allTime/counting.total;
                timeForAllTasks += avgIndividual;
            }
        }

        //Our DB works with seconds and we need to convert that
        //back into readable time.  Then we set the Text at the
        //bottom to show sum of the averages.
        estimatedTime = DoneActivity.intToTime(timeForAllTasks);
        totalTime.setText("Total   "+ estimatedTime);
        totalTime.setTypeface(tf);


        //Navigation handling:
        // If you click on a task that hasn't been completed, this
        // bit of code passes extra information along with the intent
        // which is its position in the list, the id, and the name,
        // and that's all passed to the timer page via (TimeMe)
        // If you select one that has been completed (which means it
        // should be green) it passes the same extra info with the
        // intent and loads DoneActivity.
        //Note: below this is done in the opposite order as explained.
        mainTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int pos, long ide) {

                String completed = ((Task) arg0.getItemAtPosition(pos)).complete;

                if (completed.equals("true")) {
                    Intent i = new Intent(getApplicationContext(), DoneActivity.class); 
                    i.putExtra("position", String.valueOf(pos));
                    i.putExtra("id", ide);
                    i.putExtra("name", ((Task) arg0.getItemAtPosition(pos)).name);
                    startActivityForResult(i, 1); // tells Android to make the intent active
                } else {
                    Intent i = new Intent(getApplicationContext(), TimeMe.class); 
                    i.putExtra("position", String.valueOf(pos));
                    i.putExtra("id", ide);
                    i.putExtra("name", ((Task) arg0.getItemAtPosition(pos)).name);
                    startActivityForResult(i, 1); 
                }

            }

            ;
        });
        
        //Long press to delete task
        mainTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(final AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                //Setting up dialog box that pops up with long click
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Erase this task?");
                
                // Set up the buttons, OK deletes from database, NO! does
                // nothing
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
