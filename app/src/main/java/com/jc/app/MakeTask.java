package com.jc.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

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

        db = new DBHandler(this);
        db.open();

        addToSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputTaskName = (EditText) findViewById(R.id.inputTaskName);

                String name = inputTaskName.getText().toString();
                Integer when = whenIsIt.getProgress();

                Task task = new Task(name,"false","none",0,when,100000,0,0);
                task.setId("");
                db.addTask(task);

//                Log.d("where you at, God?", task.name.toString());
//                Log.d("size of database", meow.toString());


                //go back to the last thing you looked at
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(in);
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
