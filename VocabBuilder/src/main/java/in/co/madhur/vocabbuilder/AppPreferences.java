package in.co.madhur.vocabbuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

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

        WORDS_SORT_ORDER("words_sort_order_"),
        WORDS_MODE("words_mode"),
        ENABLE_NOTIFICATIONS("enable_notifications"),
        ENABLE_LED("enable_led"),
        ENABLE_SOUND("enable_sound"),
        ENABLE_VIBRATE("enable_vibrate"),
        NOTIFICATION_INTERVAL("notification_interval"),
        SELECT_NOTIFICATION_WORDS("select_notification_words"),
        PICK_THEME("pick_theme"),
        LAST_NOTIFICATION_TIME("last_notification_time"),

        NOTIFICATION_SCREEN_ON("notification_screen_on"),
        FOLLOW_TWITTER("follow_twitter"),
        LIST_POSITION("list_position_"),
        CURRENT_LETTER("current_letter"),
        ACTION_ABOUT("action_about"),
        REVERT_DATABASE("revert_database"),
        STAR_ALL("star_all"),
        UNSTAR_ALL("unstar_all");


        public final String key;

        private Keys(String key)
        {
            this.key = key;

        }

    }


    public boolean IsProMode()
    {

        return sharedPreferences.getBoolean(Keys.ENABLE_PRO.key, context.getResources().getBoolean(R.bool.enable_pro_default));

    }

    public void SaveListPosition(int letter, int newLetter, int position, Consts.WORDS_SORT_ORDER order)
    {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(Keys.LIST_POSITION.key+String.valueOf(letter), position);
        edit.putInt(Keys.WORDS_SORT_ORDER.key+String.valueOf(letter), order.ordinal());
        edit.putInt(Keys.CURRENT_LETTER.key, newLetter);
        edit.commit();
    }

    public void SaveListPosition(int letter,  int position, Consts.WORDS_SORT_ORDER order)
    {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(Keys.LIST_POSITION.key+String.valueOf(letter), position);
        edit.putInt(Keys.WORDS_SORT_ORDER.key+String.valueOf(letter), order.ordinal());

        edit.commit();
    }



    public int GetCurrentLetter()
    {
        return sharedPreferences.getInt(Keys.CURRENT_LETTER.key, 0);
    }

    public int GetListPosition(int letter)
    {
        return sharedPreferences.getInt(Keys.LIST_POSITION.key+String.valueOf(letter), -1);
    }


    public boolean IsWakelockEnabled()
    {

        return sharedPreferences.getBoolean(Keys.NOTIFICATION_SCREEN_ON.key, context.getResources().getBoolean(R.bool.notification_screen_on_default));
    }


    public Consts.THEME GetTheme()
    {
        int theme = Integer.parseInt(sharedPreferences.getString(Keys.PICK_THEME.key, context.getString(R.string.default_theme)));
        return Consts.THEME.values()[theme];

    }

    public Consts.WORDS_SORT_ORDER GetSortOrder(int letter)
    {

        int sortOrder = sharedPreferences.getInt(Keys.WORDS_SORT_ORDER.key+String.valueOf(letter), Integer.parseInt(context.getResources().getString(R.string.words_sort_order_default)));

        return Consts.WORDS_SORT_ORDER.values()[sortOrder];

    }



    public boolean IsNotificationEnabled()
    {
        return sharedPreferences.getBoolean(Keys.ENABLE_NOTIFICATIONS.key, context.getResources().getBoolean(R.bool.enable_notifications));
    }

    public Consts.SELECT_NOTIFICATION_WORDS GetNotificationWordSetting()
    {
        int setting = Integer.parseInt(sharedPreferences.getString(Keys.SELECT_NOTIFICATION_WORDS.key, context.getResources().getString(R.string.select_notification_words_default)));

        return Consts.SELECT_NOTIFICATION_WORDS.values()[setting];
    }

    public int GetNotificationSchedule()
    {

        int setting = Integer.parseInt(sharedPreferences.getString(Keys.NOTIFICATION_INTERVAL.key, context.getResources().getString(R.string.notification_interval_default)));
        return setting;
    }

    public void SaveSuccessfulNotification()
    {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong(Keys.LAST_NOTIFICATION_TIME.key, System.currentTimeMillis());
        edit.commit();

    }

    public Consts.WORDS_MODE GetLearningMode()
    {

        int setting = Integer.parseInt(sharedPreferences.getString(Keys.WORDS_MODE.key, context.getResources().getString(R.string.words_mode_default)));

        return Consts.WORDS_MODE.values()[setting];
    }

    public boolean getBoolMetadata(Keys key)
    {
        return sharedPreferences.getBoolean(key.key, true);
    }

    public long GetLastSuccessfulNotification()
    {
        try
        {
            return sharedPreferences.getLong(Keys.LAST_NOTIFICATION_TIME.key, 0);
        }
        catch (ClassCastException e)
        {
            return 0;
        }

    }


}
