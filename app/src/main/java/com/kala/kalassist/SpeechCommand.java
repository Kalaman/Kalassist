package com.kala.kalassist;

import android.content.Context;

import java.util.Random;

/**
 * Created by Kalaman on 19.10.17.
 */
public abstract class SpeechCommand {

    private static final String [] positiveAnswers = {"Ja","Geht Klar","Natürlich","Ok","Okay"};
    private static final String [] negativeAnswers = {"Nein","Tut mir Leid", "Sorry"};
    private static final String [] confirmationAnswers = {"Natürlich","Mache ich", "Jawohl","Okay","Klar","In Ordnung", "Es soll mir Recht sein", "Selbstverständlich"};
    private static final String [] unknownAnswers = {
            "Leider kann ich dir dabei nicht helfen.",
            "Sorry, dies kann ich dir nicht beantworten.",
            "Hmmm...",
            "Ich verstehe dich leider nicht.",
            "Kann ich dir irgendwie anders helfen ?",
            "Das habe ich leider nicht verstanden.",
            "Ich tue mal so als hätte ich es verstanden.",
            "Sei mir nicht sauer, aber ich verstehe dich nicht.",
            "Ich bin mir nicht sicher, ob ich das richtig verstanden habe.",
            "Tut mir leid. Ich werde mich bessern.",
            "Hervorragende Frage.",
            "Interessante Frage.",
            "Tut mir leid, jedoch verstehe ich nur Bahnhof",
            "Ich verstehe nur Bahnhof"
    };

    protected Context context;
    protected int priority = CommandPriorities.PRIORITY_LOW;

    public SpeechCommand (Context context)
    {
        this.context = context;
    }

    /**
     * Überprüft ob die Spracheingabe einer der masterKeywords enthält
     * @param speechInput
     * @return
     */
    public final boolean masterKeywordMatch (String speechInput) {
        for (String currentKey : getMasterKeywords())
        {
            if (speechInput.toLowerCase().contains(currentKey.toLowerCase()))
                return true;
        }
        return false;
    }

    /**
     * Zählt die Anzahl der passenden 'keywords' auf.
     * @param speechInput String der von der Spracherkennung geliefert wird
     * @return Anzahl der Funde
     */
    public final int getMatchCount (String speechInput) {
        int matches = 0;
        String [] keywords = getKeywords();

        for (String currentKeyword : keywords)
        {
            if (speechInput.toLowerCase().contains(currentKeyword))
                ++matches;
        }

        return matches;
    }

    /**
     * Enthält die Funktion bzw. Logik des Befehls
     * @param speechInput String der von der Spracherkennung geliefert wird
     * @return Antwort für TTS
     */
    public abstract String executeCommand(String speechInput);

    /**
     * Jeder Command muss ein MasterKeyword besitzen
     * Dies hilft bei der Auswahl der richtigen SpeechCommands
     * @return
     */
    public abstract String [] getMasterKeywords();

    /**
     * Keywords machen die Wahl einfacher.
     * Desto mehrere Keywords es gibt, desto sicherer wird die Wahl.
     * Dies hilft bei der Auswahl der richtigen SpeechCommands
     * @return
     */
    public abstract String [] getKeywords();

    /**
     * Jeder Command sollte einen Icon besitzten welches im RecyclerViewAdapter genutzt wird.
     * @return die Drawable-ID des Icons
     */
    public abstract int getCommandPictureResourceID();

    /*
        getPositiveAnswer, getNegativeAnswer,getUnknownAnswer und getConfirmationAnswer
        sind Standardantworten,die per Zufall ausgewählt werden.
     */
    public static final String getPositiveAnswer () {
        int randomIndex = new Random().nextInt(positiveAnswers.length);
        return positiveAnswers[randomIndex];
    }

    public static final String getNegativeAnswer () {
        int randomIndex = new Random().nextInt(negativeAnswers.length);
        return negativeAnswers[randomIndex];
    }

    public static final String getUnknownAnswer () {
        int randomIndex = new Random().nextInt(unknownAnswers.length);
        return unknownAnswers[randomIndex];
    }

    public static final String getConfirmationAnswer () {
        int randomIndex = new Random().nextInt(confirmationAnswers.length);
        return confirmationAnswers[randomIndex];
    }

    public interface CommandListener {
        public void onCommandProcessed(SpeechDialog dialog);
    }

}
