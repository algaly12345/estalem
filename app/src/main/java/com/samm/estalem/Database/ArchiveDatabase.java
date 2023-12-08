package com.samm.estalem.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = Archiveitem.class, exportSchema = false)
public abstract class ArchiveDatabase extends RoomDatabase {

    public abstract ArchiveDAO cartDAO();

    private static ArchiveDatabase instance;

    public static ArchiveDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, ArchiveDatabase.class, "AlzajecArchive")
                    .build();
        }
        return instance;
    }

}
