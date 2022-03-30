package com.example.eneamatch;

public class Profile {
    public String uid;
    public String nick;
    public String photoUrl;
    public int gender;
    public String aboutMe;
    public int age;

    // Constructor vacio requerido por Firestore
    public Profile() {}

    public Profile(String uid, String nick, int gender, int age) {
        this.uid = uid;
        this.nick = nick;
        this.gender = gender;
        this.age = age;
    }
}