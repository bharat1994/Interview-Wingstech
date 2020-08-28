package com.interviewtaskwingstech.utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.interviewtaskwingstech.model.Customer;
import com.interviewtaskwingstech.model.CustomerDao;

@Database(entities = {Customer.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CustomerDao customerDao();
}
