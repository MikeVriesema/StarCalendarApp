/*/////////////////////////////////
* Application: StarCalendar
*
* Author: Mike Vriesema 17212359
* Date: 26/02/2019
*/////////////////////////////////
package mikevriesema.ul.starcalendar;


import android.app.ActionBar;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

    Button information;
    Button calendar;
    Button weather;
    Button compass;

    /*
     * SOURCES:
     * Toolbar back button: https://stackoverflow.com/questions/34110565/how-to-add-back-button-on-actionbar-in-android-studio
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        information = (Button) findViewById(R.id.information);
        calendar = (Button) findViewById(R.id.myCalendar);
        weather = (Button) findViewById(R.id.weather);
        compass = (Button) findViewById(R.id.compass);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent weatherIntent = new Intent(getBaseContext(), Weather.class);
                startActivity(weatherIntent);
            }
        });
        compass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent compassIntent = new Intent(getBaseContext(), Compass.class);
                startActivity(compassIntent);
            }
        });
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(getBaseContext(), Information.class);
                startActivity(infoIntent);
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calendarIntent = new Intent(getBaseContext(), CalendarMain.class);
                startActivity(calendarIntent);
            }
        });

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
                Intent preferencesIntent = new Intent(getBaseContext(), preferences.class);
                startActivity(preferencesIntent);
                break;
            }
            case R.id.help: {
                Intent aboutIntent = new Intent(getBaseContext(), About.class);
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






}
