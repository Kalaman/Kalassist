package com.kala.kalassist.Commands;

import android.content.Context;

import com.kala.kalassist.DatabaseActions;
import com.kala.kalassist.R;
import com.kala.kalassist.SpeechCommand;
import com.kala.kalassist.SpeechDialog;

import java.util.Random;

/**
 * Created by Kalaman on 19.10.17.
 */
public class DefaultCommand extends SpeechCommand{

    public DefaultCommand(Context context) {
        super(context);
    }

    @Override
    public String executeCommand(String speechInput) {

        SpeechDialog dbSpeechDialog = DatabaseActions.getPhrase(speechInput,context);

       /* if (dbSpeechDialog != null) {
            return dbSpeechDialog.getAnswer();

        }else {
*/
            if (speechInput.equalsIgnoreCase("Wie geht es dir") ||
                    speechInput.equalsIgnoreCase("Wie geht's dir")) {
                return "Mir geht es gut! \nWie kann ich dir weiterhelfen ?";
            } else if (speechInput.equalsIgnoreCase("Hallo") ||
                    speechInput.equalsIgnoreCase("Hi") ||
                    speechInput.equalsIgnoreCase("Hey")) {
                return "Hallo, wie kann ich dir helfen ?";
            } else if (speechInput.equalsIgnoreCase("Wer bist du")) {
                return "Ich bin dein Kalassist. \nWas kann ich für dich tun ?";
            } else if (speechInput.equalsIgnoreCase("Was machst du")) {
                return "Ich diene dir. \nWas kann ich für dich tun ?";
            } else if (speechInput.equalsIgnoreCase("Wer hat dich programmiert") ||
                    speechInput.equalsIgnoreCase("Wer hat dich erstellt") ||
                    speechInput.equalsIgnoreCase("Wer hat dich entwickelt")) {
                return "Irgendein Typ namens Ömer oder so etwas ähnliches. Programmieren muss er aber auf jeden Fall noch lernen.";
            } else {
                return getUnknownAnswer();
            }
        //}
    }

    @Override
    public String [] getMasterKeywords() {
        return new String[]{""};
    }

    @Override
    public String[] getKeywords() {
        return new String[0];
    }

    @Override
    public int getCommandPictureResourceID() {
        return -1;
    }

}
