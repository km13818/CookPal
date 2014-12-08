package cse190.cookpal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by timchi on 11/28/14.
 */
public class DirectionsFragment extends Fragment {
    private ListAdapter stepListAdapter;
    private Step currStep;
    private ListView stepListView;
    private Recipe currRecipe;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.directions_tab, container, false);
        ImageButton startAssistantButton = (ImageButton) rootView.findViewById(R.id.start_assistant_button);

        currRecipe = RecipeActivity.getCurrentRecipe();

        stepListView = (ListView) rootView.findViewById(R.id.directions_tab_listview);
        if(null != currRecipe.getStepList() && currRecipe.getStepList().size() != 0) {
            currStep = currRecipe.getStepList().get(0);
        }

        // Bind the step list adapter
        stepListAdapter = new DirectionsStepListAdapter(RecipeActivity.getContext(),
                R.layout.directions_steplist_listviewitem, currRecipe.getStepList());
        stepListView.setAdapter(stepListAdapter);
        return rootView;
    }


}
