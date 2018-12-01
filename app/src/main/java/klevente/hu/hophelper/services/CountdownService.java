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

import java.util.Locale;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.activities.BeerDetailActivity;
import klevente.hu.hophelper.activities.MainActivity;

public class CountdownService extends Service {
    private static final int NOTIF_ID = 480;

    public interface CountdownServiceListener {
        void onTick(long millisUntilFinished);
        void onFinish();
    }

    private CountdownServiceListener listener;

    private class TimerHourglass extends Hourglass {
        TimerHourglass(long timeInMillis) {
            super(timeInMillis, 1000);
        }

        @Override
        public void onTimerTick(long timeRemaining) {
            updateNotification(String.valueOf(timeRemaining));
        }

        @Override
        public void onTimerFinish() {
            stopSelf();
        }
    }


    private int startTime;
    private String title;
    private TimerHourglass hourglass;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTime = intent.getIntExtra("time", 0);

        startForeground(NOTIF_ID, getNotification(String.valueOf(startTime)));

        hourglass = new TimerHourglass(startTime);
        hourglass.startTimer();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    private Notification getNotification(String msg) {
        Intent notifIntent = new Intent(this, BeerDetailActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIF_ID, notifIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(this).
                setContentTitle("Title").
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
