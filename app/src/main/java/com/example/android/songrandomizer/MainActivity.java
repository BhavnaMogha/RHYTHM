package com.example.android.songrandomizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import java.net.URI;
import java.util.Random;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity
{
    protected Button startBtn;

    // Instantiating the MediaPlayer class
    MediaPlayer[] music = new MediaPlayer[15];
    int n;



    @Override
    protected void onCreate(
            Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.startbtn);
        startBtn.setOnClickListener((View view) -> {
            Intent categoryIntent = new Intent(MainActivity.this,MusicListActivity.class);
            startActivity(categoryIntent);
            finish();
        });

        // Adding the music file to our
        // newly created object music
        music[0] = MediaPlayer.create(MainActivity.this,R.raw.alliswell);
        music[1] = MediaPlayer.create(MainActivity.this,R.raw.khadkeglassy);
        music[2] = MediaPlayer.create(MainActivity.this,R.raw.ranjha);
        music[3] = MediaPlayer.create(MainActivity.this,R.raw.dynamite);
        music[4] = MediaPlayer.create(MainActivity.this,R.raw.chakdeindia);
        music[5] = MediaPlayer.create(MainActivity.this,R.raw.believer);
        music[6] = MediaPlayer.create(MainActivity.this,R.raw.channamereyaa);
        music[7] = MediaPlayer.create(MainActivity.this,R.raw.onceiwasachild);
        music[8] = MediaPlayer.create(MainActivity.this,R.raw.jeetehchl);
        music[9] = MediaPlayer.create(MainActivity.this,R.raw.kyakbhiamberse);
        music[10] = MediaPlayer.create(MainActivity.this,R.raw.jayjaykara);
        music[11] = MediaPlayer.create(MainActivity.this,R.raw.terayaarhumai);
        music[12] = MediaPlayer.create(MainActivity.this,R.raw.terayaarhumai);
        music[13] = MediaPlayer.create(MainActivity.this,R.raw.malang);
        music[14] = MediaPlayer.create(MainActivity.this,R.raw.hwabanke);
    }


  

    // Playing the music

    public void musicplay(View v)
    {

        for(int i = 0 ; i < 1 ; i++) {
            Random r = new Random();
            n = r.nextInt(15);


            music[n].start();

        }

        Toast.makeText(MainActivity.this ,"playing" + n,Toast. LENGTH_SHORT).show();

        }




    // Pausing the music
    public void musicpause(View v)
    {
        music[n].pause();
        Toast.makeText(MainActivity.this ,"paused" + n,Toast. LENGTH_SHORT).show();
    }

    // Stoping the music
    public void musicstop(View v)
    {
        music[n].stop();
        Toast.makeText(MainActivity.this ,"stopped" + n,Toast. LENGTH_SHORT).show();
    }}