package com.kala.kalassist;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import android.util.Log;

import com.kala.kalassist.Commands.CommunityCommand;
import com.kala.kalassist.Commands.DefaultCommand;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;

/**
 * Created by Kalaman on 30.12.2017.
 */

public class DatabaseActions {

    private static final String DATABASE_IP = "192.168.0.136";
    //private static final String DATABASE_IP = "192.168.43.246";
    private static final String DATABASE_URL_REGISTER = "http://"+ DATABASE_IP + "/kalassist/db/db_register.php";
    private static final String DATABASE_URL_LOGIN = "http://"+ DATABASE_IP + "/kalassist/db/db_login.php";
    private static final String DATABASE_URL_SAVE_QUERIE = "http://"+ DATABASE_IP + "/kalassist/db/db_save_querie.php";
    private static final String DATABASE_URL_SAVE_PHRASE = "http://"+ DATABASE_IP + "/kalassist/db/db_save_phrase.php";
    private static final String DATABASE_URL_GET_PHRASE = "http://"+ DATABASE_IP + "/kalassist/db/db_get_phrase.php";
    private static final String DATABASE_URL_REPORT_PHRASE = "http://"+ DATABASE_IP + "/kalassist/db/db_report.php";
    private static final String REQUEST_ERROR = "error";

    private static ArrayList<DBRequestListener> listeners = new ArrayList<>();

    /**
     * Registriert einen neuen
     * Account
     * @param email
     * @param username
     * @param password
     * @param context
     * @return true wenn die registrierung erfolgreich war.
     */
    public static void registerAccount (String email, String username,String password,Context context, DBRequestListener listener) {
        String [] paramsKey = new String[] {"email","username","password"};
        String [] paramsValue = new String[] {email,username,password};

        listeners.add(listener);
        new DBAsyncRequest(context,"Registrieren ...",DATABASE_URL_REGISTER).execute(paramsKey,paramsValue);
    }

    public static void loginAccount (String username,String password,Context context, DBRequestListener listener) {
        String [] paramsKey = new String[] {"username","password"};
        String [] paramsValue = new String[] {username,password};

        listeners.add(listener);
        new DBAsyncRequest(context,"Login ...",DATABASE_URL_LOGIN).execute(paramsKey,paramsValue);
    }

    public static boolean savePhrase (String question, String answer, Context context) {
        String [] paramsKey = new String[] {"username","question","answer"};
        String [] paramsValue = new String[] {LoginActivity.savedUsername,question.toLowerCase(),answer};

        try {
            String resp = new DBAsyncRequest(context,null,DATABASE_URL_SAVE_PHRASE).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,paramsKey,paramsValue).get();
            if (resp.equals(REQUEST_ERROR) || resp.equals("PHRASE NOT LEARNED"))
                return false;
            else
                return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void reportPhrase (String tpid,Context context, DBRequestListener listener) {
        String [] paramsKey = new String[] {"username","tpid"};
        String [] paramsValue = new String[] {LoginActivity.savedUsername,tpid};

        listeners.add(listener);
        new DBAsyncRequest(context,null,DATABASE_URL_REPORT_PHRASE).execute(paramsKey,paramsValue);
    }


    public static CommunityDialog getPhrase (String question, Context context) {
        String [] paramsKey = new String[] {"question"};
        String [] paramsValue = new String[] {question};

        try {
            String resp = new DBAsyncRequest(context,null,DATABASE_URL_GET_PHRASE).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,paramsKey,paramsValue).get();

            if (!resp.equals(REQUEST_ERROR) || !resp.equals("NO_RESULT")) {
                JSONObject jsonObject = new JSONObject(resp);
                CommunityDialog speechDialog = new CommunityDialog(jsonObject.getString("Question"),jsonObject.getString("Answer"), new CommunityCommand(context),jsonObject.getString("Creator"),jsonObject.getString("TPID"));
                return speechDialog;
            }
            else
                return null;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public interface DBRequestListener {
        public void onDBRequestFinished(String response);
    }

    /**
     * Führt HTTP Requests aus einem anderen Thread aus, womit das UI-Thread ungestört bleibt.
     * Hier können Request aus jeder Art gebaut werden ohne Code umzuschreiben.
     *
     * Hauptsächlich wird diese Klasse genutzt um eine Verbindungsschnittstelle mit dem MYSQL-Server zu realisieren
     */
    private static class DBAsyncRequest extends AsyncTask<String [],String,String>
    {
        Context context;
        ProgressDialog progressDialog;
        String message;
        String strURL;
        HttpURLConnection httpConnection;

        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;

        /**
         * @param context
         * @param pdMessage Wenn pdMessage NULL ist wird kein ProgressDialog gezeigt.
         */
        public DBAsyncRequest(Context context,String pdMessage, String reqUrl) {
            this.context = context;

            if (pdMessage != null) {
                progressDialog = new ProgressDialog(context);
                message = pdMessage;
            }
            strURL = reqUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (progressDialog != null) {
                progressDialog.setMessage(message);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        /**
         * Hier passiert der eigentliche Request in einem Nebenthread
         *
         * @param params Hier müssen 2 String Arrays übergeben werden.
         *               1. paramKey Array - Entält die Parameter Keywords
         *               2. paramValue Array - Enthält die passende Values zu den Keywords
         *
         *               WICHTIG: Die Indexe der beiden Arrays müssen in der gleichen Form sein.
         *                        Heißt: paramKey[0] besitzt den Value paramValue[0]
         *                               paramKey[1] besitzt den Value paramValue[1]
         *                               ...
         * @return
         */
        @Override
        protected String doInBackground(String[]... params) {
            try {
                URL url = new URL(strURL);

                // HttpURLConnection wird hier für die Anfrage eingestellt
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setReadTimeout(READ_TIMEOUT);
                httpConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                httpConnection.setRequestMethod("POST");
                httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                httpConnection.setRequestProperty("Accept-Language", "UTF-8");
                httpConnection.setRequestProperty( "charset", "utf-8");

                //Die beiden Arrays werden aus den Parametern entnommen.
                //Mehr dazu im obigen Kommentar
                String [] paramKeys = params[0];
                String [] paramValues = params[1];

                //Hier werden die Key/Value Pairs zusammengebaut
                StringBuilder tokenUri=new StringBuilder(paramKeys[0]+ "=");
                tokenUri.append(URLEncoder.encode(paramValues[0],"UTF-8"));

                for (int i=1;i<paramKeys.length;++i) {
                    tokenUri.append("&"+paramKeys[i]+"=");
                    tokenUri.append(URLEncoder.encode(paramValues[i],"UTF-8"));
                }

                // Wird auf true gesetzt damit wir auch eine Rückgabe vom Server kriegen
                httpConnection.setDoOutput(true);

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpConnection.getOutputStream());
                outputStreamWriter.write(tokenUri.toString());
                outputStreamWriter.flush();

                Thread.sleep(700);

                int response_code = httpConnection.getResponseCode();

                // Überprüfe ob die Anfrage erfolgreich war
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Die Antwort vom Server auslesen
                    InputStream input = httpConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return (result.toString());

                } else {
                    //Die Http-Anfrage war nicht erfolgreich
                    return REQUEST_ERROR;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return REQUEST_ERROR;
            } finally {
                httpConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Log.d("DB",response);

            if (progressDialog != null)
                progressDialog.dismiss();

            //Den Listener eine Antwort geben, und ihn aus dem Array entnehmen
            if (listeners.size() > 0) {
                if (listeners.get(0) != null)
                    listeners.get(0).onDBRequestFinished(response);
                listeners.remove(0);
            }
        }
    }
}
