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

    public Boolean getFetched() {
        return fetched;
    }

    public  Boolean fetched=false;
    public FetchData(String url) {
        this.url = url;
    }
    private String getUrl() {
        return url;
    }
    public String getData()
    {
        return data;
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
