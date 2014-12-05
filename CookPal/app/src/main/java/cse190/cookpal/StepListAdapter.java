package cse190.cookpal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public abstract class StepListAdapter extends ArrayAdapter<Step> {
    private ArrayList<Step> stepList;

    public StepListAdapter(Context context, int resource, ArrayList<Step> stepList) {
        super(context, resource, stepList);
        this.stepList = stepList;
    }

    // Overrides getView to determine what the list item will look like
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Assign the view we are converting to a local variable
        View currView = convertView;

        // If view is null, inflate (render/show) it.
        if(currView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //v = inflater.inflate(R.layout.list_item, null);
            currView = inflater.inflate(R.layout.steplist_listviewitem, null);
        }

        // Note: ArrayAdapter will iterate through the list, calling getView() each time
        Step currIterStep = stepList.get(position);

        if(currIterStep != null) {
            populateTextViews(currView, currIterStep);
        }

        return currView;
    }

    public abstract void populateTextViews(View currView, Step currIterStep);
}