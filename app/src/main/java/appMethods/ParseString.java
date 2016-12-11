package appMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Classes.Post;
import appClasses.AppInfo;

/**
 * Created by YoAtom on 11/11/2016.
 */

public class ParseString {
    public static ArrayList<Post> StringToArrayPost(String str, int requestLen) throws JSONException {
        ArrayList<Post> listS = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(str);
        for(int i = 0; i < requestLen; ++i) {

            JSONObject object = jsonObject.getJSONObject(String.valueOf(i));
            if(object == null) break;
            listS.add(new Post(
                    object.getInt("id"),
                    object.getInt("likes_count"),
                    object.getInt("type_id"),
                    object.getInt("country_id"),
                    object.getString("title"),
                    object.getString("header_image"),
                    object.getString("text"))
                    );
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
}
