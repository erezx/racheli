package com.example.erez.racheli2;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gemsense.common.GemSensorsData;
import com.gemsense.common.TapData;
import com.gemsense.gemsdk.Gem;
import com.gemsense.gemsdk.GemListener;
import com.gemsense.gemsdk.GemManager;
import com.gemsense.gemsdk.GemSDKUtilityApp;
import com.gemsense.gemsdk.OnTapListener;
import com.gemsense.gemsdk.algorithms.QuaternionProjector;

import static java.lang.Math.abs;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */


public class SongDetailActivity extends AppCompatActivity {

    MediaPlayer mPlayer;
    private Gem gem;
    int[] tracks = new int[35];
    int currentTrack = 0;
    int p;
    float acc[];
    float quat[];
    private QuaternionProjector qProjector;

    public static final String EXTRA_POSITION = "position";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // Set title of Detail page
        // collapsingToolbar.setTitle(getString(R.string.item_title));

        int postion = getIntent().getIntExtra(EXTRA_POSITION, 0);
        Resources resources = getResources();
        String[] places = resources.getStringArray(R.array.songs);
        collapsingToolbar.setTitle(places[postion % places.length]);

        String[] placeDetails = resources.getStringArray(R.array.song_details);
        TextView placeDetail = (TextView) findViewById(R.id.place_detail);
        placeDetail.setText(placeDetails[postion % placeDetails.length]);



        TypedArray placePictures = resources.obtainTypedArray(R.array.song_picture);
        ImageView placePicutre = (ImageView) findViewById(R.id.image);
        placePicutre.setImageDrawable(placePictures.getDrawable(postion % placePictures.length()));

        placePictures.recycle();

        p = postion;

        Log.i("Postion", "Postion" + postion);
        if(postion == 0) {
            tracks[0] = R.raw.shope1;
            tracks[1] = R.raw.shope2;
            tracks[2] = R.raw.shope3;
            tracks[3] = R.raw.shope4;
            tracks[4] = R.raw.shope5;
            tracks[5] = R.raw.shope6;
            tracks[6] = R.raw.shope7;
            tracks[7] = R.raw.shope8;
            tracks[8] = R.raw.shope9;
            tracks[9] = R.raw.shope10;
            tracks[10] = R.raw.shope11;
            tracks[11] = R.raw.shope12;
            tracks[12] = R.raw.shope13;
            tracks[13] = R.raw.shope14;
            tracks[14] = R.raw.shope15;
            tracks[15] = R.raw.shope16;
            tracks[16] = R.raw.shope17;
            tracks[17] = R.raw.shope18;
            tracks[18] = R.raw.shope19;
            tracks[19] = R.raw.shope20;
            tracks[20] = R.raw.shope21;
            tracks[21] = R.raw.shope22;
            tracks[22] = R.raw.shope23;
            tracks[23] = R.raw.shope24;
            tracks[24] = R.raw.shope25;
            tracks[25] = R.raw.shope26;
            tracks[26] = R.raw.shope27;
            tracks[27] = R.raw.shope28;
            tracks[28] = R.raw.shope29;
            tracks[29] = R.raw.shope30;
            tracks[30] = R.raw.shope31;
            tracks[31] = R.raw.shope32;
            tracks[32] = R.raw.shope33;
            tracks[33] = R.raw.shope34;
            tracks[34] = R.raw.silence;


            mPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);//Create MediaPlayer object with MP3 file under res/raw folder
        }
        if(postion == 1) {
            tracks[0] = R.raw.shwaki;
            tracks[1] = R.raw.silence;

            mPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);//Create MediaPlayer object with MP3 file under res/raw folder

        }
        if(postion == 2) {
            tracks[0] = R.raw.shapahta;
            tracks[1] = R.raw.silence;

            mPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);//Create MediaPlayer object with MP3 file under res/raw folder

        }

        if(postion == 3) {
            tracks[0] = R.raw.skahetsheli;
            tracks[1] = R.raw.silence;

            mPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);//Create MediaPlayer object with MP3 file under res/raw folder

        }

      //  String[] whitelist = GemSDKUtilityApp.getWhiteList(this);
        initGem("D0:B5:C2:90:7C:2E");
        gem.calibrateAzimuth();
        gem.calibrateOrigin();
        qProjector = new QuaternionProjector(200f);
    }

    private void initGem(String address) {
        //Get a gem
        gem = GemManager.getDefault().getGem(address, new GemListener() {

            @Override
            public void onSensorsChanged(GemSensorsData data) {

                float a[] = data.acceleration;
                float q[] = data.quaternion;
                acc = a;
                quat = q;
                float[] qSphere = qProjector.projectOnPlane(quat);
                float s = (qSphere[1] + 200)/400 + 0.5f; //speed
                float p = (qSphere[1] + 200)/400 + 0.5f; //pitch

                Log.i("Quatation", "Speed " + s);

                if (mPlayer.isPlaying()) {
                    float speed = abs(s);
                    float pitch = abs(p);
                    //float pitch = abs(qSphere[1]);
                    Log.i("p", "p" + p);
                    if(speed>0.5 && speed<1.5 && p!=0) {

                        //       mPlayer.setPlaybackParams(mPlayer.getPlaybackParams().setSpeed(speed));
                        //       mPlayer.setPlaybackParams(mPlayer.getPlaybackParams().setPitch(speed));
                    }

                }
                if(abs(s-1f)>0.4f) {

                    //    if(abs((a[0]+a[1]+a[2])/3 - 1)>0.4f) {
                    Log.i("Motion detected", "Threshold " + abs(s-1f));
                    //Log.i("Acceleration detected", "Threshold " + abs(a[0]+a[1]+a[2])/3);

                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer arg0) {
                            if (tracks[currentTrack] != R.raw.silence) {
                                currentTrack++;
                                Log.i("Track", "Value " + currentTrack);
                                Log.i("Track", "Value " + tracks.length);
                                mPlayer.stop();
                                mPlayer.release();
                                mPlayer = null;
                                mPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);//Create MediaPlayer object with MP3 file under res/raw folder
                                //  mPlayer.setOnCompletionListener(this);
                                //     float speed = abs(acc[2]+1);
                                //float pitch = 3f;
                                //    mPlayer.setPlaybackParams(mPlayer.getPlaybackParams().setSpeed(speed));
                                //mPlayer.setPlaybackParams(mPlayer.getPlaybackParams().setPitch(pitch));
                                gem.calibrateAzimuth();
                                gem.calibrateOrigin();


                            } else {
                                currentTrack = 0;
                                mPlayer.stop();
                                mPlayer.release();
                                mPlayer = null;
                                mPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);//Create MediaPlayer object with MP3 file under res/raw folder
                                gem.calibrateAzimuth();
                                gem.calibrateOrigin();

                            }
                        }


                    });
                    mPlayer.start();
                }
            }
            @Override
            public void onErrorOccurred(int i) {
                Toast.makeText(SongDetailActivity.this, "Can't find a gem", Toast.LENGTH_SHORT).show();
            }

        });
        gem.setTapListener(new OnTapListener() {
            @Override
            public void onTap(TapData tapData) {
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer arg0) {
                        if (tracks[currentTrack] != R.raw.silence) {
                            currentTrack++;
                            Log.i("Track", "Value " + currentTrack);
                            Log.i("Track", "Value " + tracks.length);
                            mPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);//Create MediaPlayer object with MP3 file under res/raw folder
                            mPlayer.setOnCompletionListener(this);

                        } else {
                            currentTrack = 0;
                            mPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);//Create MediaPlayer object with MP3 file under res/raw folder

                        }
                    }


                });

                mPlayer.start();


            }
        });
    }

    public void restoreMe(View view) {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
        currentTrack = 0;
        mPlayer = MediaPlayer.create(getApplicationContext(), tracks[currentTrack]);//Create MediaPlayer object with MP3 file under res/raw folder
        Log.i("Track", "RestoreMe " + currentTrack);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Bind the Gem Service to the app
        GemManager.getDefault().bindService(this);
        //Get list of addresses from the utility app
    /*    String[] whitelist = GemSDKUtilityApp.getWhiteList(this);

        if(whitelist.length > 0) {
            //If white list has been changed disconnect old one
            if(gem != null && !whitelist[0].equals(gem.getAddress())) {
                GemManager.getDefault().releaseGem(gem);
            }

            //It's possible to call it in OnCreate() if white list is not supposed to be changed*/
            initGem("D0:B5:C2:90:7C:2E");
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Unbind Gem Service from the application
        GemManager.getDefault().unbindService(this);

    }
}
