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
public class RecentWordListFragment extends BaseWordListFragment
{

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        LoadRecents();

    }



    private void LoadRecents()
    {

        new GetWords(Consts.SPINNER_ITEMS.RECENT).execute("");


    }


}
