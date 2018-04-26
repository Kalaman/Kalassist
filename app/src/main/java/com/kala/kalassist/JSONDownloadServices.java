package com.kala.kalassist;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Kalaman on 21.10.17.
 */
public class JSONDownloadServices {

    private static Response response;
    private static final String WEATHER_API_KEY = "f96dfbd026427609ba4ba3c73fb1c4a2";

    private static JSONObject getJSONObjectFromURL(String url) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e("Exception", e.getLocalizedMessage());
        }
        return null;
    }

    private static JSONArray getJSONArrayFromURL(String url) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            return new JSONArray(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e("", "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static double getCurrencyRate(String baseCurrency, String currency)
    {
        try {
//            JSONObject jsonObject = new AsyncJSONObjectDownloader().execute("http://api.fixer.io/latest?base=" + baseCurrency).get();
            JSONObject jsonObject = getJSONObjectFromURL("http://api.fixer.io/latest?base=" + baseCurrency);
            JSONObject jsonRates = jsonObject.getJSONObject("rates");

            return jsonRates.getDouble(currency);
        } catch (Exception e) {
            Log.d("Currency","Failed to download Currency rate");
            e.printStackTrace();
        }
        return -1;
    }

    public static Double getWeatherInfo (String cityName) {
        //ö und ä umgehen, da die openWeaterAPI damit nicht klar kommt.
        cityName = cityName.replaceAll("ö","oe").replaceAll("ä","ae");

        try {
//            JSONObject completeJSONObject = new AsyncJSONObjectDownloader().execute("http://api.openweathermap.org/data/2.5/weather?q="
//                                                                        + URLEncoder.encode(cityName,"UTF-8")
//                                                                            + "&appid="
//                                                                                + WEATHER_API_KEY
//                                                                                    + "&units=metric").get();
            JSONObject completeJSONObject = JSONDownloadServices.getJSONObjectFromURL("http://api.openweathermap.org/data/2.5/weather?q="
                                                + URLEncoder.encode(cityName,"UTF-8")
                                                    + "&appid="
                                                        + WEATHER_API_KEY
                                                            + "&units=metric");

            JSONObject mainJSONObject = completeJSONObject.getJSONObject("main");
            return mainJSONObject.getDouble("temp");

        } catch (Exception e) {
            Log.d("Weater","Failed to download Weather Informations");
            e.printStackTrace();
        }

        return null;
    }

    public static String getWikiInfoExtracts(String searchWord)
    {
        try
        {
            searchWord = Utilities.capFirstCharOfWords(searchWord);
//            JSONObject jsonObject = new AsyncJSONObjectDownloader().execute("https://de.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=" + URLEncoder.encode(searchWord,"UTF-8")).get();
            JSONObject jsonObject = JSONDownloadServices.getJSONObjectFromURL("https://de.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles="
                                                                                + URLEncoder.encode(searchWord,"UTF-8"));
            JSONObject queryJSONObject = jsonObject.getJSONObject("query");
            JSONObject pagesJSONObject = queryJSONObject.getJSONObject("pages");

            JSONObject resultJSONObject = pagesJSONObject.getJSONObject(pagesJSONObject.keys().next());

            String infoStr = resultJSONObject.getString("extract");

            return infoStr.equals("") ? null : infoStr;
        }
        catch (Exception e){

        }
        return null;
    }

    public static String getWikiInfo (String searchWord)
    {
        try
        {
//            JSONArray jsonArray = new AsyncJSONArrayDownloader().execute("https://de.wikipedia.org/w/api.php?action=opensearch&search="+ URLEncoder.encode(searchWord,"UTF-8") +"&limit=1&namespace=0&format=json").get();
            JSONArray jsonArray = JSONDownloadServices.getJSONArrayFromURL("https://de.wikipedia.org/w/api.php?action=opensearch&search="+ URLEncoder.encode(searchWord,"UTF-8") +"&limit=1&namespace=0&format=json");
            JSONArray jsonInfo = jsonArray.getJSONArray(2);

            String infoStr = jsonInfo.getString(0);

            return infoStr.equals("") ? null : infoStr;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

//    private static class AsyncJSONObjectDownloader extends AsyncTask<String,Void,JSONObject>
//    {
//        @Override
//        protected JSONObject doInBackground(String... params) {
//            JSONObject jsonObject = JSONDownloadServices.getJSONObjectFromURL(params[0]);
//
//            return jsonObject;
//        }
//    }
//
//    private static class AsyncJSONArrayDownloader extends AsyncTask<String,Void,JSONArray>
//    {
//        @Override
//        protected JSONArray doInBackground(String... params) {
//            JSONArray jsonObject = JSONDownloadServices.getJSONArrayFromURL(params[0]);
//
//            return jsonObject;
//        }
//    }
}
