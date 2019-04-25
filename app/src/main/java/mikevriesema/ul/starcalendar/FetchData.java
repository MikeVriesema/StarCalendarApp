/*/////////////////////////////////
 * Application: StarCalendar
 *
 * Author: Mike Vriesema 17212359
 * Date: 25/04/2019
 */////////////////////////////////
package mikevriesema.ul.starcalendar;

import android.content.Context;
import android.net.ConnectivityManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class FetchData {

    /*
     * SOURCE:
     * Fetch method = https://androstock.com/tutorials/create-a-weather-app-on-android-android-studio.html
     * This was adjusted to be a generalised method for retrieving json data from a url API
     */
    public static boolean isNetworkAvailable(Context context)
    {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }


    public static String executeGet(String targetURL)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //CREATES A NEW HTTP CONNECTION TO THE PASSED IN URL
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("content-type", "application/json;  charset=utf-8");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches (false);
            connection.setDoInput(true); //ONLY TAKING INPUT
            connection.setDoOutput(false); //DO NOT NEED TO WRITE TO A SERVER

            InputStream is; //NEW INPUT STREAM GETS CREATED
            int status = connection.getResponseCode(); //CHECK IF THE CONNECTION IS ESTABLISHED SUCCESSFULLY
            if (status != HttpURLConnection.HTTP_OK)
                is = connection.getErrorStream();
            else
                is = connection.getInputStream(); //IF IT IS WE BEGIN PULLING THE DATA INTO THE INPUT STREAM
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while((line = rd.readLine()) != null) { //THE BUFFERED READER ADDS THE DATA TO THE END OF THE RESPONSE STRINGBUILDER
                response.append(line);
                response.append('\r');
            }
            rd.close(); //GOTTA CLOSE THEM READERS ELSE THE OPEN CONNECTIONS USE RESOURCES
            return response.toString(); //CONVERT RESPONSE BACK TO STRING AND RETURN IT FOR PROCESSING
        } catch (Exception e) {
            e.printStackTrace(); //DEBUGGING WITH THAT STACKTRACE
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect(); //GOTTA CLOSE THEM READERS ELSE THE OPEN CONNECTIONS USE RESOURCES
            }
        }
    }


    /*
     * WEATHER SPECIFIC STUFF HERE,USES THE ICONS IN THE FONT FILE
     */
    public static String setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = "&#xf00d;";
            } else {
                icon = "&#xf02e;";
            }
        } else {
            switch(id) {
                case 2 : icon = "&#xf01e;";
                    break;
                case 3 : icon = "&#xf01c;";
                    break;
                case 7 : icon = "&#xf014;";
                    break;
                case 8 : icon = "&#xf013;";
                    break;
                case 6 : icon = "&#xf01b;";
                    break;
                case 5 : icon = "&#xf019;";
                    break;
            }
        }
        return icon;
    }
}

