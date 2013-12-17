package com.jc.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by amclaughlin on 12/16/13.
 */
public class MainActivity extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView mainTasks = (ListView) findViewById(R.id.mainTasks);
//        Log.d("RealContent here", RealContent.ITEMS.toString());

        DBHandler db = new DBHandler(this);
        db.open();
        ArrayList<Task> tasks = db.getTasks();

        TaskAdapter taskListAdapter = new TaskAdapter(this.getApplicationContext(), tasks);

        mainTasks.setAdapter(taskListAdapter);

        mainTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int pos, long ide) {

                String completed = ((Task) arg0.getItemAtPosition(pos)).complete;

                if (completed.equals("true")) {
                    Intent i = new Intent(getApplicationContext(), DoneActivity.class); // creates a new intent i, which is how Android passes information between activities, and defines this intent as a way to navigate to the SecondActivity
                    i.putExtra("position", String.valueOf(pos));
                    i.putExtra("id", ide);
                    i.putExtra("name",((Task) arg0.getItemAtPosition(pos)).name);
                    startActivityForResult(i, 1); // tells Android to make the intent active
                }
                else {
                    Intent i = new Intent(getApplicationContext(), TimeMe.class); // creates a new intent i, which is how Android passes information between activities, and defines this intent as a way to navigate to the SecondActivity
                    i.putExtra("position", String.valueOf(pos));
                    i.putExtra("id", ide);
                    i.putExtra("name",((Task) arg0.getItemAtPosition(pos)).name);
                    startActivityForResult(i, 1); // tells Android to make the intent active
                }

            }

            ;
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
