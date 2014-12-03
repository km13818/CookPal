package cse190.cookpal;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.Session;

import java.util.ArrayList;
import java.util.List;

public class BaseDrawerActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;

    List<DrawerItem> dataList;

    private static final int DRAWER_COOKBOOK = 0;
    private static final int DRAWER_ASSISTANT = 1;
    private static final int DRAWER_SHARE = 2;
    private static final int DRAWER_LOGOUT = 3;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    protected void onResume() {
        super.onResume();
        setDrawerSelectedItem(getCurrentDrawerItem());
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
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
        dataList.add(new DrawerItem(getString(R.string.nav_title_cookbook), R.drawable.nav_cookbook_icon));
        dataList.add(new DrawerItem(getString(R.string.nav_title_assistant), R.drawable.nav_chef_icon));
        dataList.add(new DrawerItem(getString(R.string.nav_title_share), R.drawable.abc_ic_menu_share_holo_dark));
        dataList.add(new DrawerItem(getString(R.string.nav_title_logout), R.drawable.nav_settings_icon));

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(isDrawerIndicatorEnabled());
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //if (savedInstanceState == null) {
        //    selectItem(0);
        //}

    }

    private void setDrawerSelectedItem(int menuItemPosition) {
        if (isDrawerIndicatorEnabled() && menuItemPosition != -1) {
            mDrawerList.setItemChecked(menuItemPosition, true);
        }
    }

    private int getCurrentDrawerItem() {
        if (this instanceof RecipeList) {
            return DRAWER_COOKBOOK;
        } else if (this instanceof AssistantActivity) {
            return DRAWER_ASSISTANT;

        } else if (this instanceof ShareActivity) {
            return DRAWER_SHARE;

        }
        return -1;
    }

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
            case DRAWER_SHARE:
                if (this instanceof ShareActivity) {
                    break;
                }
                intent = new Intent(this, ShareActivity.class);
                break;
            case DRAWER_LOGOUT:
                if (Session.getActiveSession() != null) {
                    Session.getActiveSession().closeAndClearTokenInformation();
                }

                Session.setActiveSession(null);
                intent = new Intent(this, AccountActivity.class);
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);

            if (!(this instanceof RecipeList)) {
                finish();
            }
        }

        mDrawerList.setItemChecked(position, true);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer();
            return;
        }
        super.onBackPressed();
    }

    protected boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(mDrawerList);
    }

    protected void closeDrawer() {
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    protected void openDrawer() {
        mDrawerLayout.openDrawer(mDrawerList);
    }

    protected boolean isDrawerEnabled() {
        return true;
    }

    private boolean toggleDrawer() {
        if (isDrawerIndicatorEnabled()) {
            if (mDrawerLayout.isDrawerVisible(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
            return true;
        }
        return false;
    }

    /** Override this to disable drawer indicator. */
    protected boolean isDrawerIndicatorEnabled() {
        return true;
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return toggleDrawer();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*@Override
    public void setActionBarView(Activity activity) {
        if (isDrawerOpen()) {
            getActionBar().setCustomView(null);
        } else {
            super.setActionBarView(activity);
        }
    }*/

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            SelectItem(position);

        }
    }
}
