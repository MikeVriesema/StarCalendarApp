package mikevriesema.ul.starcalendar;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class Information extends Activity {

    Spinner starSpinner;
    String[] starNames;
    TextView databaseView;
    TextView stardescriptions;

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
        stardescriptions = (TextView) findViewById(R.id.stardescriptions);

        starDB = new StarsDB(getApplicationContext());


        starNames = starDB.getStarNames();
        ArrayAdapter<String> starAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item, starNames);
        starAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        starSpinner.setAdapter(starAdapter);

        starSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (starNames.length>position) {
                    stardescriptions.setText(starDB.getStarDescriptions((starNames[position])));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

    }




}
