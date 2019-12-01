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

public class MainActivity extends AppCompatActivity implements SensorEventListener2 {

    private float xPos, xVel = 0.0f;
    private float yPos, baseY = 6.0f, yVel = 0.0f;
    private float xMax, yMax, sensitivity = 12;
    private float x, y;
    private Bitmap ball;
    private SensorManager sensorManager;
    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BallView ballView = new BallView(this);

        setContentView(ballView);

        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        xMax = (float) size.x - 100;
        yMax = (float) size.y - 280;

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
//        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
////            xAccel = sensorEvent.values[0];
////            yAccel = -sensorEvent.values[1];
//            xVel = sensorEvent.values[0];
//            yVel = baseY - sensorEvent.values[1];
//            updateBall();
//        }
    }


    private void updateBall() {
        float frameTime = 0.666f;
//        xVel += (xAccel * frameTime);
//        yVel += (yAccel * frameTime);

        float xS = (xVel * sensitivity) * frameTime;
        float yS = (yVel * sensitivity) * frameTime;

        xPos -= xS;
        yPos -= yS;

        if (xPos > xMax) {
            xPos = xMax;
        } else if (xPos < 0) {
            xPos = 0;
        }

        if (yPos > yMax) {
            yPos = yMax;
        } else if (yPos < 0) {
            yPos = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class BallView extends View {

        public BallView(Context context) {
            super(context);
            Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            final int dstWidth = 100;
            final int dstHeight = 100;
            ball = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(ball, xPos, yPos, null);
            invalidate();
        }
        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
//            Log.d("click", "X = " + String.valueOf(event.getX()) + ", Y = " + String.valueOf(event.getY()));
            x = event.getX();
            y = event.getY();
//            Log.d("click", "Pos: X = " + String.valueOf(xPos) + ", Y = " + String.valueOf(yPos));
            if(x >= xPos && x <= xPos + 100 && y >= yPos && y <= yPos + 100)
            {
                xPos = rand.nextInt((int)xMax);
                yPos = rand.nextInt((int)yMax);
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return false;
        }

    }
}
