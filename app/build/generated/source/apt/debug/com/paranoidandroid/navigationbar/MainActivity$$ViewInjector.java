// Generated code from Butter Knife. Do not modify!
package com.paranoidandroid.navigationbar;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainActivity$$ViewInjector {
  public static void inject(Finder finder, final com.paranoidandroid.navigationbar.MainActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558550, "field 'button'");
    target.button = (android.widget.ImageButton) view;
    view = finder.findRequiredView(source, 2131558552, "field 'seekBar'");
    target.seekBar = (android.widget.SeekBar) view;
    view = finder.findRequiredView(source, 2131558551, "field 'trackCurrentTime'");
    target.trackCurrentTime = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558553, "field 'trackFullTime'");
    target.trackFullTime = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131558561, "field 'songList'");
    target.songList = (android.widget.ListView) view;
  }

  public static void reset(com.paranoidandroid.navigationbar.MainActivity target) {
    target.button = null;
    target.seekBar = null;
    target.trackCurrentTime = null;
    target.trackFullTime = null;
    target.songList = null;
  }
}
