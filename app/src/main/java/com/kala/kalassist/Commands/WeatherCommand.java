package com.kala.kalassist.Commands;

import android.content.Context;

import com.kala.kalassist.JSONDownloadServices;
import com.kala.kalassist.R;
import com.kala.kalassist.SpeechCommand;
import com.kala.kalassist.SpeechCommandControlller;

import static com.kala.kalassist.SpeechCommandControlller.DIALOG_MODE;

/**
 * Created by Kalaman on 23.10.17.
 */
public class WeatherCommand extends SpeechCommand {

    public WeatherCommand(Context context)
    {
        super(context);
    }

    @Override
    public String executeCommand(String speechInput) {

        String city = "";

        String [] splitedSpeechInput = speechInput.split(" ");

        if (DIALOG_MODE)
            city = speechInput;
        else {
            for (int i = 0; i < splitedSpeechInput.length; ++i) {
                if (splitedSpeechInput[i].equalsIgnoreCase("in")) {
                    city = splitedSpeechInput[i + 1];
                }
            }
        }

        Double temp = JSONDownloadServices.getWeatherInfo(city);

        if (temp != null) {
            if (DIALOG_MODE)
                DIALOG_MODE = false;
            return "In " + city + " ist es zurzeit " + temp + " °C";
        }
        else if (DIALOG_MODE && temp == null)
        {
            DIALOG_MODE = false;
            return "Leider kenne ich diese Stadt nicht.";
        }
        else
        {
            DIALOG_MODE = true;
            return "In welcher Stadt ?";
        }
    }

    @Override
    public String[] getMasterKeywords() {
        return new String[] {"wetter"};
    }

    @Override
    public String[] getKeywords() {
        return new String[] {"wetter","wie ist"};
    }

    @Override
    public int getCommandPictureResourceID() {
        return R.drawable.img_weather;
    }
}
