package Classes;


/**
 * Created by YoAtom on 11/6/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.paranoidandroid.navigationbar.MainActivity;
import com.paranoidandroid.navigationbar.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import Classes.Post;
import Interfaces.AsyncResponse;
import Interfaces.PlayerCallback;
import appClasses.AppInfo;
import appClasses.AsyncCode;
import appClasses.Errors;
import appMethods.AsyncRequest;
import appMethods.DateToString;

import static appMethods.ParseString.StringToArrayLike;
import static appMethods.RequestMethods.downloadBitmap;
import static appMethods.RequestMethods.returnParsedJsonObject;

/**
 * Created by YoAtom on 10/12/2016.
 */

public class MyAdapter extends ArrayAdapter<Post> implements AsyncResponse {

    private PlayerCallback callback;
    //HashMap<Integer, Integer> queryLikes = new HashMap<>();

    public MyAdapter(Context context, ArrayList<Post> urls) {
        super(context, 0, urls);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Post postTmp = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.position = position;
            viewHolder.rowTitle = (TextView) convertView.findViewById(R.id.rowTitle);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.rowAuthor = (TextView) convertView.findViewById(R.id.rowAuthor);
            viewHolder.imView = (ImageButton) convertView.findViewById(R.id.rowImage);
            viewHolder.profileImage = (CircularImageView) convertView.findViewById(R.id.profileImage);
            viewHolder.rowRate = (TextView) convertView.findViewById(R.id.rowRate);
            viewHolder.postDate = (TextView) convertView.findViewById(R.id.postDate);
            viewHolder.button = (ImageView) convertView.findViewById(R.id.favB);
            viewHolder.repostCount = (TextView) convertView.findViewById(R.id.repostCount);

            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        final ViewHolder viewHolderFinal = viewHolder;
        /*
        if(queryLikes.get(position) != null) {
            postTmp.increaseCount(queryLikes.get(position));
            queryLikes.remove(position);
        }
        */

        if(postTmp.isLiked() == true) {
            viewHolder.button.setEnabled(false);
            viewHolder.button.setImageDrawable( ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_pink_24dp));
            viewHolder.rowRate.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        }
        else {
            viewHolder.button.setEnabled(true);
            viewHolder.button.setImageDrawable( ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_black_24dp));
            viewHolder.rowRate.setTextColor(ContextCompat.getColor(getContext(), R.color.cardview_dark_background));
        }


        viewHolder.rowTitle.setText(postTmp.getTitle());
        viewHolder.userName.setText(postTmp.getAuthorName() + " " + postTmp.getAuthorSName());
        viewHolder.rowAuthor.setText(postTmp.getSinger());
        viewHolder.rowRate.setText(String.valueOf(postTmp.getLikesCount()));
        viewHolder.repostCount.setText(String.valueOf(postTmp.getRepCounts()));

        try {
            viewHolder.postDate.setText(DateToString.convert(postTmp.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        viewHolderFinal.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewHolderFinal.button.setEnabled(false);
                likeClick(postTmp.getId(), AppInfo.userId, position, viewHolderFinal, postTmp);
            }
        });

        viewHolder.imView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.playPressed(postTmp, position);
            }
        });


        Picasso.with(getContext()).load(postTmp.getAuthorImage()).into(viewHolder.profileImage);
        Picasso.with(getContext()).load(postTmp.getHeaderImage()).into(viewHolder.imView);

        return convertView;

    }

    private void likeClick(int id, int uid, int position, ViewHolder holder, Post postTmp) {


        String uri = AppInfo.serverUri + "/" + AppInfo.serverRequestLike;
        String parameters = "id=" + id + "  &uid=" + uid;

        holder.button.setEnabled(false);
        postTmp.increaseCount();
        holder.button.setImageDrawable( ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_pink_24dp));
        holder.rowRate.setText(String.valueOf(postTmp.getLikesCount()));
        holder.rowRate.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        AsyncRequest asyncRequestObject = new AsyncRequest(this, -1, AsyncCode._SET_LIKE);
        asyncRequestObject.execute(uri, parameters);
    }

    @Override
    public void processFinish(String output, int position, int requestCode) {
        if(requestCode == AsyncCode._SET_LIKE) { // Like Post
            // code goes here if user likes the post
        }
    }

    public void setCallback(PlayerCallback callback) {
        this.callback = callback;
    }
}