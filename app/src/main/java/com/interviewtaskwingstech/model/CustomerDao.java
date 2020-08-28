package com.interviewtaskwingstech.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CustomerDao {

    @Query("SELECT * FROM Customer")
    List<Customer> getAll();

    @Insert
    void insertAll(Customer... cartItem);

    @Delete
    void delete(Customer cartItem);

    @Update
    void update(Customer cartItem);
}
