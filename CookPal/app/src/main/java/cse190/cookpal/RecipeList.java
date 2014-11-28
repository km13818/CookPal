package cse190.cookpal;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


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
        String serverRecipeListRequestURL = "http://ec2-54-69-39-93.us-west-2.compute.amazonaws.com:8080/request_handler.jsp?filter=select_recipes&fb_id=" +
                AccountActivity.getFbId();
        // http://ec2-54-69-39-93.us-west-2.compute.amazonaws.com:8080/request_handler.jsp?fb_id=10204925045306752&filter=select_recipes
        new LongOperation().execute(serverRecipeListRequestURL);

       /* if(getIntent().getStringExtra("RECIPE_NAME") != null) {
            String newRecipe = new String(getIntent().getStringExtra("RECIPE_NAME").toString());
            Log.d(TAG, newRecipe + " added....");
            recipeLists.add(newRecipe);
        }*/

        //TODO: pass in recipeLists array list here from db
        // populateListView();

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
    private class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String jsonReturnString;
        private String jsonRetrievalErrorString = null;
        private ProgressDialog Dialog = new ProgressDialog(RecipeList.this);
        String data = "";

        protected void onPreExecute() {
            //Start Progress Dialog (Message)
            recipeLists = new ArrayList<String>();
        Log.d("recipelist", "onPreExecute.....");
            Dialog.setMessage("Please wait..");
            Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            Log.d("recipelist", "do in background.....");
            /************ Make Post Call To Web Server ***********/
            BufferedReader reader = null;

            // Send data
            try {

                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                URLConnection conn = url.openConnection();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "");
                }

                // Append Server Response To jsonReturnString String
                jsonReturnString = sb.toString();
            } catch (Exception ex) {
                jsonRetrievalErrorString = ex.getMessage();
            } finally {
                try {

                    reader.close();
                } catch (Exception ex) {
                }
            }

            /*****************************************************/
            return null;
        }

        protected void onPostExecute(Void unused) {
            Log.d("recipelist", "onpostexecute.....");
            // Close progress dialog
            Dialog.dismiss();

            //error
            if (jsonRetrievalErrorString != null) {
                Log.d("recipelist", "error.....: " + jsonRetrievalErrorString);
                Context context = getApplicationContext();
                CharSequence text = "Error retrieving recipes";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else {
                /****************** Start Parse Response JSON Data *************/
                Log.d("recipeList activity", "json return string: " + jsonReturnString);
                JSONObject jsonResponse;
//{"Kevin Ma's recipes:":[{"cookbook status":"0","recipe name":"recipeasdf"},{"cookbook status":"private","recipe name":"r"},{"cookbook status":"private","recipe name":"asdf1"},{"cookbook status":"private","recipe name":"fqwfe"}]}
                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(jsonReturnString);

                    Iterator jsonKeysIterator = jsonResponse.keys();

                    while(jsonKeysIterator.hasNext()) {
                        String key = jsonKeysIterator.next().toString();
                        JSONArray recipeArray = (JSONArray) jsonResponse.get(key);
                        for (int i = 0; i < recipeArray.length(); i++) {
                            String recipeName = ((JSONObject)recipeArray.get(i)).get("recipe name").toString();

                            Log.d("recipeList activity", "recipeName from db: " + recipeName);
                            recipeLists.add(recipeName);
                        }
                    }
                    populateListView();
                    /****************** End Parse Response JSON Data *************/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    ///////////////////////////////////////////END JSON RETRIEVAL ///////////////////////////////////////////
}