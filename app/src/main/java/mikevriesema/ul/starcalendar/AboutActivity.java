/*/////////////////////////////////
 * Application: StarCalendar
 *
 * Author: Mike Vriesema 17212359
 * Date: 25/04/2019
 */////////////////////////////////
package mikevriesema.ul.starcalendar;

import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends MainActivity {
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        text = (TextView) findViewById(R.id.abouttext);
    }
}
