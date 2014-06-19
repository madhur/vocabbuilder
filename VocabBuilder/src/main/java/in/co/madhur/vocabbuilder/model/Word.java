package in.co.madhur.vocabbuilder.model;

/**
 * Created by madhur on 19-Jun-14.
 */
public class Word
{

    private boolean isHidden;
    private String name;
    private String meaning;

    public Word(String word, String meaning)
    {
        this.name=word;
        this.meaning=meaning;
    }

    public Word()
    {

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
}
