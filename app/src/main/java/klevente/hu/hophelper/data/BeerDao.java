package klevente.hu.hophelper.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BeerDao {

    @Query("SELECT * FROM beer")
    List<Beer> getAll();

    @Insert
    long insert(Beer beer);

    @Update
    void update(Beer beer);

    @Delete
    void delete(Beer beer);
}
