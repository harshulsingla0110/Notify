package com.harshul.notify.util;

import android.app.Application;

public class NotesApi extends Application {

    private static NotesApi instance;
    private String userName;
    private String userId;

    public NotesApi() {
    }

    public static NotesApi getInstance() {
        if (instance == null)
            instance = new NotesApi();
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
