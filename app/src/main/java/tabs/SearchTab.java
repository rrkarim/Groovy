package tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.google.android.flexbox.FlexboxLayout;
import com.paranoidandroid.navigationbar.R;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;

import Classes.Post;
import Classes.SearchAdapter;
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

public class SearchTab extends Fragment implements AsyncResponse {
    private AsyncRequest asyncRequestObject;
    private ListView listView;
    private SearchAdapter myAdapter;
    private ArrayList<Post> listS;
    private ArrayList<CheckedTextView> listChecks;
    private SearchView searchView;
    private RelativeLayout contentSearch, searchLoad;

    private int mask = 0;
    private User currentUser;
    private UserCallback callback;

    public void setCallback(UserCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_search, container, false);
        View listviewHeader = View.inflate(getContext(), R.layout.search_header, null);
        searchView = (SearchView) listviewHeader.findViewById(R.id.searchView);
        searchView.clearFocus();
        View checkText = View.inflate(getContext(), R.layout.checktextview, null);

        currentUser = callback.getUser();

        addTags(listviewHeader);

        listView = (ListView) v.findViewById(R.id.listviewSearch);
        contentSearch = (RelativeLayout) v.findViewById(R.id.searchResult);
        searchLoad = (RelativeLayout) v.findViewById(R.id.searchLoad);
        contentSearch.setVisibility(View.INVISIBLE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        listView.addHeaderView(listviewHeader, null, false);

        final String uri = AppInfo.serverUri + "/" + AppInfo.serverRequestGetPost;

        asyncRequestObject = new AsyncRequest(this, -1, AsyncCode._SEARCH_NEWS);
        asyncRequestObject.execute(uri, "id=" + callback.getUser().getId());

        return v;
    }


    private void searchQuery(String query) {

        final String uri = AppInfo.serverUri + "/" + AppInfo.serverSearchNews;
        final String params = "query=" + query + "&mask=" + this.mask + "&id=" + currentUser.getId();


        contentSearch.setVisibility(View.INVISIBLE);
        searchLoad.setVisibility(View.VISIBLE);

        asyncRequestObject = new AsyncRequest(this, -1, AsyncCode._SEARCH_NEWS);
        asyncRequestObject.execute(uri, params);
    }

    private void addTags(View listviewHeader) {
        listChecks = new ArrayList<>();
        for(int i = 0; i < AppInfo.genresSize; ++i) {

            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout
                    .LayoutParams.WRAP_CONTENT);

            layoutParams.rightMargin = 20;
            layoutParams.bottomMargin = 20;
            CheckedTextView sample = new CheckedTextView(getContext());
            sample.setText(AppInfo.genres[i]);
            sample.setBackground(getResources().getDrawable(R.drawable.chk_indicator));
            sample.setPadding(20,20,20,20);
            sample.setLayoutParams(layoutParams);
            sample.setChecked(false);
            final int id = i;
            sample.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(id == 0) {
                        if(listChecks.get(id).isChecked() == false) {
                            mask = 0;
                            listChecks.get(id).setChecked(true);
                            for(int i = 1; i < AppInfo.genresSize; ++i) {
                                if(listChecks.get(i).isChecked() == true) {
                                    listChecks.get(i).setChecked(false);
                                }
                            }
                        }
                    }
                    else {
                        if (listChecks.get(id).isChecked() == true) {
                            mask = (mask ^ (1 << (id - 1)));
                            listChecks.get(id).setChecked(false);
                        } else {
                            mask = (mask | (1 << (id - 1)));
                            if (listChecks.get(0).isChecked() == true) {
                                listChecks.get(0).setChecked(false);
                            }
                            listChecks.get(id).setChecked(true);
                        }
                    }

                    searchQuery(searchView.getQuery().toString());

                }
            });
            listChecks.add(sample);
            FlexboxLayout headerLayout = (FlexboxLayout) listviewHeader.findViewById(R.id.flexLayout);
            headerLayout.addView(sample);
        }
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

        myAdapter = new SearchAdapter(getContext(), listS, callback.getUser());
        myAdapter.setCallback((PlayerCallback) getActivity());

        listView.setAdapter(myAdapter);

        contentSearch.setVisibility(View.INVISIBLE);
        searchLoad.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
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
        }
        else if(requestCode == AsyncCode._SEARCH_NEWS) {
            if (output == null || output.equals("")) {
                System.out.println(Errors._NEWS_GET_ERROR);
            }
            else {
                int jsonResult = returnParsedJsonObject(output);
                if(jsonResult > 0) {
                    parseData(output, jsonResult);
                }
                else {
                    listS.clear();
                    myAdapter.notifyDataSetChanged();
                    contentSearch.setVisibility(View.VISIBLE);
                    searchLoad.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
