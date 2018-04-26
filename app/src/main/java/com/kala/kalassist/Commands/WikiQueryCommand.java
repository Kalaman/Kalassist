package com.kala.kalassist.Commands;

import android.content.Context;
import android.util.Log;

import com.kala.kalassist.JSONDownloadServices;
import com.kala.kalassist.R;
import com.kala.kalassist.SpeechCommand;
import com.kala.kalassist.Utilities;
import com.squareup.okhttp.internal.Util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kalaman on 31.10.17.
 */
public class WikiQueryCommand extends SpeechCommand {
    private static final String [] failedQueryAnswers = {"Darauf habe ich leider keine Antwort.", "Dies kann ich dir nicht beantworten.",
                                                         "Ich konnte leider keine Antwort darauf finden.","Leider habe ich keine Antwort darauf.",
                                                         "Sorry, aber leider konnte ich dir keine Antwort finden."};
    public WikiQueryCommand(Context context) {
        super(context);
    }

    @Override
    public String executeCommand(String speechInput) {

        speechInput = speechInput.toLowerCase();
        String [] splitedSpeechInput;
        String searchWord = "";

        //Abfragentypen:
        //Typ 1: Kannst du sagen wer * ist
        //Typ 2: Wer ist Barrack Obama

        //Typ 1
        if (speechInput.matches("(.*?)wer (.*?) ist(.*?)") || speechInput.matches("(.*?)was (.*?) ist(.*?)"))
        {
            int [] indexes;

            if (speechInput.matches("(.*?)wer (.*?) ist(.*?)"))
                indexes = getWordIndexes(speechInput, "wer (.*?) ist");
            else
                indexes = getWordIndexes(speechInput, "was (.*?) ist");

            searchWord = speechInput.substring(indexes[0], indexes[1]);

            splitedSpeechInput = searchWord.split(" ");
            searchWord = "";

            for (int i=1;i<splitedSpeechInput.length-1;++i)
            {
                if (i == splitedSpeechInput.length-1)
                    searchWord += splitedSpeechInput[i];
                else
                    searchWord += splitedSpeechInput[i] + " ";
            }
        }
        //Typ 2
        else if (speechInput.matches("wer ist (.*?)") || speechInput.matches("wer sind (.*?)"))
        {
            searchWord = speechInput.replace("wer ist ","");
            searchWord = searchWord.replace("wer sind ","");
        }
        //Typ 2
        else if (speechInput.matches("was ist (.*?)") || speechInput.matches("was sind (.*?)"))
        {
            searchWord = speechInput.replace("was ist ","");
            searchWord = searchWord.replace("was sind ","");
        }
        //Wenn keiner der parsing Versuche funktioniert -> return UnknownCommand
        else
        {
            return getUnknownAnswer();
        }

        searchWord = Utilities.cleanArticleString(searchWord);
        Log.d("WikiQuery", "searchWord : " + searchWord);

        String info = JSONDownloadServices.getWikiInfo(searchWord);

        //Wenn es mehrere Ergebnise für die Suche gibt, gibt die API keine Antwort.
        if (info != null && info.toLowerCase().contains(searchWord.toLowerCase() + " steht für:")) {
            return "Es gibt mehrere Ergebnise für deine Frage.\nVersuch es mit einer definierteren Wortwahl.";
        }
        else if (info != null)
        {
            //String [] splitedInfo = info.replaceAll(" \\((.*?)\\) ", " ").replaceAll(" \\[(.*?)\\] ", " ") .split("\\.");
            info = info.replaceAll(" \\((.*?)\\)", "").replaceAll(" \\[(.*?)\\]", "");

            return info;
        }
        //Wenn die Variable "info" auf den Wert null gesetzt wurde, wird ein UnknownAnswer zurückgegeben
        else {
            return getFailedQueryAnswer();
        }
    }

    /**
     * Findet den Index vom gesuchten Text mit der Nutzung eines Regexes.
     * Dies ist mit einer normalen "contains" Methodenaufruf nicht möglich.
     * @param text Der gesamte Text wo nach einem Spezielen pattern gesucht werden soll
     * @param searchTextWithRegex der gesuchte Text mit einem regex Ausdruck
     * @return gibt einen int [] zurück, wobei [0] die Startposition ist und [1] die Endposition des gesuchten Textes
     */
    public static int [] getWordIndexes(String text,String searchTextWithRegex) {
        Pattern pattern = Pattern.compile(searchTextWithRegex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find())
            return new int [] {matcher.start(),matcher.end()};

        return null;
    }

    private static String getFailedQueryAnswer() {
        int randomIndex = new Random().nextInt(failedQueryAnswers.length);
        return failedQueryAnswers[randomIndex];
    }

    @Override
    public String[] getMasterKeywords() {
        return new String[] {"wer ist ", "was ist "," ist "};
    }

    @Override
    public String[] getKeywords() {
        return new String[] {"wer ist ","was ist " , "wer ", " ist ", "was "};
    }

    @Override
    public int getCommandPictureResourceID() {
        return R.drawable.img_wikipedia;
    }
}
