package cse190.cookpal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


public class AssistantActivity extends BaseDrawerActivity implements PausableCountdownTimer.TimerHandler {
    // TODO: Don't display nextStep button on last step --> maybe replace with finish button?
    // TODO: Add up/down caret on the ETC/step list to denote whether the list is up or down
    // TODO: make voice say initial step
    // TODO: add code to change the 'pause' button to 'play' button when timer ends

    // Layouts
    private RelativeLayout currStepLayout;
    private RelativeLayout stepListLayout;
    private RelativeLayout stepPreviewLayout;

    // Current Step (assistant "home") views
    private TextView stepNumView;
    private TextView stepTitleView;
    private TextView stepDescriptView;
    // Timer views
    private TextView timerDisplayView;
    private ImageButton playPauseButton;

    // Step list view
    private ListView stepListView;

    // Step Preview (when user clicks from step list) views
    private TextView stepPreviewNumView;
    private TextView stepPreviewTitleView;
    private TextView stepPreviewDescriptView;
    private TextView stepPreviewTimerDisplayView;

    private ListAdapter stepListAdapter;

    private Recipe currRecipe;
    private Step currStep;

    private TextToSpeech assistantSpeaker;

    private PausableCountdownTimer timer;

    private Button stepListIsDownButton;
    private Button stepListIsUpButton;
    private ImageButton nextStepButton;

    private final int ONE_SECOND_IN_MILLISECONDS = 1000;
    public static final String NOT_APPLICABLE = "n/a";
    public static final String OK = "OK";
    public static final String CANCEL = "Cancel";
    public static final String EXIT_ASSISTANT = "Exit Assistant?";
    public static final String ALL_DONE_RETURN_TO_RECIPE_LIST = "All done! Return to Recipe List?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent previousRecipeActivityIntent = getIntent();

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
                            speakStepInfo(currStep);
                        }
                    }
                });

        // Bind the current step (main assistant page) view
        currStepLayout = (RelativeLayout) findViewById(R.id.assistant_currStep);

        // Hide the step list on the bottom (the ETC bar) until it's clicked
        stepListLayout = (RelativeLayout) findViewById(R.id.assistant_stepListLayout);
        stepListLayout.setVisibility(View.GONE);

        // Hide the step preview layout until user clicks on a step in the step list
        stepPreviewLayout = (RelativeLayout) findViewById(R.id.assistant_stepPreview);
        stepPreviewLayout.setVisibility(View.GONE);

        // Bind the views
        stepNumView = (TextView) findViewById(R.id.assistant_stepNumber);
        stepTitleView = (TextView) findViewById(R.id.assistant_stepTitle);
        stepDescriptView = (TextView) findViewById(R.id.assistant_stepDescription);
        stepListView = (ListView) findViewById(R.id.assistant_stepListView);
        stepPreviewNumView = (TextView) findViewById(R.id.assistant_stepPreviewNumber);
        stepPreviewTitleView = (TextView) findViewById(R.id.assistant_stepPreviewTitle);
        stepPreviewDescriptView = (TextView) findViewById(R.id.assistant_stepPreviewDescription);
        stepPreviewTimerDisplayView = (TextView) findViewById(R.id.assistant_timerPreviewDisplay);
        timerDisplayView = (TextView) findViewById(R.id.assistant_timerDisplay);
        playPauseButton = (ImageButton) findViewById(R.id.assistant_playPauseButton);
        stepListIsDownButton = (Button) findViewById(R.id.assistant_stepListIsDownButton);
        stepListIsUpButton = (Button) findViewById(R.id.assistant_stepListIsUpButton);
        nextStepButton = (ImageButton) findViewById(R.id.assistant_nextStepButton);

        // Recipe creation
        currRecipe = (Recipe)previousRecipeActivityIntent.getSerializableExtra("recipe");

        if(null != currRecipe.getStepList()) {
            Step firstStep = currRecipe.getStepList().get(0);
            setCurrStepAndTimer(firstStep);
        }

        // Text-to-speech tester
        stepDescriptView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                speakStepInfo(currStep);
            }
        });

        // Set the step information
        changeCurrStepView_highlightStep_speakStep();

        // Update the time-to-completion text
        updateETCView();

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
                // stepPreviewTimerDisplayView.setText( PausableCountdownTimer.formattedTime(clickedStep.getTimeInMilliseconds()) );

                if (clickedStep.getTimeInMilliseconds() == 0) {
                    stepPreviewTimerDisplayView.setText(NOT_APPLICABLE);
                } else {
                    stepPreviewTimerDisplayView.setText(PausableCountdownTimer.formattedTime(clickedStep.getTimeInMilliseconds()));
                }

                // Save the clicked step data to be accessed if the user chooses to skip there
                stepPreviewLayout.setTag(clickedStep);

                displayStepPreview(view);
            }
        });
    }

    private void createTimer(Step step) {
        timer = new PausableCountdownTimer(step.getTimeInMilliseconds(),
                ONE_SECOND_IN_MILLISECONDS);
        timer.setHandler(this);
        timer.start();
        playPauseButton.setImageResource(R.drawable.assistant_pause_icon);
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
        else if(id == R.id.exitAssistantButton) {
            AlertDialog alertDialog = createAlertDialog(EXIT_ASSISTANT, OK, CANCEL);
            alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog createAlertDialog(String message, String posButtonText, String negButtonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message)
                .setPositiveButton(posButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(AssistantActivity.this, AssistantRecipeListActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(negButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    public void moveToNextStep(View view) {
        // Note: step starts at 1, not 0.
        int currStepIdx = currStep.getStepNumber() - 1;
        ArrayList<Step> stepList = currRecipe.getStepList();

        // If next step exists, move to it and update the view
        if(++currStepIdx < stepList.size()) {
            setCurrStepAndTimer(stepList.get(currStepIdx));
            changeCurrStepView_highlightStep_speakStep();
        }
    }

    public void skipToStep(View view) {
        // Receive the step data that the user skipped to (set in the step list click listener)
        setCurrStepAndTimer((Step) stepPreviewLayout.getTag());
        changeCurrStepView_highlightStep_speakStep();
    }

    // Methods to show only the current layout and hide everything else so they aren't clickable
    // Note: View parameter is needed as a placeholder for button onClick event in XML layout files
    public void displayStepList(View view) {
        View v = findViewById(R.id.assistant_stepListLayout);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_bottom);
        v.startAnimation(anim);
        stepListLayout.setVisibility(View.VISIBLE);

        View v2 = findViewById(R.id.assistant_currStep);
        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_out_top);
        v2.startAnimation(anim2);
        currStepLayout.setVisibility(View.GONE);

        stepPreviewLayout.setVisibility(View.GONE);

        updateETCView();
    }

    public void displayStepPreview(View view) {
        stepListLayout.setVisibility(View.GONE);
        currStepLayout.setVisibility(View.GONE);
        stepPreviewLayout.setVisibility(View.VISIBLE);

        updateETCView();
    }

    public void displayCurrStep(View view) {
        View v = findViewById(R.id.assistant_stepListLayout);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_out_bottom);
        v.startAnimation(anim);
        stepListLayout.setVisibility(View.GONE);

        View v2 = findViewById(R.id.assistant_currStep);
        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_top);
        v2.startAnimation(anim2);
        currStepLayout.setVisibility(View.VISIBLE);
        
        stepPreviewLayout.setVisibility(View.GONE);

        updateETCView();
    }

    // Updates the estimated time til completion and toggles the view
    private void updateETCView() {
        // TODO: calculate ETC with system time
        // Calendar calendar = GregorianCalendar.getInstance();
        // calendar.add(Calendar.MILLISECOND, millisLeftInRecipe());

        boolean listIsUp;
        if(stepListLayout.getVisibility() == View.GONE) {
            listIsUp = false;
        } else {
            listIsUp = true;
        }

        String remainingTime = PausableCountdownTimer.formattedTime(millisLeftInRecipe());

        if(listIsUp) {
            stepListIsUpButton.setText("hide steps. ETC: " + remainingTime);
        } else {
            stepListIsDownButton.setText("show steps. ETC: " + remainingTime);
        }
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
        if(step != null) {
            String speech = "Step " + step.getStepNumber() + ". " + step.getTitle() + ", " + step.getDescription();

            assistantSpeaker.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void changeCurrStepView_highlightStep_speakStep() {
        // Set data for the new currStep view and display it
        stepNumView.setText(currStep.getStepNumber() + "");
        stepTitleView.setText(currStep.getTitle());
        stepDescriptView.setText(currStep.getDescription());

        // Note: view parameter needed as placeholder. Just pass in null
        displayCurrStep(null);

        int numSteps = stepListView.getChildCount();
        for(int i = 0; i < numSteps; i++) {
            View v = stepListView.getChildAt(i);

            // Note: Tag key/value set in AssistantStepListAdapter.java
            int listItemStepNum = (Integer) v.getTag(R.id.stepNumber);

            // Highlight the currStep. Note: Must happen after unhighlighting for 1st step case
            if(listItemStepNum == currStep.getStepNumber()) {
                updateStepListItemColors(v, R.color.orange, R.color.light_orange, getResources().getColor(R.color.orange));
            } else {
                // Unhighlight other steps
                updateStepListItemColors(v, R.color.grey, R.color.light_grey, getResources().getColor(R.color.grey));
            }
        }

        // Say the step information once the view is moved
        speakStepInfo(currStep);
    }

    public static void updateStepListItemColors(View v, int numColor, int titleColor, int timeColor) {
        TextView stepNumView = (TextView) v.findViewById(R.id.stepListItem_stepNum);
        TextView stepTitleView = (TextView) v.findViewById(R.id.stepListItem_stepTitle);
        TextView stepTimeView = (TextView) v.findViewById(R.id.stepListItem_stepTimeTakes);

        // Populate the step preview with data from this clicked step
        stepNumView.setBackgroundResource(numColor);
        stepTitleView.setBackgroundResource(titleColor);
        stepTimeView.setTextColor(timeColor);
    }

    private void setCurrStepAndTimer(Step newCurrStep) {
        this.currStep = newCurrStep;

        if(timer != null) {
            timer.cancel();
        }

        createTimer(newCurrStep);

        // Explicitly set view if no time because onTick() updates it, but it won't be called
        if(newCurrStep.getTimeInMilliseconds() == 0) {
            timerDisplayView.setText(NOT_APPLICABLE);
        }

        // If we're on the last step, disable 'next step' button
        if(newCurrStep.getStepNumber() == stepListView.getChildCount()) {
            nextStepButton.setImageResource(R.drawable.disabled_next_step_icon);
        } else {
            nextStepButton.setImageResource(R.drawable.assistant_next_step_icon);
        }
    }

    // Timer functions and button click handlers
    public void pauseResumeTimer(View v) {
        if(timer != null) {
            if(timer.getState() == PausableCountdownTimer.TimerState.RUNNING) {
                timer.pause();
                playPauseButton.setImageResource(R.drawable.assistant_play_icon);
            } else {
                timer.resume();
                playPauseButton.setImageResource(R.drawable.assistant_pause_icon);
            }
        }
    }

    public void increaseTimer(View v) {
        if(timer != null) {
            timer.addTime(ONE_SECOND_IN_MILLISECONDS * 60);
            updateTimerView();
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        // Find the view and update with the new time --> happens every second
        updateTimerView();
        updateETCView();
    }

    @Override
    public void onFinish() {
        updateTimerView();
        updateETCView();
        playPauseButton.setImageResource(R.drawable.assistant_play_icon);

        if(currStep.getStepNumber() == stepListView.getChildCount()) {
            createAlertDialog(ALL_DONE_RETURN_TO_RECIPE_LIST, OK, CANCEL).show();
        }
    }

    private void updateTimerView() {
        if(timerDisplayView != null) {
            timerDisplayView.setText(timer.formatTimeRemaining());
        }
    }

    private long millisLeftInRecipe() {
        long millis = timer.getTimeRemaining();
        ArrayList<Step> steps = currRecipe.getStepList();
        for (Step s : steps.subList(currStep.getStepNumber(), steps.size())) {
            millis += s.getHours()*3600000 + s.getMinutes()*60000;
        }
        return millis;
    }
}
