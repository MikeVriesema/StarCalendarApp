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
import android.widget.Button;

public class MainActivity extends Activity {

    Button information;

    /*/////////////////////////////////
     * ON CREATE
     *
     */////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        information = (Button) findViewById(R.id.information);



        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(getBaseContext(), Information.class);
                startActivity(infoIntent);
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
