package com.paranoidandroid.navigationbar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.baoyz.widget.PullRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import Classes.MyAdapter;
import Classes.Post;
import Interfaces.AsyncResponse;
import appClasses.AppInfo;
import appMethods.AsyncRequest;

import static appMethods.ParseString.StringToArray;
import static appMethods.RequestMethods.returnParsedJsonObject;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    private ListView listView;
    private MyAdapter myAdapter;
    private String str, uri;
    private ProgressBar progressBar;
    private int queryLen;
    private AsyncRequest asyncRequestObject;
    private PullRefreshLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        listView = (ListView) findViewById(R.id.listview);



        uri = AppInfo.serverUri + "/" + AppInfo.serverRequestGetPost;

        layout.setRefreshing(true);

        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                asyncRequestObject = new AsyncRequest(MainActivity.this);
                asyncRequestObject.execute(uri, "");
                layout.setRefreshing(false);
            }
        });

        asyncRequestObject = new AsyncRequest(this);
        asyncRequestObject.execute(uri, "");

    }

    void parseData() {
        ArrayList<Post> listS = new ArrayList<>();
        try {
            listS = StringToArray(str, queryLen);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        myAdapter = new MyAdapter(this, listS);
        listView.setAdapter(myAdapter);
    }

    @Override
    public void processFinish(String output, int position) {
        if(output == null || output.equals("")) {
            queryLen = 0;
            return;
        }
        int jsonResult = returnParsedJsonObject(output);
        if(jsonResult == 0){
            queryLen = 0;
            return;
        }
        if(jsonResult != 0){
            str = output;
            queryLen = jsonResult;
        }
        parseData();
        listView.setVisibility(View.VISIBLE);
        layout.setRefreshing(false);
    }
}
