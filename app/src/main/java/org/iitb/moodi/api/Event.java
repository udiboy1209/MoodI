package org.iitb.moodi.api;

/**
 * Created by udiboy on 26/10/15.
 */
public class Event {
    public String name;
    public String description;
    public String venue;
    public long time;
    public boolean starred;

    public Event(String n, String d, String v, long t, boolean s){
        name=n;
        description=d;
        venue=v;
        time=t;
        starred=s;
    }

    public Event(){
        name="Event";
        description="description";
        venue="Venue";
        time=0;
        starred=false;
    }
}
