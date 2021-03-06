package in.co.madhur.vocabbuilder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.model.Word;

import static in.co.madhur.vocabbuilder.Consts.SELECT_NOTIFICATION_WORDS;
import static in.co.madhur.vocabbuilder.Consts.VALUE_NOT_SET;

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
            if(Id!=-1)
                return database.update(VocabContract.Words.TABLE_NAME, values, VocabContract.Words.ID + "=" + Id, null);
            else
                return database.update(VocabContract.Words.TABLE_NAME, values, null, null);

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

    public int SetRating(int rating) throws Exception
    {
        return SetRating(-1, rating);
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

    public int HideWord(int Id, boolean isHide) throws Exception
    {

        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VocabContract.Words.ID, Id);
        if(isHide)
            values.put(VocabContract.Words.IS_HIDDEN, 1);
        else
            values.put(VocabContract.Words.IS_HIDDEN, 0);


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

    public int DeleteWord(int Id) throws Exception
    {

        SQLiteDatabase database = db.getWritableDatabase();


        try

        {

            int rowsAffected = database.delete(VocabContract.Words.TABLE_NAME,  VocabContract.Words.ID + "=" + Id, null);


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
        return HideWord(Id, true);
    }

    public int UpdateRating(int Id, int targetRating) throws Exception
    {

        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VocabContract.Words.ID, Id);
        values.put(VocabContract.Words.DIFFICULTY, targetRating);


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


                    wordList.add(GetWordFromCursor(database, c));
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

    public int GetRecentWordCount() throws Exception
    {
        SQLiteDatabase database = db.getReadableDatabase();
        int count;



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

            count=c.getCount();

            if (c.moveToFirst())
            {

            }

            c.close();
        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;
        }

        return count;
    }

    public List<Word> GetAllWords() throws Exception
    {
        return GetAllWords(false);
    }

    public List<Word> GetAllWords(boolean getRelated) throws Exception
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
                    if(!getRelated)
                        wordList.add(GetWordFromCursor(database, c));
                    else
                        wordList.add(GetWordFromCursor(database, c, true ));
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

    public int GetWordsCount(int wordType) throws Exception
    {
        SQLiteDatabase database = db.getReadableDatabase();
        int count=0;


        try

        {
            Cursor c = database.query(VocabContract.Words.TABLE_NAME, // The table to
                    // query
                    null, // The columns to return
                    VocabContract.Words.IS_USER+"="+ String.valueOf(wordType), // The columns for the WHERE clause
                    null, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );

           count= c.getCount();



            c.close();
        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;
        }

        return count;
    }

    public int GetStarredWordsCount(Consts.STAR star) throws Exception
    {
        SQLiteDatabase database = db.getReadableDatabase();
        int count=0;


        try

        {
            Cursor c = database.query(VocabContract.Words.TABLE_NAME, // The table to
                    // query
                    null, // The columns to return
                    VocabContract.Words.DIFFICULTY+"="+ String.valueOf(star.ordinal()), // The columns for the WHERE clause
                    null, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );

            count= c.getCount();



            c.close();
        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;
        }

        return count;
    }


    public List<Word> GetWords(String startLetter) throws Exception
    {
        SQLiteDatabase database = db.getReadableDatabase();
        List<Word> wordList = new ArrayList<Word>();

        String viewName = startLetter + VocabContract.WORDS_VIEW;

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
                    wordList.add(GetWordFromCursor(database, c));
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
                    wordList.add(GetWordFromCursor(database, c));
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
                    wordList.add(GetWordFromCursor(database, c));
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

    public int GetHiddenWordCount() throws Exception
    {
        SQLiteDatabase database = db.getReadableDatabase();
        int count;

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

            count=c.getCount();

            c.close();
        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());
            throw e;
        }


        return count;
    }


    public Word GetSingleWord(int Id) throws Exception
    {
        return GetSingleWord(Id, true);
    }

    public Word GetSingleWord(int Id, boolean getRelatedwords) throws Exception
    {
        SQLiteDatabase database = db.getReadableDatabase();
        Word word = null;

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

                word= GetWordFromCursor(database,  c, getRelatedwords, Id);

            }

            c.close();


        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }


        return word;
    }

    private List<Word> GetSynonyms(SQLiteDatabase database, int WordId, int groupId) throws Exception
    {
        return GetSynonymsOrSimilar(database, VocabContract.Words.SYN_GROUP, WordId, groupId);

    }

    private List<Word> GetSimilar(SQLiteDatabase database, int WordId, int groupId) throws Exception
    {
        return GetSynonymsOrSimilar(database, VocabContract.Words.SIM_GROUP, WordId, groupId);

    }

    private List<Word> GetSynonymsOrSimilar(SQLiteDatabase database, String colName, int WordId, int groupId) throws Exception
    {
        List<Word> words = new ArrayList<Word>();
        if(groupId==VALUE_NOT_SET)
            return words;

        try
        {

            Cursor c = database.query(VocabContract.Words.TABLE_NAME, // The table to
                    // query
                    null, // The columns to return
                    colName + "=" + String.valueOf(groupId),
                    null, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );

            if (c.moveToFirst())
            {

                do
                {
                    if (WordId != c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.ID)))
                    {

                        words.add(GetSingleWord(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.ID)), false));
                    }

                } while (c.moveToNext());
            }


            c.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return words;


    }


    private void SetSynonyms(SQLiteDatabase database, int WordId, List<Word> syonyms, int oldGroupId) throws Exception
    {
        SetSynonymsOrSimilar(database, VocabContract.Words.SYN_GROUP, WordId, syonyms, oldGroupId);
    }

    private void SetSimilar(SQLiteDatabase database, int WordId, List<Word> similar, int oldGroupId) throws Exception
    {
        SetSynonymsOrSimilar(database, VocabContract.Words.SIM_GROUP, WordId, similar, oldGroupId);
    }


    private void SetSynonyms(SQLiteDatabase database, int WordId, List<Word> syonyms) throws Exception
    {
        SetSynonymsOrSimilar(database, VocabContract.Words.SYN_GROUP, WordId, syonyms, VALUE_NOT_SET);
    }

    private void SetSimilar(SQLiteDatabase database, int WordId, List<Word> similar) throws Exception
    {
        SetSynonymsOrSimilar(database, VocabContract.Words.SIM_GROUP, WordId, similar, VALUE_NOT_SET);
    }

    private void SetSynonymsOrSimilar(SQLiteDatabase database, String colName, int WordId, List<Word> syonyms, int oldGroupId) throws Exception
    {

        List<Word> removedWords = new ArrayList<Word>();

        int groupId = WordId;

        if (syonyms.size() == 0)
        {
            // If there are no syns, no group is set
            groupId = VALUE_NOT_SET;

        }
        else
        {
            ArrayList<Word> tempList=new ArrayList<Word>();
            List<Word> previousSyns;

            if(colName.equals(VocabContract.Words.SIM_GROUP))
               previousSyns=GetSingleWord(WordId).getSimilar();
            else
                previousSyns=GetSingleWord(WordId).getSynonyms();


            removedWords.addAll(previousSyns);
            removedWords.removeAll(syonyms);

            Log.d(App.TAG, "Removed words: " + Word.print(removedWords));


            for (Word word : syonyms)
            {
                if (word.getId() < groupId)
                    groupId = word.getId();


                if(colName.equals(VocabContract.Words.SIM_GROUP))
                {
                    List<Word> syns=GetSingleWord(word.getId()).getSimilar();

                    if(syns!=null)
                    {

                        //syonyms.addAll(syns);
                        tempList.addAll(syns);
                        for (Word simWord : syns)
                        {
                            if (simWord.getSimGroup() < groupId)
                                groupId = simWord.getSimGroup();
                        }
                    }

                }
                else
                {
                    List<Word> syns=GetSingleWord(word.getId()).getSynonyms();
                    if(syns!=null)
                    {
                       // syonyms.addAll(syns);
                        tempList.addAll(syns);
                        for (Word simWord : syns)
                        {
                            Log.d(App.TAG, "Evaluating word " + simWord.getName());
                            if (simWord.getSynGroup() < groupId)
                                groupId = simWord.getSynGroup();
                        }
                    }

                }
            }

            syonyms.addAll(tempList);





        }

        Log.d(App.TAG, "Final group id: " + String.valueOf(groupId));
        Log.d(App.TAG, Word.join(syonyms));

        ContentValues values = new ContentValues();
        values.put(colName, groupId);

        try
        {



            if (syonyms.size() > 0)
            {
                int rowsAffected = database.update(VocabContract.Words.TABLE_NAME, values, VocabContract.Words.ID + " IN (" + Word.join(syonyms) + "," + WordId + ")", null);

            }
            else
            {
                int rowsAffected = database.update(VocabContract.Words.TABLE_NAME, values, VocabContract.Words.ID + "=" + WordId, null);


                if (WordId == oldGroupId)
                    UpdateGroupId(database, oldGroupId, colName);

            }

            // unset group if some words have been removed
            if(removedWords.size()>0)
            {
                values=new ContentValues();
                values.put(colName, VALUE_NOT_SET);
                database.update(VocabContract.Words.TABLE_NAME, values, VocabContract.Words.ID + " IN (" + Word.join(removedWords) +  ")", null);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }


    }

    private void UpdateGroupId(SQLiteDatabase database, int oldGroupId, String colName) throws Exception
    {
        List<Word> words=GetWordsinGroup(database, oldGroupId, colName);
        int newGroupId=Integer.MAX_VALUE;

        for(Word word:words)
        {
            if(word.getId() < newGroupId)
            {
                newGroupId=word.getId();
            }
        }

        ContentValues values=new ContentValues();
        values.put(colName, newGroupId);


        try
        {


            int rowsAffected=database.update(VocabContract.Words.TABLE_NAME, values, VocabContract.Words.ID + " IN (" + Word.join(words) + ")", null);


        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }




    }

    private List<Word> GetWordsinGroup(SQLiteDatabase database, int groupId, String colName) throws Exception
    {


        List<Word> wordList = new ArrayList<Word>();


        try
        {

            Cursor c = database.query(VocabContract.Words.TABLE_NAME, // The table to
                    // query
                    null, // The columns to return
                    colName + "=" + groupId,
                    null, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );

            if (c.moveToFirst())
            {
                do
                {


                    wordList.add(GetWordFromCursor(database, c));
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


    public int UpdateWord(int Id, String meaning, List<Word> synonyms, List<Word> similar) throws Exception
    {

        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VocabContract.Words.ID, Id);
        values.put(VocabContract.Words.MEANING, meaning);

        int oldSynGroupId = GetSingleWord(Id).getSynGroup();
        int oldSimGroupId = GetSingleWord(Id).getSimGroup();


        try

        {

            int rowsAffected = database.update(VocabContract.Words.TABLE_NAME, values, VocabContract.Words.ID + "=" + Id, null);


            SetSynonyms(database, Id, synonyms, oldSynGroupId);

            SetSimilar(database, Id, similar, oldSimGroupId);


        }
        catch (Exception e)
        {
            e.printStackTrace();
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

        values.put(VocabContract.Words.IS_HIDDEN, 0);
        values.put(VocabContract.Words.IS_USER, 1);


        try

        {

            long wordId = database.insert(VocabContract.Words.TABLE_NAME, null, values);


            if (synonyms.size() > 0)
                SetSynonyms(database, (int) wordId, synonyms);

            if (similar.size() > 0)
                SetSimilar(database, (int) wordId, similar);

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

    private Word GetWordFromCursor(SQLiteDatabase database, Cursor c) throws Exception
    {
       return GetWordFromCursor(database, c, false, VALUE_NOT_SET);
    }

    private Word GetWordFromCursor(SQLiteDatabase database, Cursor c, boolean getRelatedWords) throws Exception
    {
        int Id=c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.ID));

        return GetWordFromCursor(database, c, false, Id);
    }


    private Word GetWordFromCursor(SQLiteDatabase database, Cursor c, boolean getRelatedwords, int Id) throws Exception
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


        if (getRelatedwords)
        {
            singleWord.setSynonyms(GetSynonyms(database, Id, c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.SYN_GROUP))));
            singleWord.setSimilar(GetSimilar(database, Id, c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.SIM_GROUP))));
        }

        singleWord.setSynGroup(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.SYN_GROUP)));
        singleWord.setSimGroup(c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.SIM_GROUP)));


        if (c.getInt(c.getColumnIndexOrThrow(VocabContract.Words.IS_USER)) == 1)
            singleWord.setUserWord(true);
        else
            singleWord.setUserWord(false);


        return singleWord;

    }


}
