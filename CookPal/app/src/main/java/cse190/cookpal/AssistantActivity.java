package cse190.cookpal;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.util.*;

import com.google.android.gms.analytics.GoogleAnalytics;


public class AssistantActivity extends BaseDrawerActivity {

    // Layouts
    private RelativeLayout currStepLayout;
    private RelativeLayout stepListLayout;
    private RelativeLayout stepPreviewLayout;

    // Current Step (assistant "home") views
    private TextView stepNumView;
    private TextView stepTitleView;
    private TextView stepDescriptView;

    // Step list view
    private ListView stepListView;

    // Step Preview (when user clicks from step list) views
    private TextView stepPreviewNumView;
    private TextView stepPreviewTitleView;
    private TextView stepPreviewDescriptView;

    private ListAdapter stepListAdapter;

    private Recipe currRecipe;
    private Step currStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        // Google analytics tracker
        ((CookPalApp) getApplication()).getTracker(CookPalApp.TrackerName.APP_TRACKER);

        // Bind the current step (main assistant page) view
        currStepLayout = (RelativeLayout) findViewById(R.id.assistant_currStep);

        // Hide the step list on the bottom (the ETC bar) until it's clicked
        stepListLayout = (RelativeLayout) findViewById(R.id.assistant_stepListLayout);
        stepListLayout.setVisibility(View.GONE);

        // Hide the step preview layout until user clicks on a step in the step list
        stepPreviewLayout = (RelativeLayout) findViewById(R.id.assistant_stepPreview);
        stepPreviewLayout.setVisibility(View.GONE);

        // Recipe creation
        //TODO: pull in recipe class from Intent.getIntent()? something like that.
        currRecipe = new Recipe("Chicken and Rice");
        if(null != currRecipe.getStepList()) {
            currStep = currRecipe.getStepList().get(0);
        }

        // Bind the views
        stepNumView = (TextView) findViewById(R.id.assistant_stepNumber);
        stepTitleView = (TextView) findViewById(R.id.assistant_stepTitle);
        stepDescriptView = (TextView) findViewById(R.id.assistant_stepDescription);
        stepListView = (ListView) findViewById(R.id.assistant_stepListView);
        stepPreviewNumView = (TextView) findViewById(R.id.assistant_stepPreviewNumber);
        stepPreviewTitleView = (TextView) findViewById(R.id.assistant_stepPreviewTitle);
        stepPreviewDescriptView = (TextView) findViewById(R.id.assistant_stepPreviewDescription);

        // Set the step information
        setCurrStepViewData(currStep);

        // Bind the step list adapter
//        stepListAdapter = new ArrayAdapter<Step>(
//                this, android.R.layout.simple_list_item_1, currRecipe.getStepList());
        stepListAdapter = new StepListAdapter(this, R.layout.assistant_steplist_listviewitem, currRecipe.getStepList());
        stepListView.setAdapter(stepListAdapter);

        // Handle clicking the step list
        stepListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Step clickedStep = (Step) parent.getItemAtPosition(position);

                // Populate the step preview with data from this clicked step
                stepPreviewNumView.setText(clickedStep.getStepNumber() + "");
                stepPreviewTitleView.setText(clickedStep.getTitle());
                stepPreviewDescriptView.setText(clickedStep.getDescription());

                // Save the clicked step data to be accessed if the user chooses to skip there
                stepPreviewLayout.setTag(clickedStep);

                displayStepPreview(view);
            }
        });
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

    public void moveToNextStep(View view) {
        int currStepIdx = currStep.getStepNumber();
        ArrayList<Step> stepList = currRecipe.getStepList();

        // If next step exists, move to it and update the view
        if(++currStepIdx < stepList.size()) {
            currStep = stepList.get(currStepIdx);
            setCurrStepViewData(currStep);
            displayCurrStep(view);
        }
    }

    public void skipToStep(View view) {
        // Receive the step data that the user skipped to (set in the step list click listener)
        currStep = (Step) stepPreviewLayout.getTag();
        setCurrStepViewData(currStep);
        displayCurrStep(view);
    }

    private void setCurrStepViewData(Step currStep) {
        stepNumView.setText(currStep.getStepNumber() + "");
        stepTitleView.setText(currStep.getTitle());
        stepDescriptView.setText(currStep.getDescription());
    }

    // Methods to show only the current layout and hide everything else so they aren't clickable
    public void displayStepList(View view) {
        stepListLayout.setVisibility(View.VISIBLE);
        currStepLayout.setVisibility(View.GONE);
        stepPreviewLayout.setVisibility(View.GONE);
    }

    public void displayStepPreview(View view) {
        stepListLayout.setVisibility(View.GONE);
        currStepLayout.setVisibility(View.GONE);
        stepPreviewLayout.setVisibility(View.VISIBLE);
    }

    public void displayCurrStep(View view) {
        stepListLayout.setVisibility(View.GONE);
        currStepLayout.setVisibility(View.VISIBLE);
        stepPreviewLayout.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        //start tracking
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //stop tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
