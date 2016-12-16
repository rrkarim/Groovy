package com.paranoidandroid.navigationbar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import Classes.MusicController;
import Classes.MyAdapter;
import Classes.Post;
import Interfaces.AsyncResponse;
import Interfaces.MediaCallback;
import Interfaces.PlayerCallback;
import appClasses.AppInfo;
import appClasses.AsyncCode;
import appClasses.Errors;
import appMethods.AsyncRequest;
import butterknife.ButterKnife;
import butterknife.InjectView;

import static appMethods.ParseString.StringToArrayPost;
import static appMethods.RequestMethods.returnParsedJsonObject;

public class MainActivity extends AppCompatActivity implements AsyncResponse, PlayerCallback, MediaController.MediaPlayerControl, MediaCallback, SeekBar.OnSeekBarChangeListener {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.playPauseButton)
    ImageButton button;

    @InjectView(R.id.trackSeek)
    SeekBar seekBar;

    @InjectView(R.id.trackCurrentTime)
    TextView trackCurrentTime;

    @InjectView(R.id.trackFullTime)
    TextView trackFullTime;

    private ListView listView;
    private MyAdapter myAdapter;
    private String uri;
    private AsyncRequest asyncRequestObject;
    private SwipeRefreshLayout layout;
    private SlidingUpPanelLayout slidingLayout;
    private LinearLayout dragView;

    private ArrayList<Post> listS;

    // media controllers
    private boolean paused = false, playbackPaused = false;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

    private final Handler handler = new Handler();

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();
            musicSrv.getContext(MainActivity.this);
            musicBound = true;

            musicSrv.setList(listS); // fixes
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent == null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.inject(this);
        setupToolbar();

        seekBar.setOnSeekBarChangeListener(this);

        ImageView sm = (ImageView) findViewById(R.id.coverImage);
        Drawable placeholder = sm.getContext().getResources().getDrawable(R.drawable.placeholder);
        sm.setImageDrawable(placeholder);

        layout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        listView = (ListView) findViewById(R.id.listview);
        slidingLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        dragView = (LinearLayout) findViewById(R.id.dragView);
        dragView.setVisibility(View.INVISIBLE);
        layout.setRefreshing(true);

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
        listS = new ArrayList<>();
        try {
            listS = StringToArrayPost(str, queryLen);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch(ParseException e) {
            e.printStackTrace();
        }

        myAdapter = new MyAdapter(this, listS);
        if(musicSrv != null) musicSrv.setList(listS); // fixes here


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
        else if(requestCode == AsyncCode._GET_LASTFM) {

        }
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
        MediaPlayer methods
    */

    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            playbackPaused=false;
        }
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            playbackPaused=false;
        }
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
        return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
        return musicSrv.getDur();
        else return 0;
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
        return musicSrv.isPng();
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public int getBufferPercentage() {
            return 0;
    }

    @Override
    public void pause() {
        playbackPaused = true;
        handler.removeCallbacks(updatePositionRunnable);
        button.setBackgroundDrawable( getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp) );
        musicSrv.pausePlayer();
    }


    public void playPause(View view) {
        if(playbackPaused == true) {
            start();
        }
        else {
            pause();
        }
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        updatePosition();
        button.setBackgroundDrawable( getResources().getDrawable(R.drawable.ic_pause_circle_outline_black_24dp) );
        playbackPaused = false;
        musicSrv.go();
    }

    void changePlayerInfo(Post track) {
        TextView trackName = (TextView) findViewById(R.id.trackName);
        trackName.setText(track.getTitle());
        TextView trackArtist = (TextView) findViewById(R.id.trackArtist);
        trackArtist.setText(track.getSinger());
        ImageView coverImage = (ImageView) findViewById(R.id.coverImage);
        Picasso.with(this).load(track.getHeaderImage()).into(coverImage);
        button.setBackgroundDrawable( getResources().getDrawable(R.drawable.ic_pause_circle_outline_black_24dp) );
    }

    @Override
    public void trackCompleteCallback(Post track) {
        handler.removeCallbacks(updatePositionRunnable);
        button.setBackgroundDrawable( getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp) );
    }

    @Override
    public void trackReadyCallback(Post track) {
        seekBar.setMax(musicSrv.getDur());
        updatePosition();
        trackFullTime.setText(positionToString(musicSrv.getDur()));
        changePlayerInfo(track);
    }

    @Override
    public void playPressed(Post post, int position) {
        if(dragView.getVisibility() == View.INVISIBLE) {
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            dragView.setVisibility(View.VISIBLE);
        }
        musicSrv.setSong(position);
        musicSrv.playSong();

        if(playbackPaused){
            playbackPaused=false;
        }
    }

    private final Runnable updatePositionRunnable = new Runnable() {
        public void run() {
            updatePosition();
        }
    };

    private void updatePosition() {
        handler.removeCallbacks(updatePositionRunnable);
        if (musicSrv != null) seekBar.setProgress(musicSrv.getPosn());

        trackCurrentTime.setText(positionToString(musicSrv.getPosn()));

        handler.postDelayed(updatePositionRunnable, 500);
    }
    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {}

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        musicSrv.seek(seekBar.getProgress());

    }

    private String positionToString(long millis) {
        StringBuffer buf = new StringBuffer();

        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }
}
