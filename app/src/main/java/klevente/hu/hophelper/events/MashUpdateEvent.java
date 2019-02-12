package klevente.hu.hophelper.events;

public class MashUpdateEvent {
    public final float temp;
    public final long millis;

    public MashUpdateEvent(float temp, long millis) {
        this.temp = temp;
        this.millis = millis;
    }
}
