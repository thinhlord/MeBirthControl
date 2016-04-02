package com.meboo.birthcontrol;

/**
 * Created by ruler_000 on 19/03/2016.
 * Project: MeBirthControl
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

public class NotificationPublisher extends BroadcastReceiver {

    public static String EXTRA_REMIND = "remind";

    public void onReceive(Context context, Intent intent) {
        NextRemind remind = (NextRemind) intent.getSerializableExtra(EXTRA_REMIND);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("RIP");
        builder.setContentText("hello");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        NotificationManagerCompat.from(context).notify(1337,  builder.build());
    }
}