package mikevriesema.ul.starcalendar;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

    public class Compass extends MainActivity {

        ImageView image;
        float currentDegree = 0f;
        SensorManager sensorMan;
        TextView tvHeading;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.compass_information);

            image = (ImageView) findViewById(R.id.imageViewCompass);
            tvHeading = (TextView) findViewById(R.id.bearing);
            sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        }

        @Override
        public void onResume() {
            super.onResume();
            Sensor orientationSensor = sensorMan.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            sensorMan.registerListener(orientationListener, orientationSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }

        @Override
        public void onPause() {
            super.onPause();
            sensorMan.unregisterListener(orientationListener);
        }

        final SensorEventListener orientationListener = new SensorEventListener() {

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // not in use
            }

            @Override
            public void onSensorChanged(SensorEvent event) {

                float degree = Math.round(event.values[0]);
                tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

                RotateAnimation rotationAn = new RotateAnimation(
                        currentDegree,
                        -degree,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);
                rotationAn.setDuration(210);
                rotationAn.setFillAfter(true);

                // Start the animation
                image.startAnimation(rotationAn);
                currentDegree = -degree;
            }
        };
    }
