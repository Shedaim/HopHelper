package klevente.hu.hophelper.events;

public class BoilPauseEvent {
    public final boolean paused;

    public BoilPauseEvent(boolean paused) {
        this.paused = paused;
    }
}
