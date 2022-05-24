package com.example.eneamatch;

import java.util.ArrayList;
import java.util.List;

public class Search {

    public int minAge;
    public int maxAge;
    public int gender;
    public List<Boolean> eneatypes;

    public Search()
    {
        minAge = 18;
        maxAge = 99;
        this.eneatypes = new ArrayList<Boolean>();
        for(int i = 0; i < 9; i++)
            this.eneatypes.add(true);
    }
}
