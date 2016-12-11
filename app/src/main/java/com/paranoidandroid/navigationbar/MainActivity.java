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
import appClasses.AsyncCode;
import appClasses.Errors;
import appMethods.AsyncRequest;

import static appMethods.ParseString.StringToArrayPost;
import static appMethods.RequestMethods.returnParsedJsonObject;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    private ListView listView;
    private MyAdapter myAdapter;
    private String uri;
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
                asyncRequestObject = new AsyncRequest(MainActivity.this, -1, AsyncCode._GET_NEWS);
                asyncRequestObject.execute(uri, "");
                layout.setRefreshing(false);
            }
        });

        asyncRequestObject = new AsyncRequest(this, -1, AsyncCode._GET_NEWS);
        asyncRequestObject.execute(uri, "");

    }

    void parseData(String str, int queryLen) {
        ArrayList<Post> listS = new ArrayList<>();
        try {
            listS = StringToArrayPost(str, queryLen);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        myAdapter = new MyAdapter(this, listS);
        listView.setAdapter(myAdapter);
    }

    @Override
    public void processFinish(String output, int position, int requestCode) {
        if(requestCode == AsyncCode._GET_NEWS) {
            if (output == null || output.equals("")) {
                System.out.println(Errors._NEWS_GET_ERROR);
            }
            else {
                int jsonResult = returnParsedJsonObject(output);
                if(jsonResult > 0) {
                    parseData(output, jsonResult);
                }
            }
            listView.setVisibility(View.VISIBLE);
            layout.setRefreshing(false);
        }
    }
}
