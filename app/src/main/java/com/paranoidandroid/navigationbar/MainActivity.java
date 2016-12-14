package com.paranoidandroid.navigationbar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    MediaPlayer mPlayer; // sample need to be fixed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        setupToolbar();


        /**/
        ImageView sm = (ImageView) findViewById(R.id.coverImage);
        Drawable placeholder = sm.getContext().getResources().getDrawable(R.drawable.placeholder);
        sm.setImageDrawable(placeholder);

        layout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        listView = (ListView) findViewById(R.id.listview);
        slidingLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);

        uri = AppInfo.serverUri + "/" + AppInfo.serverRequestGetPost;

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                asyncRequestObject = new AsyncRequest(MainActivity.this, -1, AsyncCode._GET_NEWS);
                asyncRequestObject.execute(uri, "");
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
        layout.setRefreshing(false);
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

    /*
        fixes here
        Very simple approach
     */
    public void playPause(View view) {
        ImageButton button = (ImageButton) findViewById(R.id.playPauseButton);
        if(mPlayer != null && mPlayer.isPlaying()) {
            button.setBackgroundDrawable( getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp) );
            mPlayer.pause();
        }
        else {

            button.setBackgroundDrawable( getResources().getDrawable(R.drawable.ic_pause_circle_outline_black_24dp) );

            if(mPlayer == null) {
                mPlayer = new MediaPlayer();
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                try {
                    mPlayer.setDataSource("http://10.0.2.2/songs/mahir.mp3");
                } catch (IllegalArgumentException e) {
                    Log.w("Error", "You might not set the URI correctly!");
                } catch (SecurityException e) {
                    Log.w("Error", "You might not set the URI correctly!");
                } catch (IllegalStateException e) {
                    Log.w("Error", "You might not set the URI correctly!");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    mPlayer.prepare();
                } catch (IllegalStateException e) {
                    Log.w("Error", "You might not set the URI correctly!");
                } catch (IOException e) {
                    Log.w("Error", "You might not set the URI correctly!");
                }
            }
            mPlayer.start();
        }
    }
}
