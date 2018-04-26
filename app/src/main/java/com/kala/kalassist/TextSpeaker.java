package com.kala.kalassist;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by Kalaman on 20.10.17.
 */
public class TextSpeaker {
    TextToSpeech textToSpeech;

    public TextSpeaker(Context context) {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.GERMAN);
                }
            }
        });
    }

    public void speakText (String text) {
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH, null);
    }

    public TextToSpeech getTextToSpeech () {
        return textToSpeech;
    }
}
