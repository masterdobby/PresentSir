package com.wait4it.presentsir;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

/**
 * Created by Rahul Yadav on 9/23/2015.
 */
public class Days extends Activity {

    private static final String[] days = {"", "MONDAY", "TUESDAY",
            "WEDNESDAY", "THURSDAY", "FRIDAY"};
    private String subjectName, location, startTime, endTime;
    private int dayNumber = 1, subjectCount = 1, startHours, startMinutes, endHours, endMinutes;
    private static final int START_TIME_DIALOG_ID = 53453, END_TIME_DIALOG_ID = 34532;
    private MyDBHandler dbHandler;
    private HashSet<String> hashSet;
    private AlarmManager alarmManager;
    EditText edSubject, edLocation;
    TextView name, tvStartTime, tvEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.days_layout);
        edSubject = (EditText) findViewById(R.id.edSubject);
        edLocation = (EditText) findViewById(R.id.edLocation);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        name = (TextView) findViewById(R.id.dayName);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        hashSet = new HashSet<>();
        dbHandler = new MyDBHandler(this, null, null, 1);
        timeClickListener();
    }

    public void timeClickListener() {
        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(START_TIME_DIALOG_ID);
            }
        });
        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(END_TIME_DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_TIME_DIALOG_ID:
                return new TimePickerDialog(this, TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        startTimePickerListener, startHours, startMinutes, false);
            case END_TIME_DIALOG_ID:
                return new TimePickerDialog(this, TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        endTimePickerListener, endHours, endMinutes, false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener startTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    startHours = hourOfDay;
                    startMinutes = minute;
                    setStartTime(startHours, startMinutes);
                }
            };

    private TimePickerDialog.OnTimeSetListener endTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    endHours = hourOfDay;
                    endMinutes = minute;
                    setEndTime(endHours, endMinutes);
                }
            };

    public void addSubjectClicked(View view) {
        //Retrieve the entered data
        subjectName = edSubject.getText().toString();
        location = edLocation.getText().toString();
        startTime = tvStartTime.getText().toString();
        endTime = tvEndTime.getText().toString();
        clearEditText();
        tvStartTime.setText(endTime);

        //Set the pending intent to the start time of the subject
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayNumber + 1);
        calendar.set(Calendar.HOUR_OF_DAY, startHours);
        calendar.set(Calendar.MINUTE, startMinutes);
        Intent intent = new Intent(Days.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Days.this,
                dayNumber * 10 + subjectCount, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        /*Log.i(MainActivity.TAG, "Day Number = " +Integer.toString(dayNumber));
        Log.i(MainActivity.TAG, "Subject count = " + Integer.toString(subjectCount));
        Log.i(MainActivity.TAG, "Alarm On!!");*/

        //Set the alarm
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY * 7, pendingIntent);

        //Add a subject to the days and attendance table
        Subjects subject = new Subjects();
        subject.set_name(subjectName);
        subject.set_location(location);
        subject.set_start(startHours + ":" + startMinutes);
        subject.set_end(endHours + ":" + endMinutes);
        boolean didItWork = true;

        //For catching any database exception
        try {
            dbHandler.addSubjectToDay(subject, dayNumber);
            //If subject has not been added already, add it to the attendance table as well
            if (!hashSet.contains(subjectName)) {
                Attendance attendance = new Attendance();
                attendance.set_name(subjectName);
                attendance.set_attendedClasses(0);
                attendance.set_totalClasses(0);
                attendance.set_result(0.);
                dbHandler.addSubjectToAttendance(attendance);
                hashSet.add(subjectName);
            }
            subjectCount++;
        } catch (Exception e) {
            didItWork = false;
        } finally {
            if (didItWork) {
                Toast.makeText(this, new StringBuilder().append("Subject " +
                        subjectName + " added successfully... :-)"), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, new StringBuilder().append("Subject " +
                        subjectName + " added unsuccessfully... :-("), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void nextDayClicked(View view) {
        clearEditText();
        subjectCount = 1;
        if (dayNumber == 5) {
            dayNumber--;
            finish();
        }
        name.setText(days[++dayNumber]);
        if (dayNumber == 5) {
            Button b = (Button) findViewById(R.id.bNextDay);
            b.setText("FINISH");
        }
    }

    private void clearEditText() {
        edSubject.setText("");
        edLocation.setText("");
        tvStartTime.setText("HH:MM");
        tvEndTime.setText("HH:MM");
    }

    private void setStartTime(int hour, int minute) {
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String minutes = "";
        if (minute < 10) {
            minutes = "0" + minute;
        } else {
            minutes = String.valueOf(minute);
        }

        String setTime = new StringBuilder().append(hour).append(":")
                .append(minutes).append(" ").append(timeSet).toString();
        tvStartTime.setText(setTime);
    }

    private void setEndTime(int hour, int minute) {
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String minutes = "";
        if (minute < 10) {
            minutes = "0" + minute;
        } else {
            minutes = String.valueOf(minute);
        }

        String setTime = new StringBuilder().append(hour).append(":")
                .append(minutes).append(" ").append(timeSet).toString();
        tvEndTime.setText(setTime);
    }
}

