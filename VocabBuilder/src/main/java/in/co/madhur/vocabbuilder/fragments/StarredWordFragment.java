/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.fragments;

import android.os.Bundle;

import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.R;

/**
 * Created by madhur on 27-Jun-14.
 */
public class StarredWordFragment extends BaseWordListFragment
{


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        LoadStarred();

    }



    private void LoadStarred()
    {

        new GetWords(Consts.SPINNER_ITEMS.STARRED).execute("");


    }


    @Override
    int GetMenu()
    {
        return R.menu.recent_menu;
    }
}
