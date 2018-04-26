package com.kala.kalassist.Commands;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.widget.ArrayAdapter;

import com.kala.kalassist.Contact;
import com.kala.kalassist.R;
import com.kala.kalassist.SpeechCommand;

import java.util.ArrayList;

/**
 * Created by Kalaman on 16.11.2017.
 */

public class CallCommand extends SpeechCommand {

    private static ArrayList<Contact> contacts;

    public CallCommand(Context context) {
        super(context);

        contacts = new ArrayList<>();
        readContacts(context.getContentResolver());
    }

    public void readContacts(ContentResolver contentResolver) {
        Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(new Contact(name, phoneNumber));
        }

        phones.close();
    }

    @Override
    public String executeCommand(String speechInput) {
        String[] splitedSpeechInput = speechInput.split(" ");
        String targetName = "";

        if (splitedSpeechInput[0].contains("ruf")
                && splitedSpeechInput[splitedSpeechInput.length - 1].equalsIgnoreCase("an")) {
            for (int i = 1; i < splitedSpeechInput.length - 1; ++i) {
                targetName += splitedSpeechInput[i] + " ";
            }
            targetName = targetName.trim();

            for (Contact contact : contacts) {
                if (contact.getName().equalsIgnoreCase(targetName)) {
                    String uri = "tel:" + contact.getPhoneNumber().trim();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));

                    context.startActivity(intent);

                    return getConfirmationAnswer();
                }
            }
        }

        return "Ich konnte " + targetName + " in deinem Telefonbuch nicht finden.";
    }

    @Override
    public String[] getMasterKeywords() {
        return new String[] { "anrufen", "rufe" , "an", "ruf" };
    }

    @Override
    public String[] getKeywords() {
        return new String[] { "rufe", "ruf", "anrufen", "bitte"};
    }

    @Override
    public int getCommandPictureResourceID() {
        return R.drawable.ic_phone_black_36dp;
    }

}
