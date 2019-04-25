/*/////////////////////////////////
 * Application: StarCalendar
 *
 * Author: Mike Vriesema 17212359
 * Date: 25/04/2019
 */////////////////////////////////
package mikevriesema.ul.starcalendar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarActivity extends MainActivity {

    CalendarView calendar;
    TextView dateplan;
    String url;
    String moon_type;
    String moon_phase;
    String dateValue;
    Date date;
    TextView close;
    TextView eventInfo;
    TextView moonPhase;
    ImageView eventImage;
    Button confirm;
    Dialog popup;
    public int day_value;
    public int month_value;
    public int year_value;

    /*
     * SOURCES:
     *  Information on calendar intents: https://stackoverflow.com/questions/3721963/how-to-add-calendar-events-in-android
     *  Moon API : http://www.farmsense.net/api/astro-widgets/
     *  Asynctask help was taken from our own example for threading and the weather one I previously made
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        moon_type = getString(R.string.moon);
        moon_phase = getString(R.string.phase);
        popup = new Dialog(this);
        calendar = (CalendarView) findViewById(R.id.calendar);
        dateplan = (TextView) findViewById(R.id.date);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) { //DETECTS NEW SELECTED DATE IN CALENDAR
            day_value = dayOfMonth;
            month_value = month;
            year_value = year;
            String[] months = getResources().getStringArray(R.array.months);
            String events = "";
            String newDate = dayOfMonth + " of " + months[month] + " " + year + "\n" + events; //DISPLAY STRING FOR THE DATE
            dateplan.setText(newDate);
            month = month+1;
            dateValue=""+day_value+"/"+month+"/"+year_value+" "+"20:00:00"; //MAKE A NEW DATE STRING
            try {
                date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateValue); //MAKES NEW DATE OBJECT USING THE DATE STRING
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String timestamp =  Long.toString(date.getTime()); //THE TIMESTAMP IS ACQUIRED FOR US IN THE API
            taskLoadUp(timestamp); //CALLS ASYNCTASK AND PASSES IN THE TIMESTAMP
            }
        });
    }
    public void createEvent(){ //CREATES THE CALENDAR INTENT TO SET AN EVENT WITH THE SELECTED VARIABLES IN THE DEFAULT CALENDAR APPLICATION
        Intent event_intent = new Intent(Intent.ACTION_INSERT);
        event_intent.setType("vnd.android.cursor.item/event");
        event_intent.putExtra(CalendarContract.Events.TITLE, moon_type);
        event_intent.putExtra(CalendarContract.Events.EVENT_LOCATION, getString(R.string.outside));
        event_intent.putExtra(CalendarContract.Events.DESCRIPTION,moon_phase);
        event_intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        event_intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                date.getTime());
        event_intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                date.getTime()+(60*60*1000*2));
        startActivity(event_intent);
    }


    public void popUpEvent(){ //THIS IS THE POPUP THAT APPEARS WHEN A DATE GETS SELECTED SHOWING MOON TYPE AND PHASE,COULD BE DONE USING FRAGMENTS
        popup.setContentView(R.layout.activity_event_info);
        close = (TextView) popup.findViewById(R.id.close);
        eventImage = (ImageView) popup.findViewById(R.id.eventimage);
        eventInfo = (TextView) popup.findViewById(R.id.eventinfo);
        moonPhase = (TextView) popup.findViewById(R.id.moonphase);
        confirm = (Button) popup.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent(); //RUNS THE CREATE EVENT INTENT
            }
        });
        eventInfo.setText(moon_type);
        moonPhase.setText(moon_phase);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        eventImage.setImageBitmap(ImageViaAssets("Moon.png"));
        popup.show(); //SHOWS THE POPUP CONTENT VIEW
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void taskLoadUp(String timestamp) {
        if (FetchData.isNetworkAvailable(getApplicationContext())){ //CHECKS CONNECTION
            CalendarActivity.DownloadMoon task = new CalendarActivity.DownloadMoon();
            task.execute(timestamp); //BEGINS FETCHING MOON DATA
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    class DownloadMoon extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            url = "http://api.farmsense.net/v1/moonphases/?d=" + args[0]; //SETS MOON API URL
            String xml = FetchData.executeGet(url); //USES SAME CONNECTION AND FETCH SYSTEM AS WEATHER BUT DIFFERENT URL TO RETRIEVE JSON DATA
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) { //RETURNED JSON DATA IN STRING FORMAT GETS PROCESSED
            try {
                JSONArray json = new JSONArray(xml); //MAKES NEW ARRAY OBJECT
                if (json != null) {
                    JSONObject jsonObj = json.getJSONObject(0); //GET THE OBJECT IN POSITION 1
                    moon_type = jsonObj.getString("Moon"); //ACQUIRE MOON TYPE
                    moon_phase = jsonObj.getString("Phase"); //ACQUIRE MOON PHASE
                    Pattern pt = Pattern.compile("[^a-zA-Z0-9]"); //THIS PATTERN IS USED TO CLEANSE THE MOON TYPE SINCE IT CAN CONTAIN SPECIAL CHARACTERS
                    Matcher match = pt.matcher(moon_type); //CHECKS TO SEE DOES MOON TYPE FIT THE MATCHING PATTERN
                    while (match.find()) {
                        moon_type = moon_type.replace(match.group(), ""); //REPLACES THE SPECIAL CHARACTERS WITH WHITE SPACE
                    }
                    popUpEvent(); //CALLS POPUP EVENT NOW THAT MOON INFORMATION HAS BEEN RETRIEVED
                }
            } catch (JSONException e) {
                e.printStackTrace(); //STACKTRACE FOR DEBUGGING <3
                Toast.makeText(getApplicationContext(), getString(R.string.moon_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
}



