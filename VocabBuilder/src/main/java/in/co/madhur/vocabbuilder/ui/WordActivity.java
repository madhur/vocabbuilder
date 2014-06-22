/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

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


}
