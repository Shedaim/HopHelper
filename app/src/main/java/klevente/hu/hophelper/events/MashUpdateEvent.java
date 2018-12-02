package klevente.hu.hophelper.events;

public class MashUpdateEvent {
    public final int temp;
    public final long millis;

    public MashUpdateEvent(int temp, long millis) {
        this.temp = temp;
        this.millis = millis;
    }
}
