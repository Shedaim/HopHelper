package klevente.hu.hophelper.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Beer.class}, version = 1)
public abstract class HopHelperDatabase extends RoomDatabase {
    public abstract BeerDao beerDao();
}
