package com.example.audiovideoplayerforyvideo;

import android.graphics.Bitmap;

public class VideoModel {

    String videoName, VideoPath;
    Bitmap VideoThumbnail;

    public VideoModel(String videoName, String videoPath, Bitmap videoThumbnail) {
        this.videoName = videoName;
        VideoPath = videoPath;
        VideoThumbnail = videoThumbnail;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoPath() {
        return VideoPath;
    }

    public void setVideoPath(String videoPath) {
        VideoPath = videoPath;
    }

    public Bitmap getVideoThumbnail() {
        return VideoThumbnail;
    }

    public void setVideoThumbnail(Bitmap videoThumbnail) {
        VideoThumbnail = videoThumbnail;
    }

}

