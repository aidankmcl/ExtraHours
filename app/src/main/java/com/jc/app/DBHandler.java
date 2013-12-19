package com.jc.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by amclaughlin on 12/15/13.
 */
public class DBHandler {
    private DBModel model;
    private SQLiteDatabase database;

    private String [] allColumns = {
            DBModel.TASK_NAME,
            DBModel.COMPLETE,
            DBModel.TIME,
            DBModel.LENGTH,
            DBModel.WHEN,
            DBModel.BEST,
            DBModel.ALL_TIME,
            DBModel.TOTAL_DONE,
            DBModel.TASK_ID,
    };

    public DBHandler(Context context) {
        model = new DBModel(context);
    }

    public void resetComplete() {
        ArrayList<Task> tasks = getTasks();
        for (int i=0;i<tasks.size();i++) {
            tasks.get(i).complete = "false";
            updateTask(tasks.get(i));
        }
    }

    //Opening the Database (Getting the writable Database)
    public void open(){
        database = model.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    //Update a post
    public void updateTask(Task task){
        deleteTaskByName(task.id);
        addTask(task);
    }

    public ArrayList<String> getAllTaskIds() {
        Cursor cursor = database.query(DBModel.TABLE_NAME, new String[]{DBModel.TASK_ID},null,null,null,null,null,null);
        ArrayList<String> ids = new ArrayList<String>();
        cursor.moveToFirst();
        String CurID;
        while (!cursor.isAfterLast()){
            CurID = cursor.getString(0);
            if (CurID.length() > 16) {
                ids.add(CurID);
            }
            cursor.moveToNext();
        }
        cursor.close();
        if (ids.size() < 1) {
            ids.add("aaa");
        }
//        Log.i("ids", ids.toString());
        return ids;
    }

    public void addTask(Task newTask) {
        ContentValues values = new ContentValues();

        values.put(DBModel.TASK_NAME, newTask.name);
        values.put(DBModel.COMPLETE, newTask.complete);
        values.put(DBModel.LENGTH, newTask.length);
        values.put(DBModel.TIME, newTask.time);
        values.put(DBModel.WHEN, newTask.when);
        values.put(DBModel.BEST, newTask.best);
        values.put(DBModel.ALL_TIME, newTask.allTime);
        values.put(DBModel.TOTAL_DONE, newTask.total);

        this.database.insert(DBModel.TABLE_NAME,null,values);
    }

    //Getting Tasks by Size in descending priority order
    public ArrayList<Task> getTasks(){
        return sweepCursor(
                database.query(DBModel.TABLE_NAME, allColumns, null, null, null, null, DBModel.WHEN));
        }

        public Task getTaskByName(String name){
            return sweepCursor(database.query(DBModel.TABLE_NAME,allColumns,DBModel.TASK_NAME + " like '%" + name + "%'", null,null,null,null)).get(0);
        }

        //Delete Tasks by ID
        public void deleteTaskByName(String name){
            database.delete(DBModel.TABLE_NAME, DBModel.TASK_NAME + " like '%" + name + "%'", null);
        }

    //Get Tasks from Cursor
    public ArrayList<Task> sweepCursor (Cursor cursor) {
        ArrayList<Task> tasks = new ArrayList<Task>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            tasks.add(cursorToPost(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return tasks;
    }

    //Get Tasks from Cursor
    public Task cursorToPost(Cursor cursor){
        Task task = new Task(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3),
                cursor.getInt(4),
                cursor.getInt(5),
                cursor.getInt(6),
                cursor.getInt(7)
        );

        task.setId(cursor.getString(8));
//        Log.i ("id", cursor.getString(5));
        return task;
    }
}
