package com.example.audiovideoplayerforyvideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class PlayerAcitvity extends AppCompatActivity {


    Toolbar toolbar;

    Button btnPlay, btnNext, btnPrev, btnFF, btnFr, loopBtn;
    TextView textName,textStart,textStop;
    SeekBar seekMusic;
    BarVisualizer barVisualizer;
    ImageView imageView;

    String sName;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateSeekBar;

    boolean repeatFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_acitvity);


        toolbar = findViewById(R.id.player_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        transparentStatusAndNavigation();

        // arrow white krne ka method
        Drawable nav = toolbar.getNavigationIcon();
        if(nav != null) {
            nav.setTint(getResources().getColor(R.color.white));
        }

        btnPlay = findViewById(R.id.playBtn);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnFF = findViewById(R.id.btnFF);
        btnFr = findViewById(R.id.btnFR);
        loopBtn = findViewById(R.id.btnLoop);
        imageView = findViewById(R.id.imageView);

        textName = findViewById(R.id.txtSongName);
        textStart = findViewById(R.id.textStart);
        textStop = findViewById(R.id.textEnd);

        seekMusic = findViewById(R.id.seekBar);
        barVisualizer = findViewById(R.id.blast);


        //work 1

        if (mediaPlayer != null){

            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = intent.getStringExtra("songName");
        position = bundle.getInt("pos",0);
        textName.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).toString());
        sName = mySongs.get(position).getName();
        textName.setText(sName);

        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();

        updateSeekBar = new Thread() {
            @Override
            public void run() {

                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;

                // todo for updating the seekbar every milliSec

                while (currentPosition < totalDuration){

                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekMusic.setProgress(currentPosition);
                    }
                    catch (InterruptedException | IllegalStateException e){

                        e.printStackTrace();
                    }
                }
            }
        };


        seekMusic.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();
        seekMusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        seekMusic.getThumb().setColorFilter(getResources().getColor(R.color.naviBlue),PorterDuff.Mode.SRC_IN);

        // todo when user change seekbar position the media should update so for that purpose we create
        seekMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mediaPlayer.isPlaying()){

                    btnPlay.setBackgroundResource(R.drawable.play_arrow);
                    mediaPlayer.pause();
                }
                else {
                    btnPlay.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();

                }
            }
        });


        //work 2
        String endTime = createTime(mediaPlayer.getDuration());
        textStop.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                textStart.setText(currentTime);
                handler.postDelayed(this, delay);
            }
        },delay);


        // todo end work

        int audioSessionId = mediaPlayer.getAudioSessionId();
        if (audioSessionId != -1){

            barVisualizer.setAudioSessionId(audioSessionId);
        }

//)
        //work 3
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();

                position = ((position+1)%mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sName = mySongs.get(position).getName();
                textName.setText(sName);
                mediaPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.pause);
                startAnimation(imageView);

                String nextTime = createTime(mediaPlayer.getDuration());
                textStop.setText(nextTime);

                seekMusic.setMax(mediaPlayer.getDuration());

              //todo ( end of the video
                int audioSessionId = mediaPlayer.getAudioSessionId();
                if (audioSessionId != -1){


                    barVisualizer.setAudioSessionId(audioSessionId);
                }
                // )
            }
        });


        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)<0)?(mySongs.size()-1):(position-1);

                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sName = mySongs.get(position).getName();
                textName.setText(sName);
                mediaPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.pause);
                startAnimation(imageView);


                String nextTime = createTime(mediaPlayer.getDuration());
                textStop.setText(nextTime);

                seekMusic.setMax(mediaPlayer.getDuration());


                // todo end work

                int audioSessionId = mediaPlayer.getAudioSessionId();
                if (audioSessionId != -1){

                    barVisualizer.setAudioSessionId(audioSessionId);
                }
                // )
            }
        });
        btnFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer.isPlaying()){

                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                }
            }
        });

        btnFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer.isPlaying()){

                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                }
            }
        });

        // after complete the next song will auto play
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                btnNext.performClick();
            }
        });


        // work 5

        loopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeatSong(view);
            }
        });

        //onCreate
        }

    // work 4
    private void startAnimation(View view) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView,"rotation",0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();

    }

    // work 2
    public String createTime(int duration){

        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time+=min+":";

        if (sec < 10){
            time+="0";
        }
        time+=sec;

        return time;
    }

    // work 5
    public void repeatSong(View view){

        if (repeatFlag){

            loopBtn.setBackgroundResource(R.drawable.loop_off);
            mediaPlayer.setLooping(false);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    btnNext.performClick();
                }
            });
        }
        else {
            loopBtn.setBackgroundResource(R.drawable.loop);
            mediaPlayer.setLooping(true);
        }
        repeatFlag = !repeatFlag;
    }

    // todo end of the video
    @Override
    protected void onDestroy() {

        if (barVisualizer != null){
            barVisualizer.release();
        }
        super.onDestroy();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){

            AlertDialog.Builder builder = new AlertDialog.Builder(PlayerAcitvity.this);
            builder.setMessage("You want to play song on background");
            builder.setCancelable(true);
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mediaPlayer.pause();
                    finish();
                }
            });
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    mediaPlayer.start();
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    public void transparentStatusAndNavigation() {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}