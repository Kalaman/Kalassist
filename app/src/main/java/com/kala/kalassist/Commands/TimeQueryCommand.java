package com.kala.kalassist.Commands;

import android.content.Context;
import android.util.Log;

import com.kala.kalassist.CommandPriorities;
import com.kala.kalassist.R;
import com.kala.kalassist.SpeechCommand;

import java.util.Calendar;

/**
 * Created by Kalaman on 19.10.17.
 */
public class TimeQueryCommand extends SpeechCommand {

    public TimeQueryCommand(Context context) {
        super(context);
        this.priority = CommandPriorities.PRIORITY_HIGH;
    }

    @Override
    public String executeCommand(String speechInput) {
        Calendar calender = Calendar.getInstance();

        return "Wir haben " + calender.get(Calendar.HOUR_OF_DAY) + ":" + (calender.get(Calendar.MINUTE) > 9 ? calender.get(Calendar.MINUTE) : "0" +calender.get(Calendar.MINUTE));
    }

    @Override
    public String [] getMasterKeywords() {
        return new String[] {"uhr"};
    }

    @Override
    public String[] getKeywords() {
        return new String [] {"wie viel", "uhr", "haben wir"};
    }

    @Override
    public int getCommandPictureResourceID() {
        return R.drawable.ic_access_time_black_36dp;
    }
}
