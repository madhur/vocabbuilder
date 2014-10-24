package in.co.madhur.vocabbuilder.ui;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.controls.LayoutedTextView;
import in.co.madhur.vocabbuilder.db.VocabDB;
import in.co.madhur.vocabbuilder.model.Word;

/**
 * Created by madhur on 19-Jun-14.
 */
public class WordsAdapter extends BaseAdapter implements Filterable
{
    private List<Word> words, originalWords;
    private Context context;
    private ItemsFilter itemsFilter;
    private Consts.WORDS_SORT_ORDER activeSortOrder= Consts.WORDS_SORT_ORDER.ALPHABETICAL_ASC;
    private int displayedPosition=-1;
    private Consts.WORDS_MODE wordMode;


    public WordsAdapter(List<Word> words, Context context, Consts.WORDS_MODE wordMode)
    {
        this.words = words;
        this.context = context;
        originalWords = new ArrayList<Word>();


        for (Word word : words)
        {
            originalWords.add(word);
        }

        this.setWordMode(wordMode);

    }


    @Override
    public long getItemId(int position)
    {
        return words.get(position).getId();
    }


    @Override
    public Object getItem(int position)
    {
        return words.get(position);
    }

    @Override
    public int getCount()
    {
        return words.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Word word = (Word) getItem(position);



        View view=null;

        final ViewHolder holder;
        if (convertView == null)
        {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(getWordMode() == Consts.WORDS_MODE.FLASHCARDS)
                view = li.inflate(R.layout.word_item, parent, false);
            else if(getWordMode() ==Consts.WORDS_MODE.DICTIONARY)
                view = li.inflate(R.layout.word_item_dict, parent, false);

            holder = new ViewHolder();
            holder.word = (TextView) view.findViewById(R.id.word);
            holder.meaning = (TextView) view.findViewById(R.id.meaning);
            holder.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

            view.setTag(holder);

        }
        else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }





        holder.word.setText(word.getName());
        holder.meaning.setText(word.getMeaning());
        holder.ratingBar.setTag(word.getId());

        holder.ratingBar.setRating((float) word.getRating() / 2);

        holder.ratingBar.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {

            }
        });

        holder.ratingBar.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    RatingBar ratingBar = (RatingBar) v;

                    if (ratingBar.getRating() == 1.0)
                        ratingBar.setRating((float) 0.0);
                    else if (ratingBar.getRating() == 0.0)
                        ratingBar.setRating((float) 0.5);
                    else if (ratingBar.getRating() == 0.5)
                        ratingBar.setRating((float) 1.0);


                    int itemId = (Integer) v.getTag();
                    int dbRating = (int) (ratingBar.getRating() * 2);


                    try
                    {
                        VocabDB.getInstance(context).SetRating(itemId, dbRating);
                    }
                    catch (Exception e)
                    {
                        Log.e(App.TAG, e.getMessage());
                    }

                    Word.findById(words, itemId).setRating(dbRating);

                    return true;

                }
                return true;
            }
        });


        LayoutedTextView ltTextview = (LayoutedTextView) holder.meaning;
        ltTextview.setOnLayoutListener(new LayoutedTextView.OnLayoutListener()
        {
            @Override
            public void onLayouted(TextView view)
            {
                int lineCount = view.getLineCount();

                view.setTag(lineCount);
            }
        });

        if(getWordMode() == Consts.WORDS_MODE.FLASHCARDS)
        {
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.backview);
            if (position == getDisplayedPosition())
            {


                if (rl != null)
                {


                    LayoutedTextView tv = (LayoutedTextView) rl.findViewById(R.id.meaning);

                    if (tv != null)
                    {
                        if (tv.getTag() != null)
                        {
                            int lineCount = (Integer) tv.getTag();


                            if (lineCount > 1)
                            {
                                int desiredHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60 + 15 * lineCount, context.getResources().getDisplayMetrics());
                                rl.getLayoutParams().height = desiredHeight;
                            }
                        }
                    }


                }


            }
            else
            {

                if (rl != null)
                {

                    int desiredHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());


                    rl.getLayoutParams().height = desiredHeight;


                }

            }
        }



        return view;
    }

    @Override
    public Filter getFilter()
    {
        if (itemsFilter == null)
        {
            itemsFilter = new ItemsFilter();
        }

        return itemsFilter;

    }



    public void HideWord(int Id)
    {

        words.remove(Word.findById(words,Id));
        originalWords.remove(Word.findById(originalWords,Id));
        notifyDataSetChanged();
    }

    public int getItemPositionById(final long id)
    {
        for (int i = 0; i < getCount(); i++)
        {
            if (getItemId(i) == id)
                return i;
        }
        return Consts.VALUE_NOT_SET;
    }


    public void Sort(Consts.WORDS_SORT_ORDER sortOrder)
    {

        if (sortOrder == Consts.WORDS_SORT_ORDER.ALPHABETICAL_ASC)
        {
            Collections.sort(words, new Word.NameSorter());
            Collections.sort(originalWords, new Word.NameSorter());
        }
        else if(sortOrder== Consts.WORDS_SORT_ORDER.ALPHABETICAL_DESC)
        {
            Collections.sort(words, Collections.reverseOrder(new Word.NameSorter()));
            Collections.sort(originalWords, Collections.reverseOrder(new Word.NameSorter()));
        }
        else if(sortOrder==Consts.WORDS_SORT_ORDER.STARRED_ASC)
        {
            Collections.sort(words, new Word.RatingSorter());
            Collections.sort(originalWords, new Word.RatingSorter());
        }
        else if(sortOrder==Consts.WORDS_SORT_ORDER.STARRED_DESC)
        {
            Collections.sort(words, Collections.reverseOrder(new Word.RatingSorter()));
            Collections.sort(originalWords, Collections.reverseOrder(new Word.RatingSorter()));
        }
        else if(sortOrder== Consts.WORDS_SORT_ORDER.DATE)
        {
            Collections.sort(words, Collections.reverseOrder(new Word.DateSorter()));
            Collections.sort(originalWords, Collections.reverseOrder(new Word.DateSorter()));
        }

        setActiveSortOrder(sortOrder);

        notifyDataSetChanged();

    }

    public Consts.WORDS_SORT_ORDER getActiveSortOrder()
    {
        return activeSortOrder;
    }

    public void setActiveSortOrder(Consts.WORDS_SORT_ORDER activeSortOrder)
    {
        this.activeSortOrder = activeSortOrder;
    }

    public int getDisplayedPosition()
    {
        return displayedPosition;
    }

    public void setDisplayedPosition(int displayedPosition)
    {
        this.displayedPosition = displayedPosition;
    }

    public Consts.WORDS_MODE getWordMode()
    {
        return wordMode;
    }

    public void setWordMode(Consts.WORDS_MODE wordMode)
    {
        this.wordMode = wordMode;
    }


    private static class ViewHolder
    {
        TextView word;
        TextView meaning;
        RatingBar ratingBar;


    }


    private class ItemsFilter extends Filter
    {
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            List<Word> filteredWords = (List<Word>) results.values;

            words = filteredWords;

            notifyDataSetChanged();

        }

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
                    if (word.getName().toLowerCase().startsWith(((String) constraint).toLowerCase()))
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


    }
}
