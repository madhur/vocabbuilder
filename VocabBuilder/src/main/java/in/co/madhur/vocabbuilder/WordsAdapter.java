package in.co.madhur.vocabbuilder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.co.madhur.vocabbuilder.model.Word;

/**
 * Created by madhur on 19-Jun-14.
 */
public class WordsAdapter extends BaseAdapter
{
    private List<Word> words;
    private Context context;

    public WordsAdapter(List<Word> words, Context context)
    {
        this.words=words;
        this.context=context;
    }

    @Override
    public long getItemId(int position)
    {
         return position;
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


            view.setTag(holder);

        }
        else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        Word word = (Word) getItem(position);

        holder.word.setText(word.getName());


        return view;
    }


    private static class ViewHolder
    {
        TextView word;



    }
}
