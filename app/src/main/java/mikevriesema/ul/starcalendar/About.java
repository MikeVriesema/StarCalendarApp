package mikevriesema.ul.starcalendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class About extends MainActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        text = (TextView) findViewById(R.id.abouttext);
    }
}
