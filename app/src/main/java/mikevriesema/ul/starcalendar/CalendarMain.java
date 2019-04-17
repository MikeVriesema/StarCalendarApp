package mikevriesema.ul.starcalendar;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.GregorianCalendar;

public class CalendarMain extends MainActivity {

    /*/////////////////////////////////
     * ON CREATE
     *
     */////////////////////////////////

    CalendarView calendar;
    TextView dateplan;
    TextView close;
    ImageView eventImage;
    Dialog popDialog;


    /*
     * SOURCES:
     *  Information on calendar intents : https://stackoverflow.com/questions/3721963/how-to-add-calendar-events-in-android
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        popDialog = new Dialog(this);
        calendar = (CalendarView) findViewById(R.id.calendar);
        dateplan = (TextView) findViewById(R.id.date);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String[] months = getResources().getStringArray(R.array.months);
                String events = "";
                String newDate = dayOfMonth + " of " + months[month] + " " + year + "\n" + events;
                dateplan.setText(newDate);
                createEvent(dayOfMonth,month,year);
                popUpEvent();
            }
        });
        /*

         */
    }
    public void createEvent(int dayOfMonth,int month,int year){
        Intent event_intent = new Intent(Intent.ACTION_INSERT);
        event_intent.setType("vnd.android.cursor.item/event");
        event_intent.putExtra(CalendarContract.Events.TITLE, "Mercury Retrograde"); //NEEDS TO BE MOON PHASE
        event_intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Outside");

        GregorianCalendar calDate = new GregorianCalendar(year, month, dayOfMonth);
        event_intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        event_intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
        event_intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.getTimeInMillis()+60*60*1000);
        startActivity(event_intent);
    }


    public void popUpEvent(){
        popDialog.setContentView(R.layout.activity_event_info);
        close = (TextView) popDialog.findViewById(R.id.close);
        eventImage = (ImageView) popDialog.findViewById(R.id.eventimage);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popDialog.dismiss();
            }
        });
        popDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        eventImage.setImageBitmap(ImageViaAssets("event_image.png"));
        popDialog.show();
    }
}



