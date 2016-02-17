package com.wait4it.presentsir;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by Rahul Yadav on 9/24/2015.
 */
public class MyAttendance extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_attendance_layout);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tlMyTable);
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Vector<String[]> s = dbHandler.getAttendance();
        int i = 0;
        for (String[] t : s) {
            TableRow tr = new TableRow(this);
            tr.setId(1000 + i);

            TextView labelSNo = new TextView(this);
            labelSNo.setId(2000 + i);
            labelSNo.setText(t[0]);
            labelSNo.setGravity(Gravity.CENTER);
            labelSNo.setTextSize(getResources().getDimension(R.dimen.textsize));
            labelSNo.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.FILL_PARENT, 1f));
            tr.addView(labelSNo);

            TextView labelSubject = new TextView(this);
            labelSubject.setId(3000 + i);
            labelSubject.setText(t[1]);
            labelSubject.setGravity(Gravity.CENTER);
            labelSubject.setTextSize(getResources().getDimension(R.dimen.textsize));
            labelSubject.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.FILL_PARENT, 1f));
            tr.addView(labelSubject);

            TextView labelResult = new TextView(this);
            labelResult.setId(4000 + i);
            labelResult.setText(t[2]);
            labelResult.setGravity(Gravity.CENTER);
            labelResult.setTextSize(getResources().getDimension(R.dimen.textsize));
            labelResult.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.FILL_PARENT, 1f));
            tr.addView(labelResult);

            tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.FILL_PARENT));
            i++;
        }
    }
}
