package org.iitb.moodi.api;

/**
 * Created by udiboy1209 on 30/6/15.
 */
public class Genre {
    public String title;
    public int color;

    public Genre(String t, int c){
        title=t;
        color=c;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
