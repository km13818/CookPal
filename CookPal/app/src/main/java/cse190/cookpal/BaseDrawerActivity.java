package cse190.cookpal;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class BaseDrawerActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;

    List<DrawerItem> dataList;

    private static final int DRAWER_COOKBOOK = 0;
    private static final int DRAWER_ASSISTANT = 1;
    private static final int DRAWER_SETTINGS = 2;
    private static final int DRAWER_LOGOUT = 3;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    protected void onResume() {
        super.onResume();
        //setDrawerSelectedItem(getCurrentDrawerItem());
    }

    public void finish() {
        super.finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        setupNavDrawer();
    }

    protected void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
    }

    protected void setupNavDrawer() {
        //setContentView(R.layout.activity_main);

        // Initializing
        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        // Add Drawer Item to dataList
        dataList.add(new DrawerItem(getString(R.string.title_activity_cookbook), R.drawable.ic_action_settings));
        dataList.add(new DrawerItem(getString(R.string.title_activity_assistant), R.drawable.ic_action_settings));
        dataList.add(new DrawerItem(getString(R.string.title_activity_settings), R.drawable.ic_action_settings));
        dataList.add(new DrawerItem(getString(R.string.title_activity_logout), R.drawable.ic_action_settings));

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //if (savedInstanceState == null) {
        //    selectItem(0);
        //}

    }

    /*private void setDrawerSelectedItem(int menuItemPosition) {

    }

    private int getCurrentDrawerItem() {

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.layout, menu);
        return true;
    }

    public void SelectItem(int position) {
        Intent intent = null;
        switch (position) {
            case DRAWER_COOKBOOK:
                if (this instanceof RecipeList) {
                    break;
                }
                intent = new Intent(this, RecipeList.class);
                break;
            case DRAWER_ASSISTANT:
                if (this instanceof AssistantActivity) {
                    break;
                }
                intent = new Intent(this, AssistantActivity.class);
                break;
            case DRAWER_SETTINGS:
                if (this instanceof SettingsActivity) {
                    break;
                }
                intent = new Intent(this, SettingsActivity.class);
                break;
            case DRAWER_LOGOUT:
                intent = new Intent(this, AccountActivity.class);
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(dataList.get(position).getItemName());
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            SelectItem(position);

        }
    }
}
