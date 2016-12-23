package tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.paranoidandroid.navigationbar.R;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;

import Classes.MyAdapter;
import Classes.Post;
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
 * Created by YoAtom on 12/18/2016.
 */

public class HomeTab extends Fragment implements AsyncResponse {

    private ListView listView;
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
        View v = inflater.inflate(R.layout.fragment_tab,container,false);

        listView = (ListView) v.findViewById(R.id.listview);
        layout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);

        final String uri = AppInfo.serverUri + "/" + AppInfo.serverRequestGetPost;
        final String params = "id=" + callback.getUser().getId() + "&feed=1";

        asyncRequestObject = new AsyncRequest(this, -1, AsyncCode._GET_NEWS);
        asyncRequestObject.execute(uri, params);

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                asyncRequestObject = new AsyncRequest(HomeTab.this, -1, AsyncCode._GET_NEWS);
                asyncRequestObject.execute(uri, params);
            }
        });

        return v;
    }

    void parseData(String str, int queryLen) {
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

        listView.setAdapter(myAdapter);

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