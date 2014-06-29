package in.co.madhur.vocabbuilder.fragments;

import android.os.Bundle;

import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.R;

/**
 * Created by madhur on 19-Jun-14.
 */
public class WordListFragment extends BaseWordListFragment
{

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        LoadWord(0);

        setCurrentLetter(0);

        RestoreListPosition();
    }

//    public void LoadWord(String startLetter)
//    {
//
//        new GetWords(Consts.SPINNER_ITEMS.ACTIVE).execute(startLetter);
//
//    }

    public void LoadWord(int position)
    {

        if(getCurrentLetter()!=-1)
            SaveListPosition();

        setCurrentLetter(position);

        new GetWords(Consts.SPINNER_ITEMS.ACTIVE).execute(String.valueOf(Consts.LISTS.values()[position]).toLowerCase());


    }


    @Override
    int GetMenu()
    {
        return R.menu.main;
    }
}
