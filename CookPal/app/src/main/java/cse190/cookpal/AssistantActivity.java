package cse190.cookpal;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
    // Note: used to track previous currStep for step list highlighting purposes
    private Step prevCurrStep;

    private TextToSpeech assistantSpeaker;
    private String write;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        // Google analytics tracker
        //((CookPalApp) getApplication()).getTracker(CookPalApp.TrackerName.APP_TRACKER);

        // Initialize the speaker
        assistantSpeaker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR){
                            assistantSpeaker.setLanguage(Locale.UK);
                        }
                    }
                });
        write = "this is a test please speakerino";

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
            //currStep = currRecipe.getStepList().get(0);
            Step firstStep = currRecipe.getStepList().get(0);
            setCurrStep(firstStep);
        }

        // Bind the views
        stepNumView = (TextView) findViewById(R.id.assistant_stepNumber);
        stepTitleView = (TextView) findViewById(R.id.assistant_stepTitle);
        stepDescriptView = (TextView) findViewById(R.id.assistant_stepDescription);
        stepListView = (ListView) findViewById(R.id.assistant_stepListView);
        stepPreviewNumView = (TextView) findViewById(R.id.assistant_stepPreviewNumber);
        stepPreviewTitleView = (TextView) findViewById(R.id.assistant_stepPreviewTitle);
        stepPreviewDescriptView = (TextView) findViewById(R.id.assistant_stepPreviewDescription);

        // Text-to-speech tester
        stepDescriptView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                speakStepInfo(currStep);
            }
        });

        // Set the step information
        setCurrStepViewData(currStep);

        // Bind the step list adapter
        stepListAdapter = new AssistantStepListAdapter(this,
                R.layout.assistant_steplist_listviewitem, currRecipe.getStepList(), currStep);
        stepListView.setAdapter(stepListAdapter);

        // Handle clicking the step list
        stepListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Step clickedStep = (Step) parent.getItemAtPosition(position);

                // Populate the step preview with data from this clicked step
                stepPreviewNumView.setText(String.valueOf(clickedStep.getStepNumber()));
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
            // currStep = stepList.get(currStepIdx);
            setCurrStep(stepList.get(currStepIdx));
            changeCurrStepView(view);
        }
    }

    public void skipToStep(View view) {
        // Receive the step data that the user skipped to (set in the step list click listener)
        setCurrStep((Step) stepPreviewLayout.getTag());
        changeCurrStepView(view);
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
    public void onPause(){
        if(assistantSpeaker != null){
            assistantSpeaker.stop();
            assistantSpeaker.shutdown();
        }
        super.onPause();
    }

    private void speakStepInfo(Step step) {
        String speech = "Step " + step.getStepNumber() + ". " + step.getTitle() + ", " + step.getDescription();

        assistantSpeaker.speak(speech,TextToSpeech.QUEUE_FLUSH,null);
    }

    private void changeCurrStepView(View view) {
        // Set data and display the new view
        setCurrStepViewData(currStep);
        displayCurrStep(view);

        // Say the step information once the view is moved
        speakStepInfo(currStep);

        int numSteps = stepListView.getChildCount();
        for(int i = 0; i < numSteps; i++) {
            View v = stepListView.getChildAt(i);

            // Note: Tag key/value set in AssistantStepListAdapter.java
            int listItemStepNum = (Integer) v.getTag(R.id.stepNumber);

            // Unhighlight the previous currStep
//            if(listItemStepNum == prevCurrStep.getStepNumber()) {
//                updateStepListItemColors(v, R.color.grey, R.color.light_grey, R.color.grey);
//            }

            // Highlight the currStep. Note: Must happen after unhighlighting for 1st step case
            if(listItemStepNum == currStep.getStepNumber()) {
                updateStepListItemColors(v, R.color.orange, R.color.light_orange, R.color.orange);
            } else {
                // Unhighlight other steps
                updateStepListItemColors(v, R.color.grey, R.color.light_grey, R.color.grey);
            }
        }
    }

    private void updateStepListItemColors(View v, int numColor, int titleColor, int timeColor) {
        TextView stepNumView = (TextView) v.findViewById(R.id.stepListItem_stepNum);
        TextView stepTitleView = (TextView) v.findViewById(R.id.stepListItem_stepTitle);
        TextView stepTimeView = (TextView) v.findViewById(R.id.stepListItem_stepTimeTakes);

        // Populate the step preview with data from this clicked step
        stepNumView.setBackgroundResource(numColor);
        stepTitleView.setBackgroundResource(titleColor);
        stepTimeView.setTextColor(getResources().getColor(timeColor));
    }

    private void setCurrStep(Step newCurrStep) {
        if(this.prevCurrStep == null) {
            this.prevCurrStep = newCurrStep;
            this.currStep = newCurrStep;
        } else {
            this.prevCurrStep = currStep;
            this.currStep = newCurrStep;
        }
    }
}
