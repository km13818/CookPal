package cse190.cookpal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class DirectionsStepListAdapter extends StepListAdapter {

    public DirectionsStepListAdapter(Context context, int resource, ArrayList<Step> stepList) {
        super(context, resource, stepList);
    }

    @Override
    public View inflateListItem(LayoutInflater inflaterView) {
        return inflaterView.inflate(R.layout.directions_steplist_listviewitem, null);
    }

    public void populateTextViews(View currView, Step currIterStep) {
        // TODO: make your own view (model after assistant_steplist_listviewitem.xml) and bind text views to data
        // TODO: see commented code below for example

        // Bind TextView references
        // Note: These TextViews are created in the XML files we defined.
        TextView stepNum = (TextView) currView.findViewById(R.id.direction_stepListItem_stepNum);
        TextView stepDescription = (TextView) currView.findViewById(R.id.direction_stepListItem_stepDescription);
        TextView stepTime = (TextView) currView.findViewById(R.id.direction_stepListItem_stepTimeTakes);

        // Populate views with data
        if(stepNum != null) {
            stepNum.setText(String.valueOf(currIterStep.getStepNumber()));
        }
        if(stepDescription != null) {
            stepDescription.setText(currIterStep.getDescription());
        }
        if(stepTime != null) {
            stepTime.setText(currIterStep.getTime());
        }
    }
}
