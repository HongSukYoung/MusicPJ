package com.example.musicpj;

public class ChartDTO {
    private String title;
    private String name;
    private String rankNum;
    private String imageUrl;
    private String albumID;

    public ChartDTO() {
        this.title = title;
        this.name = name;
        this.rankNum = rankNum;
        this.imageUrl = imageUrl;
        this.albumID = albumID;
    }

    public void setRankNum(String rankNum) {
        this.rankNum = rankNum;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getRankNum() {
        return rankNum;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAlbumID() {
        return albumID;
    }
}
