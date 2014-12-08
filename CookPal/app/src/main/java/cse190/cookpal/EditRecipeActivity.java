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

        ((EditText)findViewById(R.id.editRecipeNameInput)).setText(currentRecipe.getRecipeName());
        Log.e("testing", currentRecipe.getImgUrl());
        ((EditText)findViewById(R.id.editRecipeImageUrlInput)).setText(currentRecipe.getImgUrl());

        ArrayList<Step> stepList = currentRecipe.getStepList();
        ArrayList<Ingredients> ingredientList = currentRecipe.getIngredientList();

        for (Step s : stepList) {

            LinearLayout stepLayout = (LinearLayout) findViewById(R.id.instructionsEditLinearLayout);
            View newInstructionView = getLayoutInflater().inflate(R.layout.editrecipeinstruction_listview_entry, null);
            //1. [step] Estimated cooking time:   x hr y min
          //  Log.d("newinstructionview", newInstructionView.get)
            TextView stepText = ((TextView) newInstructionView.findViewById(R.id.stepNoTextView));
            stepText.setText(Integer.toString(s.getStepNumber()) +  ".");
            //((TextView) newInstructionView.findViewById(R.id.stepNoTextView)).setText(s.getStepNumber());
            ((EditText) newInstructionView.findViewById(R.id.instructionTitleEditText)).setText(s.getTitle());
            ((EditText) newInstructionView.findViewById(R.id.instructionEditText)).setText(s.getDescription());
            ((EditText) newInstructionView.findViewById(R.id.hoursEditText)).setText(s.getHours() + "");
            ((EditText) newInstructionView.findViewById(R.id.minutesEditText)).setText(s.getMinutes() + "");

            ImageButton deleteInstructionButton = (ImageButton) newInstructionView.findViewById(R.id.edit_recipe_instruction_delete);
            deleteInstructionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get the parent's parent layout of this entry (structure of entry)
                    ViewGroup parentView = (ViewGroup)v.getParent().getParent();
                    //use the parent viewGroup to find the index of the current layout
                    int currentIndex = ((ViewGroup)parentView.getParent()).indexOfChild(parentView);
                    Log.e("index of current view's parent", currentIndex + "");
                    LinearLayout ingredientsLayout = (LinearLayout) findViewById(R.id.instructionsEditLinearLayout);
                    LinearLayout newInstructionView = (LinearLayout) ingredientsLayout.getChildAt(currentIndex);
                    ingredientsLayout.removeView(newInstructionView);
                    //decrease the ingredient counter
                    rebuildCounter();
                }

                //ensure the list order is always proper (i.e. no gaps)
                public void rebuildCounter() {
                    ViewGroup ingGroup = (ViewGroup)findViewById(R.id.instructionsEditLinearLayout);
                    //iterate through all the children and reset the step counter
                    for(int i = 0; i < (ingGroup.getChildCount() + 1); i++) {
                        View horizontalView = ingGroup.getChildAt(i);
                        if(horizontalView instanceof LinearLayout) {
                            ViewGroup horizontalViewGroup = (ViewGroup)horizontalView;
                            TextView ingredientNumView = (TextView)horizontalViewGroup.findViewById(R.id.stepNoTextView);

                            ingredientNumView.setText(i+1 + ".");
                        }
                    }
                }
            });
            stepLayout.addView(newInstructionView);
        }

        ImageButton addInstructionsButton = ((ImageButton) findViewById(R.id.addInstructionsButton));
        addInstructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout instructionLayout = (LinearLayout) findViewById(R.id.instructionsEditLinearLayout);
                Log.d("editrecipe", "num instructions b4 adding; " + instructionLayout.getChildCount());
                View newInstructionView = getLayoutInflater().inflate(R.layout.editrecipeinstruction_listview_entry, null);
                ((TextView)newInstructionView.findViewById(R.id.stepNoTextView)).setText((instructionLayout.getChildCount()+1) + ".");

                ImageButton deleteInstructionButton = (ImageButton) newInstructionView.findViewById(R.id.edit_recipe_instruction_delete);
                deleteInstructionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get the parent's parent layout of this entry (structure of entry)
                        ViewGroup parentView = (ViewGroup)v.getParent().getParent();
                        //use the parent viewGroup to find the index of the current layout
                        int currentIndex = ((ViewGroup)parentView.getParent()).indexOfChild(parentView);
                        Log.e("index of current view's parent", currentIndex + "");
                        LinearLayout ingredientsLayout = (LinearLayout) findViewById(R.id.instructionsEditLinearLayout);
                        LinearLayout newInstructionView = (LinearLayout) ingredientsLayout.getChildAt(currentIndex);
                        ingredientsLayout.removeView(newInstructionView);
                        //decrease the ingredient counter
                        rebuildCounter();
                    }

                    //ensure the list order is always proper (i.e. no gaps)
                    public void rebuildCounter() {
                        ViewGroup ingGroup = (ViewGroup)findViewById(R.id.instructionsEditLinearLayout);
                        //iterate through all the children and reset the step counter
                        for(int i = 0; i < (ingGroup.getChildCount() + 1); i++) {
                            View horizontalView = ingGroup.getChildAt(i);
                            if(horizontalView instanceof LinearLayout) {
                                ViewGroup horizontalViewGroup = (ViewGroup)horizontalView;
                                TextView ingredientNumView = (TextView)horizontalViewGroup.findViewById(R.id.stepNoTextView);

                                ingredientNumView.setText(i+1 + ".");
                            }
                        }
                    }
                });
                instructionLayout.addView(newInstructionView);
            }
        });
        for (Ingredients i : ingredientList) {
            //[ingr]   "Quantity:" [5lbs]
            LinearLayout ingredientLayout = (LinearLayout) findViewById(R.id.ingredientsEditLinearLayout);
            View newIngredientView = getLayoutInflater().inflate(R.layout.editrecipeingredient_listview_entry, null);

            ((TextView) newIngredientView.findViewById(R.id.edit_recipe_ingredients_step)).setText((ingredientLayout.getChildCount() + 1) + ".");
            ((EditText) newIngredientView.findViewById(R.id.ingredientEditText)).setText(i.getIngredientName());
            ((EditText) newIngredientView.findViewById(R.id.quantityEditText)).setText(i.getQuantity());

            ImageButton deleteIngredientsButton = (ImageButton) newIngredientView.findViewById(R.id.edit_recipe_ingredients_delete);
            deleteIngredientsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get the parent layout of this entry
                    LinearLayout parentView = (LinearLayout)v.getParent();
                    //use the parent viewGroup to find the index of the current layout
                    int currentIndex = ((ViewGroup)parentView.getParent()).indexOfChild(parentView);
                    Log.e("index of current view's parent", currentIndex + "");
                    LinearLayout ingredientsLayout = (LinearLayout) findViewById(R.id.ingredientsEditLinearLayout);
                    LinearLayout newIngredientView = (LinearLayout) ingredientsLayout.getChildAt(currentIndex);
                    ingredientsLayout.removeView(newIngredientView);
                    //decrease the ingredient counter
                    rebuildCounter();
                }

                //ensure the list order is always proper (i.e. no gaps)
                public void rebuildCounter() {
                    ViewGroup ingGroup = (ViewGroup)findViewById(R.id.ingredientsEditLinearLayout);
                    //iterate through all the children and reset the step counter
                    for(int i = 0; i < (ingGroup.getChildCount() + 1); i++) {
                        View horizontalView = ingGroup.getChildAt(i);
                        if(horizontalView instanceof LinearLayout) {
                            ViewGroup horizontalViewGroup = (ViewGroup)horizontalView;
                            TextView ingredientNumView = (TextView)horizontalViewGroup.findViewById(R.id.edit_recipe_ingredients_step);

                            ingredientNumView.setText(i+1 + ".");
                        }
                    }
                }
            });
            ingredientLayout.addView(newIngredientView);
        }

        ImageButton addIngredientsButton = ((ImageButton) findViewById(R.id.addIngredientsButton));
        addIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout ingredientsLayout = (LinearLayout) findViewById(R.id.ingredientsEditLinearLayout);
                Log.d("editrecipe", "num ingredients b4 adding; " + ingredientsLayout.getChildCount());
                View newIngredientView = getLayoutInflater().inflate(R.layout.editrecipeingredient_listview_entry, null);
                ((TextView) newIngredientView.findViewById(R.id.edit_recipe_ingredients_step)).setText((ingredientsLayout.getChildCount()+1) + ".");

                ImageButton deleteIngredientsButton = (ImageButton) newIngredientView.findViewById(R.id.edit_recipe_ingredients_delete);
                deleteIngredientsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get the parent layout of this entry
                        LinearLayout parentView = (LinearLayout)v.getParent();
                        //use the parent viewGroup to find the index of the current layout
                        int currentIndex = ((ViewGroup)parentView.getParent()).indexOfChild(parentView);
                        Log.e("index of current view's parent", currentIndex + "");
                        LinearLayout ingredientsLayout = (LinearLayout) findViewById(R.id.ingredientsEditLinearLayout);
                        LinearLayout newIngredientView = (LinearLayout) ingredientsLayout.getChildAt(currentIndex);
                        ingredientsLayout.removeView(newIngredientView);
                        //decrease the ingredient counter
                        rebuildCounter();
                    }

                    //ensure the list order is always proper (i.e. no gaps)
                    public void rebuildCounter() {
                        ViewGroup ingGroup = (ViewGroup)findViewById(R.id.ingredientsEditLinearLayout);
                        //iterate through all the children and reset the step counter
                        for(int i = 0; i < (ingGroup.getChildCount() + 1); i++) {
                            View horizontalView = ingGroup.getChildAt(i);
                            if(horizontalView instanceof LinearLayout) {
                                ViewGroup horizontalViewGroup = (ViewGroup)horizontalView;
                                TextView ingredientNumView = (TextView)horizontalViewGroup.findViewById(R.id.edit_recipe_ingredients_step);

                                ingredientNumView.setText(i+1 + ".");
                            }
                        }
                    }
                });
                ingredientsLayout.addView(newIngredientView);
            }
        });



        Button updateRecipeButton = (Button) findViewById(R.id.updateRecipeButton);
        updateRecipeButton.
                setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete
                /*
                HashMap<String,String> deleteRecipeParams = new HashMap<String,String>();
                deleteRecipeParams.put("r_name", currentRecipe.getRecipeName());
                deleteRecipeParams.put("fb_id", AccountActivity.getFbId());
                deleteRecipeParams.put("filter", "delete_recipe");
                httpUtil.makeHttpPost(deleteRecipeParams);
                */
           /*     try {
                    Thread.sleep(20000);                 //1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Log.d("thread sleep fail", "threadsleepfail");
                    Thread.currentThread().interrupt();
                }*/

                String newRecipeName = ((EditText) findViewById(R.id.editRecipeNameInput)).getText().toString();
                String newImageUrl = ((EditText) findViewById(R.id.editRecipeImageUrlInput)).getText().toString();

                //insert recipe
                HashMap<String,String> insertRecipeParams = new HashMap<String,String>();
                insertRecipeParams.put("r_name", newRecipeName);
                insertRecipeParams.put("r_name_curr", currentRecipe.getRecipeName());
                insertRecipeParams.put("fb_id", AccountActivity.getFbId());
                insertRecipeParams.put("filter", "update_recipe");
                insertRecipeParams.put("cookbook_type", "private");
                insertRecipeParams.put("image_url", newImageUrl);
                httpUtil.makeHttpPost(insertRecipeParams);

                ViewGroup instructionsEditLayout = (ViewGroup) findViewById(R.id.instructionsEditLinearLayout);
                Log.d("instrcount","instructions count: " + instructionsEditLayout.getChildCount());



                //loop through and insert instructions
                for(int i = 0;  i<instructionsEditLayout.getChildCount(); i++ ) {
                    //instructionLayout has many horizontal linearlayout as children, who each have children containing EditText
                    View horizontalView = instructionsEditLayout.getChildAt(i);

                    //loop throuhg view's children to find EditTexts

                    ViewGroup horizontalViewGroup = (ViewGroup)horizontalView;
                    TextView instructionNumView = (TextView)horizontalViewGroup.findViewById(R.id.stepNoTextView);
                    EditText instructionTitleEditText = (EditText) horizontalViewGroup.findViewById(R.id.instructionTitleEditText);
                    EditText instructionEditText = (EditText) horizontalViewGroup.findViewById(R.id.instructionEditText);
                    EditText instructionHoursEditText = (EditText) horizontalViewGroup.findViewById(R.id.hoursEditText);
                    EditText instructionMinsEditText = (EditText) horizontalViewGroup.findViewById(R.id.minutesEditText);
                    Log.d("AddRecipeActivity", "instr: " + instructionEditText.getText().toString() + " hrs: " + instructionHoursEditText.getText().toString() + " mins: " + instructionMinsEditText.getText().toString());
                    //TODO: INSERT INSTRUCTION

                    HashMap<String,String> insertRecipeInstructionParams = new HashMap<String,String>();
                    insertRecipeInstructionParams.put("name", newRecipeName);
                    insertRecipeInstructionParams.put("fb_id", AccountActivity.getFbId());
                    insertRecipeInstructionParams.put("instruction", instructionTitleEditText.getText().toString());
                    insertRecipeInstructionParams.put("description", instructionEditText.getText().toString());
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
                    EditText ingredientEditText = (EditText) horizontalViewGroup.findViewById(R.id.ingredientEditText);
                    EditText ingredientQuantityEditText = (EditText) horizontalViewGroup.findViewById(R.id.quantityEditText);
                    Log.d("AddRecipeActivity", "ingred: " + ingredientEditText.getText().toString() + " quantity: " + ingredientQuantityEditText.getText().toString());
                    //TODO: INSERT INGREDIENT
                    HashMap<String,String> insertIngredientParams = new HashMap<String,String>();
                    insertIngredientParams.put("name", newRecipeName);
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
/*<<<<<<< HEAD

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
            final String stepDescriptionTitle = stepList.get(position).getTitle();
            final String stepDescription = stepList.get(position).getDescription();
            EditText stepDescriptionTitleTextView = (EditText) convertView.findViewById(R.id.instructionTitleEditText);
            EditText stepDescriptionTextView = (EditText) convertView.findViewById(R.id.instructionEditText);
            //Log.d("editrecipeeacivity", "stepdesc editext: " + stepDescriptionTextView.toString());
            //Log.d("editrecipeeacivity", "stepdescription: " + stepDescription);


            TextView stepNoTextView = (TextView) convertView.findViewById(R.id.stepNoTextView);
            stepNoTextView.setText(stepList.get(position).getStepNumber() + ".");
            stepDescriptionTitleTextView.setText(stepDescriptionTitle);
            stepDescriptionTextView.setText(stepDescription);

            EditText hoursEditText = (EditText) (convertView.findViewById(R.id.hoursEditText));

            Log.d("hoursEditText", hoursEditText.toString());
            hoursEditText.setText(String.valueOf(stepList.get(position).getHours()));
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
    }*/
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
//=======
//>>>>>>> modifyeditrecipe
}
