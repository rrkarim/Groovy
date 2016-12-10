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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import Classes.Post;
import Interfaces.AsyncResponse;
import appClasses.AppInfo;
import appMethods.AsyncRequest;

import static appMethods.RequestMethods.downloadBitmap;
import static appMethods.RequestMethods.returnParsedJsonObject;

/**
 * Created by YoAtom on 10/12/2016.
 */

public class MyAdapter extends ArrayAdapter<Post> implements AsyncResponse {

    private final HashMap <Integer, ImageView> imVHash = new HashMap <>();
    private final HashMap <Integer, TextView> txtVHash = new HashMap<>();
    private final HashMap <Integer, Integer> userLikes = new HashMap<>();
    private final int deleteCount = 4;

    public MyAdapter(Context context, ArrayList<Post> urls) {
        super(context, 0, urls);
        //getUserLikes(1); // our user is 1 currently (superuser)
    }

    void getUserLikes(int uid) {
        userLikes.put(1, 1);
        userLikes.put(3, 1);
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
            System.out.println("hee");
            button.setEnabled(false);
        }

        if(imVHash.get(postId) == null) {
            imVHash.put(postId, button);
        }
        if(txtVHash.get(postId) == null) {
            txtVHash.put(postId, rowRate);
        }
        //System.out.println(position + " " + button);

        /*
            errors like:
                {
                    when you press like button and scroll down, you will remove object that you have to use in
                    process finish.
                }
            if(position >= deleteCount) {
                imVHash.remove(position);
                txtVHash.remove(position);
            }
        */

        rowTitle.setText(postTmp.getTitle());
        rowRate.setText("+" + postTmp.getLikesCount()); // fix this field

        /* SharedPreference to retrieve user info */
        //SharedPreferences sharedPref = cont.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        //final String userId = sharedPref.getString("email", null);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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

        AsyncRequest asyncRequestObject = new AsyncRequest(this, position);
        asyncRequestObject.execute(uri, parameters);
    }

    @Override
    public void processFinish(String output, int position) {

        if(output == null || output.equals("")) {
            return;
        }
        int jsonResult = returnParsedJsonObject(output);
        if(jsonResult == 0){
            System.out.println("error increasing like_count of the post");
        }
        else {
            System.out.println("successful increased likes count");
            final Post postTmp = getItem(position);
            postTmp.increaseCount();



            System.out.println(postTmp.getId());

            txtVHash.get(postTmp.getId()).setText("+" + postTmp.getLikesCount());
            imVHash.get(postTmp.getId()).setEnabled(false);

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