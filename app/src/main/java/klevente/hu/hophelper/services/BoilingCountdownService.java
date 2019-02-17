package klevente.hu.hophelper.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ankushgrover.hourglass.Hourglass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.activities.BeerDetailActivity;
import klevente.hu.hophelper.constants.MinSecondDateFormat;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.BeerList;
import klevente.hu.hophelper.data.Ingredient;
import klevente.hu.hophelper.events.BoilEvent;


public class BoilingCountdownService extends Service {
    public static final String BEER_INDEX = "index";
    private static final int NOTIF_ID = 190;

    private class TimerHourGlass extends Hourglass {
        private float grams;
        private String name;
        TimerHourGlass(Ingredient addition) {
            super(addition.time, 1000);
            this.grams = addition.quantity;
            this.name = addition.name;
        }

        @Override
        public void onTimerTick(long timeRemaining) {
            updateNotification(getString(R.string.boiling_for, grams, name, MinSecondDateFormat.format(timeRemaining)));
            EventBus.getDefault().post(new Ingredient(name, grams, timeRemaining, 100));
        }

        @Override
        public void onTimerFinish() {
            timerIndex++;
            if (timerIndex == timers.size()) {
                EventBus.getDefault().post(new BoilEvent("finish", true));
                stopSelf();
            } else {
                timers.get(timerIndex).startTimer();
            }
        }
    }

    @Subscribe
    public void onBoilEvent(BoilEvent event) {
        switch (event.action) {
            case "pause":
                if (event.paused) {
                    timers.get(timerIndex).pauseTimer();
                    updateNotification(getString(R.string.boiling_for, timers.get(timerIndex).grams, timers.get(timerIndex).name, getString(R.string.paused)));
                } else {
                    timers.get(timerIndex).resumeTimer();
                }
                break;
            case "stop":
                timers.get(timerIndex).stopTimer();
                //updateNotification(getString(R.string.boiling_for, timers.get(timerIndex).grams, timers.get(timerIndex).name, getString(R.string.stopped)));
                break;
            case "finish":
                break;
        }
    }

    private int beerIdx;
    private Beer beer;
    private List<TimerHourGlass> timers;
    private int timerIndex = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        beerIdx = intent.getIntExtra(BEER_INDEX, -1);
        beer = BeerList.get(beerIdx);
        timers = new ArrayList<>(beer.boilingTimes.size());
        for (Ingredient h : beer.boilingTimes) {
            timers.add(new TimerHourGlass(h));
        }

        timers.get(timerIndex).startTimer();
        startForeground(NOTIF_ID, getNotification("Timer start..."));
        EventBus.getDefault().register(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        stopForeground(true);
        super.onDestroy();
    }

    private Notification getNotification(String msg) {
        Intent notifIntent = new Intent(this, BeerDetailActivity.class);
        notifIntent.putExtra(BEER_INDEX, beerIdx);
        PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIF_ID, notifIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        return new Notification.Builder(this).
                setContentTitle(beer.name).
                setContentText(msg).
                setSmallIcon(R.drawable.ic_beerlogo).
                setContentIntent(contentIntent).build();
    }

    private void updateNotification(String msg) {
        Notification notification = getNotification(msg);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(NOTIF_ID, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }
}
