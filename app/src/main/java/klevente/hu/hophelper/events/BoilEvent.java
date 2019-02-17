package klevente.hu.hophelper.events;

public class BoilEvent {
    public final boolean paused;
    public final String action;

    public BoilEvent(String action, boolean paused) {
        this.action = action;
        this.paused = paused;
    }
}
