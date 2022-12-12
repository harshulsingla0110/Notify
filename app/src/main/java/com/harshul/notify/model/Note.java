package com.harshul.notify.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Note implements Parcelable {
    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
    private String title;
    private String note;
    private String userId;
    private Timestamp timeAdded;
    private String userName;
    private String documentId;

    public Note() {
    }

    public Note(String title, String note, String userId, Timestamp timeAdded, String userName, String documentId) {
        this.title = title;
        this.note = note;
        this.userId = userId;
        this.timeAdded = timeAdded;
        this.userName = userName;
        this.documentId = documentId;
    }

    protected Note(Parcel in) {
        title = in.readString();
        note = in.readString();
        userId = in.readString();
        timeAdded = in.readParcelable(Timestamp.class.getClassLoader());
        userName = in.readString();
        documentId = in.readString();
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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(note);
        parcel.writeString(userId);
        parcel.writeParcelable(timeAdded, i);
        parcel.writeString(userName);
        parcel.writeString(documentId);
    }
}
