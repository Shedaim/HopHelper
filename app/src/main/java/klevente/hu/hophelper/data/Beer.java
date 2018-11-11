package klevente.hu.hophelper.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "beer")
public class Beer {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "style")
    public String style;

    @ColumnInfo(name = "og")
    public double og;

    @ColumnInfo(name = "fg")
    public double fg;

    @ColumnInfo(name = "abv")
    public double abv;
}
