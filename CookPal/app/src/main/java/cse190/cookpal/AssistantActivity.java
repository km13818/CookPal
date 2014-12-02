package cse190.cookpal;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;


public class AssistantActivity extends BaseDrawerActivity {

    private LinearLayout stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        stepList = (LinearLayout) findViewById(R.id.assistant_stepList);
        stepList.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.assistant, menu);
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

    public void stepListToggle(View view) {
        if(stepList.getVisibility() == View.GONE) {
            stepList.setVisibility(View.VISIBLE);
        } else {
            stepList.setVisibility(View.GONE);
        }
    }
}
