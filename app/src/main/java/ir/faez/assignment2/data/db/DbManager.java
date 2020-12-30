package ir.faez.assignment2.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ir.faez.assignment2.data.db.DAO.UserDao;
import ir.faez.assignment2.data.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class DbManager extends RoomDatabase {
    private static final String DB_NAME = "HomeHood";
    private static DbManager dbManager;

    public static DbManager getInstance(Context context) {
        if (dbManager == null) {
            dbManager = Room.databaseBuilder(context, DbManager.class, DB_NAME).fallbackToDestructiveMigration().build();
        }
        return dbManager;
    }

    public abstract UserDao userDao();
}
