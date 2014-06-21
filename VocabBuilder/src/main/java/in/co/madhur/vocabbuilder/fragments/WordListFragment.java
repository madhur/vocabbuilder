package in.co.madhur.vocabbuilder.fragments;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;

import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;

import com.fortysevendeg.swipelistview.SwipeListView;

import java.util.List;

import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.WordsAdapter;
import in.co.madhur.vocabbuilder.db.VocabDB;
import in.co.madhur.vocabbuilder.model.Word;

/**
 * Created by madhur on 19-Jun-14.
 */
public class WordListFragment extends Fragment
{

    SwipeListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.word_fragment, container, false);

        listView = (SwipeListView) v.findViewById(R.id.wordsListView);

        listView.setFocusable(false);

        registerForContextMenu(listView);




        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.wordsListView)
        {
            SwipeListView lv = (SwipeListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Word word = (Word) lv.getItemAtPosition(acmi.position);

            menu.add(0, R.id.action_synonyms, 0, getString(R.string.action_synonyms) + " " + word.getName());
            menu.add(0, R.id.action_similar, 0, getString(R.string.action_similar) + " " + word.getName());
            menu.add(0, R.id.action_hide, 0, getString(R.string.action_hide) + " " + word.getName());

         //   MenuInflater inflater = getActivity().getMenuInflater();
          //  inflater.inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchitem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);
        SearchableInfo info = searchManager.getSearchableInfo(getActivity().getComponentName());
        searchView.setSearchableInfo(info);

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                WordsAdapter adapter = (WordsAdapter) listView.getAdapter();
                if (adapter != null)
                {
                    adapter.getFilter().filter(newText, new Filter.FilterListener()
                    {

                        @Override
                        public void onFilterComplete(int count)
                        {

                        }
                    });
                }

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query)
            {
                WordsAdapter adapter = (WordsAdapter) listView.getAdapter();
                if (adapter != null)
                {
                    adapter.getFilter().filter(query, new Filter.FilterListener()
                    {

                        @Override
                        public void onFilterComplete(int count)
                        {

                        }
                    });
                }

                return true;
            }

        };
        searchView.setOnQueryTextListener(textChangeListener);

        super.onCreateOptionsMenu(menu, inflater);

    }

    public void LoadWord(String startLetter)
    {

        new GetWords().execute(startLetter);

    }

    public void LoadWord(int position)
    {

        new GetWords().execute(String.valueOf(Consts.LISTS.values()[position]));

    }

    private class GetWords extends AsyncTask<String, Integer, List<Word>>
    {
        VocabDB vocabDB;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected List<Word> doInBackground(String... params)
        {
            vocabDB = VocabDB.getInstance(getActivity());
            List<Word> words = vocabDB.GetWords(params[0]);


            return words;


        }

        @Override
        protected void onPostExecute(List<Word> result)
        {
            super.onPostExecute(result);
            WordsAdapter adapter = new WordsAdapter(result, getActivity());
            listView.setAdapter(adapter);


        }
    }


}
