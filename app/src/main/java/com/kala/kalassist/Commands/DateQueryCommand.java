package com.kala.kalassist.Commands;

import android.content.Context;

import com.kala.kalassist.R;
import com.kala.kalassist.SpeechCommand;

import java.util.Calendar;

/**
 * Created by Kalaman on 20.10.17.
 */
public class DateQueryCommand extends SpeechCommand{

    public DateQueryCommand (Context context) {
        super(context);
    }

    @Override
    public String executeCommand(String speechInput) {
        Calendar calander = Calendar.getInstance();
        return "Wir haben heute den " + calander.get(Calendar.DAY_OF_MONTH) + "." + (calander.get(Calendar.MONTH) + 1) + "." + calander.get(Calendar.YEAR);
    }

    @Override
    public String [] getMasterKeywords() {
        return new String[] {"datum","heute"};
    }

    @Override
    public String[] getKeywords() {
        return new String [] {"wie lautet","datum","wievielten", "heute"};
    }

    @Override
    public int getCommandPictureResourceID() {
        return R.drawable.ic_date_range_black_36dp;
    }
}
