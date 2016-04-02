package com.meboo.birthcontrol;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ruler_000 on 19/03/2016.
 * Project: MeBirthControl
 */
public class AlarmHelper {

    public static void scheduleNotification(Context context, NextRemind remind) {
        Intent i = new Intent(context, NotificationPublisher.class);
        i.putExtra(NotificationPublisher.EXTRA_REMIND, remind);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1337, i, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, remind.timestamp, pendingIntent);
    }
}
