package com.kala.kalassist.Commands;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import com.kala.kalassist.MainActivity;
import com.kala.kalassist.R;
import com.kala.kalassist.SpeechCommand;
import com.kala.kalassist.SpeechCommandControlller;

/**
 * Created by Kalaman on 23.10.17.
 */
public class AlarmQueryCommand extends SpeechCommand {

    public AlarmQueryCommand (Context context) {
        super(context);
    }

    @Override
    public String executeCommand(String speechInput) {

        String [] splitedSpeechInput = speechInput.split(" ");

        int hour = -1;
        int minute = 0;

        for (int i=0;i<splitedSpeechInput.length;++i)
        {
            if (splitedSpeechInput[i].contains(":"))
            {
                String [] time = splitedSpeechInput[i].split(":");

                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
                break;
            }
            else if (splitedSpeechInput[i].matches(".*\\d+.*"))
            {
                hour = Integer.parseInt(splitedSpeechInput[i]);
                break;
            }
        }

        //Wenn aus dem String keine Uhr geparsed werden kann
        //Einfach ein Dialog starten und nochmals explizit danach fragen.
        if (hour == -1)
        {
            SpeechCommandControlller.DIALOG_MODE = true;
            return "Für wie viel Uhr soll ich den Wecker stellen ?";
        }
        else
        {
            if (SpeechCommandControlller.DIALOG_MODE)
                SpeechCommandControlller.DIALOG_MODE = false;

            Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
            i.putExtra(AlarmClock.EXTRA_HOUR, hour);
            i.putExtra(AlarmClock.EXTRA_MINUTES, minute);
            i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
            context.startActivity(i);

            return minute == 0 ? "Wecker wurde für " + hour + " Uhr eingestellt" : "Wecker wurde für " + hour + ":" + minute + " Uhr eingestellt";
        }
    }

    @Override
    public String[] getMasterKeywords() {
        return new String[] {
                "wecker"
        };
    }

    @Override
    public String[] getKeywords() {
        return new String [] { "stell", "wecker", "einen" , "für"};
    }

    @Override
    public int getCommandPictureResourceID() {
        return R.drawable.ic_alarm_black_36dp;
    }

}
