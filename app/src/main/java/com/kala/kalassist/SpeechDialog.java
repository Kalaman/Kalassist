package com.kala.kalassist;

/**
 * Created by Kalaman on 20.10.17.
 */
public class SpeechDialog {
    private String question;
    private String answer;
    private SpeechCommand usedSpeechCommand;
    private boolean fromDB;

    public SpeechDialog(String question, String answer, SpeechCommand speechCommand)
    {
        this.question = question;
        this.answer = answer;
        this.usedSpeechCommand = speechCommand;
        this.fromDB = false;

        this.answer = this.answer.substring(0,1).toUpperCase() + this.answer.substring(1);
        this.question = this.question.substring(0,1).toUpperCase() + this.question.substring(1);
    }

    public SpeechDialog(String question, String answer, SpeechCommand speechCommand, boolean fromDB)
    {
        this(question,answer,speechCommand);
        this.fromDB = fromDB;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public SpeechCommand getSpeechCommand()
    {
        return usedSpeechCommand;
    }
}
