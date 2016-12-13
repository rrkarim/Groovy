package com.paranoidandroid.navigationbar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import Classes.MyAdapter;
import Classes.Post;
import Interfaces.AsyncResponse;
import Interfaces.PlayerCallback;
import appClasses.AppInfo;
import appClasses.AsyncCode;
import appClasses.Errors;
import appMethods.AsyncRequest;
import butterknife.ButterKnife;
import butterknife.InjectView;

import static appMethods.ParseString.StringToArrayPost;
import static appMethods.RequestMethods.returnParsedJsonObject;

public class MainActivity extends AppCompatActivity implements AsyncResponse, PlayerCallback {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private ListView listView;
    private MyAdapter myAdapter;
    private String uri;
    private AsyncRequest asyncRequestObject;
    private SwipeRefreshLayout layout;
    private SlidingUpPanelLayout slidingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        setupToolbar();

        layout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        listView = (ListView) findViewById(R.id.listview);
        slidingLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);

        uri = AppInfo.serverUri + "/" + AppInfo.serverRequestGetPost;

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        myAdapter.setCallback(this);

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
           layout.setRefreshing(false);
        }
    }

    @Override
    public void playPressed(Post post) {
        TextView vi = (TextView) findViewById(R.id.name);
        vi.setText(post.getTitle());
    }

    private void setupToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search_black_24dp);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite_black_24dp);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_dehaze_black_24dp);


    }
}
