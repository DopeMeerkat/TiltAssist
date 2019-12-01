package com.java.tiltassist;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.widget.TextView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.Menu;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    SensorManager sensorManager;
    Sensor gyroSensor;
    Sensor accelSensor;
    TextView gyroText;
    TextView accelText;
    boolean singleHand = false;
    float gX = 0;
    float gY = 0;
    float gZ = 0;
    ButtonView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroText = (TextView) findViewById(R.id.gyroText);
        accelText = (TextView) findViewById(R.id.accelText);
    }
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroListener, gyroSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(accelListener, accelSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(gyroListener);
        sensorManager.unregisterListener(accelListener);
    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor gyroSensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            if(singleHand) {
                gX = event.values[0];
                gY = event.values[1];
                gZ = event.values[2];
            }
            gyroText.setText("Gyroscope X : " + gX + "| Y : " + gY + "| Z : " + gZ);
        }
    };

    public SensorEventListener accelListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor accelSensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            accelText.setText("Accelerometer X : " + x + "| Y : " + y + "| Z : " + z);
        }
    };

    private class ButtonView extends View{
        public ButtonView(Context context){
            super(context);
        }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // custom drawing code here
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);

            // make the entire canvas white
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);

            // draw blue circle with anti aliasing turned off
            paint.setAntiAlias(false);
            paint.setColor(Color.BLUE);
            canvas.drawCircle(20, 20, 15, paint);

            // draw green circle with anti aliasing turned on
            paint.setAntiAlias(true);
            paint.setColor(Color.GREEN);
            canvas.drawCircle(60, 20, 15, paint);

            // draw red rectangle with anti aliasing turned off
            paint.setAntiAlias(false);
            paint.setColor(Color.RED);
            canvas.drawRect(100, 5, 200, 30, paint);

            // draw the rotated text
            canvas.rotate(-45);

            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("Graphics Rotation", 40, 180, paint);

            //undo the rotate
            canvas.restore();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
