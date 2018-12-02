package klevente.hu.hophelper.data;

import java.io.Serializable;

public class MashTime implements Serializable {
    public long millis;
    public int temp;

    public MashTime(long millis, int temp) {
        this.millis = millis;
        this.temp = temp;
    }
}
