package org.pwr.tirt.plangen.logic;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.pwr.tirt.plangen.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerClient {
    private static final String LOG_TAG = "Server Client";

    private static final String SERVER_URL = "http://google.com/";
    private static final String ACTION_NAME = "#q=something";

    public static boolean checkInternetConenction(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            CookieHandler.setDefault(new CookieManager());
            return true;
        } else {
            Log.e(LOG_TAG, "Not connected to Internet");
            return false;
        }
    }

    public static String getDataFromServer(){
        InputStream is = null;
        try {
            URL url = new URL(SERVER_URL + ACTION_NAME);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            is = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line, response = "";
            while ((line = reader.readLine()) != null)
                response += line + "\n";
            return response.substring(0, 50);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Downloading new app error: " + e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Constants.FAIL_MESSAGE;
    }
}
