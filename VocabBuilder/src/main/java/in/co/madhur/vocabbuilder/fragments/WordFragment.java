/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.controls.WordCompletionTextView;
import in.co.madhur.vocabbuilder.db.VocabDB;
import in.co.madhur.vocabbuilder.model.Word;

/**
 * Created by madhur on 22-Jun-14.
 */
public class WordFragment extends Fragment
{
    private WordCompletionTextView synonymsTextView, similarTextView;
    private TextView word_name;
    private EditText word_meaning;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.word, container, false);
        synonymsTextView= (WordCompletionTextView) v.findViewById(R.id.synonymssearchView);
        similarTextView= (WordCompletionTextView) v.findViewById(R.id.similarsearchView);

        word_name= (TextView) v.findViewById(R.id.word);
        word_meaning= (EditText) v.findViewById(R.id.meaning);


        new GetWords().execute("0");

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Word word=null;

        int WordId=getArguments().getInt("id");

        VocabDB vocabDB=VocabDB.getInstance(getActivity());

        try
        {
            word = vocabDB.GetSingleWord(WordId);
        }
        catch(Exception e)
        {
            Crouton.showText(getActivity(), e.getMessage(), Style.ALERT);
            return;
        }

        word_name.setText(word.getName());
        word_meaning.setText(word.getMeaning());


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.word_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==R.id.action_save)
        {

            if(TextUtils.isEmpty(word_meaning.getText()))
            {
                Crouton.showText(getActivity(), getString(R.string.empty_word_error), Style.ALERT);
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class GetWords extends AsyncTask<String, Integer, List<Word>>
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
               return vocabDB.GetAllWords();

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
                Log.d(App.TAG, String.valueOf(result.size()));

                ArrayAdapter<Word> adapter = new ArrayAdapter<Word>(getActivity(), android.R.layout.simple_list_item_1, result);

                synonymsTextView.setAdapter(adapter);

            }


        }
    }
}
