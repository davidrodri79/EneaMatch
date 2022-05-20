package com.example.eneamatch;

public class ChatEntry {

    public String uid;
    public Profile companion;
    public String text;
    public long timestamp;

    // Constructor vacio requerido por Firestore
    public ChatEntry() {}

    public ChatEntry(String uid, Profile companion, String text, long timestamp) {
        this.uid = uid;
        this.companion = companion;
        this.text = text;
        this.timestamp = timestamp;
    }
}
