package tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.paranoidandroid.navigationbar.LoginActivity;
import com.paranoidandroid.navigationbar.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;

import Classes.Global;
import Classes.MyAdapter;
import Classes.Post;
import Classes.User;
import Interfaces.AsyncResponse;
import Interfaces.PlayerCallback;
import Interfaces.UserCallback;
import appClasses.AppInfo;
import appClasses.AsyncCode;
import appClasses.Errors;
import appMethods.AsyncRequest;

import static appMethods.ParseString.StringToArrayPost;
import static appMethods.RequestMethods.returnParsedJsonObject;

/**
 * Created by YoAtom on 12/19/2016.
 */

public class ProfileTab extends Fragment implements View.OnClickListener, AsyncResponse {

    private ListView profileList;
    private MyAdapter myAdapter;
    private ArrayList<Post> listS;
    private AsyncRequest asyncRequestObject;
    private SwipeRefreshLayout layout;
    private UserCallback callback;

    public void setCallback(UserCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        View listviewHeader = View.inflate(getContext(), R.layout.profile_header, null);

        final User currentUser = callback.getUser();

        profileList = (ListView) v.findViewById(R.id.profileList);
        layout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        TextView profileFoll = (TextView) listviewHeader.findViewById(R.id.profileFoll);
        TextView profileName = (TextView) listviewHeader.findViewById(R.id.profileName);
        profileName.setText(currentUser.getName() + " " + currentUser.getSurname());
        profileFoll.setText(currentUser.getFol_cont() + " followers");
        CircularImageView profileImage = (CircularImageView) listviewHeader.findViewById(R.id.profileImage2);
        Picasso.with(getContext()).load(currentUser.getImage()).into(profileImage);


        ImageButton logoutButton = (ImageButton) listviewHeader.findViewById(R.id.log_out);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.logoutAction();
            }
        });


        profileList.addHeaderView(listviewHeader, null, false);

        final String uri = AppInfo.serverUri + "/" + AppInfo.serverRequestGetPost;
        final String params = "user_id=" + currentUser.getId() + "&id=" + currentUser.getId();
        asyncRequestObject = new AsyncRequest(this, -1, AsyncCode._GET_NEWS);
        asyncRequestObject.execute(uri, params);

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                asyncRequestObject = new AsyncRequest(ProfileTab.this, -1, AsyncCode._GET_NEWS);
                asyncRequestObject.execute(uri, params);
            }
        });

        return v;
    }
    @Override
    public void onClick(View v) {
    }

    private void parseData(String str, int queryLen) {
        listS = new ArrayList<>();
        try {
            listS = StringToArrayPost(str, queryLen);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        myAdapter = new MyAdapter(getContext(), listS, this.callback.getUser());
        myAdapter.setCallback((PlayerCallback) getActivity());

        profileList.setAdapter(myAdapter);

    }

    @Override
    public void processFinish(String output, int position, int requestCode) {
        if(requestCode == AsyncCode._GET_NEWS) {
            if (output == null || output.equals("")) {
                System.out.println(Errors._NEWS_GET_ERROR);
            }
            else {
                int jsonResult = returnParsedJsonObject(output);
                if(jsonResult > 0) {
                    parseData(output, jsonResult);
                }
            }
            layout.setRefreshing(false);
        }
        else if(requestCode == AsyncCode._GET_LASTFM) {

        }
    }
}
