package com.example.musicpj;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.ViewHolder>{
    Context context;
    ArrayList<Youtube> youtubeList;
    Youtube youtube;

    public YoutubeAdapter(Context context, ArrayList<Youtube> youtubeList) {
        this.context = context;
        this.youtubeList = youtubeList;
    }

    @NonNull
    @Override
    public YoutubeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_row, parent, false);
        return new YoutubeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        youtube = youtubeList.get(position);
        holder.txtTitle.setText(youtube.title);
        holder.txtDesc.setText(youtube.description);

        GlideUrl url = new GlideUrl(youtube.thumbUrl, new LazyHeaders.Builder().addHeader("User-Agent", "Android").build());
        Glide.with(context).load(url).into(holder.imgThumb);
    }

    @Override
    public int getItemCount() {
        return youtubeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtDesc, txtAlbumId;
        ImageView imgThumb;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            imgThumb = itemView.findViewById(R.id.imgThumb);
            cardView = itemView.findViewById(R.id.cardView);

            imgThumb.setOnClickListener(view -> {
                int index = getAdapterPosition();
                youtube = youtubeList.get(index);
                Intent intent = new Intent(context, ImageViewerActivity.class);
                intent.putExtra("url", youtube.thumbUrlHigh);
                context.startActivity(intent);
            });

            cardView.setOnClickListener(view -> {
                int index = getAdapterPosition();
                youtube = youtubeList.get(index);

                String webUrl = "https://m.youtube.com/watch?v=" + youtube.videoId;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                context.startActivity(intent);
            });
        }
    }
}