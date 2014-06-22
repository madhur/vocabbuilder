/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.madhur.vocabbuilder.model.Word;

/**
 * Created by madhur on 22-Jun-14.
 */
public  class Cache
{
    private static Map<String, List<Word>> wordMap=new HashMap<String, List<Word>>();



    public static Map<String, List<Word>> getWordMap()
    {
        return wordMap;
    }

    public static void setWordMap(Map<String, List<Word>> wordMap)
    {
        Cache.wordMap = wordMap;
    }

    public static void Put(String letter, List<Word> words)
    {
        wordMap.put(letter, words);
    }

    public static List<Word> Get(String letter)
    {
        return wordMap.get(letter);
    }


}
