package in.co.madhur.vocabbuilder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by madhur on 19-Jun-14.
 */
public class AppPreferences
{

    private Context context;
    private SharedPreferences sharedPreferences;

    public AppPreferences(Context context)
    {

        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public enum Keys
    {
        ENABLE_PRO("enable_pro"),
        VIEW_HIDDEN_ITEMS("view_hidden_items"),
        WORDS_SORT_ORDER("words_sort_order"),
        WORDS_MODE("words_mode"),
        ENABLE_NOTIFICATIONS("enable_notifications"),
        ENABLE_LED("enable_led"),

        NOTIFICATION_INTERVAL("notification_interval"),
        SELECT_NOTIFICATION_WORDS("select_notification_words"),
        PICK_THEME("pick_theme"),

        FOLLOW_TWITTER("follow_twitter"),
        ACTION_ABOUT("action_about");



        public final String key;

        private Keys(String key)
        {
            this.key = key;

        }

    }

    public Consts.THEME GetTheme()
    {
        int theme=Integer.parseInt(sharedPreferences.getString(Keys.PICK_THEME.key, context.getString(R.string.default_theme)));
        return Consts.THEME.values()[theme];

    }

    public Consts.WORDS_SORT_ORDER GetSortOrder()
    {
        int sortOrder= Integer.parseInt(sharedPreferences.getString(Keys.WORDS_SORT_ORDER.key, context.getResources().getString(R.string.words_sort_order_default)));

        return Consts.WORDS_SORT_ORDER.values()[sortOrder];

    }





}
