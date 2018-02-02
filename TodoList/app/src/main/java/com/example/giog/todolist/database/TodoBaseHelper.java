package com.example.giog.todolist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wgoris31 on 2/1/2018.
 */

public class TodoBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "TodoBase.db";


    public TodoBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TodoDbSchema.TodoTable.NAME  + "(" +
                        " _id integer primary key autoincrement, " +
                        TodoDbSchema.TodoTable.Cols.UUID + ", " +
                        TodoDbSchema.TodoTable.Cols.NAME + ", " +
                        TodoDbSchema.TodoTable.Cols.DATE +  " , " +
                        TodoDbSchema.TodoTable.Cols.NOTES +  " , " +
                        TodoDbSchema.TodoTable.Cols.COMPLETED +  " , " +
                        TodoDbSchema.TodoTable.Cols.IMAGE +  ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
