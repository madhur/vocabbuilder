package in.co.madhur.vocabbuilder.fragments;

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
import in.co.madhur.vocabbuilder.AppPreferences;
import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.controls.LayoutedTextView;
import in.co.madhur.vocabbuilder.db.VocabDB;
import in.co.madhur.vocabbuilder.model.Word;

import static in.co.madhur.vocabbuilder.Consts.SPINNER_ITEMS;

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

    public WordsAdapter(List<Word> words, Context context)
    {
        this.words = words;
        this.context = context;
        originalWords = new ArrayList<Word>();

//        setActiveSortOrder(new AppPreferences(context).GetSortOrder());
//
//        Sort();

        for (Word word : words)
        {
            originalWords.add(word);
        }

    }
//
//    public WordsAdapter(List<Word> words, Context context, SPINNER_ITEMS item)
//    {
//        this.words = words;
//        this.context = context;
//        originalWords = new ArrayList<Word>();
//
//        if(item==SPINNER_ITEMS.RECENT)
//        {
//            setActiveSortOrder(Consts.WORDS_SORT_ORDER.DATE);
//        }
//        else
//        {
//            setActiveSortOrder(new AppPreferences(context).GetSortOrder());
//        }
//
//        Sort();
//
//        for (Word word : words)
//        {
//            originalWords.add(word);
//        }
//
//    }

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



        View view;
        final ViewHolder holder;
        if (convertView == null)
        {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.word_item, parent, false);

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

        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.backview);
        if (position == getDisplayedPosition())
        {


            if (rl != null)
            {
//                Log.d(App.TAG, "rl original height " + String.valueOf(rl.getHeight()));

                LayoutedTextView tv = (LayoutedTextView) rl.findViewById(R.id.meaning);

                if (tv != null)
                {
                    if (tv.getTag() != null)
                    {
                        int lineCount = (Integer) tv.getTag();
//                        Log.d(App.TAG, "Lines " + String.valueOf(lineCount));


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
//                Log.d(App.TAG, "rl original height " + String.valueOf(rl.getHeight()));
                int desiredHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());


                rl.getLayoutParams().height = desiredHeight;


            }

        }




        holder.word.setText(word.getName());
        holder.meaning.setText(word.getMeaning());
        holder.ratingBar.setTag(word.getId());

        holder.ratingBar.setRating((float) word.getRating() / 2);

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
                return false;
            }
        });


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

//    public void ToggleSort()
//    {
//
//        if (getActiveSortOrder() == Consts.WORDS_SORT_ORDER.ALPHABETICAL)
//        {
//            Collections.sort(words, Collections.reverseOrder(new Word.RatingSorter()));
//            setActiveSortOrder(Consts.WORDS_SORT_ORDER.DIFFICULTY);
//        }
//        else
//        {
//
//            Collections.sort(words, new Word.NameSorter());
//            setActiveSortOrder(Consts.WORDS_SORT_ORDER.ALPHABETICAL);
//        }
//
//        notifyDataSetChanged();
//
//    }

//    public void Sort()
//    {
//
//        Sort(getActiveSortOrder());
//
//    }

    public void HideWord(int Id)
    {
        words.remove(Word.findById(words,Id));
        notifyDataSetChanged();
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


    }
}
