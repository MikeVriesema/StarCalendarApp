package mikevriesema.ul.starcalendar;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Information extends Activity {

    Spinner starSpinner;
    String[] starNames;
    TextView databaseView;
    TextView name;

    StarsDB starDB;

    /*/////////////////////////////////
     * ON CREATE
     *
     */////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        starSpinner = (Spinner) findViewById(R.id.starspinner);
        databaseView = (TextView) findViewById(R.id.databasevalues);
        name = (TextView) findViewById(R.id.starvalue);

        starDB = new StarsDB(getApplicationContext());


        starNames = starDB.getStarNames();
        ArrayAdapter<String> starAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item, starNames);
        starAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        starSpinner.setAdapter(starAdapter);

    }
}
