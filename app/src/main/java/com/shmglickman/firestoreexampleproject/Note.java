package com.shmglickman.firestoreexampleproject;

/**
 * Created by:   Shmulik Glickman on 08/04/2019, 11:25.
 * Package Name: com.shmglickman.firestoreexampleproject.
 */

public class Note {
    private String title;
    private String description;

    public Note() {
        //public no-arg constructor needed
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
