package cse190.cookpal;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by timchi on 11/28/14.
 */
public class IngredientsFragment extends Fragment{
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ingredients_tab, container, false);
        Recipe currentRecipe = RecipeActivity.getCurrentRecipe();
        ArrayList<Ingredients> ingredientList = currentRecipe.getIngredientList();

        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.ingredientLinearLayout);
        TextView ingredient;
        for(int i = 0; i < ingredientList.size(); i++){
            ingredient = new TextView(getActivity());
            //ingredient.setTextAppearance(RecipeActivity.getContext(), R.style.ingredientTVStyle);
            ingredient.setText(ingredientList.get(i).toString());
            //ingredient.setGravity(Gravity.CENTER);
            ingredient.setBackground(getResources().getDrawable(R.drawable.ingredient_card_bg));
            ingredient.setGravity(Gravity.CENTER);
            ingredient.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            ingredient.setTextColor(getResources().getColor(R.color.white));
            ll.addView(ingredient);
        }
        return rootView;
    }
}
