package com.shmglickman.firestoreexampleproject;

import com.google.firebase.firestore.Exclude;

/**
 * Created by:   Shmulik Glickman on 08/04/2019, 11:25.
 * Package Name: com.shmglickman.firestoreexampleproject.
 */

public class Note {
    private String documentId;
    private String title;
    private String description;
    private int priority;

    public Note() {
        //public no-arg constructor needed
    }


    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
