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

        //ArrayList<Ingredients> ingredientList = RecipeActivity.ingredientList;
        Ingredients i1 = new Ingredients("boneless chicken","1 lb");
        Ingredients i2 = new Ingredients("chicken broth","1/2 cup");
        Ingredients i3 = new Ingredients("oyster sauce","2 tbsp");
        Ingredients i4 = new Ingredients("garlic cloves, minced, ","2");
        Ingredients i5 = new Ingredients("chopped onion","1");
        Ingredients i6 = new Ingredients("chopped tomato","1/2");
        Ingredients i7 = new Ingredients("chopped cucumber","1");
        Ingredients i8 = new Ingredients("sesame oil, divided","2tsp");
        Ingredients i9 = new Ingredients("chopped red bell pepper","1/2 cup");


        ArrayList<Ingredients> ingredientList = new ArrayList<Ingredients>();
        ingredientList.add(i1);
        ingredientList.add(i2);
        ingredientList.add(i3);
        ingredientList.add(i4);
        ingredientList.add(i5);
        ingredientList.add(i6);
        ingredientList.add(i7);
        ingredientList.add(i8);
        ingredientList.add(i9);
        ingredientList.add(i1);
        ingredientList.add(i2);
        ingredientList.add(i3);
        ingredientList.add(i4);
        ingredientList.add(i5);
        ingredientList.add(i6);
        ingredientList.add(i7);
        ingredientList.add(i8);
        ingredientList.add(i9);

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
