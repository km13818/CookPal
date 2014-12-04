package cse190.cookpal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by timchi on 11/28/14.
 */
public class DirectionsFragment extends Fragment {
    @SuppressLint("NewApi")
    @Override
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

        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.directionLinearLayout);
        TextView description;
        TextView time;
        int hoursTake = 0;
        int minutesTake = 0;

        for(int i = 0; i < directions.size(); i++){

            description = new TextView(getActivity());
            time = new TextView(getActivity());
            hoursTake = directions.get(i).getHours();
            minutesTake = directions.get(i).getMinutes();
//            //ingredient.setTextAppearance(RecipeActivity.getContext(), R.style.ingredientTVStyle);
            description.setText(directions.get(i).toStringDescription());
            time.setText(directions.get(i).getTime(hoursTake, minutesTake));
//            //ingredient.setGravity(Gravity.CENTER);
            description.setBackground(getResources().getDrawable(R.drawable.ingredient_card_bg));
//            ingredient.setGravity(Gravity.CENTER);
//            ingredient.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
//            ingredient.setTextColor(getResources().getColor(R.color.white));
            ll.addView(description);
            ll.addView(time);
        }
        return rootView;
    }
}
