package klevente.hu.hophelper.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Converters {

    @TypeConverter
    public static Map<String, Double> fromString(String value) {
        Type mapType = new TypeToken<HashMap<String, Double>>(){}.getType();

        return new Gson().fromJson(value, mapType);
    }

    @TypeConverter
    public static String fromHashMap(Map<String, Double> map) {
        return new Gson().toJson(map);
    }
}
