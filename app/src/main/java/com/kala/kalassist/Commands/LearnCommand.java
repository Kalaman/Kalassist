package com.kala.kalassist.Commands;

import android.content.Context;

import com.kala.kalassist.DatabaseActions;
import com.kala.kalassist.LoginActivity;
import com.kala.kalassist.MainActivity;
import com.kala.kalassist.R;
import com.kala.kalassist.SpeechCommand;
import com.kala.kalassist.SpeechCommandControlller;

/**
 * Created by Kalaman on 04.04.18.
 */

public class LearnCommand extends SpeechCommand {

    String question = null;
    String answer = null;

    public LearnCommand(Context context) {
        super(context);
    }

    @Override
    public String executeCommand(String speechInput) {
        if (SpeechCommandControlller.DIALOG_MODE) {
            if (question == null){
                question = speechInput;
                return getConfirmationAnswer() + ". Wie soll ich darauf antworten ?";
            }
            else {
                answer = speechInput;
                boolean dbSuccess = DatabaseActions.savePhrase(question,answer,context);
                question = null;
                answer = null;
                SpeechCommandControlller.DIALOG_MODE = false;
                if (dbSuccess)
                    return "Ich habs mir gemerkt, " + LoginActivity.savedUsername;
                else
                    return "Aufgrund eines Unbekannten Fehlers konnte ich mir das nicht merken";
            }
        }
        else {
            SpeechCommandControlller.DIALOG_MODE = true;
            return getConfirmationAnswer() + ". Auf was soll ich reagieren ?";
        }
    }

    @Override
    public String[] getMasterKeywords() {
        return new String[] { "beibringen","lernen" };
    }

    @Override
    public String[] getKeywords() {
        return new String[] { "ich will", "beibringen", "lernen", "satz"};
    }

    @Override
    public int getCommandPictureResourceID() {
        return R.drawable.ic_school_black_24dp;
    }
}
