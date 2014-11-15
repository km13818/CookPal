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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AddRecipeActivity extends Activity {
    final Context thisContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        Button confirmAddButton = (Button) findViewById(R.id.confirm_create_recipe_button);
        confirmAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRecipeActivity.this, RecipeList.class);
                intent.putExtra("RECIPE_NAME", ((EditText) findViewById(R.id.recipeNameInput)).getText().toString());
                //TODO dont need intent, just update database

                //loop through all EditText
                ViewGroup group = (ViewGroup)findViewById(R.id.instructionsLayout);
                int count = group.getChildCount();
                Log.d("count=", String.valueOf(count));
                for(int i = 0;  i<count; i++ ) {
                    //instructionLayout has many horizontal linearlayout as children, who each have children containing EditText
                    View horizontalView = group.getChildAt(i);
                    if(horizontalView instanceof LinearLayout)
                    {
                        //loop throuhg view's children to find EditTexts
                        int horizontalLayoutChildrenCount = ((LinearLayout) horizontalView).getChildCount();
                        for (int j = 0; j < horizontalLayoutChildrenCount; j++) {
                            ViewGroup horizontalViewGroup = (ViewGroup)horizontalView;
                            View horizontalViewSubView = horizontalViewGroup.getChildAt(j);
                            if (horizontalViewSubView instanceof EditText) {
                                Log.d("inputtextasdf", ((EditText)horizontalViewSubView).getText().toString() );
                            }
                        }

                    }
                }

                startActivity(intent);
            }
        });

        ImageButton addInstructionsButton = (ImageButton) findViewById(R.id.addInstructionsButton);
        addInstructionsButton.setOnClickListener(new View.OnClickListener() {

            int currInstructionCount = 2;
            @Override
            public void onClick(View v) {
                //add row to instructionsLayout
                LinearLayout instructionsLayout = (LinearLayout) findViewById(R.id.instructionsLayout);
                LinearLayout newInstructionRowLayout = new LinearLayout(thisContext);

                TextView instructionNum = new TextView(thisContext);
                instructionNum.setText(String.valueOf(currInstructionCount) + ".");
                EditText newInstruction = new EditText(thisContext);
                newInstructionRowLayout.addView(instructionNum);
                newInstructionRowLayout.addView(newInstruction);

                instructionsLayout.addView(newInstructionRowLayout);
                currInstructionCount++;

            }
        });
    }


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
}
