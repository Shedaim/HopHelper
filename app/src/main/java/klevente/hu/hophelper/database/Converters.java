package klevente.hu.hophelper.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import klevente.hu.hophelper.data.Ingredient;

public class Converters {

    @TypeConverter
    public static Map<String, Float> stringToMapOfStringDouble(String value) {
        if (value == null) {
            return Collections.emptyMap();
        }
        Type mapType = new TypeToken<HashMap<String, Float>>(){}.getType();

        return gson.fromJson(value, mapType);
    }

    @TypeConverter
    public static String mapOfStringDoubleToString(Map<String, Float> map) {
        return gson.toJson(map);
    }

    @TypeConverter
    public static List<Ingredient> stringToListDryHopAddition(String value) {
        if (value == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<ArrayList<Ingredient>>(){}.getType();
        return gson.fromJson(value, listType);
    }
    @TypeConverter
    public static String listOfDryHopAdditionToString(List<Ingredient> list) {
        return gson.toJson(list);
    }

    private static Gson gson = new Gson();
}
