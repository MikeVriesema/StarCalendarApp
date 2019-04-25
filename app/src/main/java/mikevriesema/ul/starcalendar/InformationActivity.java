/*/////////////////////////////////
 * Application: StarCalendar
 *
 * Author: Mike Vriesema 17212359
 * Date: 24/04/2019
 */////////////////////////////////
package mikevriesema.ul.starcalendar;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


public class InformationActivity extends MainActivity {

    Spinner starSpinner;
    String[] starNames;
    TextView databaseView;
    TextView objectInfo;
    TextView massInfo;
    TextView dimensionInfo;
    TextView gravInfo;
    TextView factInfo;
    TextView close;
    ImageView objectimage;
    ImageView starImage;
    StarsDB starDB;
    Dialog infoPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        starSpinner = (Spinner) findViewById(R.id.starspinner);
        databaseView = (TextView) findViewById(R.id.databasevalues);
        starImage = (ImageView) findViewById(R.id.starimage);
        starDB = new StarsDB(getApplicationContext());
        infoPopup = new Dialog(this);
        starNames = starDB.getStarNames();
        ArrayAdapter<String> starAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item, starNames);
        starAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        starSpinner.setAdapter(starAdapter);

        starSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (starNames.length>position && position != 0) {
                    popUpObject(starNames[position]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0){
            }
        });
    }

    public void popUpObject(String objectName){
        infoPopup.setContentView(R.layout.activity_information_popup);
        close = (TextView) infoPopup.findViewById(R.id.close);
        objectimage = (ImageView) infoPopup.findViewById(R.id.objectimage);
        objectInfo = (TextView) infoPopup.findViewById(R.id.objectInfo);
        massInfo = (TextView) infoPopup.findViewById(R.id.massInfo);
        dimensionInfo = (TextView) infoPopup.findViewById(R.id.dimensionInfo);
        gravInfo = (TextView) infoPopup.findViewById(R.id.gravityinfo);
        factInfo = (TextView) infoPopup.findViewById(R.id.factinfo);

        objectInfo.setText(objectName);
        dimensionInfo.setText(starDB.getStarDimensions((objectName)));
        massInfo.setText(starDB.getStarMass((objectName)));
        gravInfo.setText(starDB.getStarGravity((objectName)));
        factInfo.setText(starDB.getStarDescriptions((objectName)));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoPopup.dismiss();
            }
        });
        infoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        objectimage.setImageBitmap(ImageViaAssets(objectName+".png"));
        infoPopup.show();
    }
}
