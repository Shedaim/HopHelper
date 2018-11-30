package klevente.hu.hophelper.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.BeerDao;

@Database(entities = {Beer.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class HopHelperDatabase extends RoomDatabase {
    public abstract BeerDao beerDao();
}
