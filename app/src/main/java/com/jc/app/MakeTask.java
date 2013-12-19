package com.jc.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
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

        final ImageButton addToSQL = (ImageButton) findViewById(R.id.addTask);
        final SeekBar whenIsIt = (SeekBar) findViewById(R.id.whenIsIt);
        final ImageView priority = (ImageView) findViewById(R.id.imageView);
        priority.setVisibility(View.VISIBLE);

        db = new DBHandler(this);
        db.open();

        addToSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputTaskName = (EditText) findViewById(R.id.inputTaskName);
                ArrayList<Task> tasks = db.getTasks();
                String skip = "false";

                String name = inputTaskName.getText().toString();
                Integer when = whenIsIt.getProgress();

                for (int x = 0; x<tasks.size(); x++) {
                    if (name.equals(tasks.get(x).name)) {
                        Toast.makeText(getApplicationContext(), "Please use unique names", Toast.LENGTH_SHORT).show();
                        skip = "true";
                        break;
                    }
                }
                if (skip.equals("false")) {
                    Task task = new Task(name,"false","none",0,when,100000,0,0);
                    task.setId("");
                    db.addTask(task);

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }

                if (skip.equals("true")) {
                    inputTaskName.setText("");
//                    Intent i = new Intent(getApplicationContext(), MakeTask.class);
//                    startActivity(i);
                }
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
