package klevente.hu.hophelper.events;

public class MashEvent {
    public final boolean paused;
    public final String action;

    public MashEvent(String action, boolean paused) {
        this.action = action;
        this.paused = paused;
    }
}