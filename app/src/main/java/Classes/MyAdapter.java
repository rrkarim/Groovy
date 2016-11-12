package Classes;


/**
 * Created by YoAtom on 11/6/2016.
 */
import android.content.Context;
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
    Context cont;
    public MyAdapter(Context context, ArrayList<Post> urls) {
        super(context, 0, urls);
        cont = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Post postTmp = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }

        TextView rowTitle = (TextView) convertView.findViewById(R.id.rowTitle);
        ImageView imView = (ImageView) convertView.findViewById(R.id.rowImage);
        final TextView rowRate = (TextView) convertView.findViewById(R.id.rowRate);
        final ImageView button = (ImageView) convertView.findViewById(R.id.favB);

        rowTitle.setText(postTmp.getTitle());
        rowRate.setText("+" + postTmp.getLikesCount()); // fix this field

        /* SharedPreference to retrieve user info */
        //SharedPreferences sharedPref = cont.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        //final String userId = sharedPref.getString("email", null);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                postTmp.increaseCount();
                rowRate.setText("+" + postTmp.getLikesCount());
                button.setEnabled(false);
                likeClick(postTmp.getId());
            }
        });

        if(postTmp.checkBitMap() == false) new DownloadImageTask(imView, postTmp).execute(postTmp.getHeaderImage());
        else {
            imView.setImageBitmap(postTmp.getImageBitmap());
        }
        return convertView;

    }

    private void likeClick(int id) {
        String uri = AppInfo.serverUri + "/" + AppInfo.serverRequestLike;
        String parameters = "id=" + id;
        AsyncRequest asyncRequestObject = new AsyncRequest(this);
        asyncRequestObject.execute(uri, parameters);
    }

    @Override
    public void processFinish(String output) {
        if(output == null || output.equals("")) {
            return;
        }
        int jsonResult = returnParsedJsonObject(output);
        if(jsonResult == 0){
            System.out.println("error increasing like_count of the post");
            return;
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