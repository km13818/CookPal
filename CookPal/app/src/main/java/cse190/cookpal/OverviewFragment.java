package cse190.cookpal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by timchi on 11/28/14.
 */
public class OverviewFragment extends Fragment {

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.overview_tab, container, false);

        Recipe currRecipe = new Recipe("Grilled Chicken and Veggies");

        ImageView recipeImage = (ImageView) rootView.findViewById(R.id.overview_recipe_image);
        recipeImage.setImageResource(R.drawable.lasagna);

        TextView recipeName = (TextView) rootView.findViewById(R.id.overview_recipe_name);
        recipeName.setText(currRecipe.getRecipeName());
        recipeName.setBackground(getResources().getDrawable(R.drawable.overview_title_card));

        return rootView;
    }
}
