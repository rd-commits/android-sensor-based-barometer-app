package com.example.mymediaplayer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor pressureSensor;

    private float smoothedPressure = -1;
    private float lastPressure = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new DashboardFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pressureSensor != null) {
            sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {

            float pressure = event.values[0];

            if (smoothedPressure < 0) {
                smoothedPressure = pressure;
            } else {
                float alpha = 0.1f;
                smoothedPressure += alpha * (pressure - smoothedPressure);
            }

            String forecast = calculateForecast(smoothedPressure);

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if (fragment instanceof DashboardFragment) {
                ((DashboardFragment) fragment).updateData(smoothedPressure, forecast);
            }
        }
    }

    private String calculateForecast(float currentPressure) {

        if (lastPressure < 0) {
            lastPressure = currentPressure;
            return "Збір даних...";
        }

        float diff = currentPressure - lastPressure;
        lastPressure = currentPressure;

        if (diff > 0.3) return "Ясно";
        if (diff < -0.3) return "Можливий дощ";
        return "Без значних змін";
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}