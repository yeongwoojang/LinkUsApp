package com.example.linkusapp.model.vo;

public class Chat {
    private String name;
    private String script;
    private String profile_image;
    private String date_time;

    public Chat(String name, String script, String profile_image, String date_time) {
        this.name = name;
        this.script = script;
        this.profile_image = profile_image;
        this.date_time = date_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
