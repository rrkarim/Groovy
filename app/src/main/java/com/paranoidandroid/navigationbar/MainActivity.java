package com.paranoidandroid.navigationbar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        uri = AppInfo.serverUri + "/" + AppInfo.serverRequestGetPost;
        AsyncRequest asyncRequestObject = new AsyncRequest(this);
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
    public void processFinish(String output) {
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
        progressBar.setVisibility(View.INVISIBLE);
    }
}
