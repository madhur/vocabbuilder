package in.co.madhur.vocabbuilder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.co.madhur.vocabbuilder.db.DbHelper;
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

    public WordsAdapter(List<Word> words, Context context)
    {
        this.words=words;
        this.context=context;
        originalWords=new ArrayList<Word>();

        for(Word word: words)
        {
            originalWords.add(word);
        }
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
        View view = null;
        ViewHolder holder;
        if (convertView == null)
        {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.word_item, parent, false);

            holder = new ViewHolder();
            holder.word = (TextView) view.findViewById(R.id.word);
            holder.meaning=(TextView)view.findViewById(R.id.meaning);
            holder.ratingBar=(RatingBar)view.findViewById(R.id.ratingBar);

            view.setTag(holder);

        }
        else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        Word word = (Word) getItem(position);

        holder.word.setText(word.getName());
        holder.meaning.setText(word.getMeaning());
        holder.ratingBar.setTag(word.getId());

        holder.ratingBar.setRating((float)word.getRating()/2);

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


                    int itemId=(Integer)v.getTag();
                    int dbRating=(int)(ratingBar.getRating()*2);

//                    words.get(itemId).setRating(dbRating);

                    VocabDB.getInstance(context).SetRating(itemId, dbRating );
                    //((Word)getItem(itemId)).setRating(dbRating);
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
        if(itemsFilter==null)
        {
            itemsFilter=new ItemsFilter();
        }

        return itemsFilter;

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
            List<Word> filteredWords= (List<Word>) results.values;

            words=filteredWords;

            notifyDataSetChanged();

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();


            List<Word> filteredWords;

            // perform your search here using the searchConstraint String.

            if(constraint!=null && constraint.length()>0)
            {
                filteredWords=new ArrayList<Word>();

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
                filteredWords=originalWords;
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
