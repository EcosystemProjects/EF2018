package com.mentornity.ecosytemfeed.jsonConnection;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchData extends AsyncTask<Void,Void,Void>{
    public String data ="";
    private String url;
    private String TAG="FetchData";
    public Boolean fetched = false;
    public Boolean errorOccured = false;

    public FetchData(String url) {
        this.url = url;
    }

    public Boolean getFetched() {
        return fetched;
    }
    private String getUrl() {
        return url;
    }
    public String getData()
    {
        return data;
    }
    public Boolean getErrorOccured() {
        return errorOccured;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: data fetching");
        try {
            URL url = new URL(getUrl());
            Log.d(TAG, "doInBackground: url taken");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            if(httpURLConnection.getResponseCode() == 200){
                System.out.println("Page is ok!");
            }
            else{
                errorOccured = true;
                Log.d(TAG, "doInBackground: error occured");
                System.out.println("Page not found 404 /unauthorized 401 ");
            }
            Log.d(TAG, "doInBackground: connection opened");
            Log.d(TAG, "doInBackground: input streming");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            Log.d(TAG, "doInBackground: nuffering");
            while(line != null){
                data = String.format("%s%s", data, line);
                line = bufferedReader.readLine();
            }
            Log.d(TAG, "doInBackground: buffered.fetched=true");
            fetched=true;
        } catch (MalformedURLException e) {
            errorOccured = true;
            Log.d(TAG, "doInBackground: error occured");
            Log.d(TAG, "doInBackground: MalformedURLException");
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            errorOccured = true;
            Log.d(TAG, "doInBackground: error occured");
            Log.d(TAG, "doInBackground: IOException");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        fetched=true;
        Log.d(TAG, "onPostExecute: fetched=true");
    }
}
