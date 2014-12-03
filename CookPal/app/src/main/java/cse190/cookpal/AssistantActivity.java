package cse190.cookpal;

import android.os.Bundle;
import android.view.*;
import android.widget.*;


public class AssistantActivity extends BaseDrawerActivity {

    private RelativeLayout currStepLayout;
    private RelativeLayout stepListLayout;
    private RelativeLayout stepPreviewLayout;

    private TextView stepNumView;
    private TextView stepTitleView;
    private TextView stepDescriptView;
    private ListView stepListView;

    private ListAdapter stepListAdapter;

    private Recipe currRecipe;
    private Step currStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

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

        // Set the step information
        stepNumView.setText(currStep.getStepNumber() + "");
        stepTitleView.setText(currStep.getTitle());
        stepDescriptView.setText(currStep.getDescription());

        // Bind the step list adapter
        stepListAdapter = new ArrayAdapter<Step>(this, android.R.layout.simple_list_item_1, currRecipe.getStepList());
        stepListView.setAdapter(stepListAdapter);


        // TODO: VERIFY THIS WORKS
        /*
        stepListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                stepListView.removeView(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });

            }
        */
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
        if(stepListLayout.getVisibility() == View.GONE) {
            stepListLayout.setVisibility(View.VISIBLE);
        } else {
            stepListLayout.setVisibility(View.GONE);
        }
    }
}
