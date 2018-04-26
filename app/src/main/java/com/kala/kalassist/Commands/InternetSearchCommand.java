package com.kala.kalassist.Commands;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.kala.kalassist.R;
import com.kala.kalassist.SpeechCommand;

/**
 * Created by Kalaman on 29.10.17.
 */
public class InternetSearchCommand extends SpeechCommand {

    public InternetSearchCommand(Context context) {
        super(context);
    }

    @Override
    public String executeCommand(String speechInput) {
        String searchWord = speechInput.toLowerCase();

        searchWord = searchWord.replaceAll("suche im internet nach einem ","");
        searchWord = searchWord.replaceAll("suche im internet nach einer ","");
        searchWord = searchWord.replaceAll("suche im internet nach ","");
        searchWord = searchWord.replaceAll("suche nach einem ","");
        searchWord = searchWord.replaceAll("suche nach einer ","");
        searchWord = searchWord.replaceAll("suche nach ","");
        searchWord = searchWord.replaceAll("suche ","");
        searchWord = searchWord.replaceAll(" im internet","");

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + searchWord));
        context.startActivity(browserIntent);

        return getConfirmationAnswer();
    }

    @Override
    public String[] getMasterKeywords() {
        return new String[] {"internet", "suche", "such"};
    }

    @Override
    public String[] getKeywords() {
        return new String[] {"im internet", "suche im internet", "suche"};
    }

    @Override
    public int getCommandPictureResourceID() {
        return R.drawable.ic_language_black_36dp;
    }
}
