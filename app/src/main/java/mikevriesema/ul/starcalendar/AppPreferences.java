/*/////////////////////////////////
 * Application: StarCalendar
 *
 * Author: Mike Vriesema 17212359
 * Date: 24/04/2019
 */////////////////////////////////
package mikevriesema.ul.starcalendar;

import android.os.Bundle;

public class AppPreferences extends android.preference.PreferenceActivity {

    public final static String KEY_USER = "KEY_USER"; //USERNAME FOR MAIN ACTIVITY
    public final static String KEY_CITY = "KEY_CITY"; //CITY FOR WEATHER CHECK

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_preferences);
    }
}

