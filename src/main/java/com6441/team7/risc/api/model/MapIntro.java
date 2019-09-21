package com6441.team7.risc.api.model;

public class MapIntro {
    private String imgName;
    private Boolean isWrap;
    private String scroll;
    private String author;
    private Boolean isWarn;

    public MapIntro setImgName(String imgName) {
        this.imgName = imgName;
        return this;
    }

    public MapIntro setIsWrap(String isWrap) {
        this.isWrap = "yes".equalsIgnoreCase(isWrap);
        return this;
    }

    public MapIntro setScroll(String scroll) {
        this.scroll = scroll;
        return this;
    }

    public MapIntro setAuthor(String author) {
        this.author = author;
        return this;
    }

    public MapIntro setWarn(String isWarn) {
        this.isWarn = "yes".equalsIgnoreCase(isWarn);
        return this;
    }

    public String getImgName() {
        return imgName;
    }

    public Boolean getWrap() {
        return isWrap;
    }

    public String getScroll() {
        return scroll;
    }

    public String getAuthor() {
        return author;
    }

    public Boolean getWarn() {
        return isWarn;
    }

    @Override
    public String toString() {
        return "MapIntroduction{" +
                "imgName='" + imgName + '\'' +
                ", isWrap=" + isWrap +
                ", scroll='" + scroll + '\'' +
                ", author='" + author + '\'' +
                ", isWarn=" + isWarn +
                '}';
    }
}
