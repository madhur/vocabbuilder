package in.co.madhur.vocabbuilder.model;

import java.util.List;

/**
 * Created by madhur on 19-Jun-14.
 */
public class Word
{

    private boolean isHidden;
    private int Id;
    private int rating;
    private String name;
    private String meaning;

    public Word(String word, String meaning)
    {
        this.name = word;
        this.meaning = meaning;
    }

    public Word()
    {

    }

    public static Word findById(List<Word> words, int id)
    {
        for(Word word: words )
        {
            if(word.getId()==id)
            {
                return word;
            }
        }

        return null;
    }

    public boolean isHidden()
    {
        return isHidden;
    }

    public void setHidden(boolean isHidden)
    {
        this.isHidden = isHidden;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMeaning()
    {
        return meaning;
    }

    public void setMeaning(String meaning)
    {
        this.meaning = meaning;
    }

    public int getId()
    {
        return Id;
    }

    public void setId(int id)
    {
        Id = id;
    }

    public int getRating()
    {
        return rating;
    }

    public void setRating(int rating)
    {
        this.rating = rating;
    }
}
