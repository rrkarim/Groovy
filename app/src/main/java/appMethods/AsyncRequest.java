package appMethods;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import Interfaces.AsyncResponse;

import static appMethods.RequestMethods.returnParsedJsonObject;

/**
 * Created by YoAtom on 11/11/2016.
 */

public class AsyncRequest extends AsyncTask<String, Void, String> {

    public AsyncResponse delegate = null; // use delegate to callback activity for postexecute
    private int positionAdapter;
    private int requestCode;

    public AsyncRequest(AsyncResponse context, int position, int requestCode) { // constructor for adapter
        this.delegate = context;
        this.positionAdapter = position;
        this.requestCode = requestCode;
    }

    public AsyncRequest() {}

    @Override
    protected String doInBackground(String... params) {
        // data we need to send HTTP request

        URL url = null;
        HttpURLConnection connection = null;
        String requestPar = params[1];

        try {
            url = new URL(params[0]);
            // Create connection
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(requestPar.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
            wr.writeBytes (requestPar);
            wr.flush ();
            wr.close ();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if(connection != null) {
                connection.disconnect(); // disconnect whenever we failed to send request or not
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(delegate != null) {
            delegate.processFinish(result, positionAdapter, requestCode);
        }
        else {
            int jsonResult = returnParsedJsonObject(result);
            if(jsonResult == 0){
                Log.w("error-request", "error in requesting server");
                return;
            }
        }
    }
}