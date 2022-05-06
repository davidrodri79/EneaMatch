package com.example.eneamatch;

public class ChatMsg {
    public String uid;
    public String text;

    // Constructor vacio requerido por Firestore
    public ChatMsg() {}

    public ChatMsg(String uid, String text) {
        this.uid = uid;
        this.text = text;
    }
}
