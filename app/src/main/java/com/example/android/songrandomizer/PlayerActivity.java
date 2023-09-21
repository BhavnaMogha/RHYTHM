package com.example.android.songrandomizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity {
    Button btnplay,btnpause,btnff,btnfr,btnnext,btnprev,startt;
    TextView txtsname,txtsstart,txtsstop;
    SeekBar seekmusic;
    BarVisualizer visualizer;
    ImageView imageView;

    String sname;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position,n;
    ArrayList<File> mySongs;
    Thread updateseekbar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
       if(item.getItemId()==android.R.id.home)
       {
            onBackPressed();
       }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onDestroy()
    {
        if(visualizer != null)
        {
            visualizer.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //getSupportActionBar().setTitle("Now playing");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnprev= findViewById(R.id.btnprev);
        btnnext= findViewById(R.id.btnnext);
        btnplay= findViewById(R.id.playbtn);
        startt = findViewById(R.id.startt);
        btnff= findViewById(R.id.btnff);
        btnfr= findViewById(R.id.btnfr);
        txtsname= findViewById(R.id.txtsn);
        txtsstart= findViewById(R.id.txtsstart);
        txtsstop= findViewById(R.id.txtsstop);
        seekmusic= findViewById(R.id.seekbar);
        visualizer= findViewById(R.id.blast);
        imageView= findViewById(R.id.imageView);

        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs= (ArrayList) bundle.getParcelableArrayList("songs");
        String songName= i.getStringExtra("songname");
        position= bundle.getInt("pos",0);
        txtsname.setSelected(true);
        Uri uri= Uri.parse(mySongs.get(position).toString());
        sname= mySongs.get(position).getName();
        txtsname.setText(sname);

        mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();

        updateseekbar = new Thread()
        {
            @Override
            public void run()
            {
                int totalDuration = mediaPlayer.getDuration();
                int currentposition = 0;

                while(currentposition<totalDuration)
                {
                    try
                    {
                        sleep(500);
                        currentposition = mediaPlayer.getCurrentPosition();
                        seekmusic.setProgress(currentposition);
                    }

                    catch (InterruptedException | IllegalStateException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        seekmusic.setMax(mediaPlayer.getDuration());
        updateseekbar.start();

        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
               mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    btnplay.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                } else {
                    btnplay.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
            {
              btnnext.performClick();
            }
        });

        int audiosessionId = mediaPlayer.getAudioSessionId();
        if(audiosessionId != -1)
        {
            visualizer.setAudioSessionId(audiosessionId);
        }

        startt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < mySongs.size(); i++)
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    Random ran = new Random();
                    n = ran.nextInt(mySongs.size());
                    int position = n;
                    Uri u = Uri.parse(mySongs.get(position).toString());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                    sname = mySongs.get(position).getName();
                    txtsname.setText(sname);
                    seekmusic.setMax(mediaPlayer.getDuration());
                    mediaPlayer.start();
                    btnplay.setBackgroundResource(R.drawable.ic_pause);
                    int audiosessionId = mediaPlayer.getAudioSessionId();
                    if (audiosessionId != -1) {
                        visualizer.setAudioSessionId(audiosessionId);

                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                        {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer)
                            {
                                btnnext.performClick();
                            }
                        });
                    }

                }
            }
        });


        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
              mediaPlayer.stop();
              mediaPlayer.release();
              position=((position+1)%mySongs.size());
              Uri u = Uri.parse(mySongs.get(position).toString());
              mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
              sname= mySongs.get(position).getName();
              txtsname.setText(sname);
              mediaPlayer.start();
              btnplay.setBackgroundResource(R.drawable.ic_pause);
              startAnimation(imageView);
                int audiosessionId = mediaPlayer.getAudioSessionId();
                if(audiosessionId != -1)
                {
                    visualizer.setAudioSessionId(audiosessionId);
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer)
                    {
                        btnnext.performClick();
                    }
                });
            }
        });

        String endTime = createTime(mediaPlayer.getDuration());
            txtsstop.setText(endTime);

            final Handler handler = new Handler();
            final int delay = 1000;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String currentTime = createTime(mediaPlayer.getCurrentPosition());
                    txtsstart.setText(currentTime);
                    handler.postDelayed(this, delay);
                }
            },delay);

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=((position-1)%mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname= mySongs.get(position).getName();
                txtsname.setText(sname);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.ic_pause);
                startAnimation(imageView);
                int audiosessionId = mediaPlayer.getAudioSessionId();
                if(audiosessionId != -1)
                {
                    visualizer.setAudioSessionId(audiosessionId);
                }
            }
        });

        btnff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
                }
            }
        });

        btnfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
                }
            }
        });
    }

    public void startAnimation(View view)
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String createTime(int duration)
    {
        String time= "";
        int min = duration/1000/60;
        int sec = (duration/1000)%60;
        time+=min+":";
        if(sec<10)
        {
            time+="0";
        }
        //else
            time+=sec;

        return time;
    }
}