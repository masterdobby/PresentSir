package com.wait4it.presentsir;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by Rahul Yadav on 9/23/2015.
 */
public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydb.db";
    public static final String[] TABLE_DAYS = {"", "MONDAY", "TUESDAY",
            "WEDNESDAY", "THURSDAY", "FRIDAY"};
    private static final String TABLE_ATTENDANCE = "ATTENDANCE";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_START = "start";
    public static final String COLUMN_END = "end";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_RESULT = "result";
    public static final String COLUMN_ATTENDEDCLASSES = "attendedClasses";
    public static final String COLUMN_TOTALCLASSES = "totalClasses";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_ATTENDANCE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_ATTENDEDCLASSES + " INTEGER, " +
                COLUMN_TOTALCLASSES + " INTEGER, " +
                COLUMN_RESULT + " REAL);";
        db.execSQL(query);
        for (int i = 1; i <= 5; i++) {
            query = "CREATE TABLE " + TABLE_DAYS[i] + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_LOCATION + " TEXT, " +
                    COLUMN_START + " TEXT, " +
                    COLUMN_END + " TEXT); ";
            db.execSQL(query);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        for (int dayNumber = 1; dayNumber <= 5; dayNumber++) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAYS[dayNumber]);
        }
        onCreate(db);
    }

    //Add a subject to the database
    public void addSubjectToDay(Subjects subject, int dayNumber) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, subject.get_name());
        values.put(COLUMN_LOCATION, subject.get_location());
        values.put(COLUMN_START, subject.get_start());
        values.put(COLUMN_END, subject.get_end());
        db.insert(TABLE_DAYS[dayNumber], null, values);
        db.close();
    }

    public void addSubjectToAttendance(Attendance attendance) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, attendance.get_name());
        values.put(COLUMN_ATTENDEDCLASSES, attendance.get_attendedClasses());
        values.put(COLUMN_TOTALCLASSES, attendance.get_totalClasses());
        values.put(COLUMN_RESULT, attendance.get_result());
        db.insert(TABLE_ATTENDANCE, null, values);
        db.close();
    }

    public Vector<String[]> getAttendance() {
        String[] columns = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_ATTENDEDCLASSES,
                COLUMN_TOTALCLASSES, COLUMN_RESULT};
        Vector<String[]> s = new Vector<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(TABLE_ATTENDANCE, columns, null, null, null, null, null);
        String sno, subject, result;

        int iSno = c.getColumnIndex(columns[0]);
        int iSubject = c.getColumnIndex(columns[1]);
        int iAttended = c.getColumnIndex(columns[2]);
        int iTotal = c.getColumnIndex(columns[3]);
        //int iResult = c.getColumnIndex(columns[4]);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            sno = c.getString(iSno) + ".";
            subject = c.getString(iSubject);
            result = c.getString(iAttended) + " / " + c.getString(iTotal);
            String[] t = new String[3];
            t[0] = sno;
            t[1] = subject;
            t[2] = result;
            s.add(t);
        }
        c.close();
        db.close();
        return s;
    }

    public Vector<String[]> getTimeTable(int dayNumber) {
        String[] columns = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_START,
                COLUMN_END, COLUMN_LOCATION};
        Vector<String[]> s = new Vector<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(TABLE_DAYS[dayNumber], columns, null, null, null, null, null);
        String sno, subject, start, end, location;

        //int iSno = c.getColumnIndex(columns[0]);
        int iSubject = c.getColumnIndex(columns[1]);
        int iStart = c.getColumnIndex(columns[2]);
        int iEnd = c.getColumnIndex(columns[3]);
        int iLocation = c.getColumnIndex(columns[4]);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            //sno = c.getString(iSno) + ".";
            subject = c.getString(iSubject);
            start = c.getString(iStart);
            end = c.getString(iEnd);
            location = c.getString(iLocation);
            String[] t = new String[4];
            t[0] = subject;
            t[1] = location;
            t[2] = to24(start);
            t[3] = to24(end);
            s.add(t);
        }
        c.close();
        db.close();
        return s;
    }

    public int getCount(int dayNumber) {
        String query = "SELECT COUNT(*) FROM " + TABLE_DAYS[dayNumber];
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int count = Integer.parseInt(c.getString(c.getColumnIndex("COUNT(*)")));
        return count;
    }

    public String getStartTime(int _id, int dayNumber) {
        String query = "SELECT " + COLUMN_START + " FROM " + TABLE_DAYS[dayNumber]
                + " WHERE " + COLUMN_ID + "=" + _id;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String startTime = c.getString(c.getColumnIndex(COLUMN_START));
        return startTime;
    }

    private String to24(String time) {
        String timeSet = "";
        int hour = Integer.parseInt(String.valueOf(new StringBuilder()
                .append(time.charAt(0)).append(time.charAt(1))));
        int minute = Integer.parseInt(String.valueOf(new StringBuilder()
                .append(time.charAt(3)).append(time.charAt(4))));
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

        String newTime = new StringBuilder().append(hour).append(":")
                .append(minutes).toString();
        return newTime;
    }
}

