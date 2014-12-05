package cse190.cookpal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddRecipeActivity extends BaseDrawerActivity {
    final Context thisContext = this;
    HttpUtil httpUtil = new HttpUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        ((CookPalApp) getApplication()).getTracker(CookPalApp.TrackerName.APP_TRACKER);

        Button confirmAddButton = (Button) findViewById(R.id.confirm_create_recipe_button);
        confirmAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipeName = ((EditText) findViewById(R.id.recipeNameInput)).getText().toString();

                Intent intent = new Intent(AddRecipeActivity.this, RecipeList.class);
                intent.putExtra("RECIPE_NAME",recipeName);

                //INSERT RECIPE
                //recipe: id, account_id, cookbook_type, name,
                HashMap<String,String> insertRecipeParams = new HashMap<String,String>();
                insertRecipeParams.put("r_name", recipeName);
                insertRecipeParams.put("fb_id", AccountActivity.getFbId());
                insertRecipeParams.put("filter", "insert_recipe");
                insertRecipeParams.put("cookbook_type", "private");
                insertRecipeParams.put("image_url", "");
                httpUtil.makeHttpPost(insertRecipeParams);


                //recipe_ingredient: id,account_id,recipe_id, name, quantity
                //recipe_instruction: id,account_id,recipe_id, description, time, title

                //loop through all EditText
                ViewGroup group = (ViewGroup)findViewById(R.id.instructionsLayout);
                int instructionLayoutChildrenCount = group.getChildCount();
                Log.d("count=", String.valueOf(instructionLayoutChildrenCount));

                //loop through and insert instructions
                for(int i = 0;  i<instructionLayoutChildrenCount; i++ ) {
                    //instructionLayout has many horizontal linearlayout as children, who each have children containing EditText
                    View horizontalView = group.getChildAt(i);
                    if(horizontalView instanceof LinearLayout)
                    {
                        //loop throuhg view's children to find EditTexts
                        int horizontalLayoutChildrenCount = ((LinearLayout) horizontalView).getChildCount();
                        ViewGroup horizontalViewGroup = (ViewGroup)horizontalView;
                        TextView instructionNumView = (TextView)horizontalViewGroup.findViewById(R.id.add_recipe_instruction_step);
                        EditText instructionEditText = (EditText) horizontalViewGroup.findViewById(R.id.add_recipe_instruction_step_edit);
                        EditText instructionHoursEditText = (EditText) horizontalViewGroup.findViewById(R.id.add_recipe_instruction_hr_edit);
                        EditText instructionMinsEditText = (EditText) horizontalViewGroup.findViewById(R.id.add_recipe_instruction_min_edit);
                        Log.d("AddRecipeActivity", "instr: " + instructionEditText.getText().toString() + " hrs: " + instructionHoursEditText.getText().toString() + " mins: " + instructionMinsEditText.getText().toString());
                        //TODO: INSERT INSTRUCTION

                        HashMap<String,String> insertRecipeInstructionParams = new HashMap<String,String>();
                        insertRecipeInstructionParams.put("name", recipeName);
                        insertRecipeInstructionParams.put("fb_id", AccountActivity.getFbId());
                        insertRecipeInstructionParams.put("instruction", instructionEditText.getText().toString());
                        insertRecipeInstructionParams.put("hrs", instructionHoursEditText.getText().toString());
                        insertRecipeInstructionParams.put("mins", instructionMinsEditText.getText().toString());
                        insertRecipeInstructionParams.put("step_no", instructionNumView.getText().toString().substring(0, instructionNumView.getText().toString().length() -1));
                        insertRecipeInstructionParams.put("filter", "insert_instruction");

                        httpUtil.makeHttpPost(insertRecipeInstructionParams);

                    }
                } //end for
                //loop through all EditText
                ViewGroup addIngredientsLayoutGroup = (ViewGroup)findViewById(R.id.addIngredientsLayout);
                int ingredientsLayoutChildrenCount = addIngredientsLayoutGroup.getChildCount();

                //loop through and insert ingredients
                for(int i = 0; i < ingredientsLayoutChildrenCount; i++) {
                    //ingredientsLayout has many horizontal linearlayout as children, who each have children containing EditText
                    View horizontalView = addIngredientsLayoutGroup.getChildAt(i);
                    if(horizontalView instanceof LinearLayout)
                    {
                        //loop throuhg view's children to find EditTexts
                        int horizontalLayoutChildrenCount = ((LinearLayout) horizontalView).getChildCount();
                        ViewGroup horizontalViewGroup = (ViewGroup)horizontalView;
                        EditText ingredientEditText = (EditText) horizontalViewGroup.getChildAt(1);
                        EditText ingredientQuantityEditText = (EditText) horizontalViewGroup.getChildAt(3);
                        Log.d("AddRecipeActivity", "ingred: " + ingredientEditText.getText().toString() + " quantity: " + ingredientQuantityEditText.getText().toString());
                        //TODO: INSERT INGREDIENT
                        HashMap<String,String> insertIngredientParams = new HashMap<String,String>();
                        insertIngredientParams.put("name", recipeName);
                        insertIngredientParams.put("fb_id", AccountActivity.getFbId());
                        insertIngredientParams.put("ingr_name", ingredientEditText.getText().toString());
                        insertIngredientParams.put("quantity",ingredientQuantityEditText.getText().toString());
                        insertIngredientParams.put("filter", "insert_ingredient");
                        httpUtil.makeHttpPost(insertIngredientParams);
                    }
                } //end for
                startActivity(intent);
            }  //end onclick
        }); //end confirmaddbutton onclicklistener

        ImageButton addInstructionsButton = (ImageButton) findViewById(R.id.addInstructionsButton);
        addInstructionsButton.setOnClickListener(new View.OnClickListener() {

            int currInstructionCount = 0;
            @Override
            public void onClick(View v) {
                //add row to instructionsLayout
                currInstructionCount++;

                LinearLayout instructionsLayout = (LinearLayout) findViewById(R.id.instructionsLayout);
                View newInstructionView = getLayoutInflater().inflate(R.layout.add_recipe_instruction_entry, null);

                TextView instructionNum = (TextView) newInstructionView.findViewById(R.id.add_recipe_instruction_step);
                instructionNum.setText(String.valueOf(currInstructionCount) + ".");

                instructionsLayout.addView(newInstructionView);

            }
        });

        ImageButton addIngredientsButton = (ImageButton) findViewById(R.id.addIngredientsButton);
        addIngredientsButton.setOnClickListener(new View.OnClickListener() {

            int currIngredientCount = 0;
            @Override
            public void onClick(View v) {
                //add row to instructionsLayout
                currIngredientCount++;

                LinearLayout ingredientsLayout = (LinearLayout) findViewById(R.id.addIngredientsLayout);
                View newIngredientView = getLayoutInflater().inflate(R.layout.add_recipe_ingredients_entry, null);

                TextView ingredientNum = (TextView) newIngredientView.findViewById(R.id.add_recipe_ingredients_step);
                ingredientNum.setText(String.valueOf(currIngredientCount) + ".");

                ingredientsLayout.addView(newIngredientView);
            }
        });
    }//end on create



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_recipe, menu);
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


    @Override
    public void onStart() {
        super.onStart();
        //start tracking
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        //stop tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);

    }
}
