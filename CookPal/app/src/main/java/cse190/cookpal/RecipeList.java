package cse190.cookpal;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class RecipeList extends BaseDrawerActivity implements
        ActionBar.TabListener{
    private static final String TAG = "RecipeList";

    //TODO: potentially refactor. not sure if it's a good idea to have data structures as global vars in activity
    ArrayList<String> recipeList = new ArrayList<String>();
    ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    LinearLayout thisLayout;
    PopupWindow deleteConfirmWindow;
    Button deleteConfirmButton;
    TextView deleteConfirmText;
    ImageButton deleteGroceryListButton;
    String recipeWhosePictureWasTaken;
    ImageView currRecipeImageView;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Overview", "Ingredients", "Directions" };
    TabHost tabHost;
    //called after picture is taken with requestCode 0
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("recipelist", "onactivityresult");
        if (requestCode == 0) {
            if (data.getExtras() != null) {
                Bitmap recipeImageBitMap = (Bitmap) data.getExtras().get("data");
                Log.d("recipelist", "recipe image taken: " + recipeImageBitMap);

                ByteArrayOutputStream imageBaos = new ByteArrayOutputStream();
                recipeImageBitMap.compress(Bitmap.CompressFormat.JPEG, 100, imageBaos);


                currRecipeImageView.setImageBitmap(recipeImageBitMap);
                Log.d("recipeList", "recipeimagebaos: " + imageBaos);
                Log.d("recipelist", "recipewhosepicturewastaken: " + recipeWhosePictureWasTaken);
                HashMap<String, String> insertImageParams = new HashMap<String, String>();
                insertImageParams.put("r_name", recipeWhosePictureWasTaken);
                insertImageParams.put("fb_id", AccountActivity.getFbId());
                insertImageParams.put("image", new String(imageBaos.toByteArray()));
                insertImageParams.put("filter", "add_image");
                HttpUtil.makeHttpPost(insertImageParams);
            } else {
                Log.d("recipeist", "no picture was taken");
            }
        }
    }

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
        populateListView();
        super.onRestart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        //TODO: populate recipeList using db
        // WebServer Request URL
        String serverRecipeListRequestURL = "http://ec2-54-69-39-93.us-west-2.compute.amazonaws.com:8080/request_handler.jsp?filter=select_recipes&fb_id=" +
                AccountActivity.getFbId();
        new LongOperation().execute(serverRecipeListRequestURL);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        final ActionBar actionBar = getActionBar();
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.unselected_tabs)));

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        //TODO: populate recipeLists using db
        HashMap<String, String> recipeListRetrievalParams = new HashMap<String,String>();
        recipeListRetrievalParams.put("fb_id",AccountActivity.getFbId());
       // HttpResponse recipeListRetrievalResponse = HttpUtil.makeHttpPost(recipeListRetrievalParams);


        //TODO: pass in recipeList array list here from db
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

                //TODO: instead of having global variable of checkboxes, just findElementById (more organized)
                //checkBoxes has all checkboxes + junk at end. very very hacky
                for(int i = 0; i < checkBoxes.size(); i++) {
                    String recipeName = (String)checkBoxes.get(i).getText();
                    Log.d(TAG, recipeName);

                    if(checkBoxes.get(i).isChecked() && RecipeList.this.recipeList.contains(checkBoxes.get(i).getText())) {

                        //test code
                        //TODO: test DELETE FROM DB ->onrestart()
                        HashMap<String,String> deleteRecipeParams = new HashMap<String,String>();
                        deleteRecipeParams.put("name", recipeName);
                        deleteRecipeParams.put("fb_id", AccountActivity.getFbId());
                        deleteRecipeParams.put("filter", "delete_recipe");
                        HttpUtil.makeHttpPost(deleteRecipeParams);
                        RecipeList.this.recipeList.remove(checkBoxes.get(i).getText());
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
        //tab.setCustomView(getResources().getColor(R.color.selected_tabs));
        //tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.selected_tabs));
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }


    private void populateListView() {
        //Create list of items

        checkBoxes = new ArrayList<CheckBox>();
        ArrayAdapter<String> recipeListAdapter = new RecipeListAdapter();
        ListView list = (ListView) findViewById(R.id.recipeListView);
        list.setAdapter(recipeListAdapter);

    }

    private class RecipeListAdapter extends ArrayAdapter<String> {
        public RecipeListAdapter() {
            super(RecipeList.this, R.layout.recipe_listview_entry, recipeList);
        }

        public RecipeListAdapter(ArrayList<String> recipeLists) {
            super(RecipeList.this, R.layout.recipe_listview_entry, recipeLists);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.recipe_listview_entry, parent, false);
            }
            final View thisConvertView = convertView;
            final String recipeName = recipeList.get(position);
            CheckBox currCheckBox = (CheckBox) convertView.findViewById(R.id.recipeListviewEntry);
            currCheckBox.setText(recipeName);
            checkBoxes.add(currCheckBox);
            Button currAddPictureButton = (Button)convertView.findViewById(R.id.addPictureButton);
            currAddPictureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    recipeWhosePictureWasTaken = recipeName;
                    currRecipeImageView = (ImageView) thisConvertView.findViewById(R.id.recipeEntryImageView);
                    startActivityForResult(cameraIntent, 0);

                }
            });


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
            Log.d("recipelist", "onPreExecute.....");
            //Start Progress Dialog (Message)
            recipeList = new ArrayList<String>();
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
                            recipeList.add(recipeName);
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