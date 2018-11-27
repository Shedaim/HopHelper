package klevente.hu.hophelper.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
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
    public Integer og;

    @ColumnInfo(name = "fg")
    public Integer fg;

    @ColumnInfo(name = "abv")
    public Double abv;

    public Beer() {}

    @Ignore
    public Beer(long id, String name, String description, String style, Integer og, Integer fg, Double abv) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.style = style;
        this.og = og;
        this.fg = fg;
        this.abv = abv;
    }
}
