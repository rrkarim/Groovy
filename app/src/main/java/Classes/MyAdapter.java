package Classes;


/**
 * Created by YoAtom on 11/6/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import com.paranoidandroid.navigationbar.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.InputStream;
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

    //private final int deleteCount = 4;

    public MyAdapter(Context context, ArrayList<Post> urls) {
        super(context, 0, urls);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Post postTmp = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }

        TextView rowTitle = (TextView) convertView.findViewById(R.id.rowTitle);
        TextView userName = (TextView) convertView.findViewById(R.id.userName);
        TextView rowAuthor = (TextView) convertView.findViewById(R.id.rowAuthor);
        ImageButton imView = (ImageButton) convertView.findViewById(R.id.rowImage);
        CircularImageView profileImage = (CircularImageView) convertView.findViewById(R.id.profileImage);
        TextView rowRate = (TextView) convertView.findViewById(R.id.rowRate);
        TextView postDate = (TextView) convertView.findViewById(R.id.postDate);

        final ImageView button = (ImageView) convertView.findViewById(R.id.favB);

        if(postTmp.isLiked() == true) {
            button.setEnabled(false);
        }

        rowTitle.setText(postTmp.getTitle());
        userName.setText(postTmp.getAuthorName() + " " + postTmp.getAuthorSName());
        rowAuthor.setText(postTmp.getSinger());
        rowRate.setText(String.valueOf(postTmp.getLikesCount()));
        try {
            postDate.setText(DateToString.convert(postTmp.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.playPressed(postTmp, position);
                button.setEnabled(false);
                likeClick(postTmp.getId(), AppInfo.userId, position);
            }
        });

        imView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.playPressed(postTmp, position);
            }
        });


        Picasso.with(getContext()).load(postTmp.getAuthorImage()).into(profileImage);
        Picasso.with(getContext()).load(postTmp.getHeaderImage()).into(imView);

        return convertView;
    }

    private void likeClick(int id, int uid, int position) {

        String uri = AppInfo.serverUri + "/" + AppInfo.serverRequestLike;
        String parameters = "id=" + id + "  &uid=" + uid;

        AsyncRequest asyncRequestObject = new AsyncRequest(this, position, AsyncCode._SET_LIKE);
        asyncRequestObject.execute(uri, parameters);
    }

    @Override
    public void processFinish(String output, int position, int requestCode) {
        if(requestCode == AsyncCode._SET_LIKE) { // Like Post
            if (output == null || output.equals("")) {
                return;
            }
            int jsonResult = returnParsedJsonObject(output);
            final Post postTmp = getItem(position);

            if (jsonResult == 0) {
                System.out.println("error increasing like_count of the post");
                Toast.makeText(this.getContext(), Errors._LIKE_ERROR, Toast.LENGTH_LONG).show();
            } else {
                System.out.println("successful increased likes count");

                postTmp.increaseCount();

            }
        }
    }

    public void setCallback(PlayerCallback callback) {
        this.callback = callback;
    }
}