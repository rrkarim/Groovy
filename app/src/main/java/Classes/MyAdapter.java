package Classes;


/**
 * Created by YoAtom on 11/6/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paranoidandroid.navigationbar.R;

import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import Classes.Post;
import Interfaces.AsyncResponse;
import appClasses.AppInfo;
import appClasses.AsyncCode;
import appClasses.Errors;
import appMethods.AsyncRequest;

import static appMethods.ParseString.StringToArrayLike;
import static appMethods.RequestMethods.downloadBitmap;
import static appMethods.RequestMethods.returnParsedJsonObject;

/**
 * Created by YoAtom on 10/12/2016.
 */

public class MyAdapter extends ArrayAdapter<Post> implements AsyncResponse {

    private final HashMap <Integer, ImageView> imVHash = new HashMap <>();
    private final HashMap <Integer, TextView> txtVHash = new HashMap<>();
    private final HashMap <Integer, Integer> userLikes = new HashMap<>();
    //private final int deleteCount = 4;

    public MyAdapter(Context context, ArrayList<Post> urls) {
        super(context, 0, urls);
        getUserLikes(AppInfo.USER_ID); // our user is 1 currently (superuser)
    }

    void getUserLikes(String uid) {
        String uri = AppInfo.serverUri + "/" + AppInfo.serverGetLikes;
        String parameters = "uid=" + uid;
        AsyncRequest asyncRequestObject = new AsyncRequest(this, -1, AsyncCode._GET_LIKES);
        asyncRequestObject.execute(uri, parameters);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Post postTmp = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }

        TextView rowTitle = (TextView) convertView.findViewById(R.id.rowTitle);
        ImageView imView = (ImageView) convertView.findViewById(R.id.rowImage);
        final TextView rowRate = (TextView) convertView.findViewById(R.id.rowRate);
        final ImageView button = (ImageView) convertView.findViewById(R.id.favB);

        final int postId = postTmp.getId(); // actual post id

        if(userLikes.get(postId) != null) {
            button.setEnabled(false);
        }
        else {
            button.setEnabled(true);
        }

        if(imVHash.get(position) == null) {
            imVHash.put(position, button);
        }
        if(txtVHash.get(position) == null) {
            txtVHash.put(position, rowRate);
        }

        rowTitle.setText(postTmp.getTitle());
        rowRate.setText("+" + postTmp.getLikesCount()); // fix this field


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                likeClick(postTmp.getId(), AppInfo.userId, position);
            }
        });

        if(postTmp.checkBitMap() == false) new DownloadImageTask(imView, postTmp).execute(postTmp.getHeaderImage());
        else {
            imView.setImageBitmap(postTmp.getImageBitmap());
        }
        return convertView;

    }

    private void likeClick(int id, int uid, int position) {

        String uri = AppInfo.serverUri + "/" + AppInfo.serverRequestLike;
        String parameters = "id=" + id + "  &uid=" + uid;

        AsyncRequest asyncRequestObject = new AsyncRequest(this, position, AsyncCode._SET_LIKE);
        asyncRequestObject.execute(uri, parameters);
    }

    void parseData(String str, int queryLen) {
        try {
            ArrayList<Integer> listS = StringToArrayLike(str, queryLen);
            System.out.println(listS.size());
            for(Integer i : listS) {
                if(userLikes.get(i) == null) userLikes.put(i, 1);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
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
                imVHash.get(position).setEnabled(true);
            } else {
                System.out.println("successful increased likes count");

                postTmp.increaseCount();
                txtVHash.get(position).setText("+" + postTmp.getLikesCount());
                userLikes.put(position, 1);

            }
        }
        else if(requestCode == AsyncCode._GET_LIKES) {
            if (output == null || output.equals("")) {
                System.out.println(Errors._LIKES_GET_ERROR);
            }
            else {
                int jsonResult = returnParsedJsonObject(output);
                if(jsonResult > 0) {
                    parseData(output, jsonResult);
                }
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        Post postTmp;

        public DownloadImageTask(ImageView bmImage, Post postTmp) {
            this.bmImage = bmImage;
            this.postTmp = postTmp;
        }

        protected Bitmap doInBackground(String... urls) {
            return downloadBitmap(urls[0]);
        }
        @Override
        protected void onPreExecute() {
            Drawable placeholder = bmImage.getContext().getResources().getDrawable(R.drawable.placeholder);
            bmImage.setImageDrawable(placeholder);
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            if(result != null) {
                bmImage.setImageBitmap(result);
                postTmp.SetBitmap(result);
            }
        }
    }
}