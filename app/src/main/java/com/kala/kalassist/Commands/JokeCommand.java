package com.kala.kalassist.Commands;

import android.content.Context;

import com.kala.kalassist.R;
import com.kala.kalassist.SpeechCommand;

import java.util.Random;

/**
 * Created by Kalaman on 14.11.17.
 */

public class JokeCommand extends SpeechCommand {

    private String [] jokes = {"Ich hatte einen Traum, dass ich ein riesiges Brötchen aß.\nAls ich aufwachte war mein Kissen weg.",
                                "Was machen zwei wütende Schafe? \nSie kriegen sich in die Wolle.",
                                "Was ist weiß und stört beim Frühstück? \nEine Lawine",
                                "Wo leben die meisten Gespenster?\nIn BUHdapest.",
                                "Was stört auf dem Fußballplatz?\nDas Foultier.",
                                "Wann gehen U-Boote unter?\nAm Tag der offenen Tür.",
                                "Warum legen Hühner Eier? \nWenn sie, sie schmeißen würden gehen sie doch kaputt!",
                                "Wie viele Windows-Anwender braucht man, um eine Glühbirne zu wechseln?\n100, einer schraubt und 99 klicken die Fehlermeldungen weg.",
                                "Was ist die Lieblingsbeschäftigung von Bits?\nBusfahren.",
                                "Es gibt 10 Arten von Menschen in der Welt.\nDie einen verstehen das Binärsystem und die anderen nicht!",
                                "Um Rekursion zu verstehen, muss man zunächst Rekursion verstehen!",
                                "Sitzt ein Informatiker in der Sonne."
            };

    public JokeCommand(Context context) {
        super(context);
    }

    @Override
    public String executeCommand(String speechInput) {
        return getRandomJoke();
    }

    @Override
    public String[] getMasterKeywords() {
        return new String[] { "witz", "witze", "lustig", "lustiges", "joke"};
    }

    @Override
    public String[] getKeywords() {
        return new String[] { "erzähl", "witz", "lustig", "witze", "sag"};
    }

    @Override
    public int getCommandPictureResourceID() {
        return R.drawable.ic_joke;
    }

    private final String getRandomJoke () {
        int randomIndex = new Random().nextInt(jokes.length);
        return jokes[randomIndex];
    }
}
