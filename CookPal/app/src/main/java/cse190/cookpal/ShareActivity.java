package cse190.cookpal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;


public class ShareActivity extends BaseDrawerActivity {

    Facebook facebookClient;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        //loginToFacebook();

        //if (facebookClient.isSessionValid()) {
            postToWall();
        //}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookClient.authorizeCallback(requestCode, resultCode, data);
    }

    public void loginToFacebook() {
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);

        if (access_token != null) {
            facebookClient.setAccessToken(access_token);
        }

        if (expires != 0) {
            facebookClient.setAccessExpires(expires);
        }

        if (!facebookClient.isSessionValid()) {
            facebookClient.authorize(this, new String[] { "publish_stream" }, new Facebook.DialogListener() {

                @Override
                public void onCancel() {
                    // Function to handle cancel event
                }

                @Override
                public void onComplete(Bundle values) {
                    // Function to handle complete event
                    // Edit Preferences and update facebook acess_token
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("access_token", facebookClient.getAccessToken());
                    editor.putLong("access_expires", facebookClient.getAccessExpires());
                    editor.commit();

                    postToWall();
                }

                @Override
                public void onError(DialogError error) {
                    // Function to handle error

                }

                @Override
                public void onFacebookError(FacebookError fberror) {
                    // Function to handle Facebook errors

                }

            });
        }
    }

    private void postToWall() {
        Bundle parameters = new Bundle();
        parameters.putString("name", "Battery Monitor");
        parameters.putString("link", "https://play.google.com/store/apps/details?id=com.ck.batterymonitor");
        parameters.putString("picture", "link to the picture");
        parameters.putString("display", "page");
        // parameters.putString("app_id", "228476323938322");

        facebookClient.dialog(ShareActivity.this, "feed", parameters, new Facebook.DialogListener() {

            @Override
            public void onFacebookError(FacebookError e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(DialogError e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onComplete(Bundle values) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
            }
        });
    }
}
