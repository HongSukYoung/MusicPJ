package com.example.musicpj;
public class Youtube {
    public String videoId, title, description, thumbUrl, thumbUrlHigh;

    public Youtube(String videoId, String title, String description, String thumbUrl, String thumbUrlHigh) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.thumbUrl = thumbUrl;
        this.thumbUrlHigh = thumbUrlHigh;
    }
}