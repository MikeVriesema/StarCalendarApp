package mikevriesema.ul.starcalendar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Calendar extends Activity {

    /*/////////////////////////////////
     * ON CREATE
     *
     */////////////////////////////////

    CalendarView calendar;
    TextView dateplan;
    TextView close;
    Dialog popDialog;

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

                popUpEvent();
            }
        });
        /*
        * Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("allDay", true);
            intent.putExtra("rrule", "FREQ=YEARLY");
            intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
            intent.putExtra("title", "A Test Event from android app");
            startActivity(intent);
         */
    }


    public void popUpEvent(){
        popDialog.setContentView(R.layout.activity_event_info);
        close = (TextView) popDialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popDialog.dismiss();
            }
        });
        popDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popDialog.show();
    }

}
