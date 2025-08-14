/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import in.co.madhur.vocabbuilder.BaseActivity;
import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.fragments.WordAddFragment;
import in.co.madhur.vocabbuilder.fragments.WordEditFragment;
import in.co.madhur.vocabbuilder.fragments.WordViewFragment;
import in.co.madhur.vocabbuilder.utils.AnalyticsHelper;
import com.google.firebase.analytics.FirebaseAnalytics;

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

        setContentView(R.layout.word_activity);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);

        setSupportActionBar(mToolbar);

        String action=getIntent().getAction();

        if(action.equalsIgnoreCase(Consts.ACTION_EDIT_WORD))
        {
            fragment=new WordEditFragment();
            // Track word edit screen view
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "WordEditScreen");
            bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "WordEditFragment");
            AnalyticsHelper.trackEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
        }
        else if(action.equalsIgnoreCase(Consts.ACTION_VIEW_WORD))
        {
            fragment=new WordViewFragment();
            // Track word view screen view
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "WordViewScreen");
            bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "WordViewFragment");
            AnalyticsHelper.trackEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
        }
        else if(action.equalsIgnoreCase(Consts.ACTION_ADD_WORD))
        {
            fragment=new WordAddFragment();
            // Track word add screen view
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "WordAddScreen");
            bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "WordAddFragment");
            AnalyticsHelper.trackEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
        }

        if(getIntent().getExtras()!=null)
            fragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().add(R.id.linear_layout,fragment).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}
