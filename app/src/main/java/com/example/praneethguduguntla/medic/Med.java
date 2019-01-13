package com.example.praneethguduguntla.medic;

import java.util.ArrayList;

public class Med {
    private String text; // message body
    private ArrayList<String> data; // data of the user that sent this message
    private boolean belongsToCurrentUser; // is this message sent by us?

    public Med(String text, ArrayList<String> data, boolean belongsToCurrentUser) {
        this.text = text;
        this.data = data;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}
