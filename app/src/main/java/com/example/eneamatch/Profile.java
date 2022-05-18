package com.example.eneamatch;

import java.util.List;

public class Profile {

    public static final int PROFILE_NUM_PICTURES = 6;

    public String uid;
    public String nick;
    public List<String> photoUrl;
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