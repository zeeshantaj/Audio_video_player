package com.example.audiovideoplayerforyvideo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {


    private ArrayList<VideoModel> rvModelArrayList;
    private Context context;
    private VideoClickInterface videoClickInterface;



    public VideoAdapter(ArrayList<VideoModel> rvModelArrayList, Context context, VideoClickInterface videoClickInterface) {
        this.rvModelArrayList = rvModelArrayList;
        this.context = context;
        this.videoClickInterface = videoClickInterface;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_view,  parent, false);
        return new VideoHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {

        VideoModel videoRVModel = rvModelArrayList.get(position);
        holder.thumbnail.setImageBitmap(videoRVModel.getVideoThumbnail());


       // holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.rv_animation));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                videoClickInterface.onVideoClick(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return rvModelArrayList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;
        CardView cardView;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.thumbnail);
            cardView = itemView.findViewById(R.id.card_view_video);
        }
    }

    public interface VideoClickInterface{
        void onVideoClick(int position);
    }

}
