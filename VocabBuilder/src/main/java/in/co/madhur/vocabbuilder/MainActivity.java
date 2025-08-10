package in.co.madhur.vocabbuilder;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;



import de.keyboardsurfer.android.widget.crouton.Crouton;
import in.co.madhur.vocabbuilder.fragments.BaseWordListFragment;
import in.co.madhur.vocabbuilder.fragments.StatsFragment;
import in.co.madhur.vocabbuilder.service.Alarms;

import static in.co.madhur.vocabbuilder.Consts.LISTS;
import static in.co.madhur.vocabbuilder.Consts.SPINNER_ITEMS;


public class MainActivity extends BaseActivity
{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Ensure theme is properly applied for AppCompatActivity compatibility
        setTheme(R.style.Black);
        
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);

        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,  R.string.drawer_open, R.string.drawer_close)
        {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);

            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, LISTS.names()));


        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // Note: ActionBar.NAVIGATION_MODE_LIST is deprecated in newer support library versions
        // The navigation functionality is now handled through the drawer layout

        // Note: ActionBar list navigation is deprecated, using drawer navigation instead
        // The spinner functionality is now handled through the drawer list

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        // Load default fragment (ACTIVE words) when app starts
        LoadMainFragment(0); // 0 corresponds to ACTIVE in SPINNER_ITEMS

        // Schedule alarm if its enabled
        Alarms alarms = new Alarms(this);

        if (!alarms.DoesAlarmExist())
        {
            if (alarms.ShouldSchedule())
            {
                alarms.Schedule();
            }
        }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();


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


        return super.onOptionsItemSelected(item);
    }




    private void LoadMainFragment(int itemPosition)
    {
        Fragment fragment = new BaseWordListFragment();
        Bundle data = new Bundle();
        data.putLong(Consts.SPINNER_ITEMS.class.getName(), itemPosition);
        fragment.setArguments(data);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }


    private void LockDrawer(boolean isLock)
    {
        mDrawerToggle.setDrawerIndicatorEnabled(!isLock);
        getSupportActionBar().setDisplayHomeAsUpEnabled(!isLock);

        if (isLock)
        {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        else
        {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

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
        // Handle main navigation items (ACTIVE, RECENT, STARRED, etc.)
        if (position >= 27)
        {
            int itemIndex = position - 27;
            SPINNER_ITEMS item = SPINNER_ITEMS.values()[itemIndex];
            
            if (item == SPINNER_ITEMS.ACTIVE || item == SPINNER_ITEMS.RECENT || item == SPINNER_ITEMS.STARRED || item == SPINNER_ITEMS.UNSTARRED || item == SPINNER_ITEMS.HIDDEN)
            {
                if (item == SPINNER_ITEMS.ACTIVE)
                {
                    LockDrawer(false);
                }
                else
                {
                    LockDrawer(true);
                }

                if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0)
                {
                    Fragment fragment = getSupportFragmentManager().getFragments().get(0);
                    if (fragment instanceof BaseWordListFragment)
                    {
                        ((BaseWordListFragment) (fragment)).LoadData(item);
                    }
                    else
                    {
                        LoadMainFragment(itemIndex);
                    }
                }
                else
                {
                    LoadMainFragment(itemIndex);
                }
            }
            else if (item == SPINNER_ITEMS.STATS)
            {
                LockDrawer(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new StatsFragment()).commit();
            }
        }
        else
        {
            // An alphabet list is selected
            if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0)
            {
                Fragment wordFragment = getSupportFragmentManager().getFragments().get(0);
                if (wordFragment instanceof BaseWordListFragment)
                {
                    BaseWordListFragment wordsFragment = (BaseWordListFragment) wordFragment;
                    wordsFragment.LoadWord(position);
                }
            }
            else
            {
                // No fragments exist, load the default fragment first
                LoadMainFragment(0);
                // Then try to load the word
                if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0)
                {
                    Fragment wordFragment = getSupportFragmentManager().getFragments().get(0);
                    if (wordFragment instanceof BaseWordListFragment)
                    {
                        BaseWordListFragment wordsFragment = (BaseWordListFragment) wordFragment;
                        wordsFragment.LoadWord(position);
                    }
                }
            }
        }

        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

}
