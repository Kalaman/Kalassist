package com.kala.kalassist;

/**
 * Created by Kalaman on 22.04.18.
 */

public class CommunityDialog extends SpeechDialog {

    private String author;
    private String tpid;

    public CommunityDialog(String question, String answer, SpeechCommand speechCommand, String author, String tpid) {
        super(question, answer, speechCommand);
        this.author = author;
        this.tpid = tpid;
    }

    public CommunityDialog(String question, String answer, SpeechCommand speechCommand) {
        super(question, answer, speechCommand);
    }

    public CommunityDialog(String question, String answer, SpeechCommand speechCommand, boolean fromDB) {
        super(question, answer, speechCommand, fromDB);
    }

    @Override
    public String getQuestion() {
        return super.getQuestion();
    }

    @Override
    public String getAnswer() {
        return super.getAnswer();
    }

    @Override
    public SpeechCommand getSpeechCommand() {
        return super.getSpeechCommand();
    }

    public String getAuthor() {
        return author;
    }

    public String getTpid() {
        return tpid;
    }
}
