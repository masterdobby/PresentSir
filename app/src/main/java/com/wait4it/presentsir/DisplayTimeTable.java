package com.wait4it.presentsir;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by Rahul Yadav on 9/25/2015.
 */
public class DisplayTimeTable extends Activity {

    private Vector<String[]> timeTable;
    private MyDBHandler dbHandler;
    private int dayNumber;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table_layout);
        dbHandler = new MyDBHandler(this, null, null, 1);
        dayNumber = getIntent().getIntExtra("Day Number", 0);
        tableLayout = (TableLayout) findViewById(R.id.tlTimeTable);

        timeTable = dbHandler.getTimeTable(dayNumber);
        int i = 0;
        for (String[] t : timeTable) {
            TableRow tr = new TableRow(this);
            tr.setId(1000 + i);

            TextView labelSubject = new TextView(this);
            labelSubject.setId(2000 + i);
            labelSubject.setText(t[0]);
            labelSubject.setGravity(Gravity.CENTER);
            labelSubject.setTextSize(getResources().getDimension(R.dimen.textsize));
            labelSubject.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.FILL_PARENT, 0.5f));
            tr.addView(labelSubject);

            TextView labelLocation = new TextView(this);
            labelLocation.setId(3000 + i);
            labelLocation.setText(t[1]);
            labelLocation.setGravity(Gravity.CENTER);
            labelLocation.setTextSize(getResources().getDimension(R.dimen.textsize));
            labelLocation.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.FILL_PARENT, 0.2f));
            tr.addView(labelLocation);

            TextView labelStart = new TextView(this);
            labelStart.setId(4000 + i);
            labelStart.setText(t[2]);
            labelStart.setGravity(Gravity.CENTER);
            labelStart.setTextSize(getResources().getDimension(R.dimen.textsize));
            labelStart.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.FILL_PARENT, 0.15f));
            tr.addView(labelStart);

            TextView labelEnd = new TextView(this);
            labelEnd.setId(5000 + i);
            labelEnd.setText(t[3]);
            labelEnd.setGravity(Gravity.CENTER);
            labelEnd.setTextSize(getResources().getDimension(R.dimen.textsize));
            labelEnd.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.FILL_PARENT, 0.15f));
            tr.addView(labelEnd);

            tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.FILL_PARENT));
            i++;
        }
    }
}
