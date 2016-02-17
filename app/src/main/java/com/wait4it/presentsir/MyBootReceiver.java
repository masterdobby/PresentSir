package com.wait4it.presentsir;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Rahul Yadav on 9/24/2015.
 */

public class MyBootReceiver extends BroadcastReceiver {

    private AlarmManager alarmManager;
    private MyDBHandler dbHandler;
    public static Context myContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        myContext = context;
        Log.i(MainActivity.TAG, "Boot broadcast received successfully...!!");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            dbHandler = new MyDBHandler(context, null, null, 1);
            for (int dayNumber = 1; dayNumber <= 5; dayNumber++) {
                int noOfSubjects = dbHandler.getCount(dayNumber);
                //Log.i(MainActivity.TAG, "No of subjects = " + Integer.toString(noOfSubjects));
                for (int _id = 1; _id <= noOfSubjects; _id++) {
                    String startTime = dbHandler.getStartTime(_id, dayNumber);
                    int startHours = Integer.parseInt(String.valueOf(new StringBuilder()
                            .append(startTime.charAt(0)).append(startTime.charAt(1))));
                    int startMinutes = Integer.parseInt(String.valueOf(new StringBuilder()
                            .append(startTime.charAt(3)).append(startTime.charAt(4))));
                    //print(startTime, startHours, startMinutes);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_WEEK, dayNumber + 1);
                    calendar.set(Calendar.HOUR_OF_DAY, startHours);
                    calendar.set(Calendar.MINUTE, startMinutes);
                    Intent myIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                            dayNumber * 10 + _id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                }
            }
        }
    }

    private void print(String startTime, int startHours, int startMinutes) {
        Log.i(MainActivity.TAG, "Start Time = " + startTime);
        Log.i(MainActivity.TAG, "Start Hours = " + Integer.toString(startHours));
        Log.i(MainActivity.TAG, "Start Minutes = " + Integer.toString(startMinutes));
    }
}
