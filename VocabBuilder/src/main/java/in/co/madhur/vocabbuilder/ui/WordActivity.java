/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import in.co.madhur.vocabbuilder.BaseActivity;
import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.fragments.WordFragment;

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
            fragment=new WordFragment();
        }
        else if(action.equalsIgnoreCase(Consts.ACTION_VIEW_WORD))
        {
            fragment=new WordFragment();

        }

        fragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();

    }


}
