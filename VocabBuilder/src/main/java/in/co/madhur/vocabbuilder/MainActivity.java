package in.co.madhur.vocabbuilder;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import in.co.madhur.vocabbuilder.fragments.WordListFragment;
import in.co.madhur.vocabbuilder.service.Alarms;

import static in.co.madhur.vocabbuilder.Consts.*;


public class MainActivity extends BaseActivity implements ActionBar.OnNavigationListener
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

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, LISTS.names()));

        // Set the adapter for the list view
        //  mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        ArrayAdapter<CharSequence> someAdapter = new
                ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item,
                android.R.id.text1, getResources().getStringArray(R.array.spinner_items));

        someAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        getSupportActionBar().setListNavigationCallbacks(someAdapter, this);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);


        // Schedule alarm if its enabled
        Alarms alarms = new Alarms(this);

        if (!alarms.DoesAlarmExist())
        {
            if (alarms.ShouldSchedule())
                alarms.Schedule();
        }

        Intent intent = getIntent();
        if (intent != null)
        {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(Consts.ACTION_SHOW_RECENT))
            {
                LoadMainFragment(SPINNER_ITEMS.RECENT);


            }


        }
//        else
//        {
//
//            LoadMainFragment(SPINNER_ITEMS.RECENT);
//        }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();


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

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId)
    {
        WordListFragment wordFragment;
        SPINNER_ITEMS item = SPINNER_ITEMS.values()[itemPosition];

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0)
        {
            wordFragment = (WordListFragment) getSupportFragmentManager().getFragments().get(0);

            if (item == SPINNER_ITEMS.ACTIVE)
                wordFragment.LoadWord(0);
            else if (item == SPINNER_ITEMS.HIDDEN)
                wordFragment.LoadHiddenWords();
            else if (item == SPINNER_ITEMS.RECENT)
                wordFragment.LoadRecents();

        }
        else
        {
            wordFragment = new WordListFragment();
            LoadMainFragment(wordFragment, item);
        }


//            if(itemPosition== SPINNER_ITEMS.RECENT.ordinal())
//            {
//                wordsFragment.LoadRecents();
//            }
//            else if(itemPosition==SPINNER_ITEMS.ACTIVE.ordinal())
//            {
//                ((WordListFragment) wordFragment).LoadWord(1);
//            }
//            else if(itemPosition==SPINNER_ITEMS.HIDDEN.ordinal())
//            {
//                wordsFragment.LoadHiddenWords();
//            }


        return true;
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
        LoadMainFragment(SPINNER_ITEMS.ACTIVE);

    }

    public void LoadMainFragment(SPINNER_ITEMS item)
    {
        WordListFragment wordFragment = new WordListFragment();

        LoadMainFragment(wordFragment, item);


    }

    public void LoadMainFragment(Fragment wordFragment, SPINNER_ITEMS item)
    {

        Bundle data = new Bundle();
        data.putInt(Consts.SPINNER_ITEMS.class.getName(), item.ordinal());
        wordFragment.setArguments(data);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, wordFragment).commit();


    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Crouton.cancelAllCroutons();
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
