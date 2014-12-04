package cse190.cookpal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by timchi on 11/28/14.
 */
public class IngredientsFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ingredients_tab, container, false);

        //ArrayList<Ingredients> ingredientList = RecipeActivity.ingredientList;
        Ingredients i1 = new Ingredients("brocolli","3 lbs");
        Ingredients i2 = new Ingredients("peas","4 lbs");
        Ingredients i3 = new Ingredients("pie","2 lbs");
        Ingredients i4 = new Ingredients("chunk monkey","5 lbs");
        Ingredients i5 = new Ingredients("wilted flowers","34 lbs");


        ArrayList<Ingredients> ingredientList = new ArrayList<Ingredients>();
        ingredientList.add(i1);
        ingredientList.add(i2);
        ingredientList.add(i3);
        ingredientList.add(i4);
        ingredientList.add(i5);


        ListView lv = (ListView) rootView.findViewById(R.id.ingredientListView);
        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.ingredientLinearLayout);
        TextView ingredient;
        for(int i = 0; i < ingredientList.size(); i++){
            ingredient = new TextView(getActivity());
            ingredient.setText(ingredientList.get(i).toString());
            ll.addView(ingredient);
        }
        return rootView;
    }
}
