/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.MenuItem;

import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.BaseActivity;
import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.fragments.WordAddFragment;
import in.co.madhur.vocabbuilder.fragments.WordEditFragment;
import in.co.madhur.vocabbuilder.fragments.WordViewFragment;

/**
 * Created by madhur on 22-Jun-14.
 */
public class WordActivity extends BaseActivity
{
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String action=getIntent().getAction();

        if(action.equalsIgnoreCase(Consts.ACTION_EDIT_WORD))
        {
            fragment=new WordEditFragment();
        }
        else if(action.equalsIgnoreCase(Consts.ACTION_VIEW_WORD))
        {
            fragment=new WordViewFragment();

        }
        else if(action.equalsIgnoreCase(Consts.ACTION_ADD_WORD))
        {
            fragment=new WordAddFragment();
        }

        if(getIntent().getExtras()!=null)
            fragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.d(App.TAG, (String)item.getTitle());

        if (item.getItemId() == android.R.id.home)
        {
            Log.d(App.TAG, "Navigating up");

            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent))
            {
                Log.d(App.TAG, "Navigating up 1");
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(this)
                        // Add all of this activity's parents to the back stack
                        .addNextIntentWithParentStack(upIntent)
                                // Navigate up to the closest parent
                        .startActivities();
            }
            else
            {
                Log.d(App.TAG, "Navigating up 2");
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
                NavUtils.navigateUpTo(this, upIntent);
            }
            return true;
        }

        return false;
    }
}
