package com.jc.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by amclaughlin on 12/15/13.
 */
public class DBModel extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "tasks";
    public static final String TASK_NAME = "name";
    public static final String COMPLETE = "done";
    public static final String LENGTH = "time";
    public static final String TIME = "seconds";
    public static final String WHEN = "day";
    public static final String TASK_ID = "id";

    public static final String DB_NAME = "EH";
    public static final Integer DB_VERSION = 1;

    private static final String DB_CREATE = "create table "
            + TABLE_NAME + "("
            + TASK_NAME + " TEXT NOT NULL, "
            + COMPLETE + " TEXT NOT NULL, "
            + LENGTH + " TEXT NOT NULL, "
            + TIME + " INTEGER NOT NULL, "
            + WHEN + " INTEGER NOT NULL, "
            + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT);";

    public DBModel(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(DBModel.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);

    }
}
