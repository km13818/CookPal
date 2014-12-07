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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class EditRecipeActivity extends BaseDrawerActivity {
    final Context thisContext = this;
    Recipe currentRecipe;
    HttpUtil httpUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpUtil = new HttpUtil();
        setContentView(R.layout.activity_edit_recipe);

        Intent intent = getIntent();
        currentRecipe = (Recipe) intent.getSerializableExtra("recipe");

        ((EditText) findViewById(R.id.recipeNameInput)).setText(currentRecipe.getRecipeName());


        ArrayList<Step> stepList = currentRecipe.getStepList();
        ArrayList<Ingredients> ingredientList = currentRecipe.getIngredientList();

        for (Step s : stepList) {

            LinearLayout stepLayout = (LinearLayout) findViewById(R.id.instructionsEditLinearLayout);
            View newInstructionView = getLayoutInflater().inflate(R.layout.editrecipeinstruction_listview_entry, null);
            //1. [step] Estimated cooking time:   x hr y min
          //  Log.d("newinstructionview", newInstructionView.get)
            ((TextView) newInstructionView.findViewById(R.id.stepNoTextView)).setText(s.getStepNumber());
            ((EditText) newInstructionView.findViewById(R.id.instructionEditText)).setText(s.getTitle());
            ((EditText) newInstructionView.findViewById(R.id.hoursEditText)).setText(s.getHours() + "");
            ((EditText) newInstructionView.findViewById(R.id.minutesEditText)).setText(s.getMinutes() + "");
            stepLayout.addView(newInstructionView);
        }

        ImageButton addInstructionsButton = ((ImageButton) findViewById(R.id.addInstructionsButton));
        addInstructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout instructionLayout = (LinearLayout) findViewById(R.id.instructionsEditLinearLayout);
                View newInstructionView = getLayoutInflater().inflate(R.layout.editrecipeinstruction_listview_entry, null);

                instructionLayout.addView(newInstructionView);
            }
        });
        for (Ingredients i : ingredientList) {
            //[ingr]   "Quantity:" [5lbs]
            LinearLayout ingredientLayout = (LinearLayout) findViewById(R.id.ingredientsEditLinearLayout);
            View newIngredientView = getLayoutInflater().inflate(R.layout.editrecipeingredient_listview_entry, null);

            ((EditText) newIngredientView.findViewById(R.id.ingredientEditText)).setText(i.getIngredientName());
            ((EditText) newIngredientView.findViewById(R.id.quantityEditText)).setText(i.getQuantity());
        }

        ImageButton addIngredientsButton = ((ImageButton) findViewById(R.id.addIngredientsButton));
        addIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ingredientsLayout = (LinearLayout) findViewById(R.id.ingredientsEditLinearLayout);
                View newIngredientView = getLayoutInflater().inflate(R.layout.editrecipeingredient_listview_entry, null);

                ingredientsLayout.addView(newIngredientView);
            }
        });



        Button updateRecipeButton = (Button) findViewById(R.id.updateRecipeButton);
        updateRecipeButton.
                setOnClickListener(new View.OnClickListener() {
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
                insertRecipeParams.put("r_name", ((EditText)findViewById(R.id.recipeNameInput)).getText().toString());
                insertRecipeParams.put("fb_id", AccountActivity.getFbId());
                insertRecipeParams.put("filter", "insert_recipe");
                insertRecipeParams.put("cookbook_type", "private");
                insertRecipeParams.put("image_url", "");
                httpUtil.makeHttpPost(insertRecipeParams);

                ViewGroup instructionsEditLayout = (ViewGroup) findViewById(R.id.instructionsEditLinearLayout);
                Log.d("instrcount","instructions count: " + instructionsEditLayout.getChildCount());



                //loop through and insert instructions
                for(int i = 0;  i<instructionsEditLayout.getChildCount(); i++ ) {
                    //instructionLayout has many horizontal linearlayout as children, who each have children containing EditText
                    View horizontalView = instructionsEditLayout.getChildAt(i);

                    //loop throuhg view's children to find EditTexts

                    ViewGroup horizontalViewGroup = (ViewGroup)horizontalView;
                    TextView instructionNumView = (TextView)horizontalViewGroup.getChildAt(0);
                    EditText instructionEditText = (EditText) horizontalViewGroup.getChildAt(1);
                    EditText instructionHoursEditText = (EditText) horizontalViewGroup.getChildAt(3);
                    EditText instructionMinsEditText = (EditText) horizontalViewGroup.getChildAt(5);
                    Log.d("AddRecipeActivity", "instr: " + instructionEditText.getText().toString() + " hrs: " + instructionHoursEditText.getText().toString() + " mins: " + instructionMinsEditText.getText().toString());
                    //TODO: INSERT INSTRUCTION

                    HashMap<String,String> insertRecipeInstructionParams = new HashMap<String,String>();
                    insertRecipeInstructionParams.put("name", currentRecipe.getRecipeName());
                    insertRecipeInstructionParams.put("fb_id", AccountActivity.getFbId());
                    insertRecipeInstructionParams.put("instruction", instructionEditText.getText().toString());
                    insertRecipeInstructionParams.put("hrs", instructionHoursEditText.getText().toString());
                    insertRecipeInstructionParams.put("mins", instructionMinsEditText.getText().toString());
                    insertRecipeInstructionParams.put("step_no", instructionNumView.getText().toString().substring(0, instructionNumView.getText().toString().length() -1));
                    insertRecipeInstructionParams.put("filter", "insert_instruction");

                    httpUtil.makeHttpPost(insertRecipeInstructionParams);


                } //end for

                //insert ingredients
                ViewGroup ingredientsEditLayout = (ViewGroup) findViewById(R.id.ingredientsEditLinearLayout);
                Log.d("instrcount","instructions count: " + ingredientsEditLayout.getChildCount());

                //loop through and insert ingredients
                for(int i = 0; i < ingredientsEditLayout.getChildCount(); i++) {
                    //ingredientsLayout has many horizontal linearlayout as children, who each have children containing EditText
                    View horizontalView = ingredientsEditLayout.getChildAt(i);

                    //loop throuhg view's children to find EditTexts
                    ViewGroup horizontalViewGroup = (ViewGroup)horizontalView;
                    EditText ingredientEditText = (EditText) horizontalViewGroup.getChildAt(0);
                    EditText ingredientQuantityEditText = (EditText) horizontalViewGroup.getChildAt(2);
                    Log.d("AddRecipeActivity", "ingred: " + ingredientEditText.getText().toString() + " quantity: " + ingredientQuantityEditText.getText().toString());
                    //TODO: INSERT INGREDIENT
                    HashMap<String,String> insertIngredientParams = new HashMap<String,String>();
                    insertIngredientParams.put("name", currentRecipe.getRecipeName());
                    insertIngredientParams.put("fb_id", AccountActivity.getFbId());
                    insertIngredientParams.put("ingr_name", ingredientEditText.getText().toString());
                    insertIngredientParams.put("quantity",ingredientQuantityEditText.getText().toString());
                    insertIngredientParams.put("filter", "insert_ingredient");
                    httpUtil.makeHttpPost(insertIngredientParams);

                } //end for

                Intent i = new Intent(EditRecipeActivity.this, RecipeList.class);
                startActivity(i);
            }
        });//end updatebutton setonclick
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
}
