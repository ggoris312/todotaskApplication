package com.example.giog.todolist.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.giog.todolist.model.TodoModel;

import java.util.Date;
import java.util.UUID;

/**
 * Created by wgoris31 on 1/31/2018.
 */

public class TodoCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public TodoCursorWrapper(Cursor cursor) {
        super(cursor);
    }


    private String mImage;
    public TodoModel getTodo(){
        String uuidString = getString(getColumnIndex(TodoDbSchema.TodoTable.Cols.UUID));
        String name = getString(getColumnIndex(TodoDbSchema.TodoTable.Cols.NAME));
        long date = getLong(getColumnIndex(TodoDbSchema.TodoTable.Cols.DATE));
        String notes = getString(getColumnIndex(TodoDbSchema.TodoTable.Cols.NOTES));
        boolean completed = getInt(getColumnIndex(TodoDbSchema.TodoTable.Cols.COMPLETED)) > 0;
        String image = getString(getColumnIndex(TodoDbSchema.TodoTable.Cols.IMAGE));

        TodoModel mModel = new TodoModel(UUID.fromString(uuidString));
        mModel.setmName(name);
        mModel.setmDate(new Date(date));
        mModel.setmNotes(notes);
        mModel.setmCompleted(completed);
        mModel.setmImage(image);
        return mModel;

    }
}
