package klevente.hu.hophelper.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "beer")
public class Beer implements Serializable {

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

    @ColumnInfo(name = "batchsize")
    public Double batchSize;

    @ColumnInfo(name = "malts")
    public Map<String, Double> malts = new HashMap<>();

    @ColumnInfo(name = "hops")
    public Map<String, Double> hops = new HashMap<>();

    @ColumnInfo(name = "extras")
    public Map<String, Double> extras = new HashMap<>();

    @ColumnInfo(name = "yeast")
    public String yeast;

    @ColumnInfo(name = "mash")
    public List<MashTime> mashingTimes = new ArrayList<>();

    @ColumnInfo(name = "boil")
    public List<HopAddition>  boilingTimes = new ArrayList<>();

    public Beer() {}

    @Ignore
    public Beer(long id, String name, String description, String style, Integer og, Integer fg, Double abv, Double batchSize, Map<String, Double> malts, Map<String, Double> hops, Map<String, Double> extras, String yeast) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.style = style;
        this.og = og;
        this.fg = fg;
        this.abv = abv;
        this.batchSize = batchSize;
        this.malts = malts;
        this.hops = hops;
        this.extras = extras;
        this.yeast = yeast;
    }

    @Ignore
    public Beer(long id, String name, String description, String style, Integer og, Integer fg, Double abv, Double batchSize) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.style = style;
        this.og = og;
        this.fg = fg;
        this.abv = abv;
        this.batchSize = batchSize;
    }

    public void addMalt(String name, double quantity) {
        malts.put(name, quantity);
    }

    public void addHop(String name, double quantity) {
        hops.put(name, quantity);
    }

    public void addExtra(String name, double quantity) {
        extras.put(name, quantity);
    }

    public void addMashTimeMillis(long millis, int temp) { mashingTimes.add(new MashTime(millis, temp)); }

    public void addMashTimeMinutes(int minutes, int temp) { mashingTimes.add(new MashTime(TimeUnit.MINUTES.toMillis(minutes), temp)); }

    public void addBoilTimeMillis(String hopName, double grams, long millis) { boilingTimes.add(new HopAddition(hopName, grams, millis)); }

    public void addBoilTimeMinutes(String hopName, double grams, int minutes) { boilingTimes.add(new HopAddition(hopName, grams, TimeUnit.MINUTES.toMillis(minutes))); }
}
