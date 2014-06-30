/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

/**
 * Created by madhur on 22-Jun-14.
 */
public class WordViewFragment extends Fragment
{

    private TextView word_name;
    private TextView word_meaning;
    private ListView synonymsListView, similarListView;
    protected int WordId;
    private int EDIT_REQUEST_CODE=1;

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
        View v = inflater.inflate(R.layout.word_view, container, false);


        word_name = (TextView) v.findViewById(R.id.word);
        word_meaning = (TextView) v.findViewById(R.id.meaning);
        synonymsListView = (ListView) v.findViewById(R.id.synonymsListView);
        similarListView = (ListView) v.findViewById(R.id.similarListView);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        WordId = getArguments().getInt("id");

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


    }

    private void LaunchWord(ListView listView, int position)
    {

        WordTokenAdapter wordsAdapter = (WordTokenAdapter) listView.getAdapter();

        Word word= (Word) wordsAdapter.getItem(position);


        Intent wordIntent=new Intent();
        wordIntent.setClass(getActivity(), WordActivity.class);
        wordIntent.setAction(Consts.ACTION_VIEW_WORD);

        Bundle data=new Bundle();
        data.putInt("id",word.getId());
        wordIntent.putExtras(data);

        startActivity(wordIntent);
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
            data.putInt("id", WordId);
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

        if(requestCode==EDIT_REQUEST_CODE && resultCode== Activity.RESULT_OK)
        {
            if(data!=null)
            {
                int WordId = data.getIntExtra("id", -1);
                if (WordId != -1)
                {
                    LoadWord(WordId);

                }
            }

        }
    }
}
