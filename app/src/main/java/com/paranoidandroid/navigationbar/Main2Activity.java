package com.paranoidandroid.navigationbar;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Classes.PlaylistAdapter;
import Classes.Post;
import Interfaces.MediaCallback;
import Interfaces.PlayerCallback;
import butterknife.ButterKnife;
import butterknife.InjectView;
import tabs.FavTab;
import tabs.HomeTab;
import tabs.ProfileTab;
import tabs.SearchTab;

public class Main2Activity extends AppCompatActivity implements MediaController.MediaPlayerControl, MediaCallback,
        PlayerCallback, SeekBar.OnSeekBarChangeListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private LinearLayout dragView;
    private SlidingUpPanelLayout slidingLayout;
    private PlaylistAdapter playlistAdapter;

    @InjectView(R.id.playPauseButton) ImageButton button;
    @InjectView(R.id.trackSeek) SeekBar seekBar;
    @InjectView(R.id.trackCurrentTime) TextView trackCurrentTime;
    @InjectView(R.id.trackFullTime) TextView trackFullTime;
    @InjectView(R.id.song_list) ListView songList;

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
            musicSrv.getContext(Main2Activity.this);
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
        setContentView(R.layout.activity_main2);

        ButterKnife.inject(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.

        seekBar.setOnSeekBarChangeListener(this);

        slidingLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        slidingLayout.setDragView(findViewById(R.id.dragRegion));
        slidingLayout.setEnableDragViewTouchEvents(true);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        dragView = (LinearLayout) findViewById(R.id.dragView);
        dragView.setVisibility(View.INVISIBLE);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post postTmp = (Post) parent.getItemAtPosition(position);
                listviewClicked(postTmp, position);
            }
        });

        setupTabIcons();

        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite_white_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_dehaze_white_24dp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        TextView favCount = (TextView) findViewById(R.id.favCount);
        favCount.setText(String.valueOf(track.getLikesCount()));
        TextView repostCount = (TextView) findViewById(R.id.repostCount);
        repostCount.setText(String.valueOf(track.getRepCounts()));
        TextView authorName = (TextView) findViewById(R.id.authorName);
        authorName.setText("Posted by " + track.getAuthorName() + " " + track.getAuthorSName());

        ImageView coverImage = (ImageView) findViewById(R.id.coverImage);
        Picasso.with(this).load(track.getHeaderImage()).into(coverImage);

        ImageButton favB = (ImageButton) findViewById(R.id.favB);

        if(track.isLiked() == true) {
            favB.setEnabled(false);
            favB.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_pink_24dp));
            favCount.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
        else {
            favB.setEnabled(true);
            favB.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp));
            favCount.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

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
        listviewClicked(post, position);
    }

    public void listviewClicked(Post post, int position) {
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

    public void readyDatasetListAdapter(ArrayList<Post> listS) {
        playlistAdapter = new PlaylistAdapter(this, listS);
        if(musicSrv != null) musicSrv.setList(listS); // fixes here
        songList.setAdapter(playlistAdapter);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0) {
                return new HomeTab();
            }
            else if(position == 1) {
                return new SearchTab();
            }
            else if(position == 2) {
                return new FavTab();
            }
            else {
                return new ProfileTab();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
