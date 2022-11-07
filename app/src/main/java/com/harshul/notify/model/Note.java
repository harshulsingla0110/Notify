package com.harshul.notify.model;

import com.google.firebase.Timestamp;

public class Note {
    private String title;
    private String note;
    private String userId;
    private Timestamp timeAdded;
    private String userName;

    public Note() {
    }

    public Note(String title, String note, String userId, Timestamp timeAdded, String userName) {
        this.title = title;
        this.note = note;
        this.userId = userId;
        this.timeAdded = timeAdded;
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
