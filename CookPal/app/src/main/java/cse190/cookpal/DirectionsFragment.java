package cse190.cookpal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

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

        Step s1 = new Step("","Combine 1 tblsp soy sauce, sherry, 2 tspn cornstarch, " +
                "and chicken in a large bowl; toss well to coat",0,0,1);
        Step s2 = new Step("","Combine remaining 2 table-spoons soy sauce, " +
                "remaining 2 tspns cornstarch, broth and honey in a small bowl",0,0,2);
        Step s3 = new Step("","Heat 1 teaspoon oil in a large nonstick skillet over medium-high",0,0,3);
        Step s4 = new Step("","Add chicken mixture to pan; saute 3 minutes. Remove from pan.",0,3,4);

        ArrayList<Step> directions = new ArrayList<Step>();
        directions.add(s1);
        directions.add(s2);
        directions.add(s3);
        directions.add(s4);

        //LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.directionLinearLayout);
        TextView description;
        TextView time;
        int hoursTake = 0;
        int minutesTake = 0;
        stepListView = (ListView) rootView.findViewById(R.id.directions_tab_listview);
        currRecipe = new Recipe("Chicken and Rice");
        if(null != currRecipe.getStepList()) {
            currStep = currRecipe.getStepList().get(0);
        }

        // Bind the step list adapter
        stepListAdapter = new DirectionsStepListAdapter(RecipeActivity.getContext(),
                R.layout.directions_steplist_listviewitem, currRecipe.getStepList());
        stepListView.setAdapter(stepListAdapter);
        return rootView;
    }
}
