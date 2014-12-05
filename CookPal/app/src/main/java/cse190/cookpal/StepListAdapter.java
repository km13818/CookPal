package cse190.cookpal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StepListAdapter extends ArrayAdapter<Step> {
    private ArrayList<Step> stepList;

    public StepListAdapter(Context context, int resource, ArrayList<Step> stepList) {
        super(context, resource, stepList);
        this.stepList = stepList;
    }

    // Overrides getView to determine what the list item will look like
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Assign the view we are converting to a local variable
        View currView = convertView;

        // If view is null, inflate (render/show) it.
        if(currView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //v = inflater.inflate(R.layout.list_item, null);
            currView = inflater.inflate(R.layout.assistant_steplist_listviewitem, null);
        }

        // Note: ArrayAdapter will iterate through the list, calling getView() each time
        Step currIterStep = stepList.get(position);

        if(currIterStep != null) {
            // Bind TextView references
            // Note: These TextViews are created in the XML files we defined.
            TextView stepNum = (TextView) currView.findViewById(R.id.assistant_stepListItem_stepNum);
            TextView stepTitle = (TextView) currView.findViewById(R.id.assistant_stepListItem_stepTitle);
            TextView stepTime = (TextView) currView.findViewById(R.id.assistant_stepListItem_stepTimeTakes);

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
        return currView;
    }
}