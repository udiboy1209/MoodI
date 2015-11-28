package org.iitb.moodi;

import android.app.Application;

import org.iitb.moodi.ui.activity.BaseActivity;

/**
 * Created by udiboy on 28/11/15.
 */
public class BaseApplication extends Application{
    protected static DatabaseHandler db;

    public BaseApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        BaseApplication.db=new DatabaseHandler(getApplicationContext());
    }

    public static DatabaseHandler getDB(){
        return db;
    }
}
