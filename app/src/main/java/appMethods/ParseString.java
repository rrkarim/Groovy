package appMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Classes.Actions;
import Classes.Author;
import Classes.Post;
import appClasses.AppInfo;

/**
 * Created by YoAtom on 11/11/2016.
 */

public class ParseString {
    public static ArrayList<Post> StringToArrayPost(String str, int requestLen) throws JSONException, ParseException {
        ArrayList<Post> listS = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(str);
        for(int i = 0; i < requestLen; ++i) {

            JSONObject object = jsonObject.getJSONObject(String.valueOf(i));
            if(object == null) break;

            JSONObject objectAuthor = object.getJSONObject("user");
            JSONObject objectAction = object.getJSONObject("actions");


            String dateOfAuthor = object.getString("date");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date dateAuthor = format.parse(dateOfAuthor);

            Author author = new Author(objectAuthor.getInt("id"), objectAuthor.getString("name"), objectAuthor.getString("surname"), objectAuthor.getString("image_small"), dateAuthor);
            Actions actions = new Actions(objectAction.getInt("like"), objectAction.getInt("post"));

            String dateOfAPost = object.getString("date");
            Date datePost = format.parse(dateOfAPost);

            listS.add(new Post(
                            object.getInt("id"),
                            object.getString("title"),
                            object.getString("singer"),
                            object.getString("header_image"),
                            object.getInt("likes_count"),
                            object.getInt("rep_counts"),
                            object.getInt("author_id"),
                            object.getString("album"),
                            object.getInt("type_id"),
                            object.getString("track"),
                            object.getString("text"),
                            author,
                            datePost,
                            actions
                    ));
        }
        return listS;
    }

    public static ArrayList<Integer> StringToArrayLike(String str, int requestLen) throws JSONException {
        ArrayList<Integer> listS = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(str);
        for(int i = 0; i < requestLen; ++i) {
            JSONObject object = jsonObject.getJSONObject(String.valueOf(i));
            if(object == null) break;
            listS.add(object.getInt("pid"));
        }
        return listS;
    }

    public static String LastFmToString(String str) throws JSONException, ParseException {
        JSONObject jsonObject = new JSONObject(str);

        JSONObject object = jsonObject.getJSONObject("artist");

        JSONArray arrayObject = object.getJSONArray("image");

        String artistName = arrayObject.getString(3);

        return artistName;
    }
}
