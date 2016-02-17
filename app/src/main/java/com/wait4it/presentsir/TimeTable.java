package com.wait4it.presentsir;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Rahul Yadav on 9/25/2015.
 */
public class TimeTable extends ListActivity {

    String classes[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(TimeTable.this, android.R.layout.simple_list_item_1, classes));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, DisplayTimeTable.class);
        intent.putExtra("Day Number", position + 1);
        startActivity(intent);
    }
}
