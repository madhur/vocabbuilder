/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.model.Word;

/**
 * Created by madhur on 22-Jun-14.
 */
public class WordTokenAdapter extends BaseAdapter implements Filterable
{
    private List<Word> words, originalWords;
    private Context context;

    public WordTokenAdapter(List<Word> words, Context context)
    {
        this.setWords(words);
        this.context = context;

        originalWords = new ArrayList<Word>();

        for (Word word : words)
        {
            originalWords.add(word);
        }
    }

    @Override
    public int getCount()
    {
        return getWords().size();
    }

    @Override
    public Object getItem(int position)
    {
        return getWords().get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return getWords().get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Word word= (Word) getItem(position);
        View view;
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();

            view = LayoutInflater.from(context).inflate(R.layout.word_token_item, parent, false);
            holder.word = (TextView) view.findViewById(R.id.word);
            holder.ratingBar=(RatingBar)view.findViewById(R.id.ratingBar);
            view.setTag(holder);


        }
        else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();

        }

        holder.word.setText(word.getName());
        holder.word.setSelected(true);
        holder.ratingBar.setRating((float)word.getRating()/2);

        return view;
    }

    public List<Word> getWords()
    {
        return words;
    }

    public void setWords(List<Word> words)
    {
        this.words = words;
    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {

                FilterResults results = new FilterResults();


                List<Word> filteredWords;

                // perform your search here using the searchConstraint String.

                if (constraint != null && constraint.length() > 0)
                {
                    filteredWords = new ArrayList<Word>();

                    for (Word word : originalWords)
                    {
                        if (word.getName().startsWith((String) constraint))
                        {
                            filteredWords.add(word);

                        }
                    }
                }
                else
                {
                    filteredWords = originalWords;
                }


                synchronized (this)
                {
                    results.count = filteredWords.size();
                    results.values = filteredWords;
                }

                return results;


            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {

                List<Word> filteredWords = (List<Word>) results.values;

                words = filteredWords;

                notifyDataSetChanged();

            }
        };
    }


    private static class ViewHolder
    {

        TextView word;
        RatingBar ratingBar;
    }
}
