package com.kala.kalassist;

/**
 * Enthält die Währungskonstanten
 * Created by Kalaman on 21.10.17.
 */
public class Currencies {
    public static final String [] EURO = {"EUR","Euro"};
    public static final String [] US_DOLLAR = {"USD","Dollar"};
    public static final String [] BRITISH_POUND = {"GBP","Pound"};
    public static final String [] RUSSIAN_RUBLE = {"RUB","Rubel"};
    public static final String [] TURKISH_LIRA = {"TRY","Lira"};

    /**
     * Findet den Währungskürzel aus einem String
     * @param currency
     * @return
     */
    public static String getCurrencyShortcut (String currency) {
        String shortcut;

        switch (currency.toLowerCase()) {
            case "euro":
                shortcut = EURO[0];
                break;
            case "€":
                shortcut = EURO[0];
                break;
            case "dollar":
                shortcut = US_DOLLAR[0];
                break;
            case "$":
                shortcut = US_DOLLAR[0];
                break;
            case "us-dollar":
                shortcut = US_DOLLAR[0];
                break;
            case "pound":
                shortcut = BRITISH_POUND[0];
                break;
            case "pfund":
                shortcut = BRITISH_POUND[0];
                break;
            case "rubel":
                shortcut = RUSSIAN_RUBLE[0];
                break;
            case "lira":
                shortcut = TURKISH_LIRA[0];
                break;
            case "türkische":
                shortcut = TURKISH_LIRA[0];
                break;
            default:
                shortcut = null;
        }
        return shortcut;
    }
}
