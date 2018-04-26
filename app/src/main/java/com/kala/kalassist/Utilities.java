package com.kala.kalassist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


/**
 * Created by Kalaman on 31.10.17.
 */
public class Utilities {

    public static String capFirstCharOfWords(String str) {
        String[] words = str.split(" ");
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < words.length; i++) {
            ret.append(Character.toUpperCase(words[i].charAt(0)));
            ret.append(words[i].substring(1));
            if(i < words.length - 1) {
                ret.append(' ');
            }
        }
        return ret.toString();
    }

    public static String cleanArticleString (String str) {
        if (str.matches("ein (.*?)") || str.matches("eine (.*?)"))
        {
            return str.replaceAll("ein ","").replaceAll("eine ", "");
        }
        else return str;
    }

    public static void showReportAlertDialog(final CommunityDialog communityDialog, final Context context, final DialogRecyclerAdapter recyclerAdapter, final int index){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Antwort melden");
        alertDialog.setMessage("Möchten Sie diese Community Antwort melden ?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ja",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseActions.reportPhrase(communityDialog.getTpid(),context,null);
                        recyclerAdapter.deleteSpeechDialog(index);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nein",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
