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
import android.widget.ListView;
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

    private ListView listView;

    private AppPreferences appPreferences;
    private ProgressBar progressBar;
    private int currentLetter=-1;
    private Consts.WORDS_MODE wordMode;
//    private Parcelable mListState = null;
//    private static final String LIST_STATE = "listState";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(App.TAG, " WordListFragment: onCreate ");


        super.onCreate(savedInstanceState);

        appPreferences = new AppPreferences(getActivity());
        wordMode=appPreferences.GetLearningMode();

        setHasOptionsMenu(true);

        MainActivity activity= (MainActivity) getActivity();

        if(appPreferences.GetTheme()== Consts.THEME.DARK)
            activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(App.TAG, " WordListFragment: onCreateView ");

        View v = inflater.inflate(R.layout.word_fragment, container, false);

        if(wordMode== Consts.WORDS_MODE.FLASHCARDS)
        {
            listView = (SwipeListView) v.findViewById(R.id.wordsListView);

        }
        else if(wordMode==Consts.WORDS_MODE.DICTIONARY)
        {
            listView = (ListView) v.findViewById(R.id.wordsPlainListView);
        }

        listView.setVisibility(View.VISIBLE);

        progressBar=(ProgressBar)v.findViewById(R.id.scroll_progressbar);


        if(listView instanceof SwipeListView)
        {
            ((SwipeListView) (listView)).setSwipeListViewListener(new BaseSwipeListViewListener()
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
                    super.onClickFrontView(position);
                    LaunchWord(position);


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
        }
        else
        {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    LaunchWord(position);
                }
            });
        }


        listView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {


                if (totalItemCount != 0)
                {
                    if(firstVisibleItem==0)
                    {
                        progressBar.setProgress(0);
                        return;
                    }

                    int progress =((firstVisibleItem+visibleItemCount)*100) / totalItemCount;


                    progressBar.setProgress(progress);
                }

            }
        });


        registerForContextMenu(listView);


        return v;
    }

    private void LaunchWord(int position)
    {
        WordsAdapter wordsAdapter = (WordsAdapter) listView.getAdapter();

        Word word = (Word) wordsAdapter.getItem(position);




        Intent wordIntent = new Intent();
        wordIntent.setClass(getActivity(), WordActivity.class);
        wordIntent.setAction(Consts.ACTION_VIEW_WORD);

        Bundle data = new Bundle();
        data.putInt("id", word.getId());
        wordIntent.putExtras(data);

        startActivity(wordIntent);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        getActivity().supportInvalidateOptionsMenu();


        RestoreListPosition();

    }

    protected  void RestoreListPosition()
    {
        Log.d(App.TAG, "Restoring list position" + String.valueOf(currentLetter));

        if (listView != null && appPreferences != null)
        {

            WordsAdapter adapter=(WordsAdapter)listView.getAdapter();

            if(adapter!=null)
            {
                Log.d(App.TAG, appPreferences.GetSortOrder(currentLetter).name());
                adapter.Sort(appPreferences.GetSortOrder(currentLetter));

            }

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
        Log.d(App.TAG, "Saving list position" + String.valueOf(currentLetter));

        if(listView!=null &&  appPreferences!=null)
        {
            appPreferences.SaveListPosition(currentLetter, listView.getFirstVisiblePosition());
            WordsAdapter adapter=(WordsAdapter)listView.getAdapter();
            if(adapter!=null)
            {
                Log.d(App.TAG, adapter.getActiveSortOrder().name());
                appPreferences.SetSortOrder(currentLetter, adapter.getActiveSortOrder());
            }

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        boolean isPro=appPreferences.IsProMode();

        if (v.getId() == R.id.wordsListView)
        {

            getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);

            if(isPro)
            {
                menu.setGroupVisible(R.id.group_pro_context, true);
            }

            SwipeListView lv = (SwipeListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Word word = (Word) lv.getItemAtPosition(acmi.position);
            if(word!=null)
            {

                if(!word.isHidden())
                    menu.findItem(R.id.action_hide).setTitle(getString(R.string.action_hide) + " " + word.getName());
                else
                    menu.findItem(R.id.action_hide).setTitle(getString(R.string.action_unhide) + " " + word.getName());

                if(word.isUserWord() && isPro)
                {
                    menu.findItem(R.id.action_delete).setTitle(getString(R.string.action_delete) + " " + word.getName());
                    menu.findItem(R.id.action_delete).setVisible(true);
                }
                else
                    menu.findItem(R.id.action_delete).setVisible(false);

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
                    if(!word.isHidden())
                        VocabDB.getInstance(getActivity()).HideWord(word.getId());
                    else
                        VocabDB.getInstance(getActivity()).HideWord(word.getId(), false);



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
                return true;

            case R.id.action_delete:
                try
                {
                    VocabDB.getInstance(getActivity()).DeleteWord(word.getId());

                    WordsAdapter wordsAdapter = (WordsAdapter) listView.getAdapter();
                    wordsAdapter.HideWord(word.getId());
                    wordsAdapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return true;

        }
        return super.onContextItemSelected(item);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.d(App.TAG, (String)item.getTitle());

        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            Intent i = new Intent();
            i.setClass(getActivity(), SettingsActivity.class);
            startActivity(i);
            return true;
        }

        if(id==R.id.action_sortalpha_asc)
        {
            WordsAdapter wordApater = (WordsAdapter) listView.getAdapter();
            if (wordApater != null)
            {
                wordApater.Sort(Consts.WORDS_SORT_ORDER.ALPHABETICAL_ASC);
            }

            return true;
        }
         if(id==R.id.action_sortalpha_desc)
        {
            WordsAdapter wordApater = (WordsAdapter) listView.getAdapter();
            if (wordApater != null)
            {
                wordApater.Sort(Consts.WORDS_SORT_ORDER.ALPHABETICAL_DESC);
            }

            return true;

        }

        if(id==R.id.action_sortstar_asc)
        {

            WordsAdapter wordApater = (WordsAdapter) listView.getAdapter();
            if (wordApater != null)
            {
                wordApater.Sort(Consts.WORDS_SORT_ORDER.STARRED_ASC);
            }

            return true;
        }

        if(id==R.id.action_sortstar_desc)
        {

            WordsAdapter wordApater = (WordsAdapter) listView.getAdapter();
            if (wordApater != null)
            {
                wordApater.Sort(Consts.WORDS_SORT_ORDER.STARRED_DESC);
            }

            return true;
        }


        if(item.getItemId() == R.id.action_add)
        {
            Intent wordIntent = new Intent();
            wordIntent.setClass(getActivity(), WordActivity.class);
            wordIntent.setAction(Consts.ACTION_ADD_WORD);


            startActivity(wordIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    int GetMenu()
    {
        return R.menu.main;
    }

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
                else if(item==Consts.SPINNER_ITEMS.STARRED)
                    words = vocabDB.GetFilteredWords(Consts.SELECT_NOTIFICATION_WORDS.BOTH);
                else if(item==Consts.SPINNER_ITEMS.UNSTARRED)
                    words=vocabDB.GetFilteredWords(Consts.SELECT_NOTIFICATION_WORDS.UNSTARRED);
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
                WordsAdapter adapter = new WordsAdapter(result, getActivity(), wordMode);

                if(item== Consts.SPINNER_ITEMS.RECENT)
                    adapter.Sort(Consts.WORDS_SORT_ORDER.DATE);

                listView.setAdapter(adapter);

                RestoreListPosition();
            }


        }
    }

}
