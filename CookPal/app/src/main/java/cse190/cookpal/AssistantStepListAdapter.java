package cse190.cookpal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class AssistantStepListAdapter extends StepListAdapter {
    public AssistantStepListAdapter(Context context, int resource, ArrayList<Step> stepList, Step currStep) {
        super(context, resource, stepList);
    }

    @Override
    public View inflateListItem(LayoutInflater inflaterView) {
        return inflaterView.inflate(R.layout.assistant_steplist_listviewitem, null);
    }

    @Override
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
}
