/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by madhur on 21-Jun-14.
 */
public abstract class BaseActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SetTheme();
        super.onCreate(savedInstanceState);
    }

    private void SetTheme()
    {
        AppPreferences appPreferences=new AppPreferences(this);
        Consts.THEME currentTheme=appPreferences.GetTheme();

        if(currentTheme== Consts.THEME.DARK)
        {
            this.setTheme(R.style.Black);
        }
        else if(currentTheme== Consts.THEME.LIGHT)
        {
            this.setTheme(R.style.Light);

        }

    }

}
