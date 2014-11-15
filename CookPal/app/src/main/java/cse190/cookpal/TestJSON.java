package cse190.cookpal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TestJSON extends Activity {

    Button getJson;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_json);

        getJson = (Button) findViewById(R.id.getJson);

        // WebServer Request URL
        String serverURL = "http://androidexample.com/media/webservice/JsonReturn.php";

        // Use AsyncTask execute Method To Prevent ANR Problem
        //new LongOperation().execute(serverURL);
    }

    public void getJson(View view){
        JSONObject obj = new JSONObject();

        try {
            obj.put("name", "mkyong.com");
            obj.put("age", new Integer(100));

            JSONArray list = new JSONArray();
            list.put("msg 1");
            list.put("msg 2");
            list.put("msg 3");

            obj.put("messages", list);
        } catch (JSONException e) {
        }

        Log.e("testjson","obj:  "+obj);

        JSONParser parser = new JSONParser();
        result = (TextView) findViewById(R.id.result);
        String st;
        try {
            st = obj.getString("messages");
            result.setText(st);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_json, menu);
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
}
