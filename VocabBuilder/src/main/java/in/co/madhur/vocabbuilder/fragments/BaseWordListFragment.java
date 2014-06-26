/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.fragments;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ProgressBar;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.AppPreferences;
import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.MainActivity;
import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.SettingsActivity;
import in.co.madhur.vocabbuilder.db.VocabDB;
import in.co.madhur.vocabbuilder.model.Word;
import in.co.madhur.vocabbuilder.ui.WordActivity;

/**
 * Created by madhur on 25-Jun-14.
 */
public abstract class BaseWordListFragment extends Fragment
{

    private SwipeListView listView;
    private AppPreferences appPreferences;
    private ProgressBar progressBar;
    private int currentLetter=-1;
//    private Parcelable mListState = null;
//    private static final String LIST_STATE = "listState";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(App.TAG, " WordListFragment: onCreate ");


        super.onCreate(savedInstanceState);

        appPreferences = new AppPreferences(getActivity());

        setHasOptionsMenu(true);

        MainActivity activity= (MainActivity) getActivity();

        if(appPreferences.GetTheme()== Consts.THEME.DARK)
            activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));


    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
//        mListState = listView.onSaveInstanceState();
//        outState.putParcelable(LIST_STATE, mListState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState)
    {
        super.onViewStateRestored(savedInstanceState);

//        if(savedInstanceState!=null)
//            if(savedInstanceState.containsKey(LIST_STATE))
//                mListState = savedInstanceState.getParcelable(LIST_STATE);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(App.TAG, " WordListFragment: onCreateView ");

        View v = inflater.inflate(R.layout.word_fragment, container, false);

        listView = (SwipeListView) v.findViewById(R.id.wordsListView);
        progressBar=(ProgressBar)v.findViewById(R.id.scroll_progressbar);


        listView.setSwipeListViewListener(new BaseSwipeListViewListener()
        {
            @Override
            public void onOpened(int position, boolean toRight)
            {
                super.onOpened(position, toRight);

                WordsAdapter wordsAdapter = (WordsAdapter) listView.getAdapter();
                wordsAdapter.setDisplayedPosition(position);
                wordsAdapter.notifyDataSetChanged();


            }

            @Override
            public void onClosed(int position, boolean fromRight)
            {
                super.onOpened(position, fromRight);

                WordsAdapter wordsAdapter = (WordsAdapter) listView.getAdapter();
                wordsAdapter.setDisplayedPosition(-1);
                wordsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onListChanged()
            {
                super.onListChanged();
            }

            @Override
            public void onMove(int position, float x)
            {
                super.onMove(position, x);
            }

            @Override
            public void onStartOpen(int position, int action, boolean right)
            {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
                super.onStartOpen(position, action, right);
            }

            @Override
            public void onStartClose(int position, boolean right)
            {
                Log.d("swipe", String.format("onStartClose %d", position));
                super.onStartClose(position, right);
            }

            @Override
            public void onClickFrontView(int position)
            {
                Log.d("swipe", String.format("onClickFrontView %d", position));
                WordsAdapter wordsAdapter = (WordsAdapter) listView.getAdapter();

                Word word= (Word) wordsAdapter.getItem(position);

                //listView.openAnimate(position); //when you touch front view it will open
                super.onClickFrontView(position);

                Intent wordIntent=new Intent();
                wordIntent.setClass(getActivity(), WordActivity.class);
                wordIntent.setAction(Consts.ACTION_VIEW_WORD);

                Bundle data=new Bundle();
                data.putInt("id",word.getId());
                wordIntent.putExtras(data);

                startActivity(wordIntent);

            }

            @Override
            public void onClickBackView(int position)
            {
                Log.d("swipe", String.format("onClickBackView %d", position));

                //listView.closeAnimate(position);//when you touch back view it will close
                super.onClickFrontView(position);
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions)
            {
                super.onDismiss(reverseSortedPositions);

            }

        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
//                Log.d(App.TAG, String.valueOf(firstVisibleItem));
//                Log.d(App.TAG, String.valueOf(visibleItemCount));
//                Log.d(App.TAG, String.valueOf(totalItemCount));

                if (totalItemCount != 0)
                {
                    if(firstVisibleItem==0)
                    {
                        progressBar.setProgress(0);
                        return;
                    }

                    int progress =((firstVisibleItem+visibleItemCount)*100) / totalItemCount;
//                    Log.d(App.TAG, "Progress " + progress );

                    progressBar.setProgress(progress);
                }

            }
        });


        registerForContextMenu(listView);


        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(App.TAG, " WordListFragment: onResume ");
        getActivity().supportInvalidateOptionsMenu();


        RestoreListPosition();


//        if (mListState != null)
//            listView.onRestoreInstanceState(mListState);
//        mListState = null;
    }

    protected  void RestoreListPosition()
    {
        if (listView != null && appPreferences != null)
        {
            int pos=appPreferences.GetListPosition(currentLetter);

            if(listView.getCount() > pos)
                listView.setSelectionFromTop(pos, 0);
        }
        else
            Log.d(App.TAG, "is null");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        SaveListPosition();
    }

    protected  void SaveListPosition()
    {

        if(listView!=null &&  appPreferences!=null)
            appPreferences.SaveListPosition(currentLetter, listView.getFirstVisiblePosition());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.wordsListView)
        {

            getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);

            if(appPreferences.IsProMode())
            {
                menu.setGroupVisible(R.id.group_pro_context, true);
            }

            SwipeListView lv = (SwipeListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Word word = (Word) lv.getItemAtPosition(acmi.position);
            if(word!=null)
            {

                //      menu.findItem(R.id.action_synonyms).setTitle(getString(R.string.action_synonyms) + " " + word.getName());
                //     menu.findItem(R.id.action_similar).setTitle(getString(R.string.action_similar) + " " + word.getName());
                menu.findItem(R.id.action_hide).setTitle(getString(R.string.action_hide) + " " + word.getName());
                menu.findItem(R.id.action_edit).setTitle(getString(R.string.action_edit) + " " + word.getName());
                menu.findItem(R.id.action_view).setTitle(getString(R.string.action_view) + " " + word.getName());
            }

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Word word = (Word) listView.getItemAtPosition(info.position);

        Intent wordIntent;
        Bundle data;

        switch (item.getItemId())
        {

            case R.id.action_hide:

                try
                {
                    VocabDB.getInstance(getActivity()).HideWord(word.getId());

                    //LoadWord(word.getName().charAt(0));

                    WordsAdapter wordsAdapter = (WordsAdapter) listView.getAdapter();
                    wordsAdapter.HideWord(word.getId());
                    wordsAdapter.notifyDataSetChanged();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return true;

            case R.id.action_edit:
                wordIntent=new Intent();
                wordIntent.setClass(getActivity(), WordActivity.class);
                wordIntent.setAction(Consts.ACTION_EDIT_WORD);

                data=new Bundle();
                data.putInt("id", word.getId());
                wordIntent.putExtras(data);

                startActivity(wordIntent);

                return true;

            case R.id.action_view:
                wordIntent=new Intent();
                wordIntent.setClass(getActivity(), WordActivity.class);
                wordIntent.setAction(Consts.ACTION_VIEW_WORD);

                data=new Bundle();
                data.putInt("id", word.getId());
                wordIntent.putExtras(data);

                startActivity(wordIntent);

        }
        return super.onContextItemSelected(item);
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState)
//    {
//        super.onActivityCreated(savedInstanceState);
//        Log.d(App.TAG, " WordListFragment: onActivityCreated ");
//
//        Bundle data=getArguments();
//
//        if(data!=null)
//        {
//            Log.d(App.TAG, " WordListFragment: onActivityCreated: data!=null ");
//            Consts.SPINNER_ITEMS item= Consts.SPINNER_ITEMS.values()[data.getInt(Consts.SPINNER_ITEMS.class.getName())];
//            Log.d(App.TAG, item.name());
//
//            if(item== Consts.SPINNER_ITEMS.ACTIVE)
//
//            else if(item== Consts.SPINNER_ITEMS.RECENT)
//                LoadRecents();
//            else if(item== Consts.SPINNER_ITEMS.HIDDEN)
//                LoadHiddenWords();
//
//        }
//        else
//        {
//            Log.d(App.TAG, " WordListFragment: onActivityCreated: data==null ");
//            LoadWord(0);
//        }
//    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            Intent i = new Intent();
            i.setClass(getActivity(), SettingsActivity.class);
            startActivity(i);
            return true;
        }


        if (item.getItemId() == R.id.action_sort)
        {

            WordsAdapter wordApater = (WordsAdapter) listView.getAdapter();
            if (wordApater != null)
            {
                wordApater.ToggleSort();
            }

            return true;

        }

        if(item.getItemId() == R.id.action_add)
        {
            Intent wordIntent = new Intent();
            wordIntent.setClass(getActivity(), WordActivity.class);
            wordIntent.setAction(Consts.ACTION_ADD_WORD);

            //data=new Bundle();
            // data.putInt("id", word.getId());
            //wordIntent.putExtras(data);

            startActivity(wordIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    abstract int GetMenu();

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {

        inflater.inflate(GetMenu(), menu);

        if(appPreferences.IsProMode())
        {

            menu.setGroupVisible(R.id.group_pro, true);
        }

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
                if(listView!=null)
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

                return false;

            }

            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if(listView!=null)
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

                return false;
            }

        };
        searchView.setOnQueryTextListener(textChangeListener);

        super.onCreateOptionsMenu(menu, inflater);

    }

    protected int getCurrentLetter()
    {
        return currentLetter;
    }

    protected void setCurrentLetter(int currentLetter)
    {
        this.currentLetter = currentLetter;
    }


    protected class GetWords extends AsyncTask<String, Integer, List<Word>>
    {
        private VocabDB vocabDB;
        private Consts.SPINNER_ITEMS item;

        public GetWords(Consts.SPINNER_ITEMS item)
        {
            this.item=item;

        }

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

                if(item== Consts.SPINNER_ITEMS.HIDDEN)
                    words = vocabDB.GetHiddenWords();
                else if (params.length > 0 && !TextUtils.isEmpty(params[0]))
                {
                    words = vocabDB.GetWords(params[0]);
                    App.Cache.Put(params[0], words);
                }
                else if(item== Consts.SPINNER_ITEMS.RECENT)
                    words = vocabDB.GetRecentWords();
            }
            catch (final Exception e)
            {
                Log.e(App.TAG,"Error in do in background");


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
                WordsAdapter adapter = new WordsAdapter(result, getActivity());

                if(item== Consts.SPINNER_ITEMS.RECENT)
                    adapter.Sort(Consts.WORDS_SORT_ORDER.DATE);

                listView.setAdapter(adapter);

                RestoreListPosition();
            }


        }
    }

}
