package in.co.madhur.vocabbuilder;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

        View v = getLayoutInflater().inflate(R.layout.toolbar, null);

        setContentView(v);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().add(R.id.toolbar_layout, new SettingsFragment()).commit();

    }
}
