package mikevriesema.ul.starcalendar;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


public class Information extends MainActivity {

    Spinner starSpinner;
    String[] starNames;
    TextView databaseView;
    TextView stardescriptions;
    TextView objectInfo;
    TextView close;
    ImageView objectimage;
    ImageView starImage;
    StarsDB starDB;
    Dialog infoPopup;

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
                    //stardescriptions.setText(starDB.getStarDescriptions((starNames[position])));
                    popUpObject(starNames[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

    }

    public void popUpObject(String objectName){
        infoPopup.setContentView(R.layout.activty_information_popup);
        close = (TextView) infoPopup.findViewById(R.id.close);

        //objectimage = (ImageView) infoPopup.findViewById(R.id.objectimage);
        objectInfo = (TextView) infoPopup.findViewById(R.id.objectInfo);

        objectInfo.setText(objectName);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoPopup.dismiss();
            }
        });
        infoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //objectimage.setImageBitmap(ImageViaAssets(objectName+".png"));
        infoPopup.show();
    }
}
