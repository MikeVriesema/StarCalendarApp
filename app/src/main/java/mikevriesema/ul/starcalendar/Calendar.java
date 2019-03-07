package mikevriesema.ul.starcalendar;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = (CalendarView) findViewById(R.id.calendar);
        dateplan = (TextView) findViewById(R.id.date);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String[] months = getResources().getStringArray(R.array.months);
                String events = "";
                String newDate = dayOfMonth + " of " + months[month] + " " + year + "\n" + events;
                dateplan.setText(newDate);
            }
        });

    }

}
