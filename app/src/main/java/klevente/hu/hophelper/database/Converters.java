package klevente.hu.hophelper.database;

import android.arch.persistence.room.TypeConverter;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Converters {

    @TypeConverter
    public static Map<String, Double> stringToMapOfStringDouble(String value) {
        if (value == null) {
            return Collections.emptyMap();
        }
        Type mapType = new TypeToken<HashMap<String, Double>>(){}.getType();

        return gson.fromJson(value, mapType);
    }

    @TypeConverter
    public static String mapOfStringDoubleToString(Map<String, Double> map) {
        return gson.toJson(map);
    }

    @TypeConverter
    public static List<Pair<Integer, Integer>> stringToListOfIntegerInteger(String value) {
        if (value == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<ArrayList<Pair<Integer, Integer>>>(){}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String listOfIntegerIntegerToString(List<Pair<Integer, Integer>> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<Pair<String, Integer>> stringToListOfStringInteger(String value) {
        if (value == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<ArrayList<Pair<String, Integer>>>(){}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String listOfStringIntegerToString(List<Pair<String, Integer>> list) {
        return gson.toJson(list);
    }

    private static Gson gson = new Gson();
}
