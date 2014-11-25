package cse190.cookpal;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kevin Ma on 11/20/2014.
 */
public class HttpUtil {
    //recipe: id, account_id, cookbook_type, name, description
    //recipe_ingredient: id,account_id,recipe_id, name, quantity
    //recipe_instruction: id,account_id,recipe_id, description, time, title
    public static HttpResponse makeHttpPost(HashMap<String, String> params) {
        String url = "http://ec2-54-69-39-93.us-west-2.compute.amazonaws.com:8080/request_handler.jsp";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        org.apache.http.client.methods.HttpPost httppost = new org.apache.http.client.methods.HttpPost(url);


        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        for (String keyString : params.keySet()) {
            postParameters.add(new BasicNameValuePair(keyString, params.get(keyString)));
            Log.d("httppost", "key: " + keyString + " value: " + params.get(keyString));
        }

        try {
            httppost.setEntity(new UrlEncodedFormEntity(postParameters));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            HttpResponse httpResponse = httpclient.execute(httppost);
            Log.e("HttpUtil", "SUCCESS");
            return httpResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
