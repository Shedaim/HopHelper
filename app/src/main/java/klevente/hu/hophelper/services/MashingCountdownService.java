package klevente.hu.hophelper.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ankushgrover.hourglass.Hourglass;

import java.util.ArrayList;
import java.util.List;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.activities.BeerDetailActivity;
import klevente.hu.hophelper.constants.MinSecondDateFormat;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.BeerList;
import klevente.hu.hophelper.data.MashTime;

public class MashingCountdownService extends Service {
    public static final String BEER_INDEX = "index";

    private static final int NOTIF_ID = 480;

    private class TimerHourglass extends Hourglass {
        private int temp;
        TimerHourglass(MashTime time) {
            super(time.millis, 1000);
            this.temp = time.temp;
        }

        @Override
        public void onTimerTick(long timeRemaining) {
            updateNotification(getString(R.string.mashing_at, temp, MinSecondDateFormat.format(timeRemaining)));
        }

        @Override
        public void onTimerFinish() {
            timerIndex++;
            if (timerIndex == timers.size()) {
                stopSelf();
            } else {
                timers.get(timerIndex).startTimer();
            }
        }
    }


    private int beerIdx;
    private Beer beer;
    private List<TimerHourglass> timers;
    private int timerIndex = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        beerIdx = intent.getIntExtra(BEER_INDEX, -1);
        beer = BeerList.get(beerIdx);
        timers = new ArrayList<>(beer.mashingTimes.size());
        for (MashTime m : beer.mashingTimes) {
            timers.add(new TimerHourglass(m));
        }

        timers.get(timerIndex).startTimer();
        startForeground(NOTIF_ID, getNotification("Timer start..."));

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
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
