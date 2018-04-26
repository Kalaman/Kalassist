package com.kala.kalassist.Commands;

import android.content.Context;
import android.util.Log;

import com.kala.kalassist.Currencies;
import com.kala.kalassist.JSONDownloadServices;
import com.kala.kalassist.R;
import com.kala.kalassist.SpeechCommand;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Kalaman on 21.10.17.
 */
public class CurrencyQueryCommand extends SpeechCommand{

    public CurrencyQueryCommand(Context context){
        super(context);
    }

    @Override
    public String executeCommand(String speechInput) {

        String [] splitedSpeechInput = speechInput.split(" ");

        Double moneyAmount = null;
        String baseCurrency = null;
        String toCurrency = null;

        /*
            Es werden 2 Fälle betrachtet:
                1. "Wie viel Euro sind 10 Dollar"
                2. "Wie viel sind 10 Dollar in Euro"
         */
        for (int i=0;i<splitedSpeechInput.length;++i) {

            if (splitedSpeechInput[i].matches(".*\\d+.*"))
            {
                baseCurrency = Currencies.getCurrencyShortcut(splitedSpeechInput[i+1]);

                //Enthält es Cent Werte ? Wenn ja -> Komma in Punkt ersetzen für die parseDouble Methode
                if (splitedSpeechInput[i].contains(","))
                    splitedSpeechInput[i] = splitedSpeechInput[i].replaceAll(",",".");

                moneyAmount = Double.parseDouble(splitedSpeechInput[i]);

                ++i;
                Log.d("CurrencyQueryCommand", "Found Number :" + splitedSpeechInput[i]);
            }
            else {
                if (Currencies.getCurrencyShortcut(splitedSpeechInput[i]) != null) {
                    toCurrency = Currencies.getCurrencyShortcut(splitedSpeechInput[i]);
                }
            }
        }

        Log.d("CurrencyCommand","Base:" + baseCurrency + " ToCurrency: " + toCurrency);
        double rate = JSONDownloadServices.getCurrencyRate(baseCurrency,toCurrency);

        return "Umgerechnet sind es " + (new DecimalFormat("#.00").format(rate * moneyAmount).toString().replace(".", ",")) + " " + toCurrency;
    }

    @Override
    public String [] getMasterKeywords() {
        return new String[]{"€", "$", "Euro" , "Rubel", "Pfund" , "Dollar"};
    }

    @Override
    public String[] getKeywords() {
        return new String[] {"wie viel", "dollar", "euro", "rubel" , "pfund", "pound"};
    }

    @Override
    public int getCommandPictureResourceID() {
        return R.drawable.ic_monetization_on_black_36dp;
    }
}
