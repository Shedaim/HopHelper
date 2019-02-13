package klevente.hu.hophelper.data;

import java.io.Serializable;

public class Ingredient implements Serializable {
    public String name;
    public float quantity;
    public long time;
    public float temp;


    public Ingredient(String name, float quantity, long time, float temp) {
        this.name = name;
        this.quantity = quantity;
        this.time = time;
        this.temp = temp;
    }

}
