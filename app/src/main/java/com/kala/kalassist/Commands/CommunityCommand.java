package com.kala.kalassist.Commands;

import android.content.Context;

import com.kala.kalassist.CommunityDialog;
import com.kala.kalassist.DatabaseActions;
import com.kala.kalassist.R;
import com.kala.kalassist.SpeechCommand;

/**
 * Created by Kalaman on 22.04.18.
 */

public class CommunityCommand extends SpeechCommand{
    CommunityDialog lastCommunityDialog;

    public CommunityCommand(Context context) {
        super(context);
    }

    @Override
    public String executeCommand(String speechInput) {
        lastCommunityDialog = DatabaseActions.getPhrase(speechInput,context);

        if (lastCommunityDialog != null) {
            return lastCommunityDialog.getAnswer();
        }
        else
            return null;
    }

    @Override
    public String[] getMasterKeywords() {
        return new String[0];
    }

    @Override
    public String[] getKeywords() {
        return new String[0];
    }

    @Override
    public int getCommandPictureResourceID() {
        return R.drawable.ic_public_black_24dp;
    }

    public CommunityDialog getLastCommunityDialog() {
        return lastCommunityDialog;
    }
}
