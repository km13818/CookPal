package cse190.cookpal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

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


public class RecipeList extends BaseDrawerActivity {
    private static final String TAG = "RecipeList";
    ArrayList<String> recipeLists = new ArrayList<String>();
    ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    LinearLayout thisLayout;
    PopupWindow deleteConfirmWindow;
    Button deleteConfirmButton;
    TextView deleteConfirmText;
    ImageButton deleteGroceryListButton;

    public void connectToDb() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("ec2-54-69-39-93.us-west-2.compute.amazonaws.com:8080/dbaccess.jsp");


    }



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

        if(getIntent().getStringExtra("RECIPE_NAME") != null) {
            String newRecipe = new String(getIntent().getStringExtra("RECIPE_NAME").toString());
            Log.d(TAG, newRecipe + " added....");
            recipeLists.add(newRecipe);
        }

      //  System.out.println("isnewthingnull: " + (getIntent().getStringExtra("RECIPE_NAME") ==null));
        //Log.d(TAG, "is new thing null: " + (getIntent().getStringExtra("RECIPE_NAME") ==null));
       // recipeLists.add(getIntent().getStringExtra("RECIPE_NAME"));

        populateListView();
        ImageButton deleteGroceryListButton = (ImageButton)findViewById(R.id.deleteGroceryListButton);
    /*    deleteGroceryListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        ImageButton addGroceryListButton = (ImageButton)findViewById(R.id.addGroceryListButton);
        addGroceryListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecipeList.this, AddRecipeActivity.class);
                startActivity(i);
                //TODO: POPUP AND ADD TO DB
               // onRestart();
            }
        });
        popupInit();

        final Button GetServerData = (Button) findViewById(R.id.GetServerData);

        GetServerData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // WebServer Request URL
                String serverURL = "http://ec2-54-69-39-93.us-west-2.compute.amazonaws.com:8080/user.json";

                // Use AsyncTask execute Method To Prevent ANR Problem
                new LongOperation().execute(serverURL);
            }
        });
    }

    // Class with extends AsyncTask class

    private class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(RecipeList.this);
        String data ="";
        TextView uiUpdate = (TextView) findViewById(R.id.output);
        TextView jsonParsed = (TextView) findViewById(R.id.jsonParsed);
        int sizeData = 0;
        EditText serverText = (EditText) findViewById(R.id.serverText);


        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Please wait..");
            Dialog.show();

            try{
                // Set Request parameter
                data +="&" + URLEncoder.encode("data", "UTF-8") + "="+serverText.getText();

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader=null;

            // Send data
            try
            {

                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "");
                }

                // Append Server Response To Content String
                Content = sb.toString();
            }
            catch(Exception ex)
            {
                Error = ex.getMessage();
            }
            finally
            {
                try
                {

                    reader.close();
                }

                catch(Exception ex) {}
            }

            /*****************************************************/
            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (Error != null) {

                uiUpdate.setText("Output : "+Error);

            } else {

                // Show Response Json On Screen (activity)
                uiUpdate.setText( Content );

                /****************** Start Parse Response JSON Data *************/

                String OutputData = "";
                JSONObject jsonResponse;

                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);

                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    /*******  Returns null otherwise.  *******/
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

                    /*********** Process each JSON Node ************/

                    int lengthJsonArr = jsonMainNode.length();

                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        /****** Get Object for each JSON node.***********/
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        /******* Fetch node values **********/
                        String name       = jsonChildNode.optString("user").toString();
                        String number     = jsonChildNode.optString("number").toString();
                        String date_added = jsonChildNode.optString("date_added").toString();


                        OutputData += " Name           : "+ name +" "
                                + "Number      : "+ number +" "
                                + "Time                : "+ date_added +" "
                                +"--------------------------------------------------";


                    }
                    /****************** End Parse Response JSON Data *************/

                    //Show Parsed Output on screen (activity)
                    jsonParsed.setText( OutputData );


                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }

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

                    Log.d(TAG, (String)checkBoxes.get(i).getText());

                    if(checkBoxes.get(i).isChecked() && recipeLists.contains(checkBoxes.get(i).getText())) {

                        //test code
                        //TODO: DELETE FROM DB

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
        ArrayAdapter<String> recipeListAdapter = new GroceryListListAdapter();
        ListView list = (ListView) findViewById(R.id.recipeListView);
        list.setAdapter(recipeListAdapter);

        //TODO: populate recipeLists arraylist from DB

    }

    private class GroceryListListAdapter extends ArrayAdapter<String> {
        public GroceryListListAdapter() {
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
}