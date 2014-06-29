/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.fragments;

import android.os.Bundle;

import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.R;

/**
 * Created by madhur on 25-Jun-14.
 */
public class HiddenWordListFragment extends BaseWordListFragment
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        LoadHiddenWords();
    }

    public void LoadHiddenWords()
    {
        new GetWords(Consts.SPINNER_ITEMS.HIDDEN).execute();
    }
}
