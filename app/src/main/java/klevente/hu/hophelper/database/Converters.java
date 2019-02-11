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

import klevente.hu.hophelper.data.FermentationTime;
import klevente.hu.hophelper.data.HopAddition;
import klevente.hu.hophelper.data.MashTime;

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
    public static List<MashTime> stringToListOfMashTime(String value) {
        if (value == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<ArrayList<MashTime>>(){}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String listOfMashTimeToString(List<MashTime> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<HopAddition> stringToListHopAddition(String value) {
        if (value == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<ArrayList<HopAddition>>(){}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String listOfHopAdditionToString(List<HopAddition> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<FermentationTime> stringToListDryHopAddition(String value) {
        if (value == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<ArrayList<FermentationTime>>(){}.getType();
        return gson.fromJson(value, listType);
    }
    @TypeConverter
    public static String listOfDryHopAdditionToString(List<FermentationTime> list) {
        return gson.toJson(list);
    }

    private static Gson gson = new Gson();
}
