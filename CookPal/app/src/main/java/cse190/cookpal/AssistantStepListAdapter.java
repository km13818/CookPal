package cse190.cookpal;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class AssistantStepListAdapter extends StepListAdapter {
    public AssistantStepListAdapter(Context context, int resource, ArrayList<Step> stepList) {
        super(context, resource, stepList);
    }

    public void populateTextViews(View currView, Step currIterStep) {
        // Bind TextView references
        // Note: These TextViews are created in the XML files we defined.
        TextView stepNum = (TextView) currView.findViewById(R.id.stepListItem_stepNum);
        TextView stepTitle = (TextView) currView.findViewById(R.id.stepListItem_stepTitle);
        TextView stepTime = (TextView) currView.findViewById(R.id.stepListItem_stepTimeTakes);

        // Populate views with data
        if(stepNum != null) {
            stepNum.setText(String.valueOf(currIterStep.getStepNumber()));
        }
        if(stepTitle != null) {
            stepTitle.setText(currIterStep.getTitle());
        }
        if(stepTime != null) {
            stepTime.setText(currIterStep.getTime());
        }
    }
}
