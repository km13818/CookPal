package cse190.cookpal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class RecipeList extends BaseDrawerActivity {
    private static final String TAG = "RecipeList";
    private boolean checked = false;

    //TODO: potentially refactor. not sure if it's a good idea to have data structures as global vars in activity
    HttpUtil httpUtil = new HttpUtil();
    ArrayList<String> recipeList = new ArrayList<String>();
    ArrayList<String> recipeImageList = new ArrayList<String>();
    ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    LinearLayout thisLayout;
    PopupWindow deleteConfirmWindow;
    Button deleteConfirmButton;
    TextView deleteConfirmText;
    // WebServer Request URL

    public static final String SERVER_RECIPE_LIST_REQUEST_URL = "http://ec2-54-69-39-93.us-west-2.compute.amazonaws.com:8080/request_handler.jsp?filter=select_recipes&fb_id=" +
            AccountActivity.getFbId();



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override

    protected void onRestart() {
        Log.d("recipelist", "recipelist onrestart");
        recipeList = new ArrayList<String>();
        recipeImageList = new ArrayList<String>();
        new LongOperation().execute(SERVER_RECIPE_LIST_REQUEST_URL);
        super.onRestart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);


        //initialize image lazy loader
        new LongOperation().execute(SERVER_RECIPE_LIST_REQUEST_URL);
      //  new PopulateRecipeOperation().execute(AccountActivity.getFbId(),"aaaa" );

        /*ImageButton addGroceryListButton = (ImageButton)findViewById(R.id.addGroceryListButton);
        addGroceryListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecipeList.this, AddRecipeActivity.class);
                startActivity(i);
            }
        });*/
        //popupInit();
    }

    private void popupInit() {
        //deleteGroceryListButton = (ImageButton) findViewById(R.id.deleteGroceryListButton);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Confirm delete?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ListView recipeList = (ListView) findViewById(R.id.recipeListView);

                        for(int i = 0; i < checkBoxes.size(); i++) {
                            String recipeName = (String)checkBoxes.get(i).getText();
                            Log.d(TAG, recipeName);

                            if(checkBoxes.get(i).isChecked() && RecipeList.this.recipeList.contains(checkBoxes.get(i).getText())) {

                                //test code
                                //TODO: currently because checkBoxes is hacky, will delete checked recipes multiple times. should fix this
                                HashMap<String,String> deleteRecipeParams = new HashMap<String,String>();
                                deleteRecipeParams.put("r_name", recipeName);
                                deleteRecipeParams.put("fb_id", AccountActivity.getFbId());
                                deleteRecipeParams.put("filter", "delete_recipe");
                                httpUtil.makeHttpPost(deleteRecipeParams);
                            }
                        }
                        onRestart();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipelist_activity_menu, menu);

        /*MenuItem deleteItem = menu.findItem(R.id.deleteGroceryListButton);
        if (checked == true) {
            deleteItem.setVisible(true);
        } else {
            deleteItem.setVisible(false);
        }*/

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem deleteItem = menu.findItem(R.id.deleteGroceryListButton);
        deleteItem.setVisible(checked);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteGroceryListButton:
                popupInit();
                return true;
            case R.id.addGroceryListButton:
                Intent i = new Intent(RecipeList.this, AddRecipeActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateListView() {
        //Create list of items

        checkBoxes = new ArrayList<CheckBox>();

        ArrayAdapter<String> recipeListAdapter = new RecipeListAdapter(recipeList, recipeImageList);
        ListView list = (ListView) findViewById(R.id.recipeListView);
        list.setAdapter(recipeListAdapter);

    }

    private class RecipeListAdapter extends ArrayAdapter<String> {
        ImageLoader imageLoader;
        ArrayList<String> urls;
        public RecipeListAdapter() {
            super(RecipeList.this, R.layout.recipe_listview_entry, recipeList);
            imageLoader = imageLoader.getInstance();
        }

        public RecipeListAdapter(ArrayList<String> recipeLists, ArrayList<String> recipeImageURLs) {
            super(RecipeList.this, R.layout.recipe_listview_entry, recipeLists);
            imageLoader = imageLoader.getInstance();
            urls = recipeImageURLs;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.recipe_listview_entry, parent, false);
            }

            final View thisConvertView = convertView;
            final String recipeName = recipeList.get(position);
            final String recipeImageURL = recipeImageList.get(position);
            ImageView currImageView = (ImageView) thisConvertView.findViewById(R.id.recipeEntryImageView);
            //if the entry does not have an url, we use local resources to avoid slowdowns
            imageLoader.displayImage(recipeImageURL, currImageView);

            CheckBox currCheckBox = (CheckBox) convertView.findViewById(R.id.recipeListviewEntry);
            currCheckBox.setText(recipeName);
            checkBoxes.add(currCheckBox);

            ImageButton editRecipeButton = (ImageButton) convertView.findViewById(R.id.editRecipeButton);
            editRecipeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new PopulateRecipeOperation().execute(AccountActivity.getFbId(), recipeName, "EDITRECIPEACTIVITY", recipeImageURL);
                }
            });
            TextView currTextView = (TextView) thisConvertView.findViewById(R.id.recipeTitle);
            currTextView.setText(recipeName);
            currTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                new PopulateRecipeOperation().execute(AccountActivity.getFbId(), recipeName, "RECIPEACTIVITY", recipeImageURL);
                }
            });

            Log.e("this recipe is loading image", recipeName);
            return thisConvertView;
        }
    }
    /////////////////////////////////////////////Start JSON Retrieval code///////////////
    //TODO: refactor into util class
    private class PopulateRecipeOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String imgReturnString;
        private String instructionsReturnString;
        private String ingredientsReturnString;
        private String instructionsRetrievalErrorString = null;
        private String ingredientsRetrievalErrorString = null;
        private String imgRetrievalErrorString = null;
        private String recipeName;
        private String imgUrl;
        private String nextActivity;
        private ProgressDialog Dialog = new ProgressDialog(RecipeList.this);
        BufferedReader reader = null;
        String data = "";

        protected void onPreExecute() {
            Log.d("recipelist", "onPreExecute.....");
            //Start Progress Dialog (Message)
            recipeList = new ArrayList<String>();
            Dialog.setMessage("Please wait..");
            Dialog.show();
        }

        // Call after onPreExecute method
        //fb_id , recipename, nextactivity
        protected Void doInBackground(String... params) {
            Log.d("recipelist", "do in background.....");
            /************ Make Post Call To Web Server ***********/
            imgUrl = params[3];
            //retrieve img url
            try {
                // Defined URL  where to send data
                String serverUrlString = "http://ec2-54-69-39-93.us-west-2.compute.amazonaws.com:8080/request_handler.jsp?";
                String selectInstructionsUrlString = serverUrlString + "fb_id=" + params[0] + "&r_name=" + URLEncoder.encode(params[1],"UTF-8") + "&filter=" + "select_recipes";
                URL selectInstructionsUrl = new URL(selectInstructionsUrlString);

                // Send POST data request

                URLConnection conn = selectInstructionsUrl.openConnection();

                // Get the server response

                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "");
                }

                imgReturnString = sb.toString();
            } catch (Exception ex) {
                imgRetrievalErrorString = ex.getMessage();
            } finally {
                try {
                    reader.close();
                } catch (Exception ex) {
                }
            }

            // retrieve instruction
            try {
                recipeName = params[1];
                nextActivity = params[2];
                // Defined URL  where to send data
                String serverUrlString = "http://ec2-54-69-39-93.us-west-2.compute.amazonaws.com:8080/request_handler.jsp?";
                String selectInstructionsUrlString = serverUrlString + "fb_id=" + params[0] + "&r_name=" + URLEncoder.encode(params[1],"UTF-8") + "&filter=" + "select_instruction";
                URL selectInstructionsUrl = new URL(selectInstructionsUrlString);

                // Send POST data request

                URLConnection conn = selectInstructionsUrl.openConnection();

                // Get the server response

                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "");
                }

                instructionsReturnString = sb.toString();
            } catch (Exception ex) {
                instructionsRetrievalErrorString = ex.getMessage();
            } finally {
                try {

                    reader.close();
                } catch (Exception ex) {
                }
            }
            // retrieve ingredients
            try {

                // Defined URL  where to send data
                String serverUrlString = "http://ec2-54-69-39-93.us-west-2.compute.amazonaws.com:8080/request_handler.jsp?";
                String selectIngredientsUrlString = serverUrlString + "fb_id=" + params[0] + "&r_name=" + URLEncoder.encode(params[1],"UTF-8") + "&filter=" + "select_ingredient";
                URL selectIngredientsUrl = new URL(selectIngredientsUrlString);
                // Send POST data request

                URLConnection conn = selectIngredientsUrl.openConnection();

                // Get the server response

                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + "");
                }

                ingredientsReturnString = sb.toString();
            } catch (Exception ex) {
                ingredientsRetrievalErrorString = ex.getMessage();
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
            Log.d("recipeList activity", "instr json return string: " + instructionsReturnString);
            Log.d("recipeList activity", "ingr json return string: " + ingredientsReturnString);
            // Close progress dialog
            Dialog.dismiss();

            ArrayList<String> imgList = new ArrayList<String>();
            ArrayList<Step> stepList = new ArrayList<Step>();
            ArrayList<Ingredients> ingredientsList = new ArrayList<Ingredients>();
            Recipe clickedRecipe;

           // if (imgRetrievalErrorString != null) {

            //} else {
                /****************** Start Parse Response JSON Data *************/
            /*
                Log.d("recipeList activity", "json return string: " + imgReturnString);
                JSONObject jsonResponse;
                try {
                    JSONObject imgJsonResponse = new JSONObject(imgReturnString);
                    Iterator jsonKeysIterator = imgJsonResponse.keys();

                    while(jsonKeysIterator.hasNext()) {
                        String key = jsonKeysIterator.next().toString();
                        JSONArray recipeArray = (JSONArray) imgJsonResponse.get(key);
                        for (int i = 0; i < recipeArray.length(); i++) {
                            String recipeImageURL = ((JSONObject)recipeArray.get(i)).get("image").toString();
                            Log.e("img", recipeImageURL);
                            imgList.add(recipeImageURL);
                            //imgUrl = recipeImageURL;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } */

            //error
            if (instructionsRetrievalErrorString != null) {

            } else {

                try {
                    JSONObject jsonResponse = new JSONObject(instructionsReturnString);
                    Iterator jsonKeysIterator = jsonResponse.keys();

                    while (jsonKeysIterator.hasNext()) {
                        String key = jsonKeysIterator.next().toString();
                        JSONArray instructionsArray = (JSONArray) jsonResponse.get(key);
                        // public Step(String title, String desc, int hours, int minutes, int stepNum)
                        for (int i = 0; i < instructionsArray.length(); i++) {
                            int stepNumber = Integer.parseInt(((JSONObject) instructionsArray.get(i)).get("step number").toString());
                            String title = ((JSONObject) instructionsArray.get(i)).get("instruction").toString();
                            String description = ((JSONObject) instructionsArray.get(i)).get("description").toString();
                            int hours = Integer.valueOf( ((JSONObject) instructionsArray.get(i)).get("hours").toString() );
                            int minutes = Integer.valueOf(((JSONObject) instructionsArray.get(i)).get("minutes").toString());

                            //create new Step
                            Step step = new Step(title, description, hours, minutes, stepNumber);
                            stepList.add(step);
                        }
                    }
                }
                catch(JSONException e) {

                }
                /****************** Start Parse Response JSON Data *************/

                JSONObject jsonResponse;

            }

            //error
            if (ingredientsRetrievalErrorString != null) {

            } else {
                /****************** Start Parse Response JSON Data *************/
                Log.d("recipeList activity", "json return string: " + ingredientsReturnString);
                JSONObject jsonResponse;
                try {
                    JSONObject ingredientsJsonResponse = new JSONObject(ingredientsReturnString);
                    Iterator jsonKeysIterator = ingredientsJsonResponse.keys();

                    while (jsonKeysIterator.hasNext()) {
                        String key = jsonKeysIterator.next().toString();
                        JSONArray instructionsArray = (JSONArray) ingredientsJsonResponse.get(key);
                        // public Step(String title, String desc, int hours, int minutes, int stepNum)
                        for (int i = 0; i < instructionsArray.length(); i++) {
                            String quantity =  ((JSONObject) instructionsArray.get(i)).get("quantity").toString();
                            String ingr = ((JSONObject) instructionsArray.get(i)).get("ingredient").toString();
                            Ingredients ingredient = new Ingredients(ingr,quantity);


                            ingredientsList.add(ingredient);
                            Log.d("recipeList activity", "ingredient params: " + ingredient.toString());
                        }
                    }
                }
                catch(JSONException e) {

                }

            }
            Recipe recipe = new Recipe(recipeName,imgUrl,stepList,ingredientsList);
            Log.d("","steplistsize direct : " + stepList.size());
            Log.d("","ingrlistsize direct : " + ingredientsList.size());
            Log.d("","steplistsize frm recipelist b4 if: " + recipe.getStepList().size());
            Log.d("","ingrlistsize frm recipelist b4 if: " + recipe.getIngredientList().size());
            if(nextActivity.equals("RECIPEACTIVITY")) {
                Intent intent= new Intent(RecipeList.this, RecipeActivity.class);
                Log.d("","steplistsize frm recipelist: " + recipe.getStepList().size());
                Log.d("","ingrlistsize frm recipelist: " + recipe.getIngredientList().size());
                intent.putExtra("recipe", (Serializable) recipe);
                startActivity(intent);
            }
            if(nextActivity.equals("EDITRECIPEACTIVITY")) {
                Intent intent= new Intent(RecipeList.this, EditRecipeActivity.class);
                Log.d("","steplistsize frm recipelist: " + recipe.getStepList().size());
                Log.d("","ingrlistsize frm recipelist: " + recipe.getIngredientList().size());
                intent.putExtra("recipe", (Serializable) recipe);
                startActivity(intent);
            }
        }//end onPostExecute
    }
    ///////////////////////////////////////////END JSON RETRIEVAL ///////////////////////////////////////////
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
            Log.d("recipelist", "onPreExecute.....");
            //Start Progress Dialog (Message)
            recipeList = new ArrayList<String>();
            recipeImageList = new ArrayList<String>();
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
                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(jsonReturnString);

                    Iterator jsonKeysIterator = jsonResponse.keys();

                    while(jsonKeysIterator.hasNext()) {
                        String key = jsonKeysIterator.next().toString();
                        JSONArray recipeArray = (JSONArray) jsonResponse.get(key);
                        for (int i = 0; i < recipeArray.length(); i++) {
                            String recipeName = ((JSONObject)recipeArray.get(i)).get("recipe name").toString();
                            String recipeImageURL = ((JSONObject)recipeArray.get(i)).get("image").toString();
                            Log.d("recipeList activity", "recipeName from db: " + recipeName);
                            recipeList.add(recipeName);
                            recipeImageList.add(recipeImageURL);
                        }
                    }
                    Log.e("populating list view", "blah");
                    populateListView();
                    /****************** End Parse Response JSON Data *************/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    ///////////////////////////////////////////END JSON RETRIEVAL ///////////////////////////////////////////

    public void addRecipeClicked(MenuItem menuItem) {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        startActivity(intent);
    }

    public void checkSelected(View view) {
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                checked = true;
                break;
            } else {
                checked = false;
            }
        }
        invalidateOptionsMenu();
    }
}