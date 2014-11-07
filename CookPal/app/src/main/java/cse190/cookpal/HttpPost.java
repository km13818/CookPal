package cse190.cookpal;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by timchi on 11/6/14.
 */
public class HttpPost extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        Session session = Session.getActiveSession();
        String result = "failed";
        // if the session is already open,
        // try to show the selection fragment
        // Request user data and show the results
        Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    String url = "http://ec2-54-69-39-93.us-west-2.compute.amazonaws.com:8080/dbaccess.jsp";
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    org.apache.http.client.methods.HttpPost httppost = new org.apache.http.client.methods.HttpPost(url);


                    List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
                    postParameters.add(new BasicNameValuePair("username", user.getName()));
                    postParameters.add(new BasicNameValuePair("fb_id", user.getId()));

                    try {
                        httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    try {
                        HttpResponse httpResponse = httpclient.execute(httppost);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return result;
    }
}
