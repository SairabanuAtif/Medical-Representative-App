package com.triocodes.medivision.Model;

/**
 * Created by admin on 19-02-16.
 */
public class SpinnerModel implements Comparable<SpinnerModel> {
    int id;
    String text;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    @Override
    public int compareTo(SpinnerModel another) {
        return this.id < another.id? -1 : 1;
    }
}
