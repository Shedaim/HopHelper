package klevente.hu.hophelper.data;

import java.io.Serializable;

public class HopAddition implements Serializable {
    public String name;
    public double grams;
    public long millis;

    public HopAddition(String name, double grams, long millis) {
        this.name = name;
        this.grams = grams;
        this.millis = millis;
    }
}
