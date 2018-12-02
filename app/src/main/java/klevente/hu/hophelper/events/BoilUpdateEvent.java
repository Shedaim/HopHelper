package klevente.hu.hophelper.events;

public class BoilUpdateEvent {
    public final String name;
    public final double grams;
    public final long millis;

    public BoilUpdateEvent(String name, double grams, long millis) {
        this.name = name;
        this.grams = grams;
        this.millis = millis;
    }
}
