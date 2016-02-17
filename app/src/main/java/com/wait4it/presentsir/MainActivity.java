package com.wait4it.presentsir;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Calendar;


public class MainActivity extends PreferenceActivity {

    public static String TAG = "com.wait4it.presentsir";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        try {
            if (key.equals("Holidays")) {
                //holidays();
            } else {
                Class myClass = Class.forName("com.wait4it.presentsir." + key);
                Intent intent = new Intent(this, myClass);
                startActivity(intent);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public void holidays() {
        AlarmManager alarmManager;
        MyDBHandler dbHandler;
        Context context = MyBootReceiver.myContext;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("Holidays", true)) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            dbHandler = new MyDBHandler(context, null, null, 1);
            for (int dayNumber = 1; dayNumber <= 5; dayNumber++) {
                int noOfSubjects = dbHandler.getCount(dayNumber);
                for (int _id = 1; _id <= noOfSubjects; _id++) {
                    String startTime = dbHandler.getStartTime(_id, dayNumber);
                    int startHours = Integer.parseInt(String.valueOf(new StringBuilder()
                            .append(startTime.charAt(0)).append(startTime.charAt(1))));
                    int startMinutes = Integer.parseInt(String.valueOf(new StringBuilder()
                            .append(startTime.charAt(3)).append(startTime.charAt(4))));
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_WEEK, dayNumber + 1);
                    calendar.set(Calendar.HOUR_OF_DAY, startHours);
                    calendar.set(Calendar.MINUTE, startMinutes);
                    Intent myIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                            dayNumber * 10 + _id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater blowUp = getMenuInflater();
        blowUp.inflate(R.menu.cool_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutUs:
                Intent i1 = new Intent(this, AboutUs.class);
                startActivity(i1);
                break;
        }
        return false;
    }
}
