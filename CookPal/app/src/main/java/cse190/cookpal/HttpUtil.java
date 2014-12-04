package cse190.cookpal;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kevin Ma on 11/20/2014.
 */
public class HttpUtil {

    String url = "http://ec2-54-69-39-93.us-west-2.compute.amazonaws.com:8080/request_handler.jsp";
    //recipe: id, account_id, cookbook_type, name, description
    //recipe_ingredient: id,account_id,recipe_id, name, quantity
    //recipe_instruction: id,account_id,recipe_id, description, time, title
    public void makeHttpPost(HashMap<String, String> params) {
        new LongOperation().execute(params);
    }

    //TODO: refactor into util class
    private class LongOperation  extends AsyncTask<HashMap<String,String>, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();

        protected void onPreExecute() {
            Log.d("httputil", "http request onPreExecute.....");

        }

        // Call after onPreExecute method
        protected Void doInBackground(HashMap<String,String>... params) {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);


            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            for (String keyString : params[0].keySet()) {
                postParameters.add(new BasicNameValuePair(keyString, params[0].get(keyString)));
                Log.d("httppost", "adding post parameter. key: " + keyString + " value: " + params[0].get(keyString));
            }

            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                HttpResponse httpResponse = httpclient.execute(httppost);
                Log.e("HttpUtil", "http post SUCCESS");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }//end doInBackGround

        protected void onPostExecute(Void unused) {

        }
    }//END ASYNCTASK
}



