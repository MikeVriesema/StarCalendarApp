package mikevriesema.ul.starcalendar;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class preferences extends PreferenceActivity {

    public final static String KEY_USER = "KEY_USER";
    public final static String KEY_CITY = "KEY_CITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_preferences);
    }
}

