/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.controls;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.model.Word;

/**
 * Created by madhur on 22-Jun-14.
 */
public class WordCompletionTextView extends TokenCompleteTextView
{
    public WordCompletionTextView(Context context)
    {
        super(context);
    }

    public WordCompletionTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Object object)
    {
        Word p = (Word) object;

        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) l.inflate(R.layout.word_token, (ViewGroup) WordCompletionTextView.this.getParent(), false);
        ((TextView) view.findViewById(R.id.word_name)).setText(p.getName());

        return view;
    }

    @Override
    protected Object defaultObject(String completionText)
    {

            return new Word("madhur","ahuja");

    }
}
