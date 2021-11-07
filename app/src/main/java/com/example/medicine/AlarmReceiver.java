package com.example.medicine;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    String CHANNEL_ID;
    int SUMMARY_ID;

    @Override
    public void onReceive(Context context, Intent intent) {
        int req = intent.getExtras().getInt("req", 0);
        final String GROUP_KEY = context.getResources().getString(R.string.alamr_group_key);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        CHANNEL_ID = context.getString(R.string.channel_name);
        SUMMARY_ID = 0;
        String contentText = "[" + intent.getStringExtra("mediName") + "] "
                + context.getString(R.string.notification_content);
        String contentTitle = context.getString(R.string.notification_title);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, req, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String descriptionText = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setShowBadge(true);
            channel.setDescription(descriptionText);
            notificationManager.createNotificationChannel(channel);
        }

        Intent tmpIntent = new Intent(context, SettingActivity.class);
        tmpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, tmpIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.app_icon)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setWhen(intent.getLongExtra("mediLongTime", Calendar.getInstance().getTimeInMillis()))
                .setContentIntent(pendingIntent).setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                //.setCategory(NotificationCompat.CATEGORY_ALARM)
                .setStyle(new NotificationCompat.InboxStyle()
                            .addLine(contentTitle)
                            .addLine(contentText))
                .setGroup(GROUP_KEY);

        NotificationCompat.Builder summary = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(contentTitle)
                .setAutoCancel(true)
                .setGroup(GROUP_KEY)
                .setGroupSummary(true);

        notificationManager.notify(intent.getIntExtra("mediId", 0), builder.build());
        notificationManager.notify(SUMMARY_ID, summary.build());

    }


}