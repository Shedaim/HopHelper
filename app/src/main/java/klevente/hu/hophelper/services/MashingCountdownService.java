package klevente.hu.hophelper.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ankushgrover.hourglass.Hourglass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.activities.BeerDetailActivity;
import klevente.hu.hophelper.activities.MainActivity;
import klevente.hu.hophelper.constants.HourMinDateFormat;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.BeerList;
import klevente.hu.hophelper.data.MashTime;

public class MashingCountdownService extends Service {
    public static final String BEER_INDEX = "index";

    private static final int NOTIF_ID = 480;

    private class TimerHourglass extends Hourglass {
        TimerHourglass(long timeInMillis) {
            super(timeInMillis, 1000);
        }

        @Override
        public void onTimerTick(long timeRemaining) {
            updateNotification(HourMinDateFormat.format(timeRemaining));
        }

        @Override
        public void onTimerFinish() {
            stopSelf();
        }
    }


    private int beerIdx;
    private Beer beer;
    private List<MashTime> mashTimes;
    private TimerHourglass hourglass;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        beerIdx = intent.getIntExtra(BEER_INDEX, -1);
        beer = BeerList.get(beerIdx);
        mashTimes = new ArrayList<>(beer.mashingTimes);
        long initialStartTime = mashTimes.get(0).millis;

        startForeground(NOTIF_ID, getNotification(HourMinDateFormat.format(initialStartTime)));

        // hourglass = new TimerHourglass(startTime);
        // hourglass.startTimer();

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


        Notification notification = new Notification.Builder(this).
                setContentTitle(beer.name).
                setContentText(msg).
                setSmallIcon(R.drawable.ic_beerlogo).
                setContentIntent(contentIntent).build();

        return notification;
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
