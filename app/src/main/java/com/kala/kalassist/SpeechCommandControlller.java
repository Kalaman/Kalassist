package com.kala.kalassist;

import android.content.Context;
import android.os.AsyncTask;

import com.kala.kalassist.Commands.AlarmQueryCommand;
import com.kala.kalassist.Commands.CallCommand;
import com.kala.kalassist.Commands.CommunityCommand;
import com.kala.kalassist.Commands.CurrencyQueryCommand;
import com.kala.kalassist.Commands.DateQueryCommand;
import com.kala.kalassist.Commands.InternetSearchCommand;
import com.kala.kalassist.Commands.JokeCommand;
import com.kala.kalassist.Commands.LearnCommand;
import com.kala.kalassist.Commands.TimeQueryCommand;
import com.kala.kalassist.Commands.DefaultCommand;
import com.kala.kalassist.Commands.WeatherCommand;
import com.kala.kalassist.Commands.WikiQueryCommand;

/**
 * Diese Klasse hilft bei der Auswahl des richtigen SpeechCommands.
 *
 * Created by Kalaman on 19.10.17.
 */
public class SpeechCommandControlller {

    private SpeechCommand [] commands;
    private SpeechCommand lastSpeechCommand;
    public SpeechCommand.CommandListener commandListener;

    public static final int statusStandby = 0;
    public static final int statusListening = 1;
    public static final int statusProcessing = 2;

    //DIALOG_MODE wird auf true gesetzt, wenn noch auf eine Antwort
    //vom vohrherigen Command gewartet wird.
    public static boolean DIALOG_MODE = false;


    public SpeechCommandControlller (Context context) {
        commands = new SpeechCommand[]{
                new CommunityCommand(context),
                new DefaultCommand(context),
                new TimeQueryCommand(context),
                new DateQueryCommand(context),
                new CurrencyQueryCommand(context),
                new AlarmQueryCommand(context),
                new WeatherCommand(context),
                new InternetSearchCommand(context),
                new WikiQueryCommand(context),
                new JokeCommand(context),
                new CallCommand(context),
                new LearnCommand(context)
        };
    }

    /**
     * Findet den passenden Befehl.
     * @param speechInput
     * @return
     */
    private SpeechCommand getMatchingCommand (String speechInput)
    {
        if (DIALOG_MODE == true)
            return lastSpeechCommand;

        SpeechCommand foundCommand = commands[0];
        int keywordMatchCount = foundCommand.getMatchCount(speechInput);

        for (SpeechCommand currentCommand : commands)
        {
            //Wenn das masterKeyword nicht im String auftaucht, kann es übersprungen werden
            //Somit ist die matchCount nicht wichtig
            if (currentCommand.masterKeywordMatch(speechInput)) {
                int currentMatchCount = currentCommand.getMatchCount(speechInput);

                if (currentMatchCount > keywordMatchCount ||
                        (currentMatchCount == keywordMatchCount
                                && currentCommand.priority >= foundCommand.priority)
                                && currentMatchCount != 0)
                {
                    foundCommand = currentCommand;
                    keywordMatchCount = currentMatchCount;
                }
            }
        }

        return foundCommand;
    }

    public void processCommand (final String speechInput) {
        SpeechDialog speechDialog;

        final SpeechCommand command = getMatchingCommand(speechInput);
        this.lastSpeechCommand = command;

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                String answer = command.executeCommand(speechInput);
//                final SpeechDialog speechDialog = new SpeechDialog(speechInput,answer,command);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        commandListener.onCommandProcessed(speechDialog);
//                    }
//                });
//            }
//        }.start();

        new AsyncTask<Void,Void,SpeechDialog>(){
            @Override
            protected SpeechDialog doInBackground(Void... voids) {
                String answer = command.executeCommand(speechInput);
                SpeechDialog speechDialog;

                //Wenn es sich um einen CommunityCommand handelt und es keine passende Antwort
                //auf die Frage gibt, soll ein DefaultCommand (commands[1]) genutzt werden.
                if (command instanceof CommunityCommand && answer == null) {
                    answer = commands[1].executeCommand(speechInput);
                    speechDialog = new SpeechDialog(speechInput,answer,commands[1]);
                } else if (command instanceof CommunityCommand) {
                    speechDialog = new CommunityDialog(speechInput,answer,command,
                            ((CommunityCommand) command).getLastCommunityDialog().getAuthor(),
                            ((CommunityCommand) command).getLastCommunityDialog().getTpid());
                }
                else {
                    speechDialog = new SpeechDialog(speechInput,answer,command);
                }

                return speechDialog;
            }

            @Override
            protected void onPostExecute(SpeechDialog dialog) {
                commandListener.onCommandProcessed(dialog);
            }
        }.execute();
    }

    public void setCommandListener (SpeechCommand.CommandListener listener) {
        commandListener = listener;
    }

}
