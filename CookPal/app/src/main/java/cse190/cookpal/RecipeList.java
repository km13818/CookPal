package cse190.cookpal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


public class RecipeList extends BaseDrawerActivity {
    private static final String TAG = "RecipeList";

    //TODO: potentially refactor. not sure if it's a good idea to have data structures as global vars in activity
    ArrayList<String> recipeLists = new ArrayList<String>();
    ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    LinearLayout thisLayout;
    PopupWindow deleteConfirmWindow;
    Button deleteConfirmButton;
    TextView deleteConfirmText;
    ImageButton deleteGroceryListButton;

    @Override
    protected void onRestart() {
    //    setContentView(R.layout.activity_grocery_list_list);
        populateListView();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list_list);

        //TODO: populate recipeLists using db
        // WebServer Request URL
        String serverRecipeListRequestURL = "http://ec2-54-69-39-93.us-west-2.compute.amazonaws.com:8080/user.json"; //fix this
        //new LongOperation().execute(serverRecipeListRequestURL);

        if(getIntent().getStringExtra("RECIPE_NAME") != null) {
            String newRecipe = new String(getIntent().getStringExtra("RECIPE_NAME").toString());
            Log.d(TAG, newRecipe + " added....");
            recipeLists.add(newRecipe);
        }

        //TODO: pass in recipeLists array list here from db
         populateListView();

        ImageButton addGroceryListButton = (ImageButton)findViewById(R.id.addGroceryListButton);
        addGroceryListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecipeList.this, AddRecipeActivity.class);
                startActivity(i);
            }
        });
        popupInit();
    }

    private void popupInit() {
        deleteGroceryListButton = (ImageButton) findViewById(R.id.deleteGroceryListButton);
        deleteConfirmText = new TextView(this);
        deleteConfirmButton = new Button(this);
        thisLayout = new LinearLayout(this);

        deleteConfirmText.setText("Confirm delete?");
        deleteConfirmButton.setText("OK");

        deleteConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView recipeList = (ListView) findViewById(R.id.recipeListView);
                /*
                //getCheckedItemPositions not working right
                SparseBooleanArray checked = groceryListList.getCheckedItemPositions();

                int test1 = groceryListList.getAdapter().getCount();
                for(int i = 0; i < groceryListList.getAdapter().getCount(); i++) {
                    if(checked.get(i)) {
                        recipeLists[i] = "deleted";
                        onRestart();
                    }
                }*/
                //checkBoxes has all checkboxes + junk at end. very very hacky
                for(int i = 0; i < checkBoxes.size(); i++) {
                    String recipeName = (String)checkBoxes.get(i).getText();
                    Log.d(TAG, recipeName);

                    if(checkBoxes.get(i).isChecked() && recipeLists.contains(checkBoxes.get(i).getText())) {

                        //test code
                        //TODO: DELETE FROM DB ->onrestart()
                        HashMap<String,String> deleteRecipeParams = new HashMap<String,String>();
                        deleteRecipeParams.put("name", recipeName);
                        deleteRecipeParams.put("fb_id", AccountActivity.getFbId());
                        deleteRecipeParams.put("filter", "delete_recipe");
                        HttpUtil.makeHttpPost(deleteRecipeParams);
                        recipeLists.remove(checkBoxes.get(i).getText());
                        checkBoxes.remove(i);

                    }
                }
                deleteConfirmWindow.dismiss();

                onRestart();
            }
        });
        thisLayout.addView(deleteConfirmText);
        thisLayout.addView(deleteConfirmButton);

        deleteGroceryListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirmWindow = new PopupWindow(thisLayout, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
                deleteConfirmWindow.showAtLocation(thisLayout, Gravity.CENTER,50, 50);
                deleteConfirmWindow.showAsDropDown(deleteGroceryListButton);


            }
        });

    }

    private void populateListView() {
        //Create list of items

        checkBoxes = new ArrayList<CheckBox>();
        ArrayAdapter<String> recipeListAdapter = new RecipeListAdapter();
        ListView list = (ListView) findViewById(R.id.recipeListView);
        list.setAdapter(recipeListAdapter);

        //TODO: populate recipeLists arraylist from DB

    }

    private class RecipeListAdapter extends ArrayAdapter<String> {
        public RecipeListAdapter() {
            super(RecipeList.this, R.layout.grocery_list, recipeLists);
        }

        public RecipeListAdapter(ArrayList<String> recipeLists) {
            super(RecipeList.this, R.layout.grocery_list, recipeLists);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.grocery_list, parent, false);
            }
            CheckBox currCheckBox = (CheckBox) convertView.findViewById(R.id.groceryListView);
            currCheckBox.setText(recipeLists.get(position));
            checkBoxes.add(currCheckBox);
            return convertView;
        }
    }

    /////////////////////////////////////////////Start JSON Retrieval code///////////////
    //TODO: refactor into util class

    ///////////////////////////////////////////END JSON RETRIEVAL ///////////////////////////////////////////
}