package com.java.tiltassist;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
//import android.graphics.drawable.ShapeDrawable;
import android.R.drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import java.util.Random;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener2 {

    private float xPos, xVel = 0.0f;
    private float yPos, baseY = 6.0f, yVel = 0.0f;
    private float xMax, yMax, startX, startY, sensitivityX = 10, sensitivityY = 14;
    private float x, y;
    private Bitmap ball, start, button;
    private SensorManager sensorManager;
    private int singleMode = 0;
    private int testN = 0, testI = 10;
    private float testX[], testY[];
    private BallView ballView;
    private boolean startVisible;
    private long start_time;
    private long end_time;
    private double results[];
    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ballView = new BallView(this);

        setContentView(ballView);

        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        xMax = (float) size.x - 100;
        yMax = (float) size.y - 280;
        startX = xMax/2 - 240;
        startY = yMax - 500;
        startVisible = true;
        state = 0;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xVel = sensorEvent.values[0];
            yVel = sensorEvent.values[1];
            updateBall();
        }
    }


    private void updateBall() {
        if(singleMode == 1) {
            float frameTime = 0.666f;
            float xS = (xVel * sensitivityX) * frameTime;
            float yS = ((baseY - yVel) * sensitivityY) * frameTime;

            xPos -= xS;
            yPos -= yS;

            if (xPos > xMax) {
                xPos = xMax;
            } else if (xPos < 50) {
                xPos = 50;
            }

            if (yPos > yMax) {
                yPos = yMax;
            } else if (yPos < 0) {
                yPos = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class BallView extends View {

        public BallView(Context context) {
            super(context);
            Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            Bitmap startSrc = BitmapFactory.decodeResource(getResources(), R.drawable.start);
            Bitmap buttonSrc = BitmapFactory.decodeResource(getResources(), R.drawable.button);
            final int dstWidth = 100;
            final int dstHeight = 100;
            //x = 0-55
            //y = 1200 1400
            ball = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
            start = Bitmap.createScaledBitmap(startSrc, 500, 200, true);
            button = Bitmap.createScaledBitmap(buttonSrc, 55, 200, true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if(state == 1) {
                canvas.drawBitmap(button, 0, 1200, null);
            }
            if(!startVisible) {
                canvas.drawBitmap(ball, xPos, yPos, null);
            }else{
                canvas.drawBitmap(start, startX, startY, null);
            }
            invalidate();
        }
        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
//            Log.d("click", "X = " + String.valueOf(event.getX()) + ", Y = " + String.valueOf(event.getY()));
            x = event.getX();
            y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            if(x <= 55 && y >= 1200 && y <= 1400)
            {
                baseY = yVel;
//                Log.d("click", "orientation: " + String.valueOf(baseY));
//                end_time = System.nanoTime();
//                results[testN] = (end_time - start_time) / 1e6;
//                Log.d("click", "Toggle time: " + results[testN]);
//                start_time = end_time;
                if(singleMode == 1) singleMode = 0;
                else singleMode = 1;
            }
            else if(!startVisible && x >= xPos - 50 && x <= xPos + 150 && y >= yPos - 50 && y <= yPos + 150) //click dot
            {
//                Log.d("click", "click dot");
                end_time = System.nanoTime();
                results[testI + (testN * state)] = (end_time - start_time) / 1e6;
//                Log.d("click", "I = " + testI + " time: " + results[testI]);
                start_time = end_time;
                if(testI < testN - 1){
//                    Log.d("click", "set to: x = " + String.valueOf(testX[testI]) + ", y = " + String.valueOf(testY[testI]));
                    testI ++;
                    xPos = testX[testI];
                    yPos = testY[testI];
                }
                else {
                    startVisible = true;
                    if (state == 0) state = 1;
                    else state = 0;
//                    for (int j = 0; j < 2; j ++){
                    int j = singleMode;
                        int sum = 0;
                        for (int i = 0; i < testN; i ++)
                        {
                            sum += results[i + (j * testN)];
                            Log.d("click", results[i + (j * testN)] + "ms");
                        }
                        Log.d("click", "total: " + sum);
//                    }
                    singleMode = 0;
                }

            }
            else if(startVisible && x >= startX && x <= startX + 500 && y >= startY && y <= startY + 200) //press start
            {
                testN = 10;
                start_time = System.nanoTime(); //start
                results = new double[2*testN];
                Random rand = new Random();
                testX = new float[testN];
                testY = new float[testN];
                for (int i = 0; i < testN; i ++) {
                    testX[i] = rand.nextInt((int)xMax);
                    testY[i] = rand.nextInt((int)yMax);
//                    Log.d("click", "x = " + String.valueOf(testX[i]) + ", y = " + String.valueOf(testY[i]));
                }
                xPos = testX[0];
                yPos = testY[0];
                startVisible = false;
                testI = 0;
                String toastText;
                if (state == 0) toastText = "Starting Basic Test";
                else toastText = "Starting TiltAssist Test";
                Toast.makeText(this.getContext(), toastText,
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }

    }
}
