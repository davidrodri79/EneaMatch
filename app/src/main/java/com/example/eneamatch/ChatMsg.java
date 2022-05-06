package com.example.eneamatch;

public class ChatMsg {
    public String uid;
    public String text;
    public long timestamp;

    // Constructor vacio requerido por Firestore
    public ChatMsg() {}

    public ChatMsg(String uid, String text, long timestamp) {
        this.uid = uid;
        this.text = text;
        this.timestamp = timestamp;
    }
}
