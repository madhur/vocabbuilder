package in.co.madhur.vocabbuilder.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.AppPreferences;
import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.MainActivity;
import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.SettingsActivity;
import in.co.madhur.vocabbuilder.db.VocabDB;

/**
 * Created by madhur on 19-Jun-14.
 */
public class StatsFragment extends Fragment
{
    private TextView starredWordCount, unstarredWordCount, halfStarredWordCount, hiddenWordCount, notifiedWordCount, userWordCount, originalWordCount, totalWordCount;
    private TextView starredWordPercent, unstarredWordPercent, halfstarredWordPercent, hiddenWordPercent, notifiedWordPercent;
    private AppPreferences appPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.stats_fragment, container, false);
        starredWordCount= (TextView) v.findViewById(R.id.starred_words_count);
        unstarredWordCount=(TextView)v.findViewById(R.id.unstarred_words_count);
        halfStarredWordCount=(TextView)v.findViewById(R.id.halfstarred_words_count);
        hiddenWordCount=(TextView)v.findViewById(R.id.hidden_words_count);
        notifiedWordCount=(TextView)v.findViewById(R.id.notified_words_count);
        userWordCount=(TextView)v.findViewById(R.id.custom_words_count);
        originalWordCount=(TextView)v.findViewById(R.id.original_words_count);
        totalWordCount=(TextView)v.findViewById(R.id.total_words_count);

        starredWordPercent=(TextView)v.findViewById(R.id.starred_words_percent);
        unstarredWordPercent=(TextView)v.findViewById(R.id.unstarred_words_percent);
        halfstarredWordPercent=(TextView)v.findViewById(R.id.halfstarred_words_percent);
        hiddenWordPercent=(TextView)v.findViewById(R.id.hidden_words_percent);
        notifiedWordPercent=(TextView)v.findViewById(R.id.notified_words_percent);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        new GetWords().execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        appPreferences = new AppPreferences(getActivity());

        MainActivity activity= (MainActivity) getActivity();
//
//        if(appPreferences.GetTheme()== Consts.THEME.DARK)
//            activity.getSupportActionBar().setBackgroundDrawable(getActivity().getResources().getDrawable(android.support.v7.appcompat.R.drawable.abc_ab_share_pack_holo_dark));

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.stat_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==R.id.action_settings)
        {
            Intent i = new Intent();
            i.setClass(getActivity(), SettingsActivity.class);
            startActivity(i);
            return true;


        }

        return false;
    }

    private class GetWords extends AsyncTask<String, Integer, Void>
    {
        private VocabDB vocabDB;
        int totalWords, userWords, originalWords, starredWords, halfStarredWords, nostarWords, notifiedWords, hiddenWords;

        public GetWords()
        {


        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params)
        {
            vocabDB = VocabDB.getInstance(getActivity());


            try
            {
                userWords=vocabDB.GetWordsCount(1);
                originalWords=vocabDB.GetWordsCount(0);
                totalWords=userWords+originalWords;

                starredWords=vocabDB.GetStarredWordsCount(Consts.STAR.FULL_STAR);
                halfStarredWords=vocabDB.GetStarredWordsCount(Consts.STAR.HALF_STAR);
                nostarWords=totalWords-halfStarredWords-starredWords;

                notifiedWords=vocabDB.GetRecentWordCount();
                hiddenWords=vocabDB.GetHiddenWordCount();


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

            return null;


        }

        @Override
        protected void onPostExecute(Void t)
        {
            super.onPostExecute(t);

            starredWordCount.setText(String.valueOf(starredWords));
            unstarredWordCount.setText(String.valueOf(nostarWords));
            halfStarredWordCount.setText(String.valueOf(halfStarredWords));

            userWordCount.setText(String.valueOf(userWords));
            originalWordCount.setText(String.valueOf(originalWords));
            totalWordCount.setText(String.valueOf(totalWords));

            notifiedWordCount.setText(String.valueOf(notifiedWords));
            hiddenWordCount.setText(String.valueOf(hiddenWords));

            float starredPercent=(float)starredWords/totalWords;
            float unstarredPercent=(float)nostarWords/totalWords;
            float halfStarredPercent=(float)halfStarredWords/totalWords;

            float notifiedPercent=(float)notifiedWords/totalWords;
            float hiddenPercent=(float)hiddenWords/totalWords;

            NumberFormat nf=NumberFormat.getPercentInstance();
            nf.setMaximumFractionDigits(2);

            starredWordPercent.setText(nf.format(starredPercent));
            unstarredWordPercent.setText(nf.format(unstarredPercent));
            halfstarredWordPercent.setText(nf.format(halfStarredPercent));

            notifiedWordPercent.setText(nf.format(notifiedPercent));
            hiddenWordPercent.setText(nf.format(hiddenPercent));

        }
    }

}
