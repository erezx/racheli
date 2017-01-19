package com.example.erez.racheli2;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gemsense.common.TapData;
import com.gemsense.gemsdk.Gem;
import com.gemsense.gemsdk.GemListener;
import com.gemsense.gemsdk.GemManager;
import com.gemsense.gemsdk.GemSDKUtilityApp;
import com.gemsense.gemsdk.OnTapListener;

import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;



import com.gemsense.common.GemSensorsData;
import com.gemsense.gemsdk.gesture.OnGestureListener;
import com.example.erez.racheli2.drawingview.OpenGIGestureView;
import com.gemsense.gemsdk.gesture.Gesture;
import com.gemsense.gemsdk.gesture.GesturePoint;
import com.gemsense.gemsdk.gesture.GestureScore;
import com.gemsense.gemsdk.gesture.StreamGestureManager;

import java.util.ArrayList;
/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */


public class DrawDetailActivity extends AppCompatActivity {

    MediaPlayer mPlayer;
    private Gem gem;
    private StreamGestureManager gestureManager;
    private OpenGIGestureView drawingView;

    int p;

    public static final String EXTRA_POSITION = "position";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        gestureManager = new StreamGestureManager(this);
        drawingView = (OpenGIGestureView)findViewById(R.id.drawing_view);
        findViewById(R.id.drawing_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gem.calibrateOrigin();
            }
        });

        //Configure gesture recognition callback
        gestureManager.setOnGestureListener(new OnGestureListener() {
            @Override
            public void onGesture(ArrayList<GestureScore> scores) {
                if(scores.size() > 0) {
                    GestureScore score = scores.get(0);

                    //Center pointer and clear visualization view
                    gem.calibrate();
                    drawingView.reset();

                    Log.i("GestureDemo", "Gesture recognized: " + score.name + " score: " + score.score);
                }
                else {
                    Log.i("GestureDemo", "Gesture not recognized");
                }
            }
        });
        int postion = getIntent().getIntExtra(EXTRA_POSITION, 0);

        p = postion;

        Log.i("Postion", "Postion" + postion);

        String[] whitelist = GemSDKUtilityApp.getWhiteList(this);
        initGem(whitelist[0]);
    }

    private void initGem(String address) {
        //Get a gem
        gem = GemManager.getDefault().getGem(address, new GemListener() {
            @Override
            public void onErrorOccurred(int i) {
                Toast.makeText(DrawDetailActivity.this, "Can't find a gem", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSensorsChanged(GemSensorsData gemSensorsData) {
                gestureManager.nextSample(gemSensorsData.quaternion, gemSensorsData.acceleration);
                drawingView.addNext(gemSensorsData.quaternion);
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        //Bind the Gem Service to the app
        GemManager.getDefault().bindService(this);
        //Get list of addresses from the utility app
        String[] whitelist = GemSDKUtilityApp.getWhiteList(this);

        if(whitelist.length > 0) {
            //If white list has been changed disconnect old one
            if(gem != null && !whitelist[0].equals(gem.getAddress())) {
                GemManager.getDefault().releaseGem(gem);
            }

            //It's possible to call it in OnCreate() if white list is not supposed to be changed
            initGem(whitelist[0]);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Unbind Gem Service from the application
        GemManager.getDefault().unbindService(this);
    }


}
