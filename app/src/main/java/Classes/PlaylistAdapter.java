package Classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.paranoidandroid.navigationbar.R;

import java.util.ArrayList;

/**
 * Created by YoAtom on 12/16/2016.
 */

public class PlaylistAdapter extends ArrayAdapter<Post> {

    public PlaylistAdapter(Context context, int resource) {
        super(context, resource);
    }

    public PlaylistAdapter(Context context, ArrayList<Post> listS) {
        super(context, 0, listS);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Post postTmp = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.playlist_row, parent, false);
        }

        TextView rowTitle = (TextView) convertView.findViewById(R.id.playlistName);
        TextView rowSinger = (TextView) convertView.findViewById(R.id.playlistSinger);

        rowTitle.setText(postTmp.getTitle());
        rowSinger.setText(postTmp.getSinger());

        return convertView;

    }
}
