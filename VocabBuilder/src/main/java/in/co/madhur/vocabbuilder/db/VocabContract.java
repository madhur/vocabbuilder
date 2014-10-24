package in.co.madhur.vocabbuilder.db;

import android.provider.BaseColumns;

/**
 * Created by madhur on 19-Jun-14.
 */
public class VocabContract
{

    public static final String RECENTS_VIEW="recents";
    public static final String WORDS_VIEW="_VIEW";

    public static abstract class Words implements BaseColumns
    {
        public static final String TABLE_NAME = "words";
        public static final String ID="Id";
        public static final String WORD="word";
        public static final String MEANING="meaning";
        public static final String IS_HIDDEN="hidden";
        public static final String DIFFICULTY="difficulty";
        public static final String IS_USER="isUser";
        public static final String SYN_GROUP="syn_group";
        public static final String SIM_GROUP="sim_group";


    }

    public static abstract class RecentWords implements  BaseColumns
    {
        public static final String TABLE_NAME = "recent_words";
        public static final String ID="Id";
        public static final String DATE="date";

    }


}
