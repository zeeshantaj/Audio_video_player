package com.example.audiovideoplayerforyvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {



    TextView videoTitle, time;
    ImageButton backIB, forwardIB, playIB;
    SeekBar seekBar;
    String videoName, videoPath;
    RelativeLayout videoRL;
    ConstraintLayout controlRl;
    VideoView videoView;

    MediaPlayer mediaPlayer;

    boolean isOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);


        transparentStatusAndNavigation();


        videoName = getIntent().getStringExtra("videoName");
        videoPath = getIntent().getStringExtra("videoPath");


        videoTitle = findViewById(R.id.TVVideTitle);
        time = findViewById(R.id.time);
        backIB = findViewById(R.id.backButton);
        playIB = findViewById(R.id.playButton);
        forwardIB = findViewById(R.id.forwardButton);
        seekBar = findViewById(R.id.idSeekBar);
        controlRl = findViewById(R.id.RLControl);
        videoRL = findViewById(R.id.RLVideo);
        videoView = findViewById(R.id.videoView);


        videoView.setVideoURI(Uri.parse(videoPath));

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                seekBar.setMax(videoView.getDuration());
                videoView.start();
            }
        });

        videoTitle.setText(videoName);

        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    videoView.seekTo(videoView.getCurrentPosition() - 10000);

            }
        });
        forwardIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                videoView.seekTo(videoView.getCurrentPosition() + 10000);

            }
        });

        playIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (videoView.isPlaying()){
                    videoView.pause();

                    playIB.setImageDrawable(getResources().getDrawable(R.drawable.video_play_arraow));
                }else {
                    videoView.start();
                    playIB.setImageDrawable(getResources().getDrawable(R.drawable.pause_for_video));
                }
            }
        });
        videoRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    hideControl();
                    isOpen = false;
                }
                else {
                    showControl();
                    isOpen = true;
                }
            }
        });
        setHandler();
        initializeSeekBar();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

        }
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
        }
    }

    private void setHandler(){
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (videoView.getDuration() > 0){
                    int curPos = videoView.getCurrentPosition();
                    seekBar.setProgress(curPos);
                    time.setText(""+convertTime(videoView.getDuration()-curPos));
                }
                handler.postDelayed(this,0);
            }
        };
        handler.postDelayed(runnable,500);
    }

    private String convertTime(int ms){

        String time;
        int x, seconds, minutes, hours;
        x = ms/1000;
        seconds = x%60;
        x /=60;
        minutes = x % 60;
        x /= 60;
        hours = x %24;

        if (hours != 0){
            time = String.format("%02d",hours)+":"+String.format("%02d",minutes)+":"+String.format("%02d",seconds);

        }
        else {
            time = String.format("%02d",minutes)+":"+String.format("%02d",seconds);
        }
        return time;
    }

    private void initializeSeekBar(){

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == R.id.idSeekBar){

                    if (fromUser){
                        videoView.seekTo(progress);
                        videoView.start();

                        int curPos = videoView.getCurrentPosition();
                        videoTitle.setText(""+convertTime(videoView.getDuration()-curPos));
                    }


                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void showControl() {
        controlRl.setVisibility(View.VISIBLE);

        final Window window = this.getWindow();
        if (window == null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        View decorView = window.getDecorView();
        if (decorView != null){
            int uiOption = decorView.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= 23){
                uiOption&= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }

            if (Build.VERSION.SDK_INT >= 25){
                uiOption&= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            if (Build.VERSION.SDK_INT >= 27){
                uiOption&= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }

    }

    private void hideControl() {

        controlRl.setVisibility(View.GONE);

        final Window window = this.getWindow();
        if (window == null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        View decorView = window.getDecorView();
        if (decorView != null){
            int uiOption = decorView.getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= 23){
                uiOption |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }

            if (Build.VERSION.SDK_INT >= 25){
                uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            if (Build.VERSION.SDK_INT >= 27){
                uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
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