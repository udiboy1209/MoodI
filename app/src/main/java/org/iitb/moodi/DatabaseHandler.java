package org.iitb.moodi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.iitb.moodi.api.Event;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "miDB";

    private static final String TABLE_EVENTS = "events";

    private static class EventsTable
    {
        private static final String id = "id";
        private static final String name = "name";
        private static final String intro = "intro";
        private static final String intro_short = "intro_short";
        private static final String rules = "rules";
        private static final String prizes = "prizes";
        private static final String registration = "registration";
        private static final String genre = "genre";
        private static final String genrebaap = "genrebaap";
        private static final String fav = "fav";
    };

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + EventsTable.id +" INTEGER PRIMARY KEY,"
                + EventsTable.name + " TEXT,"
                + EventsTable.intro + " TEXT,"
                + EventsTable.intro_short + " TEXT,"
                + EventsTable.rules + " TEXT,"
                + EventsTable.prizes + " TEXT,"
                + EventsTable.registration + " TEXT,"
                + EventsTable.genre + " TEXT,"
                + EventsTable.genrebaap + " TEXT,"
                + EventsTable.fav + " INTEGER"
                + ")";
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

        // Create tables again
        onCreate(db);
    }

    public void addEvent(Event e){
        if(e==null)
            return;
        if(findEvent(e.id)!=null)
            updateEvent(e);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EventsTable.id, e.id);
        values.put(EventsTable.name, e.name);
        values.put(EventsTable.intro, e.intro);
        values.put(EventsTable.intro_short , e.intro_short);
        values.put(EventsTable.rules, e.rules);
        values.put(EventsTable.prizes, e.prizes);
        values.put(EventsTable.registration, e.registration);
        values.put(EventsTable.genre, e.genre);
        values.put(EventsTable.genrebaap, e.genrebaap);
        values.put(EventsTable.fav, e.fav?1:0);
        // Inserting Row
        try {
            db.insert(TABLE_EVENTS, null, values);
        }catch (Exception E){
            E.printStackTrace();
        }
        db.close(); // Closing database connection
    }

    public void removeEvent(Event e) {
        if(e==null)
            return;
        if(findEvent(e.id)==null)
            return;

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_EVENTS, EventsTable.id + " = ? ", new String[]{e.id});
        } catch (Exception E){
            E.printStackTrace();
        }
    }

    public void updateEvent(Event e){
        if(e==null)
            return;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //values.put(EventsTable.id, e.id);
        values.put(EventsTable.name, e.name);
        values.put(EventsTable.intro, e.intro);
        values.put(EventsTable.intro_short , e.intro_short);
        values.put(EventsTable.rules, e.rules);
        values.put(EventsTable.prizes, e.prizes);
        values.put(EventsTable.registration, e.registration);
        values.put(EventsTable.genre, e.genre);
        values.put(EventsTable.genrebaap, e.genrebaap);
        // Inserting Row
        try {
            db.update(TABLE_EVENTS, values, EventsTable.id+" = ? ", new String[]{e.id});
        }catch (Exception E){
            E.printStackTrace();
        }
        db.close(); // Closing database connection
    }

    public void favEvent(String id, boolean fav){
        if(id==null)
            return;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //values.put(EventsTable.id, e.id);
        values.put(EventsTable.fav, fav?1:0);
        // Inserting Row
        try {
            db.update(TABLE_EVENTS, values, EventsTable.id+" = "+id, null);
        }catch (Exception E){
            E.printStackTrace();
        }
        db.close(); // Closing database connection
    }

    public Event findEvent(String id){
        if(id==null)
            return null;

        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS + " where " + EventsTable.id + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});
        Event e = null;
        if(cursor.moveToFirst()){
            e = new Event();
            e.id=cursor.getString(cursor.getColumnIndex(EventsTable.id));
            e.name=cursor.getString(cursor.getColumnIndex(EventsTable.name));
            e.intro=cursor.getString(cursor.getColumnIndex(EventsTable.intro));
            e.intro_short=cursor.getString(cursor.getColumnIndex(EventsTable.intro_short));
            e.rules=cursor.getString(cursor.getColumnIndex(EventsTable.rules));
            e.prizes=cursor.getString(cursor.getColumnIndex(EventsTable.prizes));
            e.registration=cursor.getString(cursor.getColumnIndex(EventsTable.registration));
            e.genre=cursor.getString(cursor.getColumnIndex(EventsTable.genre));
            e.genrebaap=cursor.getString(cursor.getColumnIndex(EventsTable.genrebaap));
            e.fav=cursor.getInt(cursor.getColumnIndex(EventsTable.fav))>0;
        }

        return e;
    }
}
