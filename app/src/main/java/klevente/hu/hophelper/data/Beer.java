package klevente.hu.hophelper.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import klevente.hu.hophelper.R;

@Entity(tableName = "beer")
public class Beer implements Serializable {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "file_id")
    public String file_id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "version")
    public int version;

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
    public Map<String, Float> malts = new HashMap<>();

    @ColumnInfo(name = "hops")
    public Map<String, Float> hops = new HashMap<>();

    @ColumnInfo(name = "extras")
    public Map<String, Float> extras = new HashMap<>();

    @ColumnInfo(name = "yeast")
    public String yeast;

    @ColumnInfo(name = "mash")
    public List<Ingredient> mashingTimes = new ArrayList<>();

    @ColumnInfo(name = "boil")
    public List<Ingredient>  boilingTimes = new ArrayList<>();

    @ColumnInfo(name = "fermentation")
    public List<Ingredient>  fermentationTimes = new ArrayList<>();

    public Beer() {}

    @Ignore
    public Beer(long id, String name, int version, String description, String style, Integer og, Integer fg, Double abv, Double batchSize, HashMap<String, Float> malts, HashMap<String, Float> hops, HashMap<String, Float> extras, String yeast) {
        this.id = id;
        this.name = name;
        this.version = version;
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
    public Beer(long id, String name, int version, String description, String style, Integer og, Integer fg, Double abv, Double batchSize) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.description = description;
        this.style = style;
        this.og = og;
        this.fg = fg;
        this.abv = abv;
        this.batchSize = batchSize;
    }

    public static Beer fileToBeer(String content) throws JSONException {
        JSONObject beer_file = new JSONObject(content);
        Beer beer = new Beer();
        beer.name = beer_file.getString("name");
        beer.version = Integer.parseInt(beer_file.getString("version"));
        beer.style = beer_file.getString("style");
        beer.batchSize = Double.valueOf(beer_file.getString("batchSize"));
        beer.description = beer_file.getString("description");
        beer.abv = Double.valueOf(beer_file.getString("abv"));
        beer.og = Integer.valueOf(beer_file.getString("og"));
        beer.fg = Integer.valueOf(beer_file.getString("fg"));
        beer.yeast = beer_file.getString("yeast");

        JSONObject arr;
        arr = beer_file.getJSONObject("malts");
        for (Iterator<String> it = arr.keys(); it.hasNext(); ) {
            String i = it.next();
            beer.malts.put(i, Float.valueOf(arr.getString(i)));
        }
        arr = beer_file.getJSONObject("hops");
        for (Iterator<String> it = arr.keys(); it.hasNext(); ) {
            String i = it.next();
            beer.hops.put(i, Float.valueOf(arr.getString(i)));
        }
        arr = beer_file.getJSONObject("extras");
        for (Iterator<String> it = arr.keys(); it.hasNext(); ) {
            String i = it.next();
            beer.extras.put(i, Float.valueOf(arr.getString(i)));
        }

        JSONArray arr2;
        arr2 = beer_file.getJSONArray("mashingTimes");
        for (int i = 0; i < arr2.length(); i++) {
            JSONObject json = arr2.getJSONObject(i);
            beer.mashingTimes.add(new Ingredient(json.getString("name"),
                    Float.parseFloat(json.getString("quantity")),
                    Long.parseLong(json.getString("time")),
                    Float.parseFloat(json.getString("temp"))));
        }
        arr2 = beer_file.getJSONArray("boilingTimes");
        for (int i = 0; i < arr2.length(); i++) {
            JSONObject json = arr2.getJSONObject(i);
            beer.boilingTimes.add(new Ingredient(json.getString("name"),
                    Float.parseFloat(json.getString("quantity")),
                    Long.parseLong(json.getString("time")),
                    Float.parseFloat(json.getString("temp"))));
        }
        arr2 = beer_file.getJSONArray("fermentationTimes");
        for (int i = 0; i < arr2.length(); i++) {
            JSONObject json = arr2.getJSONObject(i);
            beer.fermentationTimes.add(new Ingredient(json.getString("name"),
                    Float.parseFloat(json.getString("quantity")),
                    Long.parseLong(json.getString("time")),
                    Float.parseFloat(json.getString("temp"))));
        }
        return beer;
    }

    public void addMalt(String name, float quantity) {
        malts.put(name, quantity);
    }

    public void addHop(String name, float quantity) {
        hops.put(name, quantity);
    }

    public void addExtra(String name, float quantity) {
        extras.put(name, quantity);
    }

    public void addMashTimeMinutes(int minutes, int temp) { mashingTimes.add(new Ingredient("", 0, TimeUnit.MINUTES.toMillis(minutes), temp)); }

    public void addBoilTimeMinutes(String hopName, float grams, int minutes) { boilingTimes.add(new Ingredient(hopName, grams, TimeUnit.MINUTES.toMillis(minutes), 100)); }

    public void addFermentationTimeDays(String dry_hop, float grams, int days, int temp) { fermentationTimes.add(new Ingredient(dry_hop, grams, TimeUnit.MINUTES.toMillis(days), temp)); }
}
