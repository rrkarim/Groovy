package appMethods;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Classes.User;

/**
 * Created by YoAtom on 11/11/2016.
 */

public class RequestMethods {
    public static int returnParsedJsonObject(String result){
        JSONObject resultObject = null;
        int returnedResult = 0;
        try {
            resultObject = new JSONObject(result);
            returnedResult = resultObject.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }
    static public Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();

            if (statusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
    static public User jsonToUser(String result) {
        JSONObject resultObject = null;
        User returnUser = new User();
        int returnedResult = 0;
        try {
            resultObject = new JSONObject(result);
            returnUser.setId(resultObject.getInt("id"));
            returnUser.setName(resultObject.getString("name"));
            returnUser.setSurname(resultObject.getString("surname"));
            returnUser.setImage(resultObject.getString("image"));
            returnUser.setImage_small(resultObject.getString("image_small"));
            returnUser.setDate(resultObject.getString("date"));
            returnUser.setFol_cont(resultObject.getInt("fol_count"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnUser;
    }
}
