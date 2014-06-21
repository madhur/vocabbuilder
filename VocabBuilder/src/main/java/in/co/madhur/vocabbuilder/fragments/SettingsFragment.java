package in.co.madhur.vocabbuilder.fragments;

import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;


import in.co.madhur.vocabbuilder.R;

/**
 * Created by madhur on 21-Jun-14.
 */
public class SettingsFragment extends PreferenceFragment
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
