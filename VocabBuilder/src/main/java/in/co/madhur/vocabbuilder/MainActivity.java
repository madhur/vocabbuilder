package in.co.madhur.vocabbuilder;

import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import in.co.madhur.vocabbuilder.fragments.AboutDialog;
import in.co.madhur.vocabbuilder.fragments.WordListFragment;


public class MainActivity extends BaseActivity
{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close)
        {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, Consts.LISTS.names()));

        // Set the adapter for the list view
        //  mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setHomeButtonEnabled(true);




    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

        LoadMainFragment();

    }



    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        // If the nav drawer is open, hide action items related to the content
        // view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            Intent i = new Intent();
            i.setClass(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class DrawerItemClickListener implements
            ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            selectItem(position);
        }
    }

    public void LoadMainFragment()
    {
        WordListFragment wordFragment=new WordListFragment();


        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, wordFragment).commit();



    }


    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position)
    {
        // An alphabet list is selected, return
        if (position > 0 && position < 27)
        {
            Fragment wordFragment = getSupportFragmentManager().getFragments().get(0);
            if (wordFragment instanceof WordListFragment)
            {
                WordListFragment wordsFragment = (WordListFragment) wordFragment;
                wordsFragment.LoadWord(position);

            }


        }



        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void setTitle(CharSequence title)
    {
        getActionBar().setTitle(title);
    }


}
