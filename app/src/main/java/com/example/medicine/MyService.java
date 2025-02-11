package com.example.medicine;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class MyService extends Service {
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return Service.START_STICKY;
        }

        myHelper = new MyDBHelper(this);
        sqlDB = myHelper.getReadableDatabase();

        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM medi JOIN time ON medi.mediId = time.timeId WHERE endDate >= date('now');", null);

        Calendar cal = Calendar.getInstance();
        Calendar n = Calendar.getInstance();

        long nowTime = System.currentTimeMillis();
        long calendarTime = 0;

        long interval = 1000 * 60 * 60 * 24;

        int nDay = n.get(Calendar.DAY_OF_WEEK);
        String day = "";
        switch (nDay) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;
        }

        while (cursor.moveToNext()) {
            if (day.equals("월") && cursor.getInt(5) == 1) {
                alarm(cal, cursor, calendarTime, nowTime, interval, 2);
            } else if (day.equals("화") && cursor.getInt(6) == 1) {
                alarm(cal, cursor, calendarTime, nowTime, interval, 3);
            } else if (day.equals("수") &&  cursor.getInt(7) == 1) {
                alarm(cal, cursor, calendarTime, nowTime, interval, 4);
            } else if (day.equals("목") && cursor.getInt(8) == 1) {
                alarm(cal, cursor, calendarTime, nowTime, interval, 5);
            } else if (day.equals("금") && cursor.getInt(9) == 1) {
                alarm(cal, cursor, calendarTime, nowTime, interval, 6);
            } else if (day.equals("토") && cursor.getInt(10) == 1) {
                alarm(cal, cursor, calendarTime, nowTime, interval, 7);
            } else if (day.equals("일") && cursor.getInt(11) == 1) {
                alarm(cal, cursor, calendarTime, nowTime, interval, 1);
            }
        }

        cursor.close();
        sqlDB.close();

        cal.add(Calendar.DATE, 1);

        return super.onStartCommand(intent, flags, startId);
    }

    private void alarm(Calendar cal, Cursor cursor, long calendarTime, long nowTime, long interval, int dayOfWeek) {
        int curCount = cursor.getCount();
        int fiveCurCount = curCount * 5;
        int count = 0;
        PendingIntent[] sender = new PendingIntent[fiveCurCount];

        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        for (int i = 14; i < 19; i++) {
            String str = cursor.getString(i);
            if (str.equals("null")) {
                continue;
            } else {
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(cursor.getString(i).substring(0, 2)));
                cal.set(Calendar.MINUTE, Integer.parseInt(cursor.getString(i).substring(3)));
                calendarTime = cal.getTimeInMillis();

                if (nowTime > calendarTime) {
                    calendarTime += interval;
                }

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra("mediId", cursor.getInt(0));
                intent.putExtra("mediName", cursor.getString(1));
                intent.putExtra("mediTime", cursor.getString(i));
                intent.putExtra("mediLongTime", calendarTime);

                final int id = (int) System.currentTimeMillis();
                sender[count] = PendingIntent.getBroadcast(getApplicationContext(), id, intent, 0);

                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendarTime, sender[count]);

                count++;
            }
        }
    }
}
