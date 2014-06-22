package in.co.madhur.vocabbuilder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.model.Word;

import static in.co.madhur.vocabbuilder.Consts.SELECT_NOTIFICATION_WORDS;

/**
 * Created by madhur on 19-Jun-14.
 */
public class VocabDB
{
    private static VocabDB vocabDb;
    private static DbHelper db;

    public static synchronized VocabDB getInstance(Context context)
    {
        if (context == null)
            throw new NullPointerException("Context cannot be null");

        if (vocabDb == null || db == null)
        {
            db = new DbHelper(context);
            vocabDb = new VocabDB();
            try
            {
                db.createDataBase();
            }
            catch (IOException e)
            {
                Log.e(App.TAG, "Error creating datrabase");
            }

            return vocabDb;
        }
        else
            return vocabDb;

    }

    public int SetRating(int Id, int rating) throws Exception
    {

        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VocabContract.Words.DIFFICULTY, rating);

        try

        {
            return database.update(VocabContract.Words.TABLE_NAME, values, VocabContract.Words.ID + "=" + Id, null);

        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;

        }
        finally
        {

            database.close();
        }


    }

    public int PutRecentWord(int Id) throws Exception
    {

        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VocabContract.RecentWords.ID, Id);
        values.put(VocabContract.RecentWords.DATE, System.currentTimeMillis());

        try

        {

            int rowsAffected = database.update(VocabContract.RecentWords.TABLE_NAME, values, VocabContract.Words.ID + "=" + Id, null);

            if (rowsAffected != 1)
            {
                database.insert(VocabContract.RecentWords.TABLE_NAME, null, values);
            }


        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;

        }
        finally
        {

            database.close();
        }

        return 0;
    }

    public int HideWord(int Id) throws Exception
    {

        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VocabContract.Words.ID, Id);
        values.put(VocabContract.Words.IS_HIDDEN, 1);


        try

        {

            int rowsAffected = database.update(VocabContract.Words.TABLE_NAME, values, VocabContract.Words.ID + "=" + Id, null);


        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;

        }
        finally
        {

            database.close();
        }

        return 0;
    }

    public List<Word> GetRecentWords() throws Exception
    {
        SQLiteDatabase database = db.getReadableDatabase();
        List<Word> wordList = new ArrayList<Word>();

        //    String viewName = startLetter + "_VIEW";

        try
        {
            Cursor c = database.query(VocabContract.RECENTS_VIEW, // The table to
                    // query
                    null, // The columns to return
                    null, // The columns for the WHERE clause
                    null, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );

            if (c.moveToFirst())
            {
                do
                {
                    Word singleWord = new Word();

                    singleWord.setName(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.WORD)));
                    singleWord.setMeaning(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.MEANING)));
                    singleWord.setId(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.ID)));
                    singleWord.setRating(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.DIFFICULTY)));

                    //Date a special column in this category
                    singleWord.setDate(c.getInt(c.getColumnIndexOrThrow(VocabContract.RecentWords.DATE)));

                    if (c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.IS_HIDDEN)) == 1)
                        singleWord.setHidden(true);
                    else
                        singleWord.setHidden(false);


                    wordList.add(singleWord);
                }
                while (c.moveToNext());
            }

            c.close();
        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;
        }

        return wordList;
    }


    public List<Word> GetAllWords() throws Exception
    {
        SQLiteDatabase database = db.getReadableDatabase();
        List<Word> wordList = new ArrayList<Word>();


        try

        {
            Cursor c = database.query(VocabContract.Words.TABLE_NAME, // The table to
                    // query
                    null, // The columns to return
                    null, // The columns for the WHERE clause
                    null, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );

            if (c.moveToFirst())
            {
                do
                {
                    Word singleWord = new Word();

                    singleWord.setName(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.WORD)));
                    singleWord.setMeaning(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.MEANING)));
                    singleWord.setId(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.ID)));
                    singleWord.setRating(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.DIFFICULTY)));

                    if (c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.IS_HIDDEN)) == 1)
                        singleWord.setHidden(true);
                    else
                        singleWord.setHidden(false);


                    wordList.add(singleWord);
                }
                while (c.moveToNext());
            }

            c.close();
        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;
        }

        return wordList;
    }


    public List<Word> GetWords(String startLetter) throws Exception
    {
        SQLiteDatabase database = db.getReadableDatabase();
        List<Word> wordList = new ArrayList<Word>();

        String viewName = startLetter + "_VIEW";

        try

        {
            Cursor c = database.query(viewName, // The table to
                    // query
                    null, // The columns to return
                    null, // The columns for the WHERE clause
                    null, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );

            if (c.moveToFirst())
            {
                do
                {
                    Word singleWord = new Word();

                    singleWord.setName(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.WORD)));
                    singleWord.setMeaning(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.MEANING)));
                    singleWord.setId(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.ID)));
                    singleWord.setRating(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.DIFFICULTY)));

                    if (c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.IS_HIDDEN)) == 1)
                        singleWord.setHidden(true);
                    else
                        singleWord.setHidden(false);


                    wordList.add(singleWord);
                }
                while (c.moveToNext());
            }

            c.close();
        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;
        }

        return wordList;
    }

    public List<Word> GetFilteredWords(SELECT_NOTIFICATION_WORDS setting) throws Exception
    {
        SQLiteDatabase database = db.getReadableDatabase();
        List<Word> wordList = new ArrayList<Word>();

        String whereCondition = null;

        if (setting == SELECT_NOTIFICATION_WORDS.ALL_WORDS)
            whereCondition = null;
        else if (setting == SELECT_NOTIFICATION_WORDS.UNSTARRED)
            whereCondition = VocabContract.Words.DIFFICULTY + "=0";
        else if (setting == SELECT_NOTIFICATION_WORDS.FULL_STARRED)
            whereCondition = VocabContract.Words.DIFFICULTY + "=2";
        else if (setting == SELECT_NOTIFICATION_WORDS.HALF_STARRED)
            whereCondition = VocabContract.Words.DIFFICULTY + "=1";
        else if (setting == SELECT_NOTIFICATION_WORDS.BOTH)
            whereCondition = VocabContract.Words.DIFFICULTY + "!=0";

        try
        {

            Cursor c = database.query(VocabContract.Words.TABLE_NAME, // The table to
                    // query
                    null, // The columns to return
                    whereCondition,
                    null, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );

            if (c.moveToFirst())
            {
                do
                {
                    Word singleWord = new Word();

                    singleWord.setName(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.WORD)));
                    singleWord.setMeaning(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.MEANING)));
                    singleWord.setId(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.ID)));
                    singleWord.setRating(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.DIFFICULTY)));

                    if (c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.IS_HIDDEN)) == 1)
                        singleWord.setHidden(true);
                    else
                        singleWord.setHidden(false);


                    wordList.add(singleWord);
                }
                while (c.moveToNext());
            }

            c.close();
        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;
        }


        return wordList;
    }


    public List<Word> GetHiddenWords() throws Exception
    {
        SQLiteDatabase database = db.getReadableDatabase();
        List<Word> wordList = new ArrayList<Word>();

        try
        {

            Cursor c = database.query(VocabContract.Words.TABLE_NAME, // The table to
                    // query
                    null, // The columns to return
                    VocabContract.Words.IS_HIDDEN + "=1",
                    null, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );

            if (c.moveToFirst())
            {
                do
                {
                    Word singleWord = new Word();

                    singleWord.setName(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.WORD)));
                    singleWord.setMeaning(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.MEANING)));
                    singleWord.setId(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.ID)));
                    singleWord.setRating(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.DIFFICULTY)));

                    if (c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.IS_HIDDEN)) == 1)
                        singleWord.setHidden(true);
                    else
                        singleWord.setHidden(false);


                    wordList.add(singleWord);
                }
                while (c.moveToNext());
            }

            c.close();
        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;
        }


        return wordList;
    }

    public Word GetSingleWord(int Id) throws Exception
    {
        SQLiteDatabase database = db.getReadableDatabase();


        try
        {

            Cursor c = database.query(VocabContract.Words.TABLE_NAME, // The table to
                    // query
                    null, // The columns to return
                    VocabContract.Words.ID + "=" + String.valueOf(Id),
                    null, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );

            if (c.moveToFirst())
            {
                Word singleWord = new Word();

                singleWord.setName(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.WORD)));
                singleWord.setMeaning(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.MEANING)));
                singleWord.setId(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.ID)));
                singleWord.setRating(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.DIFFICULTY)));

                if (c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.IS_HIDDEN)) == 1)
                    singleWord.setHidden(true);
                else
                    singleWord.setHidden(false);

                singleWord.setSimilar(GetWordsFromString(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.SIMILAR))));
                singleWord.setSynonyms(GetWordsFromString(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.SYNONYMS))));


                return singleWord;

            }

            c.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }


        return null;
    }

    public int UpdateWord(int Id, String meaning, List<Word> synonyms, List<Word> similar) throws Exception
    {

        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VocabContract.Words.ID, Id);
        values.put(VocabContract.Words.MEANING, meaning);
        values.put(VocabContract.Words.SYNONYMS , FormatWords(synonyms));
        values.put(VocabContract.Words.SIMILAR , FormatWords(similar));


        try

        {

            int rowsAffected = database.update(VocabContract.Words.TABLE_NAME, values, VocabContract.Words.ID + "=" + Id, null);

            Log.d(App.TAG, "Rows affected with update " +String.valueOf(rowsAffected));
        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;

        }
        finally
        {

            database.close();
        }

        return 0;

    }


    public int AddWord(String word, String meaning, List<Word> synonyms, List<Word> similar) throws Exception
    {

        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VocabContract.Words.WORD, word);
        values.put(VocabContract.Words.MEANING, meaning);
        values.put(VocabContract.Words.SYNONYMS , FormatWords(synonyms));
        values.put(VocabContract.Words.SIMILAR , FormatWords(similar));
        values.put(VocabContract.Words.IS_HIDDEN, 0);


        try

        {

            long rowsAffected = database.insert(VocabContract.Words.TABLE_NAME, null,  values);
            Log.d(App.TAG, "Rows affected with insert " +String.valueOf(rowsAffected));
        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;

        }
        finally
        {

            database.close();
        }

        return 0;

    }

    private String FormatWords(List<Word> words)
    {
        StringBuilder sb=new StringBuilder();

        if(words.size()==0)
            return "";

        for(Word word:words)
        {
            sb.append(word.getId());
            sb.append(";");
        }

        return sb.toString();

    }

    private List<Word> GetWordsFromString(String format)
    {

        ArrayList<Word> words=new ArrayList<Word>();
        if(TextUtils.isEmpty(format))
            return words;

        String[] ids=format.split(";");

        for(String id: ids)
        {
            Word word=null;
            try
            {
                word = GetSingleWord(Integer.parseInt(id));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                continue;
            }

            words.add(word);

        }

        return words;

    }


    private Word GetWordFromCursor(Cursor c)
    {
        Word singleWord = new Word();

        singleWord.setName(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.WORD)));
        singleWord.setMeaning(c.getString(c.getColumnIndexOrThrow(VocabContract.Words.MEANING)));
        singleWord.setId(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.ID)));
        singleWord.setRating(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.DIFFICULTY)));

        if (c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.IS_HIDDEN)) == 1)
            singleWord.setHidden(true);
        else
            singleWord.setHidden(false);

        return singleWord;

    }


}
