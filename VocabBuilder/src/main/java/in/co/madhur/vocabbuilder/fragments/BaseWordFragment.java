/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.controls.WordCompletionTextView;
import in.co.madhur.vocabbuilder.db.VocabDB;
import in.co.madhur.vocabbuilder.model.Word;
import in.co.madhur.vocabbuilder.ui.WordTokenAdapter;

/**
 * Created by madhur on 22-Jun-14.
 */
public abstract class BaseWordFragment extends Fragment
{
    protected WordCompletionTextView synonymsTextView, similarTextView;
    protected int WordId;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.word_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    protected class GetWords extends AsyncTask<String, Integer, List<Word>>
    {
        private VocabDB vocabDB;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected List<Word> doInBackground(String... params)
        {
            vocabDB = VocabDB.getInstance(getActivity());

            List<Word> words = null;
            try
            {
                return vocabDB.GetAllWords(true);
            }
            catch (final Exception e)
            {
                Log.e(App.TAG, "Error in do in background");


                getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Crouton.showText(getActivity(), e.getMessage(), Style.ALERT);

                    }
                });

            }

            return words;


        }

        @Override
        protected void onPostExecute(List<Word> result)
        {
            super.onPostExecute(result);
            if (result != null)
            {
                // Remove the word itself from the list, else it causes stackoverflow.
                // Prevent circular reference
                result.remove(Word.findById(result, WordId));


                WordTokenAdapter adapter=new WordTokenAdapter(result, getActivity());

                synonymsTextView.setAdapter(adapter);
                similarTextView.setAdapter(adapter);

            }


        }
    }
}
