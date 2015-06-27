package tk.wlemuel.cotable.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import java.util.ArrayList;
import java.util.List;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.base.BaseActivity;
import tk.wlemuel.cotable.core.AppConfig;
import tk.wlemuel.cotable.model.DrawerMenu;
import tk.wlemuel.cotable.ui.drawer.DrawerMenuAdapter;


public class MainActivity extends BaseActivity {

    private Toolbar mActionBar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private String[] mMenuTitles;
    private ListView mMenuListView;
    private LinearLayout mDrawerLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(savedInstanceState);

    }

    private void initView(Bundle savedInstanceState) {
        initActionBar();
        initSlidingMenu();
    }

    /**
     * init the action bar.
     */
    private void initActionBar() {

        mActionBar = (Toolbar) findViewById(R.id.actionBar);
        mActionBar.setTitle(R.string.app_name);
        setSupportActionBar(mActionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initSlidingMenu() {
        List<DrawerMenu> listMenuData = new ArrayList<>();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLeft = (LinearLayout) findViewById(R.id.drawer_left);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mActionBar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mMenuTitles = getResources().getStringArray(R.array.menu_array);
        mMenuListView = (ListView) findViewById(R.id.drawer_left_list);

        for (String data : mMenuTitles) {
            DrawerMenu itemData = new DrawerMenu();
            itemData.setIcon("{fa-rss}");
            itemData.setText(data);
            listMenuData.add(itemData);
        }

        mMenuListView.setAdapter(new DrawerMenuAdapter(this, listMenuData));
        mMenuListView.setOnItemClickListener(new DrawerItemClickListener());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_menu_search).setIcon(
                new IconDrawable(this, Iconify.IconValue.fa_search)
                        .colorRes(R.color.toolbar_text_color)
                        .sizeDp(AppConfig.SEARCH_ICON_SIZE_PATCH)
        );

        menu.findItem(R.id.action_menu_favor).setIcon(
                new IconDrawable(this, Iconify.IconValue.fa_star)
                        .colorRes(R.color.toolbar_text_color)
                        .actionBarSize()
        );

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case (R.id.action_menu_search):


                return true;
            case (R.id.action_menu_exit):
                if (!this.isFinishing())
                    this.finish();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mDrawerLayout.isDrawerOpen(mDrawerLeft)) {
                mDrawerLayout.closeDrawer(mDrawerLeft);
                return true;
            }
        } else if ((keyCode == KeyEvent.KEYCODE_MENU)) {
            if (mDrawerLayout.isDrawerOpen(mDrawerLeft)) {
                mDrawerLayout.closeDrawer(mDrawerLeft);
            } else {
                mDrawerLayout.openDrawer(mDrawerLeft);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void selectItem(int position) {
//        mMenuListView.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerLeft);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

}
