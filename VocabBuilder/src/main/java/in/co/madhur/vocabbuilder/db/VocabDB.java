package in.co.madhur.vocabbuilder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.model.Word;

/**
 * Created by madhur on 19-Jun-14.
 */
public class VocabDB
{
    private static VocabDB vocabDb;
    private static DbHelper db;

    public static synchronized VocabDB getInstance(Context context)
    {
        if (vocabDb == null)
        {
            db = new DbHelper(context);
            vocabDb = new VocabDB();
            try
            {
                db.createDataBase();
            } catch (IOException e)
            {
                Log.e(App.TAG, "Error creating datrabase");
            }

            return vocabDb;
        } else
            return vocabDb;

    }

    public int SetRating(int Id, int rating)
    {

        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(VocabContract.Words.DIFFICULTY, rating);

        try

        {
            return database.update(VocabContract.Words.TABLE_NAME,values, VocabContract.Words.ID+"="+Id, null );

        }
        catch (Exception e)
        {
            Log.e(App.TAG, e.getMessage());

        }
        finally
        {

            database.close();
        }

        return 0;
    }


    public List<Word> GetWords(String startLetter)
    {
        SQLiteDatabase database = db.getReadableDatabase();
        List<Word> wordList = new ArrayList<Word>();

        String viewName = startLetter + "_VIEW";

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

        return wordList;
    }

}
