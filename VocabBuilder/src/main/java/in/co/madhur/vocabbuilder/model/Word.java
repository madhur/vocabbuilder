package in.co.madhur.vocabbuilder.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * Created by madhur on 19-Jun-14.
 */
public class Word implements Serializable
{

    private boolean isHidden;
    private int Id;
    private int rating;
    private String name;
    private String meaning;
    private long date;
    private List<Word> synonyms;
    private List<Word> similar;
    private boolean isUserWord;

    private int synGroup;
    private int simGroup;

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
        for (Word word : words)
        {
            if (word.getId() == id)
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

    public long getDate()
    {
        return date;
    }

    public void setDate(long date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return getName();
    }

    public List<Word> getSynonyms()
    {
        return synonyms;
    }

    public void setSynonyms(List<Word> synonyms)
    {
        this.synonyms = synonyms;
    }

    public List<Word> getSimilar()
    {
        return similar;
    }

    public void setSimilar(List<Word> similar)
    {
        this.similar = similar;
    }

    public int getSynGroup()
    {
        return synGroup;
    }

    public void setSynGroup(int synGroup)
    {
        this.synGroup = synGroup;
    }

    public int getSimGroup()
    {
        return simGroup;
    }

    public void setSimGroup(int simGroup)
    {
        this.simGroup = simGroup;
    }

    public boolean isUserWord()
    {
        return isUserWord;
    }

    public void setUserWord(boolean isUserWord)
    {
        this.isUserWord = isUserWord;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Word && ((Word) o).getId() == this.getId())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static class NameSorter implements Comparator<Word>
    {

        @Override
        public int compare(Word lhs, Word rhs)
        {

            return lhs.getName().compareTo(rhs.getName());
        }

    }

    public static class RatingSorter implements Comparator<Word>
    {


        @Override
        public int compare(Word lhs, Word rhs)
        {
            if (lhs.getRating() < rhs.getRating())
            {
                return -1;
            }
            else if (lhs.getRating() > rhs.getRating())
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
    }


    public static class DateSorter implements Comparator<Word>
    {

        @Override
        public int compare(Word lhs, Word rhs)
        {
            if (lhs.getDate() < rhs.getDate())
            {
                return -1;
            }
            else if (lhs.getDate() > rhs.getDate())
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
    }

    public static String join(Iterable<Word> elements)
    {
        return join(elements, ",");
    }

    private static String join(Iterable<Word> elements, String delimiter)
    {
        StringBuilder sb = new StringBuilder();
        for (Word e : elements)
        {
            if (sb.length() > 0)
            {
                sb.append(delimiter);
            }
            sb.append(e.getId());
        }
        return sb.toString();
    }

    public static String print(Iterable<Word> elements)
    {
        StringBuilder sb = new StringBuilder();
        for (Word e : elements)
        {
            if (sb.length() > 0)
            {
                sb.append(",");
            }
            sb.append(e.getName());
        }
        return sb.toString();
    }
}
