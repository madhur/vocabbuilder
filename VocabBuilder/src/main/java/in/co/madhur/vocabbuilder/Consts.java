package in.co.madhur.vocabbuilder;

/**
 * Created by madhur on 19-Jun-14.
 */
public class Consts {

    public static final String PACKAGE="in.co.madhur.vocabbuilder";

    public static final String TWITTER_URL="https://twitter.com/intent/user?screen_name=madhur25";

    public static final String ABOUT_URL="file:///android_asset/about.html";
    public static final String ABOUT_TAG="about";
    public static final String CHANGES_URL="file:///android_asset/changes.html";
    public static final String WHATS_NEW_TAG="whatsnew";
    public static final String MY_EMAIL="ahuja.madhur@gmail.com";

    public static final String ACTION_REMIND_WORD="in.co.madhur.vocabbuilder.ACTION.REMIND_WORD";
    public static final String ACTION_EDIT_WORD="in.co.madhur.vocabbuilder.ACTION.EDIT_WORD";
    public static final String ACTION_VIEW_WORD="in.co.madhur.vocabbuilder.ACTION.VIEW_WORD";
    public static final String ACTION_ADD_WORD="in.co.madhur.vocabbuilder.ACTION.ADD_WORD";
    public static final String ACTION_MARK_WORD="in.co.madhur.vocabbuilder.ACTION.MARK_WORD";
    //public static final String ACTION_SHOW_RECENT="in.co.madhur.vocabbuilder.ACTION.SHOW_RECENT";
    public static final String INTENT_EXTRA_NOTIFICATION_ID ="notification_ID" ;
    public static final String TARGET_RATING="target_rating";
    public static final String TARGET_WORD="target_word";

    public static final int UPDATE_RESULT_CODE=0;

    public enum STAR
    {
        NO_STAR, HALF_STAR, FULL_STAR
    }

    public enum WORDS_SORT_ORDER
    {
        ALPHABETICAL_ASC, ALPHABETICAL_DESC, STARRED_ASC, STARRED_DESC, DATE
    }

    public enum THEME
    {
        DARK, LIGHT
    }

    public enum SELECT_NOTIFICATION_WORDS
    {
        ALL_WORDS, UNSTARRED, HALF_STARRED, FULL_STARRED, BOTH
    }

    public enum SPINNER_ITEMS
    {
        ACTIVE,RECENT, STARRED, UNSTARRED, HIDDEN
    }



    public enum LISTS
    {

        A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z;



        public static String[] names() {
            LISTS[] states = values();
            String[] names = new String[states.length];

            for (int i = 0; i < states.length; i++) {
                names[i] = states[i].name();
            }

            return names;
        }
    };

}
