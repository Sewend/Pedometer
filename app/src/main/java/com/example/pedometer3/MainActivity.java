package com.example.pedometer3;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public boolean active = true;
    private SensorManager sensorManager;
    private int count = 0;
    private TextView text;
    private long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.textView2);
        text.setText(String.valueOf(count));
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener((SensorEventListener ) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        lastUpdate = System.currentTimeMillis();
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener((SensorEventListener) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener((SensorEventListener) this);
    }
    public void OnStopped(View view){
        active = !active;
        if(!active) {
            Button button = findViewById(R.id.button);
            button.setText("ВОЗОБНОВИТЬ");
        } else {
            Button button = findViewById(R.id.button);
            button.setText("ПАУЗА");
        }
    }
    public void onSensorChanged(SensorEvent event){
        if (event.sensor.getType()== Sensor.TYPE_ACCELEROMETER){
            float[] values=event.values;
            float x= values[0];
            float y= values[1];
            float z= values[2];

            float accelationSquareRoot=(x*x+y*y+z*z)
                    /(SensorManager.GRAVITY_EARTH*SensorManager.GRAVITY_EARTH);

            long actualTime=System.currentTimeMillis();

            if(accelationSquareRoot>=2){
                if(actualTime-lastUpdate<200){
                    return;
                }
                lastUpdate=actualTime;
                count++;
                text.setText(String.valueOf(count));
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
}


