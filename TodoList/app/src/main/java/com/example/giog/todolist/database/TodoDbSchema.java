package com.example.giog.todolist.database;

/**
 * Created by wgoris31 on 1/31/2018.
 */

public class TodoDbSchema {
    public static final class TodoTable {
        public static final String NAME = "todo";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String DATE = "date";
            public static final String NAME = "name";
            public static final String NOTES = "notes";
            public static final String COMPLETED = "completed";
            public static final String IMAGE = "image";
        }
    }
}
