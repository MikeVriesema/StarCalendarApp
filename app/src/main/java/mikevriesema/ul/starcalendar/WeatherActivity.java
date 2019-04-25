/*/////////////////////////////////
 * Application: StarCalendar
 *
 * Author: Mike Vriesema 17212359
 * Date: 25/04/2019
 */////////////////////////////////
package mikevriesema.ul.starcalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherActivity extends MainActivity {

    /*
     * SOURCE:
     * WeatherActivity info = https://androstock.com/tutorials/create-a-weather-app-on-android-android-studio.html
     */
    TextView selectCity, cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField, pos,  wind_speed, sunrise , sunset;
    ProgressBar loader;
    Typeface weatherFont;
    String city;
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
        wind_speed = (TextView) findViewById(R.id.wind_speed);
        sunrise = (TextView) findViewById(R.id.sunrise);
        sunset = (TextView) findViewById(R.id.sunset);
        pos = (TextView) findViewById(R.id.pos);
        weatherIcon = (TextView) findViewById(R.id.weather_icon);
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather_icons.ttf");
        weatherIcon.setTypeface(weatherFont);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        city = prefs.getString(AppPreferences.KEY_CITY,getString(R.string.city_default));
        taskLoadUp(city);

        selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //LISTENER TO CHANGE CITY
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(WeatherActivity.this);
                alertDialog.setTitle(getString(R.string.change_city));
                final EditText input = new EditText(WeatherActivity.this);
                input.setText(city);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setPositiveButton(getString(R.string.change),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                city = input.getText().toString(); //UPDATES SHARED PREFERENCE FOR THE CITY VALUE AND RELAUNCHES THE WEATHER TASK
                                SharedPreferences.Editor edit;
                                edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                                edit.putString(AppPreferences.KEY_CITY,city);
                                edit.apply();
                                taskLoadUp(city);
                            }
                        });
                alertDialog.setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        taskLoadUp(city); //STARTS TASK TO FETCH WEATHER FOR PASSED IN CITY NAME
    }

    public void taskLoadUp(String query) {
        if (FetchData.isNetworkAvailable(getApplicationContext())) { //CHECKS CONNECTION
            DownloadWeather task = new DownloadWeather();
            task.execute(query);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    class DownloadWeather extends AsyncTask < String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
        }
        protected String doInBackground(String...args) {
            url = "http://api.openweathermap.org/data/2.5/weather?q=" + args[0] + "&units=metric&appid=" + getString(R.string.api_key);
            String xml = FetchData.executeGet(url); //USES CONNECTION AND FETCH SYSTEM WITH URL TO RETRIEVE JSON DATA
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) { //PROCESSING THE WEATHER DATA
            try {
                JSONObject json = new JSONObject(xml); //MAKE A NEW JSON OBJECT FROM THE STRING
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                    //RETRIEVE THE INDIVIDUAL FIELDS FROM THE JSON OBJECT AND UPDATE THE TEXTVIEWS
                    cityField.setText(json.getString("name").toUpperCase(Locale.US)+", "+json.getJSONObject("sys").getString("country"));
                    detailsField.setText(details.getString("description").toUpperCase(Locale.US));
                    currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp"))+"°C");
                    sunrise.setText(getString(R.string.sunrise)+sdf.format(new Date(json.getJSONObject("sys").getLong("sunrise") * 1000)));
                    sunset.setText(getString(R.string.sunset)+sdf.format(new Date(json.getJSONObject("sys").getLong("sunset") * 1000)));
                    humidity_field.setText(getString(R.string.humidity)+main.getString("humidity")+"%");
                    pressure_field.setText(getString(R.string.pressure)+main.getString("pressure")+" hPa");
                    wind_speed.setText(getString(R.string.wind)+ json.getJSONObject("wind").getString("speed")+" km " + fetchDirection(json.getJSONObject("wind").getString("deg")));
                    pos.setText("Lon: "+json.getJSONObject("coord").getString("lon")+" Lat: "+json.getJSONObject("coord").getString("lat"));
                    updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));
                    weatherIcon.setText(Html.fromHtml(FetchData.setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000)));

                    loader.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.city_error), Toast.LENGTH_SHORT).show();
                taskLoadUp(getString(R.string.city_default)); //IF THE CITY FAILS AND NO WEATHER DATA IS FOUND IT RUNS THE DEFAULT WEATHER STRING
            }
        }

        public String fetchDirection(String degrees){ //I HAD TO CREATE THIS METHOD TO GET THE DIRECTION FROM THE DEGREES ASSOCIATED WITH THE WIND DATA
            String direction = ""; //I WANTED TO USE SWITCH CASES BUT SINCE THE DEGREES IS NOT CONSISTENT AND IS BETWEEN A RANGE IT WOULDN'T WORK
            int degree = Integer.parseInt(degrees);
            if((degree >= 0 && degree < 23) || (degree >= 337 && degree <= 360)){ //360/0 N
                direction = "N";
            }else if(degree >= 23 && degree < 68){ //45 NE
                direction = "NE";
            }else if(degree >= 68 && degree < 112){ //90 E
                direction = "E";
            }else if(degree >= 112 && degree < 158){ //135 SE
                direction = "SE";
            }else if(degree >= 158 && degree < 203){ //180 S
                direction = "S";
            }else if(degree >= 203 && degree < 248){ //225 SW
                direction = "SW";
            }else if(degree >= 248 && degree < 293){ //270 W
                direction = "W";
            }else if(degree >= 293 && degree < 337){ //315 NW
                direction = "NW";
            }
            return direction;
        }
    }
}
