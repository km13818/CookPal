package cse190.cookpal;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.analytics.GoogleAnalytics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class RecipeActivity extends BaseDrawerActivity implements
        ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"Overview", "Ingredients", "Directions"};

    public static ArrayList<Ingredients> ingredientList;
    private static Context mContext;
    private static Recipe currentRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ((CookPalApp) getApplication()).getTracker(CookPalApp.TrackerName.APP_TRACKER);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        //actionBar.setHomeButtonEnabled(false);
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

        // Save variables for fragments to use
        this.mContext = this;
        //Get Recipe Object passed by recipeList
        Intent intent = getIntent();
        currentRecipe = (Recipe)intent.getSerializableExtra("recipe");


    }

    public static Context getContext(){
        return mContext;
    }

    public static Recipe getCurrentRecipe() { return currentRecipe; }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start_assistant:
                Intent intent = new Intent(this, AssistantActivity.class);
                intent.putExtra("recipe",currentRecipe);
                startActivity(intent);
                return true;
            case R.id.delete_recipe:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Delete this recipe?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                HttpUtil httpUtil = new HttpUtil();
                                HashMap<String,String> deleteRecipeParams = new HashMap<String,String>();
                                deleteRecipeParams.put("r_name", currentRecipe.getRecipeName());
                                deleteRecipeParams.put("fb_id", AccountActivity.getFbId());
                                deleteRecipeParams.put("filter", "delete_recipe");
                                httpUtil.makeHttpPost(deleteRecipeParams);
                                Intent intent = new Intent(RecipeActivity.this, RecipeList.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;
            case R.id.edit_recipe:
                Intent i = new Intent(this, EditRecipeActivity.class);
                i.putExtra("recipe", (Serializable) getCurrentRecipe());
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public void addRecipeClicked(MenuItem menuItem) {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        startActivity(intent);
    }*/


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

    public void startAssistantClicked(View view){
        Intent intent = new Intent(RecipeActivity.getContext(), AssistantActivity.class);
        intent.putExtra("recipe",currentRecipe);
        startActivity(intent);
    }
}
