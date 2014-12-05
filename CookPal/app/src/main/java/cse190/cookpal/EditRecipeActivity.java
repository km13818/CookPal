package cse190.cookpal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class EditRecipeActivity extends Activity {
    final Context thisContext = this;
    Recipe currentRecipe;
    HttpUtil httpUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpUtil = new HttpUtil();
        setContentView(R.layout.activity_edit_recipe);

        Intent intent = getIntent();
        currentRecipe = (Recipe)intent.getSerializableExtra("recipe");

        ((EditText)findViewById(R.id.recipeNameInput)).setText(currentRecipe.getRecipeName());


        ArrayList<Step> stepList = currentRecipe.getStepList();
        ArrayAdapter<Step> stepListAdapter = new StepListAdapter(stepList);
        ListView instructionsListView = (ListView) findViewById(R.id.instructionsListView);
        instructionsListView.setAdapter(stepListAdapter);

        ArrayList<Ingredients> ingredientList = currentRecipe.getIngredientList();
        ArrayAdapter<Ingredients> ingredientsListAdapter = new IngredientsListAdapter(ingredientList);
        ListView ingredientsListView = (ListView) findViewById(R.id.ingredientsListView);
        ingredientsListView.setAdapter(ingredientsListAdapter);

        Button updateRecipeButton = (Button) findViewById(R.id.updateRecipeButton);
        updateRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete
                HashMap<String,String> deleteRecipeParams = new HashMap<String,String>();
                deleteRecipeParams.put("r_name", currentRecipe.getRecipeName());
                deleteRecipeParams.put("fb_id", AccountActivity.getFbId());
                deleteRecipeParams.put("filter", "delete_recipe");
                httpUtil.makeHttpPost(deleteRecipeParams);

                //insert recipe
                HashMap<String,String> insertRecipeParams = new HashMap<String,String>();
                insertRecipeParams.put("r_name", currentRecipe.getRecipeName());
                insertRecipeParams.put("fb_id", AccountActivity.getFbId());
                insertRecipeParams.put("filter", "insert_recipe");
                insertRecipeParams.put("cookbook_type", "private");
                httpUtil.makeHttpPost(insertRecipeParams);


                //insert instructions

                //insert ingredients

                
                Intent i = new Intent(EditRecipeActivity.this, RecipeList.class);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class StepListAdapter extends ArrayAdapter<Step> {


        public StepListAdapter(ArrayList<Step> stepList) {
            super(EditRecipeActivity.this, R.layout.editrecipeinstruction_listview_entry, stepList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ArrayList<Step> stepList = currentRecipe.getStepList();


            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.editrecipeinstruction_listview_entry, parent, false);
            }
            final View thisConvertView = convertView;
            final String stepDescription = stepList.get(position).getDescription();
            EditText stepDescriptionTextView = (EditText) convertView.findViewById(R.id.instructionEditText);
            Log.d("editrecipeeacivity", "stepdesc editext: " + stepDescriptionTextView.toString());
            Log.d("editrecipeeacivity", "stepdescription: " + stepDescription);


            TextView stepNoTextView = (TextView) convertView.findViewById(R.id.stepNoTextView);
            stepNoTextView.setText(stepList.get(position).getStepNumber() + ".");
            stepDescriptionTextView.setText(stepDescription);

            EditText hoursEditText = (EditText) (convertView.findViewById(R.id.hoursEditText));

            Log.d("hoursEditText", hoursEditText.toString());
            hoursEditText.setText("12");
            EditText minutesEditText = (EditText) convertView.findViewById(R.id.minutesEditText);
            minutesEditText.setText(String.valueOf(stepList.get(position).getMinutes()));

            return convertView;
        }
    }
    private class IngredientsListAdapter extends ArrayAdapter<Ingredients> {
        public IngredientsListAdapter(ArrayList<Ingredients> ingredientsList) {
            super(EditRecipeActivity.this, R.layout.editrecipeingredient_listview_entry, ingredientsList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ArrayList<Ingredients> ingredientsList = currentRecipe.getIngredientList();


            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.editrecipeingredient_listview_entry, parent, false);
            }
            final View thisConvertView = convertView;
            final String name = ingredientsList.get(position).getIngredientName();
            final String quantity = ingredientsList.get(position).getQuantity();
            EditText nameEditText = (EditText) convertView.findViewById(R.id.ingredientEditText);
            EditText quantityEditText = (EditText) convertView.findViewById(R.id.quantityEditText);

            nameEditText.setText(name);
            quantityEditText.setText(quantity);

            return convertView;
        }
    }
    //sort steps

    // 2 1 3
    //1 2 3
    //1 2 3
   /* public ArrayList<Step> sortSteps (ArrayList<Step> stepList) {
        for (int i = 0; i < stepList.size(); i++) {
            for (int j = i; j < stepList.size(); j++) {
                if(stepList.get(j).getStepNumber() == i+1) {
                    //swap j with i
                    Step temp = stepList.get(j); //TODO: IMPLEMENT CLONE
                    stepList.set(j, stepList.get(i));
                    stepList.set(i,temp );
                    break; //break out inner
                }
            }
        }
    }*/
}
