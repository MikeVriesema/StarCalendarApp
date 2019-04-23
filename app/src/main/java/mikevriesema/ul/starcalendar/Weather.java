package mikevriesema.ul.starcalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Weather extends MainActivity {

    /*
     * SOURCE:
     * Weather info and remote fetch = https://androstock.com/tutorials/create-a-weather-app-on-android-android-studio.html
     *
     */
    TextView selectCity, cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;
    ProgressBar loader;
    Typeface weatherFont;
    String city = "Dublin, IE";
    String OPEN_WEATHER_MAP_API = "27bba67d4f9f2258695c40a1e74c16da";
    double lat,lon;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_information);

        loader = (ProgressBar) findViewById(R.id.loader);
        selectCity = (TextView) findViewById(R.id.selectCity);
        cityField = (TextView) findViewById(R.id.city_field);
        updatedField = (TextView) findViewById(R.id.updated_field);
        detailsField = (TextView) findViewById(R.id.details_field);
        currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
        humidity_field = (TextView) findViewById(R.id.humidity_field);
        pressure_field = (TextView) findViewById(R.id.pressure_field);
        weatherIcon = (TextView) findViewById(R.id.weather_icon);
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather_icons.ttf");
        weatherIcon.setTypeface(weatherFont);

        taskLoadUp(city);

        selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Weather.this);
                alertDialog.setTitle("Change City");
                final EditText input = new EditText(Weather.this);
                input.setText(city);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Change",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                city = input.getText().toString();
                                taskLoadUp(city);
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });
    }


    public void taskLoadUp(String query) {
        if (FetchWeather.isNetworkAvailable(getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(query);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }



    class DownloadWeather extends AsyncTask < String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);

        }
        protected String doInBackground(String...args) {
            GPSTracker gpsTracker = new GPSTracker(this);
            if (gpsTracker.getIsGPSTrackingEnabled())
            {
                String stringLatitude = String.valueOf(gpsTracker.latitude);
                textview = (TextView)findViewById(R.id.fieldLatitude);
                textview.setText(stringLatitude);

                String stringLongitude = String.valueOf(gpsTracker.longitude);
                textview = (TextView)findViewById(R.id.fieldLongitude);
                textview.setText(stringLongitude);

                String country = gpsTracker.getCountryName(this);
                textview = (TextView)findViewById(R.id.fieldCountry);
                textview.setText(country);

                String city = gpsTracker.getLocality(this);
                textview = (TextView)findViewById(R.id.fieldCity);
                textview.setText(city);

                String postalCode = gpsTracker.getPostalCode(this);
                textview = (TextView)findViewById(R.id.fieldPostalCode);
                textview.setText(postalCode);

                String addressLine = gpsTracker.getAddressLine(this);
                textview = (TextView)findViewById(R.id.fieldAddressLine);
                textview.setText(addressLine);
            }
            else
            {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gpsTracker.showSettingsAlert();
            }
            if(lat == 0.0 && lon == 0.0){
                url = String.format("api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid="+OPEN_WEATHER_MAP_API,lat,lon);
            }else{
                url = "http://api.openweathermap.org/data/2.5/weather?q=" + args[0] + "&units=metric&appid=" + OPEN_WEATHER_MAP_API;
            }
            String xml = FetchWeather.excuteGet(url);
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {
            try {
                JSONObject json = new JSONObject(xml);

                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();

                    cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));
                    detailsField.setText(details.getString("description").toUpperCase(Locale.US));
                    currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + "°");
                    humidity_field.setText("Humidity: " + main.getString("humidity") + "%");
                    pressure_field.setText("Pressure: " + main.getString("pressure") + " hPa");
                    updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));
                    weatherIcon.setText(Html.fromHtml(FetchWeather.setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000)));

                    loader.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
