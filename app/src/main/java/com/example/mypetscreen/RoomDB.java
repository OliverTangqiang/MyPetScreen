package com.example.mypetscreen;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//Add database entites
@Database(entities = {MainData.class}, version = 1, exportSchema = false)

public abstract class RoomDB extends RoomDatabase {
    //Create database instance
    private static RoomDB database;
    //Define database name
    private static String DATABASE_NAME = "database";

    public synchronized static RoomDB getInstance(Context context){
        //check condition
        if (database == null){
            //when database is null
            //initialize database
            database = Room.databaseBuilder(context.getApplicationContext()
                ,RoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        //Return database
        return database;


    }
    //create Dao
    public abstract MainDao mainDao();
}
