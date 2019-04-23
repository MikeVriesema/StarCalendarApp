package mikevriesema.ul.starcalendar;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

public class LocationService extends Service{
    public static final int MY_PERMISSION_ACCESS_LOCATION = 0;
    private LocationManager lm;
    Location location;
    private double latitude = 0.0;
    private double longitude = 0.0;

    @Override
    public void onCreate() {
        super.onCreate();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocationService();
    }

    @TargetApi(23)
    public void getLocationService(){
        try {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}