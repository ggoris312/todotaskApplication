package com.example.giog.todolist.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by wgoris31 on 1/31/2018.
 */

public class TodoModel {
    private UUID mId;
    private Date mDate;
    private String mName = "";
    private String mNotes = "";
    private Boolean mCompleted = false;
    private String mImage = "";

    public TodoModel(){
        this(UUID.randomUUID());
    }

    public TodoModel(UUID id){
        this.mId = id;
        this.mDate = new Date();
    }

    public UUID getmId() {
        return mId;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmNotes() {
        return mNotes;
    }

    public void setmNotes(String mNotes) {
        this.mNotes = mNotes;
    }

    public Boolean getmCompleted() {
        return mCompleted;
    }

    public void setmCompleted(Boolean mCompleted) {
        this.mCompleted = mCompleted;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}
