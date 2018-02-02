package com.example.giog.todolist.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.giog.todolist.database.TodoBaseHelper;
import com.example.giog.todolist.database.TodoCursorWrapper;
import com.example.giog.todolist.database.TodoDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by wgoris31 on 1/31/2018.
 */

public class TodoLab {
    private static TodoLab sTodoLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static TodoLab get(Context context){
        if(sTodoLab == null){
            sTodoLab = new TodoLab(context);
        }
        return sTodoLab;
    }

    public TodoLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new TodoBaseHelper(mContext)
                .getWritableDatabase();
    }

    public List<TodoModel> getTodoList() {
        List<TodoModel> todoList = new ArrayList<>();
        TodoCursorWrapper cursor = queryTodos(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                todoList.add(cursor.getTodo());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return todoList;
    }

    public TodoModel getTodo(UUID id) {
        TodoCursorWrapper cursor = queryTodos(
                TodoDbSchema.TodoTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getTodo();
        }
        finally {
            cursor.close();
        }
    }

    public void updateTodo(TodoModel todoModel) {
        String uuidString = todoModel.getmId().toString();
        ContentValues values = getContentValues(todoModel);

        mDatabase.update(TodoDbSchema.TodoTable.NAME, values,
                TodoDbSchema.TodoTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }


    private static ContentValues getContentValues(TodoModel todoModel) {
        ContentValues values = new ContentValues();
        values.put(TodoDbSchema.TodoTable.Cols.UUID,
                todoModel.getmId().toString());

        values.put(TodoDbSchema.TodoTable.Cols.NAME,
                todoModel.getmName());

        values.put(TodoDbSchema.TodoTable.Cols.DATE,
                todoModel.getmDate().getTime());

        values.put(TodoDbSchema.TodoTable.Cols.NOTES,
                todoModel.getmNotes());

        values.put(TodoDbSchema.TodoTable.Cols.COMPLETED,
                todoModel.getmCompleted());

        values.put(TodoDbSchema.TodoTable.Cols.IMAGE,
                todoModel.getmImage());

        return values;
    }

    private TodoCursorWrapper queryTodos(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TodoDbSchema.TodoTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return  new TodoCursorWrapper(cursor);
    }

    public void addTimer(TodoModel t){
        ContentValues values = getContentValues(t);
        mDatabase.insert(TodoDbSchema.TodoTable.NAME, null, values);
    }

    public void eraseTimer(TodoModel todoModel) {
        mDatabase.delete(TodoDbSchema.TodoTable.NAME, TodoDbSchema.TodoTable.Cols.UUID + " = '"  + todoModel.getmId() + "'", null);
    }

}
