package in.co.madhur.vocabbuilder.fragments;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceFragmentCompat;

import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.R;
import in.co.madhur.vocabbuilder.db.DbHelper;
import in.co.madhur.vocabbuilder.db.VocabDB;
import in.co.madhur.vocabbuilder.service.Alarms;

import static in.co.madhur.vocabbuilder.AppPreferences.Keys;

/**
 * Created by madhur on 21-Jun-14.
 */
public class SettingsFragment extends PreferenceFragmentCompat
{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }

    private final Preference.OnPreferenceChangeListener listPreferenceChangeListerner = new Preference.OnPreferenceChangeListener()
    {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue)
        {
            UpdateLabel((ListPreference) preference, newValue.toString());
            return true;
        }
    };

    protected void UpdateLabel(ListPreference listPreference, String newValue)
    {

        if (newValue == null)
        {
            newValue = listPreference.getValue();
        }

        int index = listPreference.findIndexOfValue(newValue);
        if (index != -1)
        {
            newValue = (String) listPreference.getEntries()[index];

            listPreference.setTitle(newValue);
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();

        SetListeners();

//        UpdateLabel((ListPreference) findPreference(Keys.WORDS_SORT_ORDER.key), null);
        UpdateLabel((ListPreference) findPreference(Keys.WORDS_MODE.key), null);
        UpdateLabel((ListPreference) findPreference(Keys.PICK_THEME.key), null);
        UpdateLabel((ListPreference) findPreference(Keys.NOTIFICATION_INTERVAL.key), null);
        UpdateLabel((ListPreference) findPreference(Keys.SELECT_NOTIFICATION_WORDS.key), null);


    }

    protected void SetListeners()
    {
//        findPreference(Keys.WORDS_SORT_ORDER.key).setOnPreferenceChangeListener(listPreferenceChangeListerner);
        findPreference(Keys.WORDS_MODE.key).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                UpdateLabel((ListPreference)preference, (String) newValue);

                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent().setAction(Consts.ACTION_LIST_SETTINGS_CHANGED));

                return true;
            }
        });
        findPreference(Keys.PICK_THEME.key).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                UpdateLabel((ListPreference)preference, (String) newValue);

                new AlertDialog.Builder(getActivity()).setTitle(R.string.app_name).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        Intent i = getActivity().getPackageManager()
                                .getLaunchIntentForPackage( getActivity().getPackageName() );
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(i);

                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                }).setMessage(R.string.restart_app).show();


                return true;
            }
        });
        findPreference(Keys.NOTIFICATION_INTERVAL.key).setOnPreferenceChangeListener(listPreferenceChangeListerner);
        findPreference(Keys.SELECT_NOTIFICATION_WORDS.key).setOnPreferenceChangeListener(listPreferenceChangeListerner);

        findPreference(Keys.ACTION_ABOUT.key).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {

            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                AboutDialog dialog = new AboutDialog();
                dialog.show(getFragmentManager(), Consts.ABOUT_TAG);

                return true;
            }
        });

        findPreference(Keys.FOLLOW_TWITTER.key).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {

            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Consts.TWITTER_URL)));
                return true;
            }
        });


        findPreference(Keys.ENABLE_NOTIFICATIONS.key).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                Boolean newVal = (Boolean) newValue;
                Alarms alarms = new Alarms(getActivity());

                if (newVal)
                    alarms.Schedule();
                else
                    alarms.cancel();

                return true;
            }
        });


        findPreference(Keys.REVERT_DATABASE.key).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                new AlertDialog.Builder(getActivity()).setTitle(R.string.app_name).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        DbHelper dbHelper=new DbHelper(getActivity());

                        try
                        {
                            dbHelper.createDataBase(true);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                }).setMessage(R.string.revert_database_msg).show();


                return true;
            }
        });

        findPreference(Keys.STAR_ALL.key).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                new AlertDialog.Builder(getActivity()).setTitle(R.string.app_name).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        VocabDB db=VocabDB.getInstance(getActivity());
                        try
                        {
                            db.SetRating(Consts.STAR.FULL_STAR.ordinal());
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                }).setMessage(R.string.star_all_msg).show();

                return true;
            }
        });

        findPreference(Keys.UNSTAR_ALL.key).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                new AlertDialog.Builder(getActivity()).setTitle(R.string.app_name).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        VocabDB db=VocabDB.getInstance(getActivity());
                        try
                        {
                            db.SetRating(Consts.STAR.NO_STAR.ordinal());
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                }).setMessage(R.string.unstar_all_msg).show();

                return true;
            }
        });


    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // addPreferencesFromResource is already called in onCreatePreferences
    }


    /** Sets up the action bar for an {@link PreferenceScreen} */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void initializeActionBar(PreferenceScreen preferenceScreen)
    {
        // AndroidX PreferenceScreen doesn't have getDialog() method
        // This method is no longer needed in modern Android
    }


        @Override
        public boolean onPreferenceTreeClick(Preference preference)
        {
            super.onPreferenceTreeClick(preference);

            // This code cannot run pre honeycomb
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                // If the user has clicked on a preference screen, set up the action
                // bar
                if (preference instanceof PreferenceScreen)
                {
                    initializeActionBar((PreferenceScreen) preference);
                }
            }

            return false;
        }

    }
