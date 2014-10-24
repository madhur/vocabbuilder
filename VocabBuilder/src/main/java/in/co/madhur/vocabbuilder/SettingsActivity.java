package in.co.madhur.vocabbuilder;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import in.co.madhur.vocabbuilder.fragments.SettingsFragment;

/**
 * Created by madhur on 19-Jun-14.
 */
public class SettingsActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

    }
}
