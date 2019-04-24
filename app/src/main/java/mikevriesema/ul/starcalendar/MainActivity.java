/*/////////////////////////////////
* Application: StarCalendar
*
* Author: Mike Vriesema 17212359
* Date: 24/04/2019
*/////////////////////////////////
package mikevriesema.ul.starcalendar;


import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

    Button information;
    Button calendar;
    Button weather;
    Button compass;
    TextView name_view;
    SharedPreferences myPrefs;
    SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    /*
     * SOURCES:
     * Toolbar back button: https://stackoverflow.com/questions/34110565/how-to-add-back-button-on-actionbar-in-android-studio
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        information = (Button) findViewById(R.id.information);
        calendar = (Button) findViewById(R.id.myCalendar);
        weather = (Button) findViewById(R.id.weather);
        compass = (Button) findViewById(R.id.compass);
        name_view = (TextView) findViewById(R.id.name);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent weatherIntent = new Intent(getBaseContext(), WeatherActivity.class);
                startActivity(weatherIntent);
            }
        });
        compass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent compassIntent = new Intent(getBaseContext(), CompassActivity.class);
                startActivity(compassIntent);
            }
        });
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(getBaseContext(), InformationActivity.class);
                startActivity(infoIntent);
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calendarIntent = new Intent(getBaseContext(), CalendarActivity.class);
                startActivity(calendarIntent);
            }
        });


        myPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        updateUIFromPreferences(myPrefs);
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                updateUIFromPreferences(prefs);
            }
        };
        myPrefs.registerOnSharedPreferenceChangeListener(prefListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings: {
                Intent preferencesIntent = new Intent(getBaseContext(), AppPreferences.class);
                startActivity(preferencesIntent);
                break;
            }
            case R.id.help: {
                Intent aboutIntent = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(aboutIntent);
                break;
            }
            case android.R.id.home:
                //app icon in action bar clicked; go to homepage
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Bitmap ImageViaAssets(String fileName){

        AssetManager assetmanager = getAssets();
        InputStream is = null;
        try{

            is = assetmanager.open(fileName);
        }catch(IOException e){
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    private void updateUIFromPreferences(SharedPreferences prefs) {
        String name = prefs.getString(AppPreferences.KEY_USER,"");
        if (!name.contentEquals("")) {
            name_view.setText(name+"'s");
        }
    }

    public void SavePreferences(SharedPreferences prefs){

    }
}
