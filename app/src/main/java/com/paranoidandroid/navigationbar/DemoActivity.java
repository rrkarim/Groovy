package com.paranoidandroid.navigationbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import Classes.MyAdapter;

public class DemoActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        listView = (ListView) findViewById(R.id.listview1);
        final PullRefreshLayout layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.setRefreshing(false);
            }
        });

        ArrayList<String> list = new ArrayList<>();
        list.add("dsds");
        list.add("dsds");
        list.add("dsds");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row2, list);
        System.out.println(listView.toString());
        listView.setAdapter(adapter);

    }
}
