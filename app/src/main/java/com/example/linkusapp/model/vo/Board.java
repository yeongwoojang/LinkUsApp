package com.example.linkusapp.model.vo;

public class Board {

    private String userNickname;
    private String title;
    private String part;
    private String area;
    private String writeTime;

    public Board(String part, String area, String userNickname, String title, String writeTime) {
        this.part = part;
        this.area = area;
        this.userNickname = userNickname;
        this.title = title;
        this.writeTime = writeTime;
    }

    public Board() {

    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(String writeTime) {
        this.writeTime = writeTime;
    }
}
