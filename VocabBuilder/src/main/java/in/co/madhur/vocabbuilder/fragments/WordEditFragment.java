/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.controls.WordCompletionTextView;
import in.co.madhur.vocabbuilder.db.VocabDB;
import in.co.madhur.vocabbuilder.model.Word;

/**
 * Created by madhur on 22-Jun-14.
 */
public class WordEditFragment extends BaseWordFragment
{

    private TextView word_name;
    private EditText word_meaning;

    private ArrayList<Word> synonyms=new ArrayList<Word>();
    private ArrayList<Word> similar=new ArrayList<Word>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.word_edit, container, false);
        synonymsTextView = (WordCompletionTextView) v.findViewById(R.id.synonymssearchView);
        similarTextView = (WordCompletionTextView) v.findViewById(R.id.similarsearchView);

        word_name = (TextView) v.findViewById(R.id.word);
        word_meaning = (EditText) v.findViewById(R.id.meaning);

        synonymsTextView.setTokenListener(new TokenCompleteTextView.TokenListener()
        {
            @Override
            public void onTokenAdded(Object o)
            {
                synonyms.add((Word) o);
            }

            @Override
            public void onTokenRemoved(Object o)
            {
                synonyms.remove(o);
            }
        });

        similarTextView.setTokenListener(new TokenCompleteTextView.TokenListener()
        {
            @Override
            public void onTokenAdded(Object o)
            {
                similar.add((Word) o);
            }

            @Override
            public void onTokenRemoved(Object o)
            {
                similar.remove(o);
            }
        });


        new GetWords().execute("0");

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Word word = null;

         WordId = getArguments().getInt("id");

        VocabDB vocabDB = VocabDB.getInstance(getActivity());

        try
        {
            word = vocabDB.GetSingleWord(WordId);
        }
        catch (Exception e)
        {
            Crouton.showText(getActivity(), "Exception wile getting single word", Style.ALERT);
            e.printStackTrace();
            return;
        }

        word_name.setText(word.getName());
        word_meaning.setText(word.getMeaning());

        for(Word w: word.getSynonyms())
        {
            synonymsTextView.addObject(w);
        }

        for(Word w:word.getSimilar())
        {
            similarTextView.addObject(w);
        }


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_save)
        {

            if (TextUtils.isEmpty(word_meaning.getText()))
            {
                Crouton.showText(getActivity(), getString(R.string.empty_word_error), Style.ALERT);
                return true;
            }


            try
            {

                VocabDB.getInstance(getActivity()).UpdateWord(WordId, word_meaning.getText().toString(),synonyms, similar);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            getActivity().finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
