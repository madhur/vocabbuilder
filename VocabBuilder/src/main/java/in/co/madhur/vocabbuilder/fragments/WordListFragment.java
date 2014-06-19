package in.co.madhur.vocabbuilder.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fortysevendeg.swipelistview.SwipeListView;

import java.util.List;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.word_fragment, container, false);

        listView=(SwipeListView)v.findViewById(R.id.wordsListView);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        new GetWords().execute(0);
    }

    private class GetWords extends AsyncTask<Integer, Integer, List<Word>>
    {
        VocabDB vocabDB;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected  List<Word> doInBackground(Integer... params)
        {
            vocabDB=VocabDB.getInstance(getActivity());
            List<Word> words=vocabDB.GetWords("a");



            return words;



        }

        @Override
        protected void onPostExecute( List<Word>  result)
        {
            super.onPostExecute(result);
            WordsAdapter adapter=new WordsAdapter(result, getActivity());
            listView.setAdapter(adapter);


        }
    }


}
