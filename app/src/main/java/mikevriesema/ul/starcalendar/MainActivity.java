/*/////////////////////////////////
* Application: StarCalendar
*
* Author: Mike Vriesema 17212359
* Date: 26/02/2019
*/////////////////////////////////
package mikevriesema.ul.starcalendar;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

    Button starlist;
    Spinner starSpinner;
    String[] databasevalues;
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
        setContentView(R.layout.activity_main);

        starlist = (Button) findViewById(R.id.starlist);
        starSpinner = (Spinner) findViewById(R.id.starspinner);
        databaseView = (TextView) findViewById(R.id.databasevalues);
        name = (TextView) findViewById(R.id.starvalue);

        starDB = new StarsDB(getApplicationContext());

        starNames = starDB.getStarNames();
        ArrayAdapter<String> starAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item, starNames);
        starAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        starSpinner.setAdapter(starAdapter);

        starlist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }

        });

    }



    /*/////////////////////////////////
     * MENU
     *
     */////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings: {
                Intent preferencesIntent = new Intent(getBaseContext(), preferences.class);
                startActivity(preferencesIntent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }






}
