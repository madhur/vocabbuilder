/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.AppPreferences;
import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.db.VocabDB;
import in.co.madhur.vocabbuilder.model.Word;
import in.co.madhur.vocabbuilder.ui.WordActivity;
import in.co.madhur.vocabbuilder.ui.WordTokenAdapter;

import static in.co.madhur.vocabbuilder.Consts.VALUE_NOT_SET;

/**
 * Created by madhur on 22-Jun-14.
 */
public class WordViewFragment extends Fragment
{

    private TextView word_name;
    private TextView word_meaning;
    private ListView synonymsListView, similarListView;
    protected int WordId;
    private int EDIT_REQUEST_CODE = 1;
    private RatingBar ratingBar;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.view_menu, menu);

        if (new AppPreferences(getActivity()).IsProMode())
        {
            menu.setGroupVisible(R.id.group_pro, true);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v;


        v= inflater.inflate(R.layout.word_view, container, false);


        word_name = (TextView) v.findViewById(R.id.word);
        word_meaning = (TextView) v.findViewById(R.id.meaning);
        synonymsListView = (ListView) v.findViewById(R.id.synonymsListView);
        similarListView = (ListView) v.findViewById(R.id.similarListView);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);


        ratingBar.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                Log.d(App.TAG, String.valueOf(hasFocus));
            }
        });

        ratingBar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        ratingBar.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {



                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    RatingBar ratingBar = (RatingBar) v;



                    if (ratingBar.getRating() == 1.0)
                    {
                        ratingBar.setRating((float) 0.0);
                    }
                    else if (ratingBar.getRating() == 0.0)
                    {
                        ratingBar.setRating((float) 0.5);
                    }
                    else if (ratingBar.getRating() == 0.5)
                    {
                        ratingBar.setRating((float) 1.0);
                    }




                    int dbRating = (int) (ratingBar.getRating() * 2);


                    try
                    {
                        VocabDB.getInstance(getActivity()).SetRating(WordId, dbRating);
                    }
                    catch (Exception e)
                    {
                        Log.e(App.TAG, e.getMessage());
                    }

                    NotifyChange();

                    return true;
                }

                return true;

            }

        });


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        WordId = getArguments().getInt(Consts.ID_PARAMETER);

        LoadWord(WordId);

        synonymsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                LaunchWord(synonymsListView, position);

            }
        });

        similarListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                LaunchWord(similarListView, position);

            }
        });


    }

    private void LoadWord(int id)
    {
        Word word;
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

        WordTokenAdapter synonymsAdapter = new WordTokenAdapter(word.getSynonyms(), getActivity());
        WordTokenAdapter similarAdapter = new WordTokenAdapter(word.getSimilar(), getActivity());

        synonymsListView.setAdapter(synonymsAdapter);
        similarListView.setAdapter(similarAdapter);

        ratingBar.setRating((float)word.getRating()/2);


    }

    private void LaunchWord(ListView listView, int position)
    {

        WordTokenAdapter wordsAdapter = (WordTokenAdapter) listView.getAdapter();

        Word word = (Word) wordsAdapter.getItem(position);


        Intent wordIntent = new Intent();
        wordIntent.setClass(getActivity(), WordActivity.class);
        wordIntent.setAction(Consts.ACTION_VIEW_WORD);

        Bundle data = new Bundle();
        data.putInt(Consts.ID_PARAMETER, word.getId());
        wordIntent.putExtras(data);

        startActivity(wordIntent);
    }

    private void NotifyChange()
    {
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent().setAction(Consts.ACTION_LIST_DATA_CHANGED));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        if (item.getItemId() == R.id.action_edit)
        {

            Intent wordIntent = new Intent();
            wordIntent.setClass(getActivity(), WordActivity.class);
            wordIntent.setAction(Consts.ACTION_EDIT_WORD);

            Bundle data = new Bundle();
            data.putInt(Consts.ID_PARAMETER, WordId);
            wordIntent.putExtras(data);

            startActivityForResult(wordIntent, EDIT_REQUEST_CODE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            if (data != null)
            {
                int WordId = data.getIntExtra(Consts.ID_PARAMETER, VALUE_NOT_SET);
                if (WordId != VALUE_NOT_SET)
                {
                    LoadWord(WordId);
                    NotifyChange();
                }
            }

        }
    }
}
