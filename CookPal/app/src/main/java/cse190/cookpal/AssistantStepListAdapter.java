package cse190.cookpal;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class AssistantStepListAdapter extends StepListAdapter {
    // Pass in and save current step to highlight it in the step list
    Step currStep;

    public AssistantStepListAdapter(Context context, int resource, ArrayList<Step> stepList, Step currStep) {
        super(context, resource, stepList);
        setCurrStep(currStep);
    }

    public void populateTextViews(View currView, Step currIterStep) {
        // Set tag to hold step number --> use as an ID to determine which step to highlight
        currView.setTag(R.id.stepNumber, currIterStep.getStepNumber());

        // Bind TextView references
        // Note: These TextViews are created in the XML files we defined.
        TextView stepNumView = (TextView) currView.findViewById(R.id.stepListItem_stepNum);
        TextView stepTitleView = (TextView) currView.findViewById(R.id.stepListItem_stepTitle);
        TextView stepTimeView = (TextView) currView.findViewById(R.id.stepListItem_stepTimeTakes);

        // Populate views with data
        if(stepNumView != null) {
            stepNumView.setText(String.valueOf(currIterStep.getStepNumber()));
        }
        if(stepTitleView != null) {
            stepTitleView.setText(currIterStep.getTitle());
        }
        if(stepTimeView != null) {
            stepTimeView.setText(currIterStep.getTime());
        }
    }

    public Step getCurrStep() {
        return currStep;
    }

    public void setCurrStep(Step currStep) {
        this.currStep = currStep;
    }
}
