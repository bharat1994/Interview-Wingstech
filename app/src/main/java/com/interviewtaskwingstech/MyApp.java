package com.interviewtaskwingstech;

import androidx.multidex.MultiDexApplication;
import androidx.room.Room;

import com.interviewtaskwingstech.utils.AppDatabase;

public class MyApp extends MultiDexApplication {

    private static MyApp ourInstance;
    private AppDatabase appDatabase;

    public static MyApp getInstance() {
        return ourInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ourInstance = this;
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "test-database").build();
    }

}
