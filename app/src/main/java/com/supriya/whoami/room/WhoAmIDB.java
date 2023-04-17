package com.supriya.whoami.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PatientEntity.class,PersonDataEntity.class},version = 2,exportSchema = false)
public abstract class WhoAmIDB extends RoomDatabase {

    private static WhoAmIDB whoAmIDB = null;

    public abstract DataDAO dataDao();

    public static synchronized WhoAmIDB getInstance(Context context){
        if (whoAmIDB == null){
            whoAmIDB = Room.databaseBuilder(
                    context.getApplicationContext(),
                    WhoAmIDB.class,
                    "WhoAmI")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return whoAmIDB;
    }

}
